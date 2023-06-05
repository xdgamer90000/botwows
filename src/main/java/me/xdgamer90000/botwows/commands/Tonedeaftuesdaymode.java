package me.xdgamer90000.botwows.commands;

import com.github.Vincentvibe3.efplayer.core.EventListener;
import com.github.Vincentvibe3.efplayer.core.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdgamer90000.botwows.audio.AudioHandler;
import me.xdgamer90000.botwows.audio.PlayerListener;
import me.xdgamer90000.botwows.audio.PlayerManager;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Tonedeaftuesdaymode extends Command
{
    public Tonedeaftuesdaymode() {
        this.name = "play";
        this.aliases = new String[]{"p"};
    }

    @Override
    protected void execute(CommandEvent event) {
        String messageContent = event.getMessage().getContentDisplay();
        String[] splitMessage = messageContent.split(" ");
        String query = splitMessage[1];
        for (int i = 2;splitMessage.length>i;i++){
            query=query+" "+splitMessage[i];
        }

        Player music = PlayerManager.getplayer(event.getGuild().getId());
        music.load(query);
        ((PlayerListener)music.getEventListener()).setUpdatechannelid(event.getEvent().getGuildChannel().getId());
        ((PlayerListener)music.getEventListener()).setGuildid(event.getGuild().getId());
        AudioChannel vc = event.getMember().getVoiceState().getChannel();
        if (vc==null){
            event.reply("You need to be in a channel you pepega");
        }
        else{
            AudioManager audiohandle = event.getGuild().getAudioManager();
            audiohandle.setSendingHandler(new AudioHandler(music));
            audiohandle.openAudioConnection(vc);
        }
    }
}
