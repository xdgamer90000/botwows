package me.xdgamer90000.botwows.commands;

public class Player {
    long userID;
    boolean division;
    String ign;
    String server;
    int score;
    String shipUsed;

    public Player(long userID, boolean division, String ign, String server,int score, String shipUsed){
        this.userID = userID;
        this.division = division;
        this.ign = ign;
        this.server = server;
        this.score = score;
        this.shipUsed = shipUsed;
    }
}
