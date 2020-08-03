package com.starbun.bot.exceptions;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class CommandException extends DiscordBotException {
    public EmbedBuilder eb;

    public CommandException(String message) {
        super(message);
        eb = new EmbedBuilder();
        eb.setTitle("Исключиительная ситуация", "https://github.com/AminevRamil/StarBunBot");
        eb.setDescription(this.toString());
        eb.setColor(new Color(235, 192, 0));
        eb.setThumbnail("https://avatars1.githubusercontent.com/u/41346424?s=460&u=4ef760d7b73102d55baf43237e33e32c37e3b2b6&v=4");

    }
    public CommandException(String message, EmbedBuilder eb) {
        super(message);
        this.eb = eb;
        eb.setDescription(this.toString());
    }
}
