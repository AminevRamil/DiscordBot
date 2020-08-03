package com.starbun.bot.commands;

import com.starbun.bot.exceptions.CommandException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@EqualsAndHashCode(callSuper = true) //У Command есть поля answer и targetChannel
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreateCommand extends Command {

    private Guild targetGuild;
    private String targetDist;
    private String targetName;

    @Override
    public void execute() {
        try {
            answer.setDescription("--");
            if (targetDist != null) { //Если нужно создать категорию
                if (targetGuild.getCategories()
                        .stream()
                        .map(Category::getName)
                        .map(String::toLowerCase)
                        .anyMatch(string -> string.equals(targetDist.toLowerCase()))) { // Если категория уже существует
                    List<Category> categoryList = targetGuild.getCategoriesByName(targetDist, true);
                    if (categoryList.size() > 1)
                        throw new CommandException("Существует несколько категорий с данным названием", answer);
                    Category currentCategory = categoryList.get(0);

                    CreateChannelIfNotExist(currentCategory);
                } else { // Если категории ещё нет
                    Category currentCategory = targetGuild.createCategory(targetDist).submit().get();

                    CreateChannelIfNotExist(currentCategory);
                }
            } else { //Если не нужно создать категорию
                if (targetGuild.getTextChannels()
                        .stream()
                        .filter(textChannel -> textChannel.getParent() == null)
                        .map(TextChannel::getName)
                        .map(String::toLowerCase)
                        .anyMatch(string -> string.equals(targetName.toLowerCase()))) { // Если канал существует в общей стопке
                    answer.setDescription("Канал с названием \"" + targetName + "\" в общей стопке уже существует");
                } else { // Если канала не существует в общей стопке
                    targetGuild.createTextChannel(targetName).submit();
                    answer.setDescription("Создан канал с названием \"" + targetName + "\" в общей стопке");
                }
            }
        } catch (Exception e) {
            answer.setDescription(e.toString());
        } finally {
            targetChannel.sendMessage(answer.build()).submit();
        }
    }

    private void CreateChannelIfNotExist(Category currentCategory) {
        if (currentCategory.getTextChannels()
                .stream()
                .map(TextChannel::getName)
                .map(String::toLowerCase)
                .anyMatch(string -> string.equals(targetName.toLowerCase()))) { //Если канал уже существует
            answer.setDescription("Канал с названием \"" + targetName + "\" в категории \"" + targetDist + "\" уже существует");
        } else { //Если канала ещё не существует
            currentCategory.createTextChannel(targetName).submit();
            answer.setDescription("Создан канал с названием \"" + targetName + "\" в категории \"" + targetDist + "\"");
        }
    }

}
