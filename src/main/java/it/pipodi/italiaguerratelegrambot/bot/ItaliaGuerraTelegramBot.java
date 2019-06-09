package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

    public static Long CHAT_ID = 0L;

    public static Long ITALIAGUERRABOT_ID = 1134239469735960577L;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId();
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("x6K10ScMFzJuHm74OpV9ujZ4W")
                    .setOAuthConsumerSecret("fh8nVQ2nGtExpnbc3R3YoI3ag1g3FZs3HWlxir48vLYk8LOClE")
                    .setOAuthAccessToken("1137659424657596416-6fmeo86a0Ieede5EfQ7lEgqEUsYutr")
                    .setOAuthAccessTokenSecret("ysCL64VhUShe4n0GUz4y795sk00uilDSQihTGr5ikjH3V");

            FilterQuery filterQuery = new FilterQuery(ITALIAGUERRABOT_ID);
            TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(cb.build());
            TwitterStream twitterStream = twitterStreamFactory.getInstance();
            twitterStream.addListener(new ItaliaGuerraBotListener(this));
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
        return "822163318:AAFxGumbYeBAGsE4x_gKXHAQxYRHTxNPsEo";
    }

}
