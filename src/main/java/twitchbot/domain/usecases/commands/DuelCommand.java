package twitchbot.domain.usecases.commands;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.CommandUseCase;
import twitchbot.domain.ports.outbound.MessageSenderPort;
import twitchbot.domain.usecases.DuelManager;

public class DuelCommand implements CommandUseCase {
    private final DuelManager duelManager;
    private final MessageSenderPort messageSender;

    public DuelCommand(DuelManager duelManager, MessageSenderPort messageSender) {
        this.duelManager = duelManager;
        this.messageSender = messageSender;
    }

    @Override
    public String getName() {
        return "duell";
    }

    @Override
    public void execute(ChatMessage message, String[] args) {
        if (args.length == 0) {
            messageSender.sendMessage(message.channel(), "@" + message.sender() + ", Nutzung: !duell <Name> ODER !duell accept");
            return;
        }
        if (args[0].equalsIgnoreCase("accept")) {
            duelManager.acceptChallenge(message.channel(), message.sender());
            return;
        }
        String target = args[0].startsWith("@") ? args[0].substring(1) : args[0];
        if (target.equalsIgnoreCase(message.sender())) {
            messageSender.sendMessage(message.channel(), "@" + message.sender() + ", du kannst dich nicht selbst herausfordern!");
            return;
        }
        duelManager.createChallenge(message.channel(), message.sender(), target);
    }
}
