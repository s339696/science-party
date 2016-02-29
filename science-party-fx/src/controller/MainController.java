package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Richard on 26.02.2016.
 */
public class MainController implements Initializable {

    @FXML
    private Tab UserTab;

    @FXML
    private Tab QuizTab;

    @FXML
    private Tab StatisticsTab;

    @FXML
    private Tab QrTab;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleTab() throws IOException {
        if(UserTab.isSelected()){
            FXMLLoader userTabLoader = new FXMLLoader();
            //System.out.println(loader.getLocation());
            userTabLoader.setLocation(getClass().getResource("../view/gui_user.fxml"));
            System.out.println(userTabLoader.getLocation());
           // loader.load(getClass().getResource("../view/gui_user.fxml"));
            SplitPane sp = userTabLoader.load();

            UserTab.setContent(sp);
        } else if (QuizTab.isSelected()){
            //FXMLLoader

        } else if (StatisticsTab.isSelected()){

        } else if (QrTab.isSelected()){

        }
    }






}
