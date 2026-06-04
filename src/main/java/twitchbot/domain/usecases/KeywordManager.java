package twitchbot.domain.usecases;

import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.ports.inbound.ChatTriggerUseCase;

import java.util.ArrayList;
import java.util.List;

public class KeywordManager {
    private final List<ChatTriggerUseCase> triggers = new ArrayList<>();

    public void registerTrigger(ChatTriggerUseCase trigger) {
        triggers.add(trigger);
    }

    public void checkMessageForKeywords(ChatMessage message) {
        String lowerCaseMessage = message.text().toLowerCase();
        for(ChatTriggerUseCase trigger : triggers) {
            if(lowerCaseMessage.contains(trigger.getKeyword())) {
                trigger.execute(message);
                break;
            }
        }
    }
}
