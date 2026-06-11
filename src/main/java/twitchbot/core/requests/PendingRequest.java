package twitchbot.core.requests;

import twitchbot.domain.model.MessageContext;

import java.util.concurrent.ScheduledFuture;

public class PendingRequest<T> {
    private final MessageContext context;
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

    public PendingRequest(MessageContext context, String sender, String target, T additionalData) {
        this.context = context;
        this.sender = sender;
        this.target = target;
        this.additionalData = additionalData;
    }

    public MessageContext getContext() {
        return context;
    }

    public String getSender() {
        return sender;
    }

    public String getTarget() {
        return target;
    }
}
