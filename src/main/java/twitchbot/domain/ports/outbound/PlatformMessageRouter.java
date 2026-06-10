package twitchbot.domain.ports.outbound;

import twitchbot.adapters.outbound.DiscordMessageSenderAdapter;
import twitchbot.adapters.outbound.TwitchMessageSenderAdapter;
import twitchbot.adapters.outbound.YoutubeMessageSenderAdapter;
import twitchbot.domain.model.MessageContext;

public class PlatformMessageRouter implements MessageSenderPort {
    private final TwitchMessageSenderAdapter twitchSender;
    private final YoutubeMessageSenderAdapter youtubeSender;
    private final DiscordMessageSenderAdapter discordSender;

    public PlatformMessageRouter(TwitchMessageSenderAdapter twitchSender,
                                 YoutubeMessageSenderAdapter youtubeSender,
                                 DiscordMessageSenderAdapter discordSender) {
        this.twitchSender = twitchSender;
        this.youtubeSender = youtubeSender;
        this.discordSender = discordSender;
    }

    @Override
    public void sendMessage(MessageContext context, String text) {
        switch(context.platform()) {
            case TWITCH -> twitchSender.send(context, text);
            case DISCORD -> discordSender.send(context, text);
            case YOUTUBE -> youtubeSender.send(context, text);
        }
    }
}
