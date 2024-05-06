package me.xdgamer90000.botwows.commands;

import me.xdgamer90000.botwows.App;
import me.xdgamer90000.botwows.Config;
import me.xdgamer90000.botwows.SlashCommands;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EndContest extends SlashCommands {
    private static final String name = "endcontest";
    private static final CommandData command = Commands.slash(name,"Tally the winners for the contest");

    public EndContest() {
        super(name,command);
    }

    public String division(boolean div){
        if(div){
            return "in a division";
        }else{
            return "alone";
        }
    }

    @Override
    public void handleEvent(SlashCommandInteractionEvent event) {
        System.out.println("command received, ending...");
        Role allowedRole = App.getJDA().getRoleById(Config.getAllowedRole());
        List<Role> roles = Objects.requireNonNull(event.getMember()).getRoles();
        if (roles.contains(allowedRole)) {
            ArrayList<Player> winners = null;
            try {
                ArrayList<Player> standings = CompileResults.sort(CompileResults.parser(CompileResults.reader()));
                CompileResults.toCSV(standings);
                winners = CompileResults.winners(standings);
            } catch (IOException e) {
                e.printStackTrace();
            }

            event.reply(new MessageCreateBuilder().addContent("The Monthly Contest has come to an end, :MaltaWUT: :MaltaPog: \n\nWinners: :MaltaStonks: \n\n" +
                    "1st Place: <@" +winners.get(0).userID+"> Using: "+winners.get(0).shipUsed+" Score: "+winners.get(0).score+" Server: "+winners.get(0).server+" "+division(winners.get(0).division)+"\n"
                    +"2nd Place: <@"+winners.get(1).userID+"> Using: "+winners.get(1).shipUsed+" Score: "+winners.get(1).score+" Server: "+winners.get(1).server+" "+division(winners.get(1).division)+"\n"
                    +"3rd Place: <@"+winners.get(2).userID+"> Using: "+winners.get(2).shipUsed+" Score: "+winners.get(2).score+" Server: "+winners.get(2).server+" "+division(winners.get(2).division)+"\n"
                    +"Raffle Winner: <@"+winners.get(3).userID+"> Using: "+winners.get(3).shipUsed+" Score: "+winners.get(3).score+" Server: "+winners.get(3).server+" "+division(winners.get(3).division)+"\n"
                    +"Raffle Winner: <@"+winners.get(4).userID+"> Using: "+winners.get(4).shipUsed+" Score: "+winners.get(4).score+" Server: "+winners.get(4).server+" "+division(winners.get(4).division)+"\n"
                    +"Raffle Winner: <@"+winners.get(5).userID+"> Using: "+winners.get(5).shipUsed+" Score: "+winners.get(5).score+" Server: "+winners.get(5).server+" "+division(winners.get(5).division)+"\n"
                    +"\n\n(All data is compiled by <@378342991410102272> developed by <@362055945121038343>)\n\nCongratulations to all who have taken part, :MaltaGrober: \n"
                    +"Please private message <@278984293135679488> to claim your prize, the codes will be sent to you by private message\n" +
                    "New contest will be announced at a later date. :MaltaZao: ").build()).queue();
        }else{
            event.reply("You do not have the authority to end a contest").queue();
        }
    }


}
