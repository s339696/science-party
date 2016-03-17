package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Richard on 16.03.2016.
 */
public class PerkController implements Initializable {

    @FXML
    private ListView<String> perkListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (int i = 0; i < 30; i++) {
            list.add("Testeintrag-" + i);
        }


        perkListView.setItems(list);
        perkListView.setCellFactory(TextFieldListCell.forListView());
        perkListView.setEditable(true);
        perkListView.edit(0);
    }

}
