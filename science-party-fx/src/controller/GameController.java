package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import model.manager.GameManager;
import model.models.Game;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Richard on 24.03.2016.
 */
public class GameController implements Initializable {

    @FXML
    BarChart BarChart;

    @FXML
    CategoryAxis AxisX;

    @FXML
    Button reloadButton;

    ObservableList<String> dayNames = FXCollections.observableArrayList();
    Integer[][] dataArray = new Integer[7][1];
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDataArray();
        try {
            GameManager.refreshGameList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dayNames.addAll(Arrays.asList("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"));
        AxisX.setCategories(dayNames);
        setBarChartData();
    }

    public void initializeDataArray(){
        for (int i = 0; i < 7; i++) {
            dataArray[i][0]=0;
        }
    }

    public void setBarChartData(){


        computeBarChartData();


        BarChart.getData().add(addBarChartData());
    }

    public XYChart.Series addBarChartData(){
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        for (int i = 0; i < 7; i++) {
            series.getData().add(new XYChart.Data<>(dayNames.get(i), dataArray[i][0]));
        }

        return series;
    }

    public void computeBarChartData(){
        for (Game game :
                GameManager.gameList) {
            switch(game.getDay()){
                case "Mo":
                    dataArray[0][0]+=1;
                    break;
                case "Di":
                    dataArray[1][0]+=1;
                    break;
                case "Mi":
                    dataArray[2][0]+=1;
                    break;
                case "Do":
                    dataArray[3][0]+=1;
                    break;
                case "Fr":
                    dataArray[4][0]+=1;
                    break;
                case "Sa":
                    dataArray[5][0]+=1;
                    break;
                case "So":
                    dataArray[6][0]+=1;
                    break;
            }
        }
    }

    public void reloadStatistic(){
        initializeDataArray();
        try {
            GameManager.refreshGameList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        computeBarChartData();
        System.out.println("neuladen fertisch");
        BarChart.getData().set(0, addBarChartData());
    }



}
