package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class ItaliaGuerraBotListener implements StatusListener {

    private ItaliaGuerraTelegramBot bot;
    private long chatId;

    public ItaliaGuerraBotListener(ItaliaGuerraTelegramBot bot, long chatId){
        this.bot = bot;
        this.chatId = chatId;
    }

    @Override
    public void onStatus(Status status) {
        if (!status.isRetweet() && status.getUser().getScreenName().equals("italiaguerrabot")) {
            System.out.println("[LOG] Status received: " + status.toString());
            SendMessage message = new SendMessage().setChatId(this.chatId).setText(status.getText());
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

    public void setChatId(long chatId){
        this.chatId = chatId;
    }
}
