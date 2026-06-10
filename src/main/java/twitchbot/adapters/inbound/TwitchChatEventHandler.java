package twitchbot.adapters.inbound;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import twitchbot.domain.model.ChatMessage;
import twitchbot.domain.model.Platform;
import twitchbot.domain.usecases.CommandManager;
import twitchbot.domain.usecases.KeywordManager;

public class TwitchChatEventHandler {
    private final CommandManager commandManager;
    private final KeywordManager keywordManager;
    private final String botName;
    private final String prefix;

    public TwitchChatEventHandler(CommandManager commandManager, KeywordManager keywordManager, String botName, String prefix) {
        this.commandManager = commandManager;
        this.keywordManager = keywordManager;
        this.botName = botName;
        this.prefix = prefix;
    }

    @EventSubscriber
    public void onChannelMessage(ChannelMessageEvent event) {
        if (event.getUser().getName().equalsIgnoreCase(botName)) return;

        ChatMessage cleanMessage = new ChatMessage(
                Platform.TWITCH,
                event.getChannel().getName(),
                event.getUser().getName(),
                event.getMessage()
        );

        if (cleanMessage.text().startsWith(prefix)) {
            commandManager.handleMessage(cleanMessage);
        } else {
            keywordManager.checkMessageForKeywords(cleanMessage);
        }
    }
}
