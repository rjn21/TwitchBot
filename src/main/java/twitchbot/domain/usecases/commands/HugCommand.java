package twitchbot.domain.usecases.commands;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.CommandUseCase;
import twitchbot.domain.ports.outbound.MessageSenderPort;

public class HugCommand implements CommandUseCase {
    private final MessageSenderPort messageSender;

    public HugCommand(MessageSenderPort messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public String getName() { return "hug"; }

    @Override
    public void execute(ChatMessage message, String[] args) {
        String receiver = args.length > 0 ? String.join(" ", args).trim() : "";
        if (receiver.isEmpty()) {
            messageSender.sendMessage(message.channel(), message.sender() + " hugs themselves! :)");
        } else {
            if (receiver.startsWith("@")) receiver = receiver.substring(1).trim();
            messageSender.sendMessage(message.channel(), message.sender() + " hugs " + receiver + " :)");
        }
    }
}