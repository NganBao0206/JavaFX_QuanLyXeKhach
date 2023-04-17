/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Ticket;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Transform;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Hi
 */
public class TicketExportController implements Initializable {

    private List<Ticket> tickets;
    private List<TicketController> controllers = new ArrayList<>();
    @FXML
    private VBox vbTicket; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void loadTickets() throws IOException {
        for (int i = 0; i < tickets.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ticket.fxml"));
            Parent root = loader.load();
            TicketController controller = loader.getController();
            controller.setTxtBus(tickets.get(i).getLicensePlates());

            controller.setTxtBusNum(String.valueOf(tickets.get(i).getBusId()));
            controller.setTxtBus(tickets.get(i).getLicensePlates());
            controller.setTxtStaff(tickets.get(i).getStaffName());
            controller.setTxtCode(tickets.get(i).getId());
            controller.setImgQR(tickets.get(i).getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = tickets.get(i).getDepartureTime().format(formatter);
            controller.setTxtDate(formattedDate);
            DateTimeFormatter formattertime = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = tickets.get(i).getDepartureTime().format(formattertime);
            controller.setTxtTime(formattedTime);
            controller.setTxtCusName(tickets.get(i).getCusName());
            controller.setTxtPhone(tickets.get(i).getCusPhone());
            controller.setTxtSeat(tickets.get(i).getSeatName());
            controller.setTxtPhone(tickets.get(i).getCusPhone());
            controllers.add(controller);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            double value = tickets.get(i).getTicketPrice() * 1000;
            controller.setTxtPrice(currencyFormat.format(value));
            
            vbTicket.getChildren().add(root);
        }
        vbTicket.setPrefHeight(331*tickets.size());
    }

    public void export() throws IOException {
        for (int i = 0; i < vbTicket.getChildren().size(); i++) {
            Node node = vbTicket.getChildren().get(i);
            double scale = 2; // tỉ lệ phóng to
            WritableImage image = new WritableImage((int) Math.round(node.getBoundsInLocal().getWidth() * scale),
                                                    (int) Math.round(node.getBoundsInLocal().getHeight() * scale));
            SnapshotParameters params = new SnapshotParameters();
            params.setTransform(Transform.scale(scale, scale));
            node.snapshot(params, image);

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(1400, 600));
                document.addPage(page);
                
                PDImageXObject imageObject = LosslessFactory.createFromImage(document, SwingFXUtils.fromFXImage(image, null));
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(imageObject, 0, 0);
                }
                
                document.save(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replaceAll("[^a-zA-Z0-9]", "") + controllers.get(i).getTxtCode().replaceAll("[^a-zA-Z0-9]", "") + ".pdf");
                
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Xuất vé thành công");
                al.showAndWait();
                App.setRoot("employee");
            }
        }
    }

    
    public void quit() throws IOException{
        App.setRoot("employee");
    }
}
