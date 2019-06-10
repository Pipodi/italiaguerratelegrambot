package it.pipodi.italiaguerratelegrambot;

import it.pipodi.italiaguerratelegrambot.bot.ItaliaGuerraTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(args[0])
				.setOAuthConsumerSecret(args[1])
				.setOAuthAccessToken(args[2])
				.setOAuthAccessTokenSecret(args[3]);

		Configuration configuration = cb.build();

		TwitterFactory twitterFactory = new TwitterFactory(configuration);
		Twitter twitter = twitterFactory.getInstance();

		User italiaguerrabotProfile = null;
		try {
			System.out.println(String.format("[LOG][%s] Getting italiaguerrabot Twitter profile...", LocalDateTime.now().toString()));
			italiaguerrabotProfile = twitter.showUser("italiaguerrabot");
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(configuration);
		TwitterStream twitterStream = twitterStreamFactory.getInstance();

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();
		Connection db = null;

		try {
			System.out.println(String.format("[LOG][%s] Opening database...", LocalDateTime.now().toString()));
			db = DriverManager.getConnection("jdbc:sqlite:/home/pipodi/italiaguerrabot/sessions.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			botsApi.registerBot(ItaliaGuerraTelegramBot.getInstance(twitterStream, args[4], italiaguerrabotProfile.getId(), db));
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

	}
}
