package me.xdgamer90000.botwows.audio;

import com.github.Vincentvibe3.efplayer.core.Player;
import java.util.HashMap;

public class PlayerManager {
    static HashMap <String, Player> players = new HashMap<>();
    public static Player getplayer (String Guildid){
        Player play = players.get(Guildid);
        if (play == null){
            PlayerListener Listener = new PlayerListener();
            play = new Player(Listener);
            players.put(Guildid,play);
            return play;
        }
        else{
            return play;
        }
    }
}
