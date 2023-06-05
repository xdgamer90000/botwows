package me.xdgamer90000.botwows.audio;

import com.github.Vincentvibe3.efplayer.core.Player;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioHandler implements AudioSendHandler {
    Player audio;
    ByteBuffer buffer = ByteBuffer.allocate(22500000);

    @Override
    public boolean canProvide() {
        return audio.canProvide();
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        buffer.clear();
        buffer.put(audio.provide());
        buffer.flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
    public AudioHandler(Player audio){
        this.audio = audio;
    }
}
