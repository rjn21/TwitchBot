package twitchbot.adapters.outbound;

import twitchbot.domain.ports.outbound.MessageSenderPort;
import com.github.twitch4j.TwitchClient;

public class TwitchMessageSenderAdapter implements MessageSenderPort {
    private final TwitchClient twitchClient;

    public TwitchMessageSenderAdapter(TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }
    @Override
    public void sendMessage(String channel, String message) {
        if (this.twitchClient != null && this.twitchClient.getChat() != null) {
            this.twitchClient.getChat().sendMessage(channel, message);
        }
    }
}
