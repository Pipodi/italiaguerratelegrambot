package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

    private String telegramAPIKey;
    private Twitter twitter;
    private TwitterStream twitterStream;

    public ItaliaGuerraTelegramBot(Twitter twitter, TwitterStream twitterStream, String telegramAPIKey){
        this.telegramAPIKey = telegramAPIKey;
        this.twitter = twitter;
        this.twitterStream = twitterStream;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {

            User italiaguerrabotProfile = null;
            try {
                italiaguerrabotProfile = twitter.showUser("italiaguerrabot");
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            FilterQuery filterQuery = new FilterQuery(italiaguerrabotProfile.getId());
            this.twitterStream.addListener(new ItaliaGuerraBotListener(this, update.getMessage().getChatId()));
            twitterStream.filter(filterQuery);
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bot avviato. In attesa di aggiornamenti da parte di [ItaliaGuerraBot 2020](https://twitter.com/italiaguerrabot)").setParseMode("Markdown");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "italiaguerratelegrambot";
    }

    @Override
    public String getBotToken() {
        return telegramAPIKey;
    }

}
