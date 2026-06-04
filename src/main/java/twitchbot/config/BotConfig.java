package twitchbot.config;

import io.github.cdimascio.dotenv.Dotenv;

public class BotConfig {
    private String oauthToken;
    private String channelName;
    private String botName;
    // Der Prefix ist hier fest auf "!" gesetzt, kann aber bei Bedarf auch in die .env ausgelagert werden.
    private final String prefix = "!";

    public BotConfig() {
        loadEnv();
    }

    private void loadEnv() {
        try {
            // Lädt die Umgebungsvariablen aus der .env-Datei im Hauptverzeichnis des Projekts
            Dotenv dotenv = Dotenv.load();
            oauthToken = dotenv.get("ACCESS_TOKEN");
            channelName = dotenv.get("CHANNEL_NAME");
            botName = dotenv.get("BOT_NAME");
        } catch (Exception ex) {
            System.err.println("Fehler beim Lesen der .env-Datei: " + ex.getMessage());
            System.err.println("Stelle sicher, dass eine Datei namens '.env' im Projekt-Root (neben der pom.xml) existiert.");
        }
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getBotName() {
        return botName;
    }

    public String getPrefix() {
        return prefix;
    }
}