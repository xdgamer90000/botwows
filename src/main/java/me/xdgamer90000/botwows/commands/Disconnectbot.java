package me.xdgamer90000.botwows.commands;

import com.github.Vincentvibe3.efplayer.core.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdgamer90000.botwows.audio.PlayerListener;
import me.xdgamer90000.botwows.audio.PlayerManager;

public class Disconnectbot extends Command {
    public Disconnectbot(){
        this.name= "disconnect";
        this.aliases =new String[]{"dc"};
    }
    @Override
    protected void execute(CommandEvent event) {
        Player music = PlayerManager.getplayer(event.getGuild().getId());
        ((PlayerListener)music.getEventListener()).clear();
        music.stop();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
