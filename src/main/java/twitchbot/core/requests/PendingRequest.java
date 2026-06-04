package twitchbot.core.requests;

import java.util.concurrent.ScheduledFuture;

public class PendingRequest<T> {
    private final String channel;
    private final String sender;
    private final String target;
    private final T additionalData;

    public ScheduledFuture<?> getTimeoutTask() {
        return timeoutTask;
    }

    public void setTimeoutTask(ScheduledFuture<?> timeoutTask) {
        this.timeoutTask = timeoutTask;
    }

    private ScheduledFuture<?> timeoutTask;

    public PendingRequest(String channel, String sender, String target, T additionalData) {
        this.channel = channel;
        this.sender = sender;
        this.target = target;
        this.additionalData = additionalData;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }

    public String getTarget() {
        return target;
    }
}
