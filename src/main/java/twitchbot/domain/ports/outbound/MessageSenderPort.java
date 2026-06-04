package twitchbot.domain.ports.outbound;

public interface MessageSenderPort {
    void sendMessage(String channel, String message);
}
