package com.starbun.bot;

import com.starbun.bot.util.Filter;
import com.starbun.bot.util.VoiceChannelCloser;
import com.starbun.bot.util.WordFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

@Data
@Slf4j
@Component
@EqualsAndHashCode(callSuper = true)
@PropertySource("classpath:application.properties")
public class Bot extends ListenerAdapter {

    @Value("${bot.prefix}")
    private String prefix;
    @Value("${bot.guild_id}")
    private long ourGuildId;
    @Value("${bot.debug}")
    private Boolean debugMode;
    @Value("${bot.id}")
    private long thisBotId;
    private Filter filter;
    private Category dynamicVoiceChannels;
    private WordFilter wordFilter;

    @Autowired
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Autowired
    public void setWordFilter(WordFilter wordFilter) {
        this.wordFilter = wordFilter;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event){
        super.onReady(event);
        wordFilter.startupCensoring(event);
        Guild guild = event.getJDA().getGuildById(ourGuildId);
        assert guild != null;
        Optional<Category> optionalCategory = guild.getCategories()
                .stream()
                .filter(isDynamicCategoryExist())
                .findAny();
        dynamicVoiceChannels = optionalCategory.orElseGet(() -> {
            // Создание категории, если нет
            try {
                return guild.createCategory("Dynamic (r)").submit().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null; // Идея считает, что здесь без этого не обойтись
        });

        CreateVoiceChannelIfNoEmptyOne();
    }

    private void CreateVoiceChannelIfNoEmptyOne() {
        //TODO исправить баг с созданием двух каналов
        assert dynamicVoiceChannels != null;
        int voiceChannelCount = dynamicVoiceChannels.getVoiceChannels().size();
        if (dynamicVoiceChannels
                .getVoiceChannels()
                .stream()
                .noneMatch(voiceChannel -> voiceChannel.getMembers().size() == 0)) {
            dynamicVoiceChannels.createVoiceChannel("DevVoiceChannel " + (voiceChannelCount + 1)).submit();
        }
    }

    @NotNull
    private Predicate<Category> isDynamicCategoryExist() {
        return category -> category.getName().toLowerCase().equals("dynamic (r)");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        log.info("Получено сообщение: {}", event.getMessage().toString());
        try {
            if (filter.isCommand(event.getMessage().getContentRaw())) {
                filter.execute(event);
            } else if (wordFilter.hasBlockWords(event.getMessage().getContentRaw())) {
                event.getMessage().delete().submit();
                event.getChannel().sendMessage("Сообщение пользователя "
                        + event.getAuthor().getAsTag() + " было удалено в связи с цензурой").submit();
            }
//        } catch (CommandException e) {
//            log.trace("Exception: ", e);
//            if (debugMode) {
//                event.getChannel().sendMessage(e.eb.build()).submit();
//            }
        } catch (Exception e) {
            log.trace("Exception: ", e);
            e.printStackTrace();
            if (debugMode) {
                event.getChannel().sendMessage(String.format("```Java\n%s\n```", e.toString())).submit();
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        CreateVoiceChannelIfNoEmptyOne();
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        Runnable voiceChannelCloser = new VoiceChannelCloser(event.getChannelLeft());
        Thread thread = new Thread(voiceChannelCloser);
        thread.start();
    }
}
