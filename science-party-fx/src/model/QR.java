package model;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;

/**
 * Created by Richard on 22.02.2016.
 */
public class QR {

    String name;
    String url;

    public QR(String name){
        this.name = name;
        this.url= "hier Pfad einfügen" + name;
    }

    public BufferedImage createQR(String name){
        int x = 200;
        int y = 200;
        BufferedImage qrImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

        // hier QR Generator im Internet suchen

        return qrImage;

    }

    public void saveQR(BufferedImage img, String url, String name){
        // name und url in DB schreiben

        // Bild an entsprechender Stelle abspeichern
    }

}
