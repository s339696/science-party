package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Richard on 22.02.2016.
 */
public class Main extends Application {


    @FXML TextField tf;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/gui_user.fxml"));
        primaryStage.setTitle("Sciene Party Author Tool");

        tf = new TextField("hallo");


        System.out.println(tf.getText());
        
        primaryStage.setScene(new Scene(root, 949, 632));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
