package me.xdgamer90000.botwows.commands;

import me.xdgamer90000.botwows.SlashCommands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputData extends SlashCommands {
    private static final String name = "submit";
    private static final CommandData command = Commands.slash(name,"submit an entry for the contest")
            .addOption(OptionType.STRING,"in-game-name","your in game name for the account entered",true)
            .addOption(OptionType.STRING,"server","enter your account server",true,true)
            .addOption(OptionType.INTEGER,"score","Base xp value or damage value",true)
            .addOption(OptionType.BOOLEAN,"division","was this played in a division",true)
            .addOption(OptionType.STRING,"ship-used","ship must be tier 6-10",true,true)
            .addOption(OptionType.ATTACHMENT,"scorecard-screenshot","please submit your scorecard",true)
            .addOption(OptionType.ATTACHMENT,"team-score-screenshot","please submit your team score",true)
            ;
    public void getList() throws IOException {
        ArrayList<String> pages = new ArrayList<>();
            Fetcharray.run(pages,1);
            for (String page: pages) {
                System.out.println(page);
            }
    }

    public byte[] downloadImage(Message.Attachment image){
        String imgURL = image.getUrl();
        Request request = new Request.Builder()
                .url(imgURL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().bytes();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    static OkHttpClient client = new OkHttpClient();
    String[] servers = new String[]{"NA","EU","SEA"};
    List<String> serverList = Arrays.asList(servers);

    @Override
    public void handleEvent(SlashCommandInteractionEvent event){
        String inGameName = event.getOption("in-game-name").getAsString();
        String server = event.getOption("server").getAsString();
        Integer score = event.getOption("score").getAsInt();
        Boolean divs = event.getOption("division").getAsBoolean();
        String usedShip = event.getOption("ship-used").getAsString();
        Message.Attachment scorecard = event.getOption("scorecard-screenshot").getAsAttachment();
        Message.Attachment teamScore = event.getOption("team-score-screenshot").getAsAttachment();
        byte[] imageData = null;
        byte[] imageData2 = null;
        String username = event.getMember().getUser().getId();
        if(scorecard.isImage()&&teamScore.isImage()){
            imageData = downloadImage(scorecard);
            imageData2 = downloadImage(teamScore);
        }
        else{
            //send error message
        }
        serverList.contains(server);

        System.out.println("recieved");
        try{
            saveData(inGameName,server,score,divs,usedShip,username);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //MessageEmbed embed = new EmbedBuilder().setImage("attachment://scorecard");
        if(imageData != null && serverList.contains(server) && !divs){
            event.reply(new MessageCreateBuilder().addContent("In game name: " + inGameName + "\n" + server + "\nScore: " + score + "\nShip: " + usedShip + "\n").addFiles(FileUpload.fromData(imageData,"scorecard.jpg")).addFiles(FileUpload.fromData(imageData2,"teamScore.jpg")).build()).queue();//fix extensions
        }
        else{
            event.reply("Entry is invalid").setEphemeral(true).queue();
        }



    }
    //public boolean replaceData()
    public  void saveData(String  ign, String server, Integer score, Boolean divs, String ship, String user) throws IOException {
        File dataSubmit = new File("./data/data.json");
        File theDir = new File("./data");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        JSONObject jsonData = new JSONObject();
        JSONObject newEntry = new JSONObject()
                .put("ign", ign)
                .put("server", server)
                .put("score", score)
                .put("division", divs)
                .put("ship-used", ship);
        if(dataSubmit.exists()) {
            String initialData= "";

            try {
                Scanner myReader = new Scanner(dataSubmit);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    initialData+=data;
                }
                jsonData = new JSONObject(initialData);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else{
            dataSubmit.createNewFile();
        }
        JSONObject finalData = jsonData.put(user,newEntry);
        String finalString = finalData.toString();
        try {
            FileWriter myWriter = new FileWriter("./data/data.json");
            myWriter.write(finalString);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    ArrayList<String> ships = new ArrayList<>();

    public InputData(){
        super(name,command);
    }
    @Override
    public void handleAutocomplete(CommandAutoCompleteInteractionEvent event,String option){
        if (option.equals("ship-used")) {
            ArrayList<String> tempShips = new ArrayList<>();
            new Thread(() -> {
                if (Fetcharray.expiry == null) {
                    try {
                        Fetcharray.run(tempShips, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ships = tempShips;
                } else if (Fetcharray.isExpired()) {
                    //ships.clear();
                    try {
                        Fetcharray.run(tempShips, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ships = tempShips;
                }
            }).start();
            List<Command.Choice> options = ships.stream()
                    .filter(word -> word.toLowerCase().startsWith(event.getFocusedOption().getValue()))// only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .limit(10)
                    .collect(Collectors.toList());
            System.out.println(options);
            event.replyChoices(options).queue();
        }
        else if(option.equals("server")){
            List<Command.Choice> options = serverList.stream()
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }
}
