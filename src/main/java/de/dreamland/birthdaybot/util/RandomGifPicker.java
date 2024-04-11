package de.dreamland.birthdaybot.util;

import de.dreamland.birthdaybot.DreamBirthdayBot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class RandomGifPicker {

    public static String getRandomGif(long uid) throws SQLException {

        String link = "";

        ResultSet rs = DreamBirthdayBot.getInstance().databaseConnector.getResult("SELECT link FROM gifs WHERE uid="+uid+";");

        while(rs.next()) {
            link = rs.getString("link");
        }

        if(link.equalsIgnoreCase("")) {
            int i = new Random().nextInt(DreamBirthdayBot.getInstance().gifs.size());
            link = DreamBirthdayBot.getInstance().gifs.get(i);
        }

        return link;
    }


}
