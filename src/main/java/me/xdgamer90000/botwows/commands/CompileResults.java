package me.xdgamer90000.botwows.commands;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CompileResults {
    public static String reader (){
        try{
            File document = new File("data/data.json");
            Scanner reader = new Scanner(document);
            String output = "";
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                output = output+data;
            }
            reader.close();
            return output;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Player> parser (String input){
        JSONObject json = new JSONObject(input);
        ArrayList<Player> players = new ArrayList<>();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String userID = it.next();
            JSONObject playerData = json.getJSONObject(userID);
            Player newPLayer = new Player(Long.parseLong(userID),playerData.getBoolean("division"),playerData.getString("ign"),playerData.getString("server"),playerData.getInt("score"),playerData.getString("ship-used"));
            players.add(newPLayer);
        }
        return players;
    }

    public static ArrayList<Player> sort (ArrayList<Player> input){
        Comparator<Player> comparator = Comparator.comparingInt(o -> o.score);
        input.sort(comparator);
        return input;
    }

    public static void toCSV(ArrayList<Player> entries) throws IOException {
        FileWriter writer = new FileWriter("final.csv");
        for(int i = 0; i<entries.size();i++){
            String performance = entries.get(i).userID+","+entries.get(i).division+","+entries.get(i).ign+","+entries.get(i).server+","+entries.get(i).score+","+entries.get(i).shipUsed+"\n";
            writer.write(performance);
        }
        writer.close();
    }

    public static ArrayList<Player> winners(ArrayList<Player> input){
        ArrayList<Player> winners = new ArrayList<>();
        if (input.size()<=6){
            return input;
        }
        for(int i = 1; i<4;i++){
            winners.add(input.remove(input.size()-1));
        }
        for (int i = 0; i < 3; i++) {
            int random = new Random().nextInt(0,input.size());
            winners.add(input.remove(random));
        }
        return winners;
    }
}