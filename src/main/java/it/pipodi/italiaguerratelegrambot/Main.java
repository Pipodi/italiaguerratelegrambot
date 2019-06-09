package it.pipodi.italiaguerratelegrambot;

import it.pipodi.italiaguerratelegrambot.bot.ItaliaGuerraTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {

    public static void main(String[] args){

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new ItaliaGuerraTelegramBot(args[0], args[1], args[2], args[3], args[4]));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
}
