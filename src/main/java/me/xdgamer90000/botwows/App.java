package me.xdgamer90000.botwows;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.xdgamer90000.botwows.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;


public class App {
    public static void main(String[] args) throws LoginException, IOException {
       Config.load();
       String token=Config.getToken();
       EventWaiter waiter = new EventWaiter();
       CommandClientBuilder client = new CommandClientBuilder();
       client.setActivity(Activity.playing("with your mind"));
       client.setPrefix("#");
       client.setOwnerId("362055945121038343");
       client.addCommands(//add commmands here later//
               new Hello(),
               new Wowsnumbers(),
               new Tonedeaftuesdaymode(),
               new Musicloop(),
               new Disconnectbot(),
               new Clearqueue()
       );

       EventListener event = new EventListener();
       jdaclient=JDABuilder.createDefault(token)
               .setStatus(OnlineStatus.ONLINE)
               .setActivity(Activity.playing("with your head"))//add things here later
               .addEventListeners(waiter, client.build(),event)
               .build();

    }
   static JDA jdaclient;

   public static JDA getJDA(){
      return jdaclient;
   }
}

