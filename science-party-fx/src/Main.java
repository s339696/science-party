import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Richard on 22.02.2016.
 */
public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/gui_user.fxml"));
        primaryStage.setTitle("Sciene Party Author Tool");



        primaryStage.setScene(new Scene(root, 949, 632));
        primaryStage.show();

    }


    public static void main(String[] args) {

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add(0, "hallo");

       // System.out.println(list.get(0));


      launch(args);
    }


}
