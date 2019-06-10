package it.pipodi.italiaguerratelegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import twitter4j.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItaliaGuerraTelegramBot extends TelegramLongPollingBot {

    private String telegramAPIKey;
    private TwitterStream twitterStream;
    private long userId;
    private Connection db;

    private static ItaliaGuerraTelegramBot instance = null;

    public static ItaliaGuerraTelegramBot getInstance(TwitterStream twitterStream, String telegramAPIKey, long userId, Connection db){
        if (instance != null){
            return instance;
        } else {
          instance = new ItaliaGuerraTelegramBot(twitterStream, telegramAPIKey, userId, db);
          return instance;
        }

    }

    private ItaliaGuerraTelegramBot(TwitterStream twitterStream, String telegramAPIKey, long userId, Connection db){
        this.telegramAPIKey = telegramAPIKey;
        this.twitterStream = twitterStream;
        this.userId = userId;
        this.db = db;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            Statement select = null;
            try {
                select = db.createStatement();
                ResultSet resultSet = select.executeQuery("select * from chatids");
                while (resultSet.next()){
                    String chatId = resultSet.getString("chatid");
                    System.out.println("[LOG] chatId in db: " + chatId);
                    if (chatId.equals(String.valueOf(update.getMessage().getChatId()))){
                        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId()).setText("Bot già avviato.").setParseMode("Markdown");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                Statement insert = db.createStatement();
                String  query = String.format("insert into chatids (chatid) values ('%s')", String.valueOf(update.getMessage().getChatId()));
                insert.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            ItaliaGuerraBotListener listener = new ItaliaGuerraBotListener(this, update.getMessage().getChatId());
            this.twitterStream.addListener(listener);
            FilterQuery filterQuery = new FilterQuery(userId);
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
