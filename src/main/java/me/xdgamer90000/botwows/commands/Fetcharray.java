package me.xdgamer90000.botwows.commands;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;//a rei list
import java.util.Iterator;

public class Fetcharray {
    public static Long expiry = null;
    public static boolean isExpired(){
        return System.currentTimeMillis() >= expiry + 604800000;
    }
    public static String endpoint = "https://api.worldofwarships.com/wows/encyclopedia/ships/?application_id=16c24f80db74346c4465bdf449dfca15&fields=name";
    static OkHttpClient client = new OkHttpClient();

    public static void run(ArrayList names, int currentPage) throws IOException {
        if (currentPage==1){
            expiry=System.currentTimeMillis();
        }
        Request request = new Request.Builder()
                .url(endpoint+"&page_no="+String.valueOf(currentPage))
                .build();

        try (Response response = client.newCall(request).execute()) {
            //System.out.println(response.body().string());
            String data = response.body().string();
            System.out.println(data);
            JSONObject jsonResponse = new JSONObject(data);
            JSONObject shipData = jsonResponse.getJSONObject("data");
            JSONObject meta = jsonResponse.getJSONObject("meta");
            int lastpage = meta.getInt("page_total");
            for (Iterator<String> it = shipData.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject nameObject = shipData.getJSONObject(id);
                String name = nameObject.getString("name");
                if(!name.startsWith("[")&&!name.endsWith("]")){
                    names.add(name);
                }
            }
            if(currentPage!=lastpage){
                Thread.sleep(200);
                run(names,currentPage+1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
