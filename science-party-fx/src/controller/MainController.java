package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.database.DatabaseConnect;

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

    @FXML
    private SplitPane QuizSplitPane;

    @FXML
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

        String email = LoginEmail.getText();
        String password = LoginPassword.getText();

        DatabaseConnect.setRecentUser(email, password);

        if(DatabaseConnect.connectedToDatabase()){
            LoadMainWindow();
        } else {
            System.out.println("Anmeldung fehlgeschlagen");
        }

    }



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

        standard.Main.mainStage.setScene(new Scene(mainTabPane));
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