package me.xdgamer90000.botwows;

import me.xdgamer90000.botwows.commands.EndContest;
import me.xdgamer90000.botwows.commands.InputData;
import me.xdgamer90000.botwows.commands.RemoveData;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventListener extends ListenerAdapter {
    private HashMap<String,SlashCommands> commands = new HashMap();
    public void addCommand(SlashCommands command){
        commands.put(command.name,command);
        App.getJDA().upsertCommand(command.command).queue();
        App.getJDA().getGuildById(923682300993106011L).upsertCommand(command.command).queue();
        LoggerFactory.getLogger(EventListener.class).info("added a slash command");
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent input){
        String commandName = input.getName();
        SlashCommands command = commands.get(commandName);
        if(command != null){
            command.handleEvent(input);
        }
    }
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

        String commandName = event.getName();
        SlashCommands command = commands.get(commandName);

        if(command != null){
            command.handleAutocomplete(event,event.getFocusedOption().getName());
        }
    }

    @Override
    public void onReady(ReadyEvent event){
        addCommand(new InputData());
        addCommand(new RemoveData());
        addCommand(new EndContest());
    }

}