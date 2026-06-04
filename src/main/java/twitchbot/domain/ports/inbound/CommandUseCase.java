package twitchbot.domain.ports.inbound;

import twitchbot.domain.model.ChatMessage;

public interface CommandUseCase {
    String getName();
    void execute(ChatMessage message, String[] args);
}
