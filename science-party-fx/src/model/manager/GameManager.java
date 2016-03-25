package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.database.DatabaseConnect;
import model.models.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Richard on 24.03.2016.
 */
public class GameManager {
    public static ObservableList<Game> gameList = FXCollections.observableArrayList();

    public static String getAllGamesJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/games/list";
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

    public static void refreshGameList() throws IOException {
        ObservableList<Game> list = FXCollections.observableArrayList();
        String allGamessJson = GameManager.getAllGamesJson();
        ObjectMapper mapper = new ObjectMapper();
        List<Game> gameList = mapper.readValue(allGamessJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Game.class));

        for (Game game :
                gameList) {
            list.add(game);
        }
        GameManager.gameList=list;

    }

    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");
        DatabaseConnect.setServerAddress("http://localhost:9000");

        System.out.println(GameManager.getAllGamesJson());
        refreshGameList();

        for (Game game :
                gameList) {
            System.out.println(game.getName());
            System.out.println(game.getWhenCreated());
        }

    }

}
