package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.manager.PerkManager;
import model.models.Perk;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Richard on 16.03.2016.
 */
public class PerkController implements Initializable {

    @FXML
    private ListView<Perk> perkListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            PerkManager.refreshPerkList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        perkListView.setItems(PerkManager.perkList);
    }

}
