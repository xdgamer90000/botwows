package me.xdgamer90000.botwows.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

public class Wowsnumbers extends Command
{
    public Wowsnumbers() {
        this.name = "numbers";
        this.aliases = new String[]{"stats"};
        this.help = "displays stats for a player";
    }
    private String getURL(String username) throws URISyntaxException, IOException, InterruptedException {
        String url = "https://na.wows-numbers.com/search.ajax?query="+username;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
            JSONObject jsonResponse = new JSONObject(response.body());
            Integer length = jsonResponse.getInt("length");
            if(length == 0){
                return null;
            }
            else{
                JSONArray userdata = jsonResponse.getJSONArray("data");
                boolean found = false;
                for(int i = 0;userdata.length()>i;i++){
                    JSONObject user = userdata.getJSONObject(i);
                    String nickname = user.getString("nickname");
                    if(nickname.equals(username)){
                        return "https://na.wows-numbers.com"+user.getString("url");
                    }
                }
                JSONObject user = userdata.getJSONObject(0);
                return "https://na.wows-numbers.com" + user.getString("url");

            }
        }
        else{
            return null;
        }
    }
    private HashMap getdata(String url, String ship) throws URISyntaxException, IOException, InterruptedException {
        HashMap<String,String> output = new HashMap<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
            Document htmldoc=Jsoup.parse(response.body());
            List<Element>scripts = htmldoc.select("script");
            String rawdata = null;
            for (Element script: scripts) {
                String scriptdata = script.data().trim();
                if (scriptdata.startsWith("var dataProvider")){
                    int start = scriptdata.indexOf("dataProvider.ships =");
                    int end = scriptdata.indexOf("dataProvider.accountId");
                    rawdata = scriptdata.substring(start, end).replaceFirst("dataProvider.ships = ","" );
                    break;
                }
            }
            if (rawdata==null){
                return null;
            }
            else {
                JSONArray jsondata = new JSONArray(rawdata);
                JSONObject shipinfo =null;
                for (int i = 0; jsondata.length()>i;i++){
                    JSONObject shipdata =jsondata.getJSONObject(i);
                    String shipname = shipdata.getJSONObject("ship").getString("name");
                    if(shipname.equals(ship)){
                        shipinfo = shipdata;
                        break;
                    }
                }
                if (shipinfo == null){
                    return null;
                }
                else {
                    output.put("shipavgdmg", shipinfo.getString("average_damage"));
                    output.put("shipbattles", shipinfo.getString("battles"));
                    output.put("shipwr", shipinfo.getString("win_rate"));
                    output.put("shippr", shipinfo.getString("rating"));
                    output.put("shipprcol", shipinfo.getString("rating_color"));
                    output.put("shipkills", String.valueOf(shipinfo.getFloat("average_frags")));
                    output.put("shipplanes", String.valueOf(shipinfo.getFloat("average_planes_killed")));
                    output.put("shipimage", shipinfo.getJSONObject("ship").getJSONObject("images").getString("small"));
                    return output;
                }
            }
        }
        else{
            return null;
        }
    }

    @Override
    protected void execute(CommandEvent event) {
        String messageContent = event.getMessage().getContentDisplay();
        System.out.println("hello world");
        String[] splitMessage = messageContent.split(" ");
        String username = splitMessage[1];
        String ship = "";
        if(splitMessage.length>2){
            for(int i = 2;splitMessage.length>i;i++){
                ship=ship+" "+splitMessage[i];
            }
            try {
                String userid = getURL(username);
                HashMap<String,String> research = getdata(userid,ship.trim());
                MessageEmbed embed = new EmbedBuilder()
                        .setColor(Color.decode(research.get("shipprcol").replaceFirst("#","0x")))
                        .setTitle(ship+" ("+username+")")
                        .setDescription("**Battles**: " + research.get("shipbattles"))
                        .addField("PR", research.get("shippr"),false)
                        .addField("Average Damage", research.get("shipavgdmg"),false)
                        .addField("Win Rate", research.get("shipwr"), false)
                        .addField("Average Kills", research.get("shipkills"),false)
                        .addField("Average Plane Kills", research.get("shipplanes"),false)
                        .setThumbnail(research.get("shipimage"))
                        .build();
                if(userid == null){
                    event.reply("this potato does not exist");
                }
                else{
                    event.getEvent().getGuildChannel().sendMessage(
                            new MessageCreateBuilder()
                                    .setEmbeds(embed).build()
                    );
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (splitMessage.length==2){
            try {
                String userid = getURL(username);
                event.reply(userid);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            event.reply("does not exist");
        }
    }
}