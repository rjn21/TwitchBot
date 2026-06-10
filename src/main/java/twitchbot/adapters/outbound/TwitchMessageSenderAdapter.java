package twitchbot.adapters.outbound;

import twitchbot.domain.ports.outbound.MessageSenderPort;
import com.github.twitch4j.TwitchClient;
import twitchbot.domain.model.MessageContext;

public class TwitchMessageSenderAdapter {
    private final TwitchClient twitchClient;

    public TwitchMessageSenderAdapter(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }

    public void send(MessageContext context, String text) {
        if (this.twitchClient != null && this.twitchClient.getChat() != null) {
            this.twitchClient.getChat().sendMessage(context.channelId(), text);
        }
    }
}
