package twitchbot.domain.usecases.commands;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.CommandUseCase;
import twitchbot.domain.ports.outbound.MessageSenderPort;

public class PingCommand implements CommandUseCase {
    private final MessageSenderPort messageSender;

    public PingCommand(MessageSenderPort messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void execute(ChatMessage message, String[] args) {
        messageSender.sendMessage(message.channel(), "@" + message.sender() + " Pong! 🏓");
    }
}
