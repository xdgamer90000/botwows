package me.xdgamer90000.botwows.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Hello extends Command
{
    public Hello() {
        this.name = "hello";
        this.aliases = new String[]{"hi"};
        this.help = "says hello and waits for a response";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ara Ara my ass is blasted WTF");
    }
}
