package twitchbot.domain.model;

public record ChatMessage(String channel, String sender, String text) {
}
