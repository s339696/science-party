package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.print.Printer;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.database.DatabaseConnect;
import model.manager.PerkManager;
import model.models.Perk;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.ResourceBundle;

/**
 * Created by Richard on 16.03.2016.
 */
public class PerkController implements Initializable, Serializable {

    @FXML
    ImageView qrImageView;

    @FXML
    private ListView<Perk> perkListView;

    @FXML
    private javafx.scene.control.Label topicLabel;

    /**
     * contains the qr codes as Images
     */
    static ObservableMap<Integer, Image> qrMap = FXCollections.observableHashMap();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            PerkManager.refreshPerkList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            makeQrMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        perkListView.setItems(PerkManager.perkList);



    }





    /**
     * shows the appropriate image for the selected qr code
     */
    public void handlePerksInSelect(){
        try {
            qrImageView.setImage(qrMap.get(perkListView.getSelectionModel().getSelectedItem().getId()));
            topicLabel.setText("Thema: " + perkListView.getSelectionModel().getSelectedItem().getTopicName());
        }catch (NullPointerException e){
            e.getMessage();
        }
    }

    /**
     * fill the qrMap with data
     *
     * @throws IOException
     * @throws WriterException
     */
    public static void makeQrMap() throws IOException, WriterException {
        qrMap.clear();
        for (Perk p : PerkManager.perkList) {
            qrMap.put(p.getId(), generateQr(p.getQrCode()));
        }

    }

    /**
     * generating the qr code
     *
     * @param qrText                    string to decode
     * @return                          qr code as image
     * @throws WriterException
     * @throws IOException
     */
    public static Image generateQr(String qrText) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        byte[] data = out.toByteArray();
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        return image;
    }

    /**
     * creates a print job for printing the selected qr code
     */
    public void printQr(){
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(qrImageView.getImage(), null);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                // Get the upper left corner that it printable
                int x = (int) Math.ceil(pageFormat.getImageableX());
                int y = (int) Math.ceil(pageFormat.getImageableY());
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                graphics.drawImage(bufferedImage, x, y, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                return PAGE_EXISTS;
            }
        });
        try {
            printerJob.print();
        } catch (PrinterException e1) {
            e1.printStackTrace();
        }
    }

    public void deletePerksPerTopic(String tName) throws IOException, WriterException {
        try {
            for (Perk perk :
                    PerkManager.perkList) {
                System.out.println(perk.toString());
                if (perk.getTopicName().equals(tName)) {
                    System.out.println(perk.toString() + "soll entfernt werden");
                    PerkManager.perkList.remove(perk);
                    qrImageView.setImage(null);
                    topicLabel.setText("");
                }
            }
        }catch (ConcurrentModificationException e){
            //System.out.printf(e.getMessage());
        } catch (NullPointerException e){

        }

    }

    public void setList() throws IOException, WriterException {
        perkListView.setItems(null);
        perkListView.setItems(PerkManager.perkList);
        makeQrMap();
    }




    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");
        DatabaseConnect.setServerAddress("http://localhost:9000");

        PerkManager.refreshPerkList();
        for (Perk p :
                PerkManager.perkList) {
            System.out.println("name: " + p.getPerkName());
            System.out.println("qr:   " + p.getQrCode());

        }


    }

}
