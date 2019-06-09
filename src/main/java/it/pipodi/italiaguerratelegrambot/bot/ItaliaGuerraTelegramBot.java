package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    private String telegramAPIKey;

    public ItaliaGuerraTelegramBot(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String telegramAPIKey){
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.telegramAPIKey = telegramAPIKey;
    }

    public static Long CHAT_ID = 0L;

    public static Long ITALIAGUERRABOT_ID = 1134239469735960577L;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId();
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessTokenSecret);

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
        return telegramAPIKey;
    }

}
