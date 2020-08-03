package com.starbun.bot.service;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class VoiceChannelService {

    public void closeChannel(VoiceChannel channelToClose) {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (channelToClose.getMembers().size() == 0) {
                channelToClose.delete().submit();
            }
        });
        thread.start();
    }

    public void createVoiceChannelIfNoEmptyOne(@Nonnull Category dynamicVoiceChannel) {
        int voiceChannelCount = dynamicVoiceChannel.getVoiceChannels().size();
        if (!isThereEmptyVoiceChannel(dynamicVoiceChannel)) {
            dynamicVoiceChannel.createVoiceChannel("DevVoiceChannel " + (voiceChannelCount + 1)).submit();
        }
    }

    private boolean isThereEmptyVoiceChannel(@Nonnull Category dynamicVoiceChannel) {
        return dynamicVoiceChannel.getVoiceChannels().stream()
                .anyMatch(voiceChannel -> voiceChannel.getMembers().size() == 0);
    }
}
