package twitchbot.domain.model;

public record ChatMessage(Platform platform,String channelId, String sender, String text) {
}
