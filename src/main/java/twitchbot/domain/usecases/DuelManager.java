package twitchbot.domain.usecases;

import twitchbot.core.requests.PendingRequest;
import twitchbot.core.requests.RequestManager;
import twitchbot.domain.model.MessageContext;
import twitchbot.domain.ports.outbound.MessageSenderPort;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DuelManager {
    private final RequestManager<Void> requestManager;
    private final MessageSenderPort messageSender;
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();

    public DuelManager(ScheduledExecutorService scheduler, MessageSenderPort messageSender) {
        this.requestManager = new RequestManager<>(scheduler);
        this.messageSender = messageSender;
        this.scheduler = scheduler;
    }

    public void createChallenge(MessageContext context, String challenger, String target) {
        int minutes = 3;
        Runnable timeoutAction = () -> messageSender.sendMessage(
                context,
                "Das Duell zwischen @" + challenger + " und @" + target + " ist abgelaufen.");

        PendingRequest<Void> request = new PendingRequest<>(context, challenger, target, null);
        boolean success = requestManager.createRequest(request, minutes, timeoutAction);

//        Fehlerhandling
        if (!success) {
            messageSender.sendMessage(
                    context,
                    "@" + challenger + ", dieser Nutzer hat bereits eine offene Herausforderung!");
            return;
        }

        messageSender.sendMessage(
                context,
                "⚔️ @" + target + "! Du wurdest von @" + challenger + " herausgefordert! Schreibe '!duell accept' zum Annehmen."
        );
    }

    public void acceptChallenge(MessageContext context, String target) {
        PendingRequest<Void> challenge = requestManager.acceptRequest(context, target);

        if (challenge == null) {
            messageSender.sendMessage(
                    context,
                    "@" + target + ", du hast aktuell keine offenen Duell-Anfragen.");
            return;
        }

        scheduler.schedule(() -> {
            boolean challengerWins = random.nextBoolean();
            String winner = challengerWins ? challenge.getSender() : challenge.getTarget();
            String loser = challengerWins ? challenge.getTarget() : challenge.getSender();

            messageSender.sendMessage(
                    context,
                    "🏆 ERGEBNIS: Nach einem harten und epischen Kampf triumphiert @" + winner + " über @" + loser + "! 🎉"
            );
        }, 10, TimeUnit.MINUTES);
    }
}
