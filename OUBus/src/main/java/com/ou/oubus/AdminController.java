/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Bus;
import com.ou.pojo.BusTrip;
import com.ou.pojo.Location;
import com.ou.pojo.Route;
import com.ou.service.BusService;
import com.ou.service.BusTripService;
import com.ou.service.LocationService;
import com.ou.service.RouteService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author yuumm
 */
public class AdminController implements Initializable {

    private GridPane selectedTab;
    private Button selectedItem;
    @FXML
    private GridPane tabHome;
    @FXML
    private Button btnHome;
    @FXML
    private TextField txtSearchLocation;
    @FXML
    private TextField txtNewLocation;
    @FXML
    private VBox listLocations;
    @FXML
    private ToggleButton toggleBtnSearchBusLine;
    @FXML
    private ToggleButton toggleBtnSearchBusLineEdit;
    @FXML
    private HBox boxSearchRouteExtra;
    @FXML
    private HBox boxSearchRouteExtraEdit;
    @FXML
    private ComboBox combxRoute;
    @FXML
    private ComboBox comBxSearchDeparture;
    @FXML
    private ComboBox comBxSearchDestination;
    @FXML
    private ComboBox comBxSearchDepartureEdit;
    @FXML
    private ComboBox comBxSearchDestinationEdit;
    @FXML
    private ComboBox combxBus;
    @FXML
    private ComboBox cbBusEdit;
    @FXML
    private ComboBox cbEditRoute;
    @FXML
    private TextField txtSurcharge;
    @FXML
    private TextField txtSurchargeEdit;
    @FXML
    private HBox HBoxTime1;
    @FXML
    private HBox HBoxTimeEdit;

    @FXML
    private Pane tabAddBusTrip;

    @FXML
    private Pane tabEditBusTrip;
    @FXML
    private Pane tabViewBusTrip;
    @FXML
    private TableView tbBusTrip;
    @FXML
    private DatePicker departureDate;
    @FXML
    private DatePicker departureDateEdit;
    @FXML
    private Text txtIDBusTrip;

    private Button editButtonCurrent;

