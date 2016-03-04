package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Database;
import model.User;
import model.manager.UserManager;
import sun.applet.Main;
import sun.rmi.runtime.Log;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Richard on 26.02.2016.
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane LoginMainPage;

    @FXML
    private TextField LoginEmail;

    @FXML
    private TextField LoginPassword;

    @FXML
    private TextField LoginServer;

    @FXML
    private Button LoginButton;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab UserTab;

    @FXML
    private Tab QuizTab;

    @FXML
    private Tab StatisticsTab;

    @FXML
    private Tab QrTab;

    private Node node;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void checkLogin() throws IOException {
        String email = LoginEmail.getText();
        String password = LoginPassword.getText(); //Password wird auf dem Server auf MD5 gespeicher -> muss also erst umgewandelt werden
        String MD5Passoword = getMD5fromString(password);
        String server = LoginServer.getText();

        boolean authentificated = false;

        UserManager userManager = new UserManager();

        Database.setRecentUser(email, MD5Passoword);
        System.out.println(Database.recentUser);

        String s =Database.sendPost("user/list");
        System.out.println(s);


        if(s.equals("Bad Request")){
            System.out.println("Login fehlgeschlagen");
        }else {
            System.out.println("Scheiße!!");

            stage=(Stage) LoginButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("../view/gui_main.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            LoadMainWindow();
            System.out.println(mainTabPane.getTabs().toString());


        }



    }




/*
        List<User> userList = userManager.getAllUsers();
        System.out.println(userList.toString());
        for(User user : userList){
            if(user.getEmail().equals(email) && user.getPassword().equals(MD5Passoword)){
                authentificated=user.isAuthor();
                System.out.println("alter geil es läuft");
            }
        }
        */





    public static String getMD5fromString(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return (new HexBinaryAdapter()).marshal(md5.digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

// MainWindow wird erst nach den einzelnen tabs geladen!!!!
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
        TabPane mainContent = null;
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
