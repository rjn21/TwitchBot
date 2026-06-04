package twitchbot.domain.ports.inbound;

import twitchbot.domain.model.ChatMessage;

public interface ChatTriggerUseCase {
    String getKeyword();
    void execute(ChatMessage message);
}
