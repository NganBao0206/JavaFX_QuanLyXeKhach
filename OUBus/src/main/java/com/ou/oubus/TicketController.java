/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ou.oubus;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author Hi
 */
public class TicketController {
   @FXML
   private Text txtCusName;
   @FXML
   private Text txtPhone;
   @FXML
   private Text txtDeparture;
   @FXML
   private Text txtDestination;
   @FXML
   private Text txtBusNum;
   @FXML
   private Text txtSeat;
   @FXML
   private Text txtDate;
   @FXML
   private Text txtTime;
   @FXML
   private Text txtPrice;
   @FXML
   private Text txtCode;
   @FXML
   private Text txtStaff;
   @FXML
   private Text txtBus;
   @FXML
   private ImageView imgQR;
   @FXML
   private ImageView imgQR2;
   
   public void setImgQR(String id) throws IOException{
       try {
           QRCodeWriter qRCodeWriter = new QRCodeWriter();
           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           BitMatrix bit = qRCodeWriter.encode(id, BarcodeFormat.QR_CODE, 200, 200);
           
           MatrixToImageWriter.writeToStream(bit, "PNG", outputStream);
           ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
           Image qr = new Image(inputStream);
           imgQR.setImage(qr);
           
       } catch (WriterException ex) {
           Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

    public String getTxtCusName() {
        return txtCusName.getText();
    }

    public void setTxtCusName(String txtCusName) {
        this.txtCusName.setText(txtCusName);
    }

    public String getTxtPhone() {
        return txtPhone.getText();
    }

    public void setTxtPhone(String txtPhone){
        this.txtPhone.setText(txtPhone);
    }

    public String getTxtDeparture() {
        return txtDeparture.getText();
    }

    public void setTxtDeparture(String txtDeparture) {
        this.txtDeparture.setText(txtDeparture);
    }

    public String getTxtDestination() {
        return txtDestination.getText();
    }

    public void setTxtDestination(String txtDestination) {
        this.txtDestination.setText(txtDestination);
    }

    public String getTxtBusNum() {
        return txtBusNum.getText();
    }

    public void setTxtBusNum(String txtBusNum) {
        this.txtBusNum.setText(txtBusNum);
    }

    public String getTxtSeat() {
        return txtSeat.getText();
    }

    public void setTxtSeat(String txtSeat) {
        this.txtSeat.setText(txtSeat);
    }

    public String getTxtDate() {
        return txtDate.getText();
    }

    public void setTxtDate(String txtDate) {
        this.txtDate.setText(txtDate);
    }

    public String getTxtTime() {
        return txtTime.getText();
    }

    public void setTxtTime(String txtTime) {
        this.txtTime.setText(txtTime);
    }

    public String getTxtPrice() {
        return txtPrice.getText();
    }

    public void setTxtPrice(String txtPrice) {
        this.txtPrice.setText(txtPrice);
    }

    public String getTxtCode() {
        return txtCode.getText();
    }

    public void setTxtCode(String txtCode) {
        this.txtCode.setText(txtCode);
    }

    public String getTxtStaff() {
        return txtStaff.getText();
    }

    public void setTxtStaff(String txtStaff) {
        this.txtStaff.setText(txtStaff);
    }

    public String getTxtBus() {
        return txtBus.getText();
    }

    public void setTxtBus(String txtBus) {
        this.txtBus.setText(txtBus);
    }
    
}
