package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.database.DatabaseConnect;
import main.Main;

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

    /**
     * Declaration of the FXML elements.
     */
    @FXML
    private AnchorPane LoginMainPage;

    @FXML
    private TextField LoginEmail;

    @FXML
    private PasswordField LoginPassword;

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

    /**
     * Triggered if LoginButton gets pressed.
     * Checking of the typed in user data if it's valid.
     * If an error occurs a popup with a suitable message will be shown.
     *
     * @throws IOException
     */
    public void checkLogin() throws IOException {
        String email = LoginEmail.getText();
        String password = LoginPassword.getText();
        String server = LoginServer.getText();

        DatabaseConnect.setRecentUser(email, password);
        DatabaseConnect.setServerAddress(server);

        boolean connected = false;
        try {
            connected=DatabaseConnect.connectedToDatabase();
        } catch (Exception e){
            Main.showPopup("Keine Verbindung zum Server möglich!" +
                    "\n" +
                    "Bitte überprüfen Sie die Verbindung und die angegebene Adresse."
            );
        }

        String loginCookie = DatabaseConnect.getLoginCookie();
        System.out.println(loginCookie);
        if(connected && loginCookie!=null){
            try {
                LoadMainWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            Main.showPopup("Die Anmeldedaten sind ungültig!");
        }
    }


    /**
     * Adding the tabs to the mainPane and Loading the content of the tabs.
     *
     * @throws IOException
     */
    public void LoadMainWindow() throws IOException {
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
        QrTab = new Tab("Perk-Editor");
        QrTab.setContent(QrSplitPane);

        mainTabPane.getTabs().add(0,UserTab);
        mainTabPane.getTabs().add(1,QuizTab);
        mainTabPane.getTabs().add(3,QrTab);

        System.out.println(UserTab.getContent());

        main.Main.mainStage.setScene(new Scene(mainTabPane));
    }

    /**
     * Generates a MD5-String of an input string.
     *
     * @param string            string is the base for generating the MD5-String
     * @return                  MD5-String
     */
    public static String getMD5fromString(String string) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return (new HexBinaryAdapter()).marshal(md5.digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


}