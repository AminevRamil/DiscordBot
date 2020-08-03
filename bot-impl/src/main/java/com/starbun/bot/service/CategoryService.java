package com.starbun.bot.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class CategoryService {

    @Value("${bot.dynamic_voice_channel}")
    private String dynamicVoiceCategoryName;

    public Category getDynamicVoiceCategory(Guild guild) {
        List<Category> categoriesByName = guild.getCategoriesByName(dynamicVoiceCategoryName, true);
        boolean result = categoriesByName.size() == 1;
        // If или switch на 0,1 и дефолт?
        if (result) {
            return categoriesByName.get(0);
        } else if (categoriesByName.size() > 1)  {
            throw new IllegalStateException("Существует несколько категорий с одинаковым названием динамической категории");
        } else {
            try {
                return guild.createCategory(dynamicVoiceCategoryName).submit().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean isDynamicCategory(Category category) {
        return category.getName().equals(dynamicVoiceCategoryName);
    }
}
