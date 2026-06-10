package twitchbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import twitchbot.adapters.inbound.TwitchChatEventHandler;
import twitchbot.adapters.outbound.DiscordMessageSenderAdapter;
import twitchbot.adapters.outbound.TwitchMessageSenderAdapter;
import twitchbot.adapters.outbound.YoutubeMessageSenderAdapter;
import twitchbot.config.BotConfig;
import twitchbot.domain.ports.outbound.MessageSenderPort;
import twitchbot.domain.ports.outbound.PlatformMessageRouter;
import twitchbot.domain.usecases.CommandManager;
import twitchbot.domain.usecases.DuelManager;
import twitchbot.domain.usecases.KeywordManager;
import twitchbot.domain.usecases.commands.DuelCommand;
import twitchbot.domain.usecases.commands.HugCommand;
import twitchbot.domain.usecases.commands.PingCommand;
import twitchbot.domain.usecases.triggers.RaufasertapeteTrigger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) {
        BotConfig config = new BotConfig();

        // 1. Framework/Infrastruktur starten
        OAuth2Credential credential = new OAuth2Credential("twitch", config.getOauthToken());
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

        // Adapter initialisieren
        TwitchMessageSenderAdapter twitchSender = new TwitchMessageSenderAdapter(twitchClient);
        YoutubeMessageSenderAdapter youtubeSender = new YoutubeMessageSenderAdapter();
        DiscordMessageSenderAdapter discordSender = new DiscordMessageSenderAdapter();

        // Router erstellen und adapter übergeben
        MessageSenderPort messageRouter = new PlatformMessageRouter(twitchSender, youtubeSender, discordSender);

        DuelManager duelManager = new DuelManager(scheduler, messageRouter);

        // CommandManager initialisieren
        CommandManager commandManager = new CommandManager(config.getPrefix());

        // Commands registrieren
        commandManager.registerCommand(new PingCommand(messageRouter));
        commandManager.registerCommand(new HugCommand(messageRouter));
        commandManager.registerCommand(new DuelCommand(duelManager, messageRouter));

        // KeywordManager initialisieren
        KeywordManager keywordManager = new KeywordManager();

        // Keywords registrieren
        keywordManager.registerTrigger(new RaufasertapeteTrigger(messageRouter));

        // EventHandler an TwitchClient geben
        TwitchChatEventHandler eventHandler = new TwitchChatEventHandler(
                commandManager, keywordManager, config.getBotName(), config.getPrefix()
        );

        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(eventHandler);

        // Chat betretenm
        twitchClient.getChat().joinChannel(config.getChannelName());
        System.out.println("Bot [" + config.getBotName() + "] wurde erfolgreich mit Hexagonaler Architektur gestartet.");
    }
}