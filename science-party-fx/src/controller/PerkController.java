package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;
import javafx.beans.value.ObservableMapValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.print.Printer;
import javafx.scene.control.ListView;
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
import java.util.ResourceBundle;

/**
 * Created by Richard on 16.03.2016.
 */
public class PerkController implements Initializable {

    @FXML
    ImageView qrImageView;

    @FXML
    private ListView<Perk> perkListView;

    ObservableMap<Integer, Image> qrMap = FXCollections.observableHashMap();

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

    public void handlePerksInSelect(){
        qrImageView.setImage(qrMap.get(perkListView.getSelectionModel().getSelectedItem().getId()));
    }


    public void makeQrMap() throws IOException, WriterException {
        for (Perk p : PerkManager.perkList) {
            qrMap.put(p.getId(), generateQr(p.getQrCode()));
        }
    }

    public Image generateQr(String qrText) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        byte[] data = out.toByteArray();
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        return image;
    }

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
