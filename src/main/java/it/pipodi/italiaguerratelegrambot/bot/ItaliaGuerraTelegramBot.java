package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;

import java.sql.*;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

	private static ItaliaGuerraTelegramBot instance = null;
	private String telegramAPIKey;
	private Connection db;

	private ItaliaGuerraTelegramBot(TwitterStream twitterStream, String telegramAPIKey, long userId, Connection db) {
		this.telegramAPIKey = telegramAPIKey;
		this.db = db;
		ItaliaGuerraBotListener listener = new ItaliaGuerraBotListener(this, db);
		twitterStream.addListener(listener);
		FilterQuery filterQuery = new FilterQuery(userId);
		twitterStream.filter(filterQuery);
	}

	public static ItaliaGuerraTelegramBot getInstance(TwitterStream twitterStream, String telegramAPIKey, long userId, Connection db) {
		if (instance != null) {
			return instance;
		} else {
			instance = new ItaliaGuerraTelegramBot(twitterStream, telegramAPIKey, userId, db);
			return instance;
		}

	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage().getText().equals("/start")) {
			Statement select = null;
			try {
				select = db.createStatement();
				ResultSet resultSet = select.executeQuery("select * from chatids");
				while (resultSet.next()) {
					String chatId = resultSet.getString("chatid");
					System.out.println("[LOG] chatId in db: " + chatId);
					if (chatId.equals(String.valueOf(update.getMessage().getChatId()))) {
						SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bot già avviato.")
								.setParseMode("Markdown");
						try {
							execute(message);
						} catch (TelegramApiException e) {
							e.printStackTrace();

						}
						return;
					}
				}
				String query = "insert into chatids (chatid) values (?)";
				PreparedStatement statement = db.prepareStatement(query);
				statement.setString(1, String.valueOf(update.getMessage().getChatId()));
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("Bot avviato. In attesa di aggiornamenti da parte di [ItaliaGuerraBot 2020](https://twitter.com/italiaguerrabot)")
					.setParseMode("Markdown");
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
