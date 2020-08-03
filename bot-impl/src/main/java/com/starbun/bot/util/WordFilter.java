package com.starbun.bot.util;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordFilter {

    final private static List<String> blockList = ImmutableList.<String>builder()
            .add("говнокод").add("php").add("javascript").build();
    @Value("${com.starbun.bot.id}")
    private long thisBotId;
    @Value("${com.starbun.bot.test_channel_id}")
    private long testChannelId;

    public void startupCensoring(@Nonnull ReadyEvent event){
        TextChannel textChannel = event.getJDA().getTextChannelById(testChannelId);
        StringBuilder report = new StringBuilder();
        assert textChannel != null;
        Map<User, List<Message>> userListMap = textChannel
                .getIterableHistory().stream()
                .takeWhile(message -> message.getAuthor().getIdLong() != thisBotId)
                .filter(message -> hasBlockWords(message.getContentRaw()))
                .collect(Collectors.groupingBy(Message::getAuthor));
        userListMap.forEach((user, messages) -> {
            messages.forEach(message -> message.delete().submit());
            report.append("Удалено ").append(messages.size()).append(" сообщений от пользователя ").append(user.getAsTag()).append("\n");
        });
        if (report.length() != 0) {
            report.insert(0, "Доклад о цензуре канала:\n");
            textChannel.sendMessage(report).submit();
        }
    }

    public boolean hasBlockWords(String message) {
        message = message.toLowerCase();
        return blockList.stream().anyMatch(message::contains);
    }
}
