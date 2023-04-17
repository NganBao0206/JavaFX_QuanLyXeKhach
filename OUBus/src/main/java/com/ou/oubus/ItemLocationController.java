/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Location;
import com.ou.pojo.Route;
import com.ou.service.LocationService;
import com.ou.service.RouteService;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
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

    @FXML
    private Text txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnEdit;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s]*")) {
                txtName.setText(newValue.replaceAll("[^\\p{L}\\s]", ""));
            }
        });
    }

    public void setId(int id) {
        txtId.setText(id + "");
    }

    public void setName(String name) {
        txtName.setText(name);
    }

    // 1: THÀNH CÔNG
    // 0: NOTHING
    // -1:  THẤT BẠI
    // 2: BẮT ĐẦU CHỈNH SỬA
    public int clickBtnEdit() throws SQLException {
        if (txtName.isEditable()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn xác nhận không?");

            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                LocationService ls = new LocationService();
                Location check = ls.getLocation(Integer.parseInt(txtId.getText()), formatString(txtName.getText()));
                if (check == null) {
                    Location lct = new Location(Integer.parseInt(txtId.getText()), formatString(txtName.getText()));
                    btnEdit.getStyleClass().remove("isPressed");
                    txtName.setEditable(false);
                    if (ls.editLocation(lct)) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    Alert warning = new Alert(AlertType.ERROR);
                    warning.setHeaderText("Đã tồn tại địa điểm " + formatString(txtName.getText()));
                    warning.show();
                    return -1;
                }
            }
            return 0;
        } else {
            btnEdit.getStyleClass().add("isPressed");
            txtName.setEditable(true);
            txtName.requestFocus();
            return 2;
        }
    }

    public boolean clickBtnDel() throws SQLException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa không?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            RouteService rs = new RouteService();
            List<Route> routes = rs.getRoutes(Integer.parseInt(txtId.getText()));
            if (!routes.isEmpty()) {
                Alert warning = new Alert(AlertType.ERROR);
                warning.setTitle("Không thể xóa");
                warning.setHeaderText("Có tuyến xe sử dụng địa điểm này, không thể xóa!");
                warning.showAndWait();
                return false;
            } else {
                LocationService ls = new LocationService();
                if (ls.deleteLocation(Integer.parseInt(txtId.getText()))) {
                    Alert al = new Alert(AlertType.INFORMATION);
                    al.setHeaderText("Xóa thành công");
                    al.showAndWait();
                    return true;
                } else {
                    Alert al = new Alert(AlertType.ERROR);
                    al.setHeaderText("Xóa không thành công");
                    al.showAndWait();
                    return false;
                }

            }
        } else {
            return false;
        }
    }
    
    public String formatString(String input) {
        input = input.toLowerCase();
        String[] words = input.split("\\s+"); // Split into words
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 1) {
                result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
            } else {
                result.append(word.toUpperCase()).append(" ");
            }
        }
        return result.toString().trim(); // Remove trailing whitespace
    }

}
