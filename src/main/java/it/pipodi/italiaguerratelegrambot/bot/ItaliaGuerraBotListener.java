package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class ItaliaGuerraBotListener implements StatusListener {

    private ItaliaGuerraTelegramBot bot;

    public ItaliaGuerraBotListener(ItaliaGuerraTelegramBot bot){
        this.bot = bot;
    }

    @Override
    public void onStatus(Status status) {
        if (!status.getText().startsWith("RT") || !status.getText().contains("@italiaguerrabot")) {
            SendMessage message = new SendMessage().setChatId(ItaliaGuerraTelegramBot.CHAT_ID).setText(status.getText());
            try {
                this.bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Invalid status received: " + status.getText());
        }

    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {

    }
}
