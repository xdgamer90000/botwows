package me.xdgamer90000.botwows;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class SlashCommands {
    String name;
    CommandData command;
    public abstract void handleEvent(SlashCommandInteractionEvent event);
    public SlashCommands(String name, CommandData command){
        this.name = name;
        this.command = command;
    }
    public void handleAutocomplete(CommandAutoCompleteInteractionEvent event, String focussedOption){}
}