package me.xdgamer90000.botwows.audio;


import com.github.Vincentvibe3.efplayer.core.EventListener;
import com.github.Vincentvibe3.efplayer.core.Player;
import com.github.Vincentvibe3.efplayer.core.Track;
import me.xdgamer90000.botwows.App;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerListener extends EventListener {
    LinkedBlockingQueue<Track> queue = new LinkedBlockingQueue<>();
    Boolean loop = false;
    String updatechannelid;
    public Boolean toggleLoop() {
          loop = !loop;
          return loop;
    }
    public void setUpdatechannelid(String id){
        updatechannelid=id;
    }
    String updateguildid;
    public void setGuildid(String id){
        updateguildid=id;
    }
    public void clear(){
       queue.clear();
    }

    @Override
    public void onTrackLoad(@NotNull Track track, @NotNull Player player) {
        queue.offer(track);
        if (queue.size()==1) {
            player.play(track);
        }
    }

    @Override
    public void onTrackStart(@NotNull Track track, @NotNull Player player) {
        JDA client = App.getJDA();
        TextChannel channel = client.getTextChannelById(updatechannelid);
        if(channel!=null){
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Now Playing")
                    .setDescription(String.format("[%s](%s)",track.getTitle(),track.getUrl()))
                    .build();
            channel.sendMessage(new MessageCreateBuilder()
                    .setEmbeds(embed).build()).queue();

        }
    }

    @Override
    public void onPlaylistLoaded(@NotNull List<Track> tracks, @NotNull Player player) {
        super.onPlaylistLoaded(tracks, player);
    }

    @Override
    public void onTrackDone(@NotNull Track track, @NotNull Player player, boolean canStartNext) {
        if(canStartNext){
            List<Track> queuesnapshot = queue.stream().toList();
            int index= queuesnapshot.indexOf(track)+1;
            if(index < queue.size()){
                player.play(queuesnapshot.get(index));
            }
            else if (index == queue.size()&& loop){
                player.play(queuesnapshot.get(0));

            }
        }
    }

    @Override
    public void onLoadFailed() {
        super.onLoadFailed();
    }

    @Override
    public void onTrackError(@NotNull Track track) {
        super.onTrackError(track);
    }
}
