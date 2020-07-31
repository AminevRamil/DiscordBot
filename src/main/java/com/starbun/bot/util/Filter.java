package com.starbun.bot.util;

import com.starbun.bot.Bot;
import com.starbun.bot.commands.Command;
import com.starbun.bot.commands.CommandType;
import com.starbun.bot.exceptions.CommandException;
import com.starbun.bot.handlers.CommandHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Data
@Slf4j
@Component
public class Filter implements BeanFactoryAware{
    private static final Map<String, Class<? extends CommandHandler<? extends Command>>> stringToCommand = new HashMap<>();
    BeanFactory beanFactory;
    private Pattern commandPattern;
    @Value("${bot.prefix}")
    private String prefix;
    private int prefixLength;
    private Bot bot;
    @Autowired
    public Filter(Bot bot) {
        this.bot = bot;
        prefixLength = bot.getPrefix().length();
    }

    @Autowired
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void init() {
        commandPattern = Pattern.compile("^(" + prefix + ").*");
        // Пока не осилил как в Guava создать карту с множественным ключом для одного значения
        Arrays.stream(CommandType.values()).forEach(commandType -> {
            List<String> aliases = commandType.getAliases();
            aliases.forEach(alias -> stringToCommand.put(alias, commandType.getCorrespondCommandHandler()));
        });
    }

    public boolean isCommand(String message) {
        return commandPattern.matcher(message).matches();
    }

    public void execute(MessageReceivedEvent event) throws Exception {
        String rawMessage = event.getMessage().getContentRaw().trim();

        if (rawMessage.equals(prefix)) {
            event.getChannel().sendMessage("Онлайн! Напишите помощь/справка/help для получения справки").submit();
            return;
        } else rawMessage = rawMessage.substring(prefixLength + 1);

        String coreCommand = rawMessage.split("\\s")[0].toLowerCase();
        log.info("coreCommand: " + coreCommand);
        Class<? extends CommandHandler<? extends Command>> commandClass = stringToCommand.get(coreCommand);
        if (commandClass == null) throw new CommandException("Несуществующая команда");


        //CommandHandler<? extends Command> commandHandler = commandClass.getDeclaredConstructor().newInstance();
        CommandHandler<? extends Command> commandHandler = beanFactory.getBean(commandClass);
        Command command = commandHandler.prepare(event);
        command.execute();
    }
}
