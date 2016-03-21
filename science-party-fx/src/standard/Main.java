package standard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;


/**
 * Created by Richard on 22.02.2016.
 */
public class Main extends Application {

    /**
     * Declaration of the mainStage so that it is possible to change the stage from anywhere else
     */
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage =primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../view/gui_login.fxml"));
        primaryStage.setTitle("Sciene Party Author Tool");

        primaryStage.setScene(new Scene(root, 949, 632));
        primaryStage.show();

    }

    public static void showPopup(String text){
        Stage popup = new Stage();
        popup.initModality(Modality.NONE);
        popup.setResizable(false);
        popup.initOwner(mainStage);

        popup.setScene(new Scene(new Label(text)));
        popup.show();

    }


    public static void main(String[] args) {
      launch(args);
    }

}
