package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItaliaGuerraBotListener implements StatusListener {

	private ItaliaGuerraTelegramBot bot;
	private Connection db;

	public ItaliaGuerraBotListener(ItaliaGuerraTelegramBot bot, Connection db) {
		this.bot = bot;
		this.db = db;
	}

	@Override
	public void onStatus(Status status) {
		if (!status.isRetweet() && status.getUser().getScreenName().equals("italiaguerrabot")) {
			System.out.println("[LOG] Status received: " + status.toString());

			String tweetText = status.getText();

			Statement select = null;
			try {
				select = db.createStatement();
				ResultSet resultSet = select.executeQuery("select * from chatids");
				while (resultSet.next()) {
					String chatId = resultSet.getString("chatid");
					SendMessage message = new SendMessage().setChatId(Long.valueOf(chatId)).setText(tweetText);
					bot.execute(message);
				}
			} catch (SQLException | TelegramApiException e) {
				e.printStackTrace();
			}


		} else {
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
