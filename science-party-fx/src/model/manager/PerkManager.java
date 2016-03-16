package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Perk;
import model.database.DatabaseConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Richard on 14.03.2016.
 */
public class PerkManager {
    public static ObservableList<Perk> perkList = FXCollections.observableArrayList();

    public static String getAllPerksJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/perk/list";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Cookie", loginCookie);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        String jsonString = response.toString();

        return jsonString;
    }

    public static void refreshPerkList() throws IOException {
        ObservableList<Perk> list = FXCollections.observableArrayList();
        String allTopicsJson = TopicManager.getAllTopicsJson();
        ObjectMapper mapper = new ObjectMapper();
        List<Perk> topicList = mapper.readValue(allTopicsJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Perk.class));

        for(Perk perk : perkList){
            list.add(perk);
        }
        PerkManager.perkList = list;

    }

    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");
        DatabaseConnect.setServerAddress("http://localhost:9000");


        PerkManager.getAllPerksJson();
        refreshPerkList();

        System.out.println(perkList.toString());
    }
}
