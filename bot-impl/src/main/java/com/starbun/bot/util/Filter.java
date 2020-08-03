package com.starbun.bot.util;

import com.starbun.bot.commands.Command;
import com.starbun.bot.commands.CommandType;
import com.starbun.bot.exceptions.CommandException;
import com.starbun.bot.handlers.CommandHandler;
import com.starbun.bot.listeners.MessageListener;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class Filter {

    private final Map<String, String> stringToCommandName = new HashMap<>();
    private final MessageListener messageListener;
    private final BeanFactory beanFactory;
    private Pattern commandPattern;

    @Value("${bot.bot.prefix}")
    private String prefix;

    @Value("#{'${bot.prefix}'.length()}")
    private int prefixLength;


    @PostConstruct
    public void init() {
        commandPattern = Pattern.compile("^(" + prefix + ").*");
        // Пока не осилил как в Guava создать карту с множественным ключом для одного значения
        Arrays.stream(CommandType.values()).forEach(commandType -> {
            List<String> aliases = commandType.getAliases();
            aliases.forEach(alias -> stringToCommandName.put(alias, commandType.getCorrespondCommandHandler().getSimpleName()));
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
        String commandHandlerClassName = stringToCommandName.get(coreCommand);
        if (commandHandlerClassName == null) {
            throw new CommandException("Несуществующая команда");
        }
        // TODO Записать в мапу название бинов. Произвести преобразование ниже в методе init()
        StringBuilder beanDefinition = new StringBuilder(commandHandlerClassName);
        beanDefinition.deleteCharAt(0);
        beanDefinition.insert(0, commandHandlerClassName.substring(0,1).toLowerCase());

        CommandHandler<? extends Command> commandHandler = (CommandHandler<?>)beanFactory.getBean(beanDefinition.toString());
        Command command = commandHandler.prepare(event);
        command.execute();
    }
}
