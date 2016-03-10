package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private SplitPane UserSplitPane;

    private SplitPane QuizSplitPane;

    private SplitPane QrSplitPane;

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

    public void checkLogin() throws IOException {

/*      zu Testzwecken mal auskommentiert

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



            //System.out.println(mainTabPane.getTabs().toString());
            LoadMainWindow();
            //mainTabPane.clipProperty().


        }

*/
        LoadMainWindow();

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








    public void LoadMainWindow() throws IOException {

        MainController mc = new MainController();


        mainTabPane = FXMLLoader.load(getClass().getResource("../view/gui_main.fxml"));

        //alle Loader für die einzelnen Tabs

        UserSplitPane = FXMLLoader.load(getClass().getResource("../view/gui_user.fxml"));
        QuizSplitPane = FXMLLoader.load(getClass().getResource("../view/gui_quiz.fxml"));
        QrSplitPane = FXMLLoader.load(getClass().getResource("../view/gui_qr.fxml"));

        //FXML-Files als Inhalt der Tabs

        UserTab = new Tab("Benutzerdaten");
        UserTab.setContent(UserSplitPane);
        QuizTab = new Tab("Quiz-Editor");
        QuizTab.setContent(QuizSplitPane);
        QrTab = new Tab("QR-Editor");
        QrTab.setContent(QrSplitPane);



        mainTabPane.getTabs().add(0,UserTab);
        mainTabPane.getTabs().add(1,QuizTab);
        mainTabPane.getTabs().add(3,QrTab);


        System.out.println(UserTab.getContent());

        standard.Main.loginStage.setScene(new Scene(mainTabPane));
    }


    public static String getMD5fromString(String string) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return (new HexBinaryAdapter()).marshal(md5.digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


}