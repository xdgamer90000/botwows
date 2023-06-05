package me.xdgamer90000.botwows.commands;

import me.xdgamer90000.botwows.App;
import me.xdgamer90000.botwows.Config;
import me.xdgamer90000.botwows.SlashCommands;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RemoveData extends SlashCommands {
    private static final String name = "remove";
    private static final CommandData command = Commands.slash(name,"remove an entry from the contest")
            .addOption(OptionType.USER,"discord-username","name of user's entry to remove");
    public RemoveData() {
        super(name, command);
    }

    @Override
    public void handleEvent(SlashCommandInteractionEvent event) {
        Role allowedRole = App.getJDA().getRoleById(Config.getAllowedRole());
        List roles = event.getMember().getRoles();
        if(roles.contains(allowedRole)){
            String userID = event.getOption("discord-username").getAsUser().getId();
            try {
                removeName(userID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("removed");
            event.reply("Removed entry from <@"+userID+">").queue();
        }
        else{
            event.reply("You do not have permission to remove entries").setEphemeral(true).queue();
        }

    }
    public void removeName(String userID) throws IOException {
        File dataSubmit = new File("./data/data.json");
        File theDir = new File("./data");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        JSONObject jsonData = new JSONObject();
        if(dataSubmit.exists()) {
            String initialData= "";

            try {
                Scanner myReader = new Scanner(dataSubmit);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    initialData+=data;
                }
                jsonData = new JSONObject(initialData);
                jsonData.remove(userID);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else{
            dataSubmit.createNewFile();
        }
        String finalString = jsonData.toString();
        try {
            FileWriter myWriter = new FileWriter("./data/data.json");
            myWriter.write(finalString);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
