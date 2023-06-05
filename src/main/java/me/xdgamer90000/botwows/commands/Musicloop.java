package me.xdgamer90000.botwows.commands;

import com.github.Vincentvibe3.efplayer.core.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdgamer90000.botwows.audio.PlayerListener;
import me.xdgamer90000.botwows.audio.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class Musicloop extends Command
{
    public Musicloop() {
        this.name = "loop";
    }
    @Override
    protected void execute(CommandEvent event) {
        Player music = PlayerManager.getplayer(event.getGuild().getId());
        Boolean loop = ((PlayerListener)music.getEventListener()).toggleLoop();
        String message;
        if(loop) {
            message = "Looping the queue";
        }
        else {
            message = "No longer looping the queue";
        }
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(message)
                .build();
        event.getEvent().getGuildChannel().sendMessage(new MessageCreateBuilder().setEmbeds(embed).build());
    }
}