    /**
     * Initializes the controller class.
     */
    Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 05);
    Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 00);

    Spinner<Integer> hourSpinnerEdit = new Spinner<>(0, 23, 05);
    Spinner<Integer> minuteSpinnerEdit = new Spinner<>(0, 59, 00);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            loadTableBusTripColumns();
            loadTableBusTripData(null, -1, -1);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectedTab = tabHome;
        selectedItem = btnHome;
        boxSearchRouteExtra.setManaged(false);
        loadItemLocation(null);
        this.txtSearchLocation.textProperty().addListener(e -> {
            loadItemLocation(txtSearchLocation.getText());
        });

        boxSearchRouteExtraEdit.setManaged(false);
        loadItemLocation(null);
        this.txtSearchLocation.textProperty().addListener(e -> {
            loadItemLocation(txtSearchLocation.getText());
        });

        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);
        minuteSpinner.setId("departureMinute");
        hourSpinner.setPrefWidth(80);
        hourSpinner.setId("departureHour");
        minuteSpinner.setPrefWidth(80);
        Text temp = new Text(":");
        temp.getStyleClass().addAll("normalText", "purpleText", "boldText");
        HBoxTime1.getChildren().add(hourSpinner);
        HBoxTime1.getChildren().add(temp);
        HBoxTime1.getChildren().add(minuteSpinner);

        hourSpinnerEdit.setEditable(true);
        minuteSpinnerEdit.setEditable(true);
        minuteSpinnerEdit.setId("departureMinuteEdit");
        hourSpinnerEdit.setPrefWidth(80);
        hourSpinnerEdit.setId("departureHourEdit");
        minuteSpinnerEdit.setPrefWidth(80);
        Text temp2 = new Text(":");
        temp2.getStyleClass().addAll("normalText", "purpleText", "boldText");
        HBoxTimeEdit.getChildren().add(hourSpinnerEdit);
        HBoxTimeEdit.getChildren().add(temp);
        HBoxTimeEdit.getChildren().add(minuteSpinnerEdit);

        LocationService ls = new LocationService();
        List<Location> listloc = null;
        try {
            listloc = ls.getLocations(null);
            listloc.add(null);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        comBxSearchDeparture.setItems(FXCollections.observableList(listloc));
        comBxSearchDestination.setItems(FXCollections.observableList(listloc));

        comBxSearchDepartureEdit.setItems(FXCollections.observableList(listloc));
        comBxSearchDestinationEdit.setItems(FXCollections.observableList(listloc));
        
        
        filterDeparture.setItems(FXCollections.observableList(listloc));        
        filterDestination.setItems(FXCollections.observableList(listloc));
        RouteService rs = new RouteService();
        List<Route> listrs = null;
        try {
            listrs = rs.getRoutes(null, null);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        combxRoute.setItems(FXCollections.observableList(listrs));
        cbEditRoute.setItems(FXCollections.observableList(listrs));

        BusService bs = new BusService();
        List<Bus> listbus = null;
        try {
            listbus = bs.getBuses();
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        combxBus.setItems(FXCollections.observableList(listbus));
        cbBusEdit.setItems(FXCollections.observableList(listbus));

        txtSurcharge.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        txtSurchargeEdit.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        
        
    }

    public void loadItemLocation(String keyword) {
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
                        if (editButtonCurrent == null || btnE == editButtonCurrent) {
                            try {
                                int result = a.clickBtnEdit();
                                switch (result) {
                                    case 1: {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sửa thành công", ButtonType.OK);
                                        loadItemLocation(txtSearchLocation.getText());
                                        editButtonCurrent = null;
                                        alert.show();
                                        break;
                                    }
                                    case 0: {
                                        Alert alert = new Alert(Alert.AlertType.ERROR, "Sửa không thành công", ButtonType.OK);
                                        loadItemLocation(txtSearchLocation.getText());
                                        editButtonCurrent = null;
                                        alert.show();
                                        break;
                                    }
                                    case 2: {
                                        editButtonCurrent = btnE;
                                        break;
                                    }
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
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

    public void selectItem(ActionEvent e) {
        selectedItem.getStyleClass().remove("menuSelectedItem");
        selectedTab.setVisible(false);
        Button btn = (Button) e.getTarget();
        selectedItem = btn;
        selectedItem.getStyleClass().add("menuSelectedItem");
        String id = btn.getId().replace("btn", "");
        GridPane tab = (GridPane) App.getScene().lookup("#tab" + id);
        selectedTab = tab;
        selectedTab.setVisible(true);
    }

    public void addItemLocation() throws SQLException {
        if (txtNewLocation.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên địa điểm mới", ButtonType.OK);
            alert.show();
        } else {
            LocationService ls = new LocationService();
            Location l = new Location(0, txtNewLocation.getText());
            if (ls.addLocation(l)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công", ButtonType.OK);
                alert.show();
                loadItemLocation(txtSearchLocation.getText());
                txtNewLocation.setText("");
            }
        }
    }

    public void toggleSearchBusLine() {
        if (toggleBtnSearchBusLine.isSelected()) {
            boxSearchRouteExtra.setManaged(true);
            boxSearchRouteExtra.setVisible(true);
        } else {
            boxSearchRouteExtra.setManaged(false);
            boxSearchRouteExtra.setVisible(false);
        }
    }

    public void toggleSearchBusLineEdit() {
        if (toggleBtnSearchBusLineEdit.isSelected()) {
            boxSearchRouteExtraEdit.setManaged(true);
            boxSearchRouteExtraEdit.setVisible(true);
        } else {
            boxSearchRouteExtraEdit.setManaged(false);
            boxSearchRouteExtraEdit.setVisible(false);
        }
    }

    public void selectRouteByLocation() throws SQLException {
        Location a = null;
        Location b = null;
        Object departure = comBxSearchDeparture.getSelectionModel().getSelectedItem();
        Object destination = comBxSearchDestination.getSelectionModel().getSelectedItem();
        if (departure != null && departure instanceof Location) {
            a = (Location) departure;
        }
        if (destination != null && destination instanceof Location) {
            b = (Location) destination;
        }

        if (a != null && b != null) {
            RouteService rs = new RouteService();
            Route selected = rs.getRoute(a.getName(), b.getName());
            if (selected != null) {
                int index = -1;
                for (int i = 0; i < combxRoute.getItems().size(); i++) {
                    String s1 = selected.toString();
                    String s2 = combxRoute.getItems().get(i).toString();
                    if (s1.equals(s2)) {
                        index = i;
                        break;
                    }
                }

                combxRoute.getSelectionModel().select(index);
                comBxSearchDeparture.getSelectionModel().select(null);
                comBxSearchDestination.getSelectionModel().select(null);
                toggleBtnSearchBusLine.setSelected(false);
                boxSearchRouteExtra.setManaged(false);
                boxSearchRouteExtra.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Không có tuyến xe này", ButtonType.NO);
                alert.show();
            }
        }
    }

    public void selectRouteByLocationEdit() throws SQLException {
        Location a = null;
        Location b = null;
        Object departure = comBxSearchDepartureEdit.getSelectionModel().getSelectedItem();
        Object destination = comBxSearchDestinationEdit.getSelectionModel().getSelectedItem();
        if (departure != null && departure instanceof Location) {
            a = (Location) departure;
        }
        if (destination != null && destination instanceof Location) {
            b = (Location) destination;
        }

        if (a != null && b != null) {
            RouteService rs = new RouteService();
            Route selected = rs.getRoute(a.getName(), b.getName());
            if (selected != null) {
                int index = -1;
                for (int i = 0; i < cbEditRoute.getItems().size(); i++) {
                    String s1 = selected.toString();
                    String s2 = cbEditRoute.getItems().get(i).toString();
                    if (s1.equals(s2)) {
                        index = i;
                        break;
                    }
                }

                cbEditRoute.getSelectionModel().select(index);
                comBxSearchDepartureEdit.getSelectionModel().select(null);
                comBxSearchDestinationEdit.getSelectionModel().select(null);
                toggleBtnSearchBusLineEdit.setSelected(false);
                boxSearchRouteExtraEdit.setManaged(false);
                boxSearchRouteExtraEdit.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Không có tuyến xe này", ButtonType.NO);
                alert.show();
            }
        }
    }

    public void addBusTrip() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đủ thông tin", ButtonType.OK);
        Route route = null;
        Object r = combxRoute.getSelectionModel().getSelectedItem();
        if (r != null && r instanceof Route) {
            route = (Route) combxRoute.getSelectionModel().getSelectedItem();
        } else {
            alert.show();
            return;
        }
        if (departureDate.getValue() == null) {
            alert.show();
            return;
        }
        LocalDate dDate = departureDate.getValue();
        LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));

        if (dDateTime.isBefore(LocalDateTime.now()) || dDateTime.isEqual(LocalDateTime.now())) {
            alert.setContentText("Thời điểm xuất phát không thể nhỏ hơn thời điểm hiện tại");
            alert.show();
        }
        Bus bus = null;
        Object b = combxBus.getSelectionModel().getSelectedItem();
        if (b != null && b instanceof Bus) {
            bus = (Bus) b;
        }

        if (b == null) {
            alert.show();
            return;
        }
        int surcharge;
        if (txtSurcharge.getText() == null) {
            alert.show();
        }

        try {
            surcharge = Integer.parseInt(txtSurcharge.getText().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            alert.setContentText("Nhập liệu không hợp lệ");
            alert.show();
            return;
        }

        if (surcharge < 0) {
            alert.setContentText("Phụ thu phải từ 0");
            alert.show();
        }

        BusTrip bs = new BusTrip(route.getId(), dDateTime, bus.getId(), surcharge, route.getTotalTime());
        BusTripService bts = new BusTripService();
        if (bts.isValidBusTrip(bs)) {
            if (bts.addBusTrip(bs)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Thêm thành công");
                alert.show();
            } else {
                alert.setContentText("Thêm không thành công");
                alert.show();
            }
        } else {
            alert.setContentText("Xe đã chạy chuyến khác cùng thời gian");
            alert.show();
        }
        filterBusTrip();
    }

    private void loadTableBusTripColumns() {
        TableColumn col = new TableColumn("ID");
        col.setCellValueFactory(new PropertyValueFactory("id"));
        col.setPrefWidth(50);

        TableColumn colRouteID = new TableColumn("Mã tuyến");
        colRouteID.setCellValueFactory(new PropertyValueFactory("routeId"));
        colRouteID.setPrefWidth(100);

        TableColumn colDeparture = new TableColumn("Nơi xuất phát");
        colDeparture.setCellValueFactory(new PropertyValueFactory("departureName"));
        colDeparture.setPrefWidth(130);

        TableColumn colDestination = new TableColumn("Nơi đến");
        colDestination.setCellValueFactory(new PropertyValueFactory("destinationName"));
        colDestination.setPrefWidth(130);

        TableColumn colDepartureTime = new TableColumn("Giờ xuất phát");
        colDepartureTime.setCellValueFactory(new PropertyValueFactory("departureTime"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        colDepartureTime.setCellFactory(column -> new TableCell<BusTrip, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        TableColumn colBusID = new TableColumn("Mã xe");
        colBusID.setCellValueFactory(new PropertyValueFactory("busId"));
        colBusID.setPrefWidth(70);

        TableColumn colPrice = new TableColumn("Giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        colPrice.setCellFactory(column -> new TableCell<BusTrip, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    double value = item.doubleValue() * 1000;
                    setText(currencyFormat.format(value));
                }
            }
        });
        TableColumn colSurcharge = new TableColumn("Phụ phí");

        colSurcharge.setCellValueFactory(
                new PropertyValueFactory("surcharge"));
        colSurcharge.setCellFactory(column
                -> new TableCell<BusTrip, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    double value = item.doubleValue() * 1000;
                    setText(currencyFormat.format(value));
                }
            }
        });

        TableColumn colDel = new TableColumn();
        colDel.setCellFactory(e -> {
            Button btn = new Button();
            StackPane sp = new StackPane();
            SVGPath img = new SVGPath();
            img.setContent("M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5ZM11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0H11Zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5h9.916Zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5Z");
            sp.getChildren().add(img);
            btn.setGraphic(sp);
            btn.getStyleClass().add("btnTransparent");
            btn.setOnAction(evt -> {
                Button b = (Button) evt.getSource();
                TableCell cell = (TableCell) b.getParent();
                BusTrip bt = (BusTrip) cell.getTableRow().getItem();
                if (bt != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this question?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {

                            BusTripService bts = new BusTripService();
                            try {
                                if (bts.deleteBusTrip(bt.getId()) == true) {
                                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thành công", ButtonType.OK);
                                    al.show();
                                    this.filterBusTrip();
                                } else {
                                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thất bại", ButtonType.OK);
                                    al.show();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                }

            });

            TableCell cell = new TableCell();
            cell.setGraphic(btn);
            return cell;
        });

        TableColumn colEdit = new TableColumn();
        colEdit.setCellFactory(e -> {
            Button btn = new Button();
            StackPane sp = new StackPane();
            SVGPath img = new SVGPath();
            img.setContent("M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z");
            sp.getChildren().add(img);
            btn.setGraphic(sp);
            btn.getStyleClass().add("btnTransparent");
            btn.setOnAction(evt -> {
                Button b = (Button) evt.getSource();
                TableCell cell = (TableCell) b.getParent();
                BusTrip bt = (BusTrip) cell.getTableRow().getItem();
                if (bt != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn muốn chỉnh sửa chuyến đi này?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(res -> {
                        if (res == ButtonType.YES) {
                            //id
                            txtIDBusTrip.setText(bt.getId());
                            //Route
                            int index = -1;
                            for (int i = 0; i < cbEditRoute.getItems().size(); i++) {
                                Route r = null;
                                if (cbEditRoute.getItems().get(i) instanceof Route) {
                                    r = (Route) cbEditRoute.getItems().get(i);
                                }
                                if (bt.getRouteId().equals(r.getId())) {
                                    index = i;
                                    break;
                                }
                            }

                            cbEditRoute.getSelectionModel().select(index);
                            //Datetime
                            departureDateEdit.setValue(bt.getDepartureTime().toLocalDate());
                            hourSpinnerEdit = new Spinner<>(0, 23, bt.getDepartureTime().getHour());
                            minuteSpinnerEdit = new Spinner<>(0, 59, bt.getDepartureTime().getMinute());

                            //Bus
                            index = -1;
                            for (int i = 0; i < cbBusEdit.getItems().size(); i++) {
                                int busId = bt.getBusId();
                                Bus bs = cbBusEdit.getItems().get(i) instanceof Bus ? (Bus) cbBusEdit.getItems().get(i) : null;
                                if (busId == bs.getId()) {
                                    index = i;
                                    break;
                                }
                            }
                            cbBusEdit.getSelectionModel().select(index);

                            // Surcharge
                            String surcharge = String.valueOf(bt.getSurcharge() * 1000);
                            txtSurchargeEdit.setText(surcharge);

                            tabEditBusTrip.setVisible(true);
                            tabViewBusTrip.setVisible(false);
                        }
                    });
                }

            });

            TableCell cell = new TableCell();
            cell.setGraphic(btn);
            return cell;
        });
        this.tbBusTrip.getColumns()
                .setAll(col, colRouteID, colDeparture, colDestination, colDepartureTime, colBusID, colPrice, colSurcharge, colEdit, colDel);
    }

    private void loadTableBusTripData(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
        BusTripService bts = new BusTripService();
        List<BusTrip> lbt = bts.getBusTrips(departureDate, departureId, destinationId);

        this.tbBusTrip.getItems().clear();
        this.tbBusTrip.setItems(FXCollections.observableList(lbt));
    }

    public void switchToAddBusTrip() {
        tabAddBusTrip.setVisible(true);
        tabViewBusTrip.setVisible(false);
    }

    public void backFromAddBusTrip() {
        tabAddBusTrip.setVisible(false);
        tabViewBusTrip.setVisible(true);
    }

    public void backFromEditBusTrip() {
        tabEditBusTrip.setVisible(false);
        tabViewBusTrip.setVisible(true);
    }

    public void editBusTrip() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đủ thông tin", ButtonType.OK);
        Route route = null;
        Object r = cbEditRoute.getSelectionModel().getSelectedItem();
        if (r != null && r instanceof Route) {
            route = (Route) cbEditRoute.getSelectionModel().getSelectedItem();
        } else {
            alert.show();
            return;
        }
        if (departureDateEdit.getValue() == null) {
            alert.show();
            return;
        }
        LocalDate dDate = departureDateEdit.getValue();
        LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of(hourSpinnerEdit.getValue(), minuteSpinnerEdit.getValue()));

        if (dDateTime.isBefore(LocalDateTime.now()) || dDateTime.isEqual(LocalDateTime.now())) {
            alert.setContentText("Thời điểm xuất phát không thể nhỏ hơn thời điểm hiện tại");
            alert.show();
        }
        Bus bus = null;
        Object b = cbBusEdit.getSelectionModel().getSelectedItem();
        if (b != null && b instanceof Bus) {
            bus = (Bus) b;
        }

        if (b == null) {
            alert.show();
            return;
        }
        int surcharge;
        if (txtSurchargeEdit.getText() == null) {
            alert.show();
        }

        try {
            surcharge = Integer.parseInt(txtSurchargeEdit.getText().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            alert.setContentText("Nhập liệu không hợp lệ");
            alert.show();
            return;
        }

        if (surcharge < 0) {
            alert.setContentText("Phụ thu phải từ 0");
            alert.show();
        }
        String idBusTrip = txtIDBusTrip.getText();
        BusTrip bs = new BusTrip(idBusTrip, route.getId(), dDateTime, bus.getId(), surcharge, route.getTotalTime());
        BusTripService bts = new BusTripService();
        if (bts.isValidBusTrip(bs)) {
            if (bts.editBusTrip(bs)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Sửa thành công");
                alert.show();
            } else {
                alert.setContentText("Sửa không thành công");
                alert.show();
            }
        } else {
            alert.setContentText("Xe đã chạy chuyến khác cùng thời gian");
            alert.show();
        }
        filterBusTrip();
    }
    
    @FXML
    private DatePicker filterDepartureDate;
    @FXML
    private ComboBox filterDeparture;
    @FXML
    private ComboBox filterDestination;
    public void filterBusTrip() throws SQLException 
    {
        LocalDate dptDate = filterDepartureDate.getValue();
        Location l1 = null;
        Location l2 = null;
        Object location1 = filterDeparture.getSelectionModel().getSelectedItem();
        Object location2 = filterDestination.getSelectionModel().getSelectedItem();
        if (location1 != null && location1 instanceof Location)
            l1 = (Location) location1;
        if (location2 != null && location2 instanceof Location)
            l2 = (Location) location2;
        int dptId = l1 != null? l1.getId() : -1;
        int dstId = l2 != null? l2.getId() : -1;
        loadTableBusTripData(dptDate, dptId, dstId);
    }
}
