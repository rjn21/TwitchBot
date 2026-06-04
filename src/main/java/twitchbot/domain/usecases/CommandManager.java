package twitchbot.domain.usecases;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.CommandUseCase;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, CommandUseCase> commands = new HashMap<>();
    private final String prefix;

    public CommandManager(String prefix) {
        this.prefix = prefix;
    }

    public void registerCommand(CommandUseCase command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public void handleMessage(ChatMessage message) {
        String text = message.text();
        if (text.startsWith(prefix)) {
            String[] split = text.substring(prefix.length()).split(" ");
            String commandName = split[0].toLowerCase();
            String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, args.length);
//            Hier wird in commands<key, value> geschaut, welche implementierung eines COmmands zum key gespeichert wird.
            CommandUseCase command = commands.get(commandName);
            if (command != null) {
                command.execute(message, args);
            }
        }
    }
}
