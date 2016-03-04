package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sun.applet.Main;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Richard on 26.02.2016.
 */
public class MainController implements Initializable {

    @FXML
    private TextField LoginEmail;

    @FXML
    private TextField LoginPassword;

    @FXML
    private TextField LoginServer;

    @FXML
    private Button LoginButton;

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
        FXMLLoader loginPageLoader = new FXMLLoader();
        loginPageLoader.setLocation(getClass().getResource("../view/gui_login.fxml"));
        AnchorPane loginPage = null;

        try {
            loginPage = loginPageLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void checkLogin(){
        String email = LoginEmail.getText();
        String password = LoginPassword.getText();
        String server = LoginServer.getText();


    }


    public void LoadMainWindow(){
        //alle Loader für die einzelnen Tabs
        FXMLLoader userTabLoader = new FXMLLoader();
        FXMLLoader quizTablLoader = new FXMLLoader();
        FXMLLoader statisticTabLoader = new FXMLLoader();
        FXMLLoader qrTabLoader = new FXMLLoader();


        // Pfadangaben der FXML-Files
        //System.out.println(loader.getLocation());
        userTabLoader.setLocation(getClass().getResource("../view/gui_user.fxml"));
        quizTablLoader.setLocation((getClass().getResource("../view/gui_quiz.fxml")));
        // statisticTabLoader.setLocation(getClass().getResource("../view/gui_statistic.fxml"));
        qrTabLoader.setLocation(getClass().getResource("../view/gui_qr.fxml"));
        //System.out.println(userTabLoader.getLocation());
        // loader.load(getClass().getResource("../view/gui_user.fxml"));


        //Laden der FXML-Files
        SplitPane userTabContent = null;
        SplitPane quizTabContent = null;
        SplitPane qrTabContent = null;

        try {
            userTabContent = userTabLoader.load();
            quizTabContent = quizTablLoader.load();
            qrTabContent = qrTabLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //FXML-Files als Inhalt der Tabs
        UserTab.setContent(userTabContent);
        QuizTab.setContent(quizTabContent);
        QrTab.setContent(qrTabContent);
    }








}
