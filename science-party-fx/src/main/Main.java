package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by Richard on 22.02.2016.
 */
public class Main extends Application {

    /**
     * Declaration of the mainStage so that it is possible to change the stage from anywhere else.
     */
    public static Stage mainStage;

    /**
     * Starting the application.
     *
     * @param primaryStage
     * @throws Exception        if an error occurs while loading the FXML-file
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage =primaryStage;
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("../view/SPLogo.png")));
        Parent root = FXMLLoader.load(getClass().getResource("../view/gui_login.fxml"));
        primaryStage.setTitle("Sciene Party Author Tool");

        primaryStage.setScene(new Scene(root, 949, 632));
        primaryStage.show();

    }

    /**
     * Declaration of an simple PopupWindow on the mainStage.
     *
     * @param text              the text which will be presented
     */
    public static void showPopup(String text){
        Stage popup = new Stage();
        popup.initModality(Modality.NONE);
        popup.setResizable(false);
        popup.initOwner(mainStage);

        popup.setScene(new Scene(new Label(text)));
        popup.show();
    }

    /**
     * Launch of the application.
     * @param args
     */
    public static void main(String[] args) {
      launch(args);
    }

}
