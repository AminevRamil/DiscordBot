package com.starbun.bot.listeners;

import com.starbun.bot.service.CategoryService;
import com.starbun.bot.service.VoiceChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoiceListener extends ListenerAdapter {

    private final VoiceChannelService voiceChannelService;
    private final CategoryService categoryService;

    @Value("${com.starbun.bot.guild_id}")
    private long ourGuildId;

    @Override
    public void onReady(@Nonnull ReadyEvent event){
        Guild guild = event.getJDA().getGuildById(ourGuildId);
        assert guild != null;
        Category voiceCategory = categoryService.getDynamicVoiceCategory(guild);
        voiceChannelService.createVoiceChannelIfNoEmptyOne(voiceCategory);
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        Category voiceChannelParent = event.getChannelJoined().getParent();
        if (voiceChannelParent != null && categoryService.isDynamicCategory(voiceChannelParent)) {
            voiceChannelService.createVoiceChannelIfNoEmptyOne(voiceChannelParent);
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        VoiceChannel channelToClose = event.getChannelLeft();
        voiceChannelService.closeChannel(channelToClose);
    }



}
