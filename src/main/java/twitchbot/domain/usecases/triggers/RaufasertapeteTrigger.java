package twitchbot.domain.usecases.triggers;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.ChatTriggerUseCase;
import twitchbot.domain.ports.outbound.MessageSenderPort;

public class RaufasertapeteTrigger implements ChatTriggerUseCase {
    private final MessageSenderPort messageSender;

    public RaufasertapeteTrigger(MessageSenderPort messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public String getKeyword() { return "raufasertapete"; }

    @Override
    public void execute(ChatMessage message) {
        messageSender.sendMessage(message.channel(), "@" + message.sender() + " Bernd das Brot wäre stolz auf dich! 🍞");
    }
}