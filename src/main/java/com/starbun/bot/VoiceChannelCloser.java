package com.starbun.bot;

import lombok.Data;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Data
@Component
@Scope("prototype")
public class VoiceChannelCloser implements Runnable {

    VoiceChannel toClose;

    public VoiceChannelCloser(VoiceChannel toClose) {
        this.toClose = toClose;
    }

    @Override
    public void run() {
        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (toClose.getMembers().size() == 0) toClose.delete().submit();
        };
        Thread closeTask = new Thread(task);
        closeTask.start();
    }
}
