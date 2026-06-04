package twitchbot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import twitchbot.adapters.inbound.TwitchChatEventHandler;
import twitchbot.adapters.outbound.TwitchMessageSenderAdapter;
import twitchbot.config.BotConfig;
import twitchbot.domain.ports.outbound.MessageSenderPort;
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

        // 2. Outbound Adapter initialisieren
        MessageSenderPort messageSender = new TwitchMessageSenderAdapter(twitchClient);

        // 3. Domain Logic (Hexagon) zusammenbauen
        DuelManager duelManager = new DuelManager(scheduler, messageSender);

        CommandManager commandManager = new CommandManager(config.getPrefix());
        commandManager.registerCommand(new PingCommand(messageSender));
        commandManager.registerCommand(new HugCommand(messageSender));
        commandManager.registerCommand(new DuelCommand(duelManager, messageSender));

        KeywordManager keywordManager = new KeywordManager();
        keywordManager.registerTrigger(new RaufasertapeteTrigger(messageSender));

        // 4. Inbound Adapter mit der Domain Logic verbinden
        TwitchChatEventHandler eventHandler = new TwitchChatEventHandler(
                commandManager, keywordManager, config.getBotName(), config.getPrefix()
        );
        twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(eventHandler);

        // 5. Dem Chat beitreten
        twitchClient.getChat().joinChannel(config.getChannelName());
        System.out.println("Bot [" + config.getBotName() + "] wurde erfolgreich mit Hexagonaler Architektur gestartet.");
    }
}