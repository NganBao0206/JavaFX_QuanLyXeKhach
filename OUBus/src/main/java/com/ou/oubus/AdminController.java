/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Location;
import com.ou.service.LocationService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author yuumm
 */
public class AdminController implements Initializable {
    private GridPane selectedTab;
    private Button selectedItem;
    @FXML private static GridPane tabContainer;
    @FXML private GridPane tabHome;
    @FXML private Button btnHome;
    @FXML private TextField txtSearchLocation;
    @FXML private TextField txtNewLocation;
    @FXML private VBox listLocations;
    
    private Button editButtonCurrent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedTab = tabHome;
        selectedItem = btnHome;
        
        loadItemLocation(null);
        this.txtSearchLocation.textProperty().addListener(e -> {
            loadItemLocation(txtSearchLocation.getText());
        });
    }    
    
    public void loadItemLocation(String keyword)
    {
        listLocations.getChildren().clear();
        LocationService ls = new LocationService();
        try {
            List<Location> locations = ls.getLocations(keyword);
            for (int i = 0; i < locations.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("itemLocation.fxml"));
                Parent root = loader.load();
                ItemLocationController a = loader.getController();
                a.setId(locations.get(i).getId());
                a.setName(locations.get(i).getName());
                Button btnE = (Button) root.lookup("#btnEdit");
                btnE.onActionProperty().set((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (editButtonCurrent == null || btnE == editButtonCurrent)
                        {
                            try {
                                int result = a.clickBtnEdit();
                                switch (result) {
                                    case 1:
                                    {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sửa thành công", ButtonType.OK);
                                        loadItemLocation(txtSearchLocation.getText());
                                        editButtonCurrent = null;
                                        alert.show();
                                        break;
                                    }
                                    case 0:
                                    {
                                        Alert alert = new Alert(Alert.AlertType.ERROR, "Sửa không thành công", ButtonType.OK);
                                        loadItemLocation(txtSearchLocation.getText());
                                        editButtonCurrent = null;
                                        alert.show();
                                        break;
                                    }
                                    case 2:
                                    {
                                        editButtonCurrent = btnE;
                                        break;
                                    }
                                }                              
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Vui lòng hoàn thành chỉnh sửa hiện tại", ButtonType.OK);
                            alert.show();
                        }
                    }
                });
                listLocations.getChildren().add(root);
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void selectItem (ActionEvent e)
    {
        selectedItem.getStyleClass().remove("menuSelectedItem");
        selectedTab.setVisible(false);
        Button btn = (Button)e.getTarget();
        selectedItem = btn;
        selectedItem.getStyleClass().add("menuSelectedItem");
        String id = btn.getId().replace("btn", "");
        GridPane tab = (GridPane) App.getScene().lookup("#tab" + id);
        selectedTab = tab;
        selectedTab.setVisible(true);
    }
    
    public void addItemLocation() throws SQLException
    {
        if(txtNewLocation.getText().isBlank())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên địa điểm mới", ButtonType.OK);
            alert.show();
        }
        else {
            LocationService ls = new LocationService();
            Location l = new Location(0, txtNewLocation.getText());
            if (ls.addLocation(l))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công", ButtonType.OK);
                alert.show();
                loadItemLocation(txtSearchLocation.getText());
                txtNewLocation.setText("");
            }
        }
    }
    
}
