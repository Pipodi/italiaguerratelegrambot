package it.pipodi.italiaguerratelegrambot;

import it.pipodi.italiaguerratelegrambot.bot.ItaliaGuerraBotListener;
import it.pipodi.italiaguerratelegrambot.bot.ItaliaGuerraTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

    public static void main(String[] args){

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(args[0])
                .setOAuthConsumerSecret(args[1])
                .setOAuthAccessToken(args[2])
                .setOAuthAccessTokenSecret(args[3]);

        Configuration configuration = cb.build();

        TwitterFactory twitterFactory = new TwitterFactory(configuration);
        Twitter twitter = twitterFactory.getInstance();


        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(configuration);
        TwitterStream twitterStream = twitterStreamFactory.getInstance();

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new ItaliaGuerraTelegramBot(twitter, twitterStream, args[4]));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
}
