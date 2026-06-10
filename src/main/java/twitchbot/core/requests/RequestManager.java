package twitchbot.core.requests;

import twitchbot.domain.model.MessageContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestManager<T> {
    private final Map<String, PendingRequest<T>> requests = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler;

    public RequestManager(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    private String buildKey(String channel, String target) {
        return channel.toLowerCase() + ":" + target.toLowerCase();
    }

    public boolean createRequest(PendingRequest<T> request, int timeoutMinutes, Runnable onTimeout) {
        String key = buildKey(request.getChannel(), request.getTarget());

        if (requests.containsKey(key)) {
            return false;
        } ;

        var timeoutTask = this.scheduler.schedule(() -> {
            if (requests.remove(key) != null) {
                onTimeout.run();
            }
        }, timeoutMinutes, TimeUnit.MINUTES);

        request.setTimeoutTask(timeoutTask);
        requests.put(key, request);
        return true;
    }

    public PendingRequest<T> acceptRequest(MessageContext context, String target) {
        String key = buildKey(context.channelId(), target);

        PendingRequest<T> request = requests.remove(key);

        if (request != null && request.getTimeoutTask() != null) {
            request.getTimeoutTask().cancel(false);
        }

        return request;
    }
}
