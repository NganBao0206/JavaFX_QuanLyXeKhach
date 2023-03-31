/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Location;
import com.ou.service.LocationService;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author yuumm
 */
public class ItemLocationController implements Initializable {

    @FXML private Text txtId;
    @FXML private TextField txtName;
    @FXML private Button btnEdit;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setId (int id)
    {
        txtId.setText(id + "");
    }
    
    public void setName (String name)
    {
        txtName.setText(name);
    }
    
    // 1: THÀNH CÔNG
    // 0: NOTHING
    // -1:  THẤT BẠI
    // 2: BẮT ĐẦU CHỈNH SỬA
    public int clickBtnEdit () throws SQLException
    {
        if (txtName.isEditable())
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn xác nhận không?");

            alert.getButtonTypes().setAll( ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES)
            {
                LocationService ls = new LocationService();
                Location lct = new Location(Integer.parseInt(txtId.getText()), txtName.getText());
                btnEdit.getStyleClass().remove("isPressed");
                txtName.setEditable(false);
                if (ls.editLocation(lct))
                    return 1;
                else
                    return -1;
            }
            return 0;
        }
        else
        {
            btnEdit.getStyleClass().add("isPressed");      
            txtName.setEditable(true);
            txtName.requestFocus();
            return 2;
        }
    }
    
}
