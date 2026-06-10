package twitchbot.domain.ports.outbound;

import twitchbot.domain.model.MessageContext;

public interface MessageSenderPort {
    void sendMessage(MessageContext context, String message);
}
