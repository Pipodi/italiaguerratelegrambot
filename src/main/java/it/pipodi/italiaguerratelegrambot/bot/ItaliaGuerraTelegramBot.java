package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.*;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

    private String telegramAPIKey;
    private TwitterStream twitterStream;
    private long userId;

    private static ItaliaGuerraTelegramBot instance = null;

    private boolean isStarted = false;

    public static ItaliaGuerraTelegramBot getInstance(TwitterStream twitterStream, String telegramAPIKey, long userId){
        if (instance != null){
            return instance;
        } else {
          instance = new ItaliaGuerraTelegramBot(twitterStream, telegramAPIKey, userId);
          return instance;
        }

    }

    private ItaliaGuerraTelegramBot(TwitterStream twitterStream, String telegramAPIKey, long userId){
        this.telegramAPIKey = telegramAPIKey;
        this.twitterStream = twitterStream;
        this.userId = userId;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {

            if (isStarted){
                SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bot già avviato");
                try {
                    execute(message);
                    return;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            FilterQuery filterQuery = new FilterQuery(userId);
            this.twitterStream.addListener(new ItaliaGuerraBotListener(this, update.getMessage().getChatId()));
            twitterStream.filter(filterQuery);
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bot avviato. In attesa di aggiornamenti da parte di [ItaliaGuerraBot 2020](https://twitter.com/italiaguerrabot)").setParseMode("Markdown");
            try {
                execute(message);
                isStarted = true;
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
