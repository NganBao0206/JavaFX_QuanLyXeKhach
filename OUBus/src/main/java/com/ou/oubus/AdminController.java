/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.Bus;
import com.ou.pojo.BusTrip;
import com.ou.pojo.Location;
import com.ou.pojo.Route;
import com.ou.pojo.User;
import com.ou.service.BusService;
import com.ou.service.BusTripService;
import com.ou.service.LocationService;
import com.ou.service.RouteService;
import com.ou.service.TicketService;
import com.ou.service.UserService;
import com.ou.utils.CurrentUser;
import com.ou.utils.StatisticalValue;
import com.ou.utils.ValidResult;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

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
    Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 05, 1);
    Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 00, 1);

    private final RouteService rs = new RouteService();
    private final BusTripService bts = new BusTripService();
    private final BusService bs = new BusService();
    private final LocationService ls = new LocationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            loadTableBusTripColumns();
            loadTableBusTripData(null, -1, -1);

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

            hourSpinner.getEditor().setText(String.format("%02d", Integer.parseInt(hourSpinner.getEditor().getText())));
            minuteSpinner.getEditor().setText(String.format("%02d", Integer.parseInt(minuteSpinner.getEditor().getText())));

            hourSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        if (newValue > 23) {
                            hourSpinner.getEditor().setText("23");
                        }
                        loadAvailableBus();
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            hourSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (!newValue.matches("[0-9]*")) {
                        hourSpinner.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
                    }
                    if (newValue.length() > 3) {
                        hourSpinner.getEditor().setText(newValue.substring(0, 2));
                    }
                    if (newValue.isBlank()) {
                        hourSpinner.getEditor().setText("00");
                    }
                    hourSpinner.getEditor().setText(String.format("%02d", Integer.parseInt(hourSpinner.getEditor().getText())));
                }
            });

            minuteSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        if (newValue > 59) {
                            minuteSpinner.getEditor().setText("59");
                        }
                        loadAvailableBus();
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            minuteSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (!newValue.matches("[0-9]*")) {
                        minuteSpinner.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
                    }
                    if (newValue.length() > 3) {
                        minuteSpinner.getEditor().setText(newValue.substring(0, 2));
                    }
                    if (newValue.isBlank()) {
                        minuteSpinner.getEditor().setText("00");
                    }
                    minuteSpinner.getEditor().setText(String.format("%02d", Integer.parseInt(minuteSpinner.getEditor().getText())));
                }
            });

            Text temp = new Text(":");
            temp.getStyleClass().addAll("normalText", "purpleText", "boldText");
            HBoxTime1.getChildren().add(hourSpinner);
            HBoxTime1.getChildren().add(temp);
            HBoxTime1.getChildren().add(minuteSpinner);

            loadListLocation();

            loadListRoute();
            loadListBus();

            txtSurcharge.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-9]*")) {
                    txtSurcharge.setText(newValue.replaceAll("[^0-9]", ""));
                }
            });
            txtSurchargeEdit.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-9]*")) {
                    txtSurchargeEdit.setText(newValue.replaceAll("[^0-9]", ""));
                }
            });
            txtRoutePrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-9]*")) {
                    txtRoutePrice.setText(newValue.replaceAll("[^0-9]", ""));
                }
            });
            txtRouteTime.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[0-9]*")) {
                    txtRouteTime.setText(newValue.replaceAll("[^0-9]", ""));
                }
            });
            txtName.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\p{L}\\s]*")) {
                    txtName.setText(newValue.replaceAll("[^\\p{L}\\s]", ""));
                }
            });

            txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[a-zA-Z0-9.]*")) {
                    txtUsername.setText(newValue.replaceAll("[^a-zA-Z0-9.]", ""));
                }
            });
            txtNewLocation.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\p{L}\\s]*")) {
                    txtNewLocation.setText(newValue.replaceAll("[^\\p{L}\\s]", ""));
                }
            });
            loadTableRouteColumns();
            loadTableRouteData();
            tbRoute.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectTableRoute();
                }
            });

            loadTableUserColumns();
            loadTableUserData();
            tbUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectTableUser();
                }
            });
            loadChart();

        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadListBus() {
        try {
            List<Bus> listbus = bs.getBuses();
            combxBus.setItems(FXCollections.observableList(listbus));
            cbBusEdit.setItems(FXCollections.observableList(listbus));
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadListLocation() {
        try {
            List<Location> listloc = ls.getLocations(null);
            listloc.add(null);
            comBxSearchDeparture.setItems(FXCollections.observableList(listloc));
            comBxSearchDestination.setItems(FXCollections.observableList(listloc));

            comBxSearchDepartureEdit.setItems(FXCollections.observableList(listloc));
            comBxSearchDestinationEdit.setItems(FXCollections.observableList(listloc));

            filterDeparture.setItems(FXCollections.observableList(listloc));
            filterDestination.setItems(FXCollections.observableList(listloc));

            cbRouteDeparture.setItems(FXCollections.observableList(listloc));
            cbRouteDestination.setItems(FXCollections.observableList(listloc));
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadListRoute() {
        try {
            List<Route> listrs = rs.getRoutes(null, null);
            combxRoute.setItems(FXCollections.observableList(listrs));
            cbEditRoute.setItems(FXCollections.observableList(listrs));
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadItemLocation(String keyword) {
        listLocations.getChildren().clear();
        try {
            List<Location> locations = ls.getLocations(keyword);
            for (int i = 0; i < locations.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("itemLocation.fxml"));
                Parent root = loader.load();
                ItemLocationController a = loader.getController();
                a.setId(locations.get(i).getId());
                a.setName(locations.get(i).getName());
                Button btnE = (Button) root.lookup("#btnEdit");
                Button btnD = (Button) root.lookup("#btnDel");
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
                                        loadListLocation();
                                        loadTableRouteData();
                                        loadListRoute();
                                        filterBusTrip();
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
                btnD.onActionProperty().set((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        try {
                            if (a.clickBtnDel()) {
                                loadItemLocation(txtSearchLocation.getText());
                                loadListLocation();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Xác nhận thêm");
            al.setHeaderText("Bạn có chắc chắn muốn thêm không?");

            al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> r = al.showAndWait();
            if (r.get() == ButtonType.NO) {
                return;
            }
            Location old = ls.getLocation(-1, txtNewLocation.getText());
            if (old != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Đã tồn tại địa điểm " + txtNewLocation.getText(), ButtonType.OK);
                alert.show();
            } else {
                Location l = new Location(0, formatString(txtNewLocation.getText()));
                if (ls.addLocation(l)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công", ButtonType.OK);
                    alert.show();
                    loadItemLocation(txtSearchLocation.getText());
                    txtNewLocation.setText("");
                    loadListLocation();
                    loadTableRouteData();
                    loadListRoute();
                    filterBusTrip();
                    editButtonCurrent = null;
                }
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
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Xác nhận thêm");
        al.setHeaderText("Bạn có chắc chắn muốn thêm không?");

        al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> rs = al.showAndWait();
        if (rs.get() == ButtonType.NO) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đủ thông tin", ButtonType.OK);
        Route route;
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
            return;
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
        if (bus != null) {
            BusTrip busTrip = new BusTrip(route.getId(), dDateTime, bus.getId(), surcharge, route.getTotalTime());

            if (!bts.isValidBusTrip(busTrip)) {
                alert.setContentText("Xe đã chạy chuyến khác cùng thời gian");
                alert.show();
                return;
            }
            ValidResult result = bts.isValidBusTripWithBeforeTrip(busTrip);
            if (result.getResult() == -1) {

                TextArea textArea = new TextArea(result.getFailureReason());
                textArea.setEditable(false);
                textArea.setWrapText(true);

                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0, 0);
                alert.getDialogPane().setContent(gridPane);
                alert.show();
                return;
            } else if (result.getResult() == 0) {
                Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                TextArea textArea = new TextArea(result.getFailureReason());
                textArea.setEditable(false);
                textArea.setWrapText(true);

                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0, 0);
                warning.getDialogPane().setContent(gridPane);
                warning.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> resultChoose = warning.showAndWait();
                if (resultChoose.isPresent() && resultChoose.get() == ButtonType.NO) {
                    return;
                }
            }
            ValidResult result2 = bts.isValidBusTripWithAfterTrip(busTrip);
            if (result.getResult() == -1) {
                TextArea textArea = new TextArea(result2.getFailureReason());
                textArea.setEditable(false);
                textArea.setWrapText(true);

                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0, 0);
                alert.getDialogPane().setContent(gridPane);
                alert.show();
                return;

            } else if (result.getResult() == 0) {
                Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
                TextArea textArea = new TextArea(result2.getFailureReason());
                textArea.setEditable(false);
                textArea.setWrapText(true);

                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0, 0);
                warning.getDialogPane().setContent(gridPane);
                warning.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> resultChoose = warning.showAndWait();
                if (resultChoose.isPresent() && resultChoose.get() == ButtonType.NO) {
                    return;
                }
            }
            if (bts.addBusTrip(busTrip)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Thêm thành công");
                alert.show();

            } else {
                alert.setContentText("Thêm không thành công");
                alert.show();
            }
            filterBusTrip();
            loadAvailableBus();
            loadChart();
        }
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
                    try {
                        if (!ts.getTicketsByBusTrip(bt.getId()).isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Chuyến xe đã được bán không thể xóa");
                            alert.showAndWait();
                            return;
                        }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa chuyến xe này không?");
                        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.YES) {
                            try {
                                if (bts.deleteBusTrip(bt.getId()) == true) {
                                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thành công", ButtonType.OK);
                                    al.show();
                                    this.filterBusTrip();
                                    loadChart();
                                } else {
                                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thất bại", ButtonType.OK);
                                    al.show();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                    try {
                        if (!ts.getTicketsByBusTrip(bt.getId()).isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Chuyến xe đã được bán không thể sửa");
                            alert.showAndWait();
                            return;
                        }
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
                                Spinner hourSpinnerEdit = new Spinner<>(0, 23, bt.getDepartureTime().getHour(), 1);
                                Spinner minuteSpinnerEdit = new Spinner<>(0, 59, bt.getDepartureTime().getMinute(), 1);
                                hourSpinnerEdit.setEditable(true);
                                minuteSpinnerEdit.setEditable(true);
                                minuteSpinnerEdit.setId("departureMinuteEdit");
                                hourSpinnerEdit.setPrefWidth(80);
                                hourSpinnerEdit.setId("departureHourEdit");
                                minuteSpinnerEdit.setPrefWidth(80);

                                hourSpinnerEdit.getEditor().setText(String.format("%02d", Integer.parseInt(hourSpinnerEdit.getEditor().getText())));
                                minuteSpinnerEdit.getEditor().setText(String.format("%02d", Integer.parseInt(minuteSpinnerEdit.getEditor().getText())));

                                hourSpinnerEdit.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue != null) {
                                        try {
                                            int value = (int) newValue;
                                            if (value > 23) {
                                                hourSpinnerEdit.getEditor().setText("23");
                                            }
                                            loadAvailableBusEdit();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });

                                hourSpinnerEdit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue != null) {
                                        if (!newValue.matches("[0-9]*")) {
                                            hourSpinnerEdit.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
                                        }
                                        if (newValue.length() > 3) {
                                            hourSpinnerEdit.getEditor().setText(newValue.substring(0, 2));
                                        }
                                        if (newValue.isBlank()) {
                                            hourSpinnerEdit.getEditor().setText("00");
                                        }
                                        hourSpinnerEdit.getEditor().setText(String.format("%02d", Integer.parseInt(hourSpinnerEdit.getEditor().getText())));
                                    }
                                });

                                minuteSpinnerEdit.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue != null) {
                                        try {
                                            int value = (int) newValue;
                                            if (value > 59) {
                                                minuteSpinnerEdit.getEditor().setText("59");
                                            }
                                            loadAvailableBusEdit();
                                        } catch (SQLException ex) {
                                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });

                                minuteSpinnerEdit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                                    if (newValue != null) {
                                        if (!newValue.matches("[0-9]*")) {
                                            minuteSpinnerEdit.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
                                        }
                                        if (newValue.length() > 3) {
                                            minuteSpinnerEdit.getEditor().setText(newValue.substring(0, 2));
                                        }
                                        if (newValue.isBlank()) {
                                            minuteSpinnerEdit.getEditor().setText("00");
                                        }
                                        minuteSpinnerEdit.getEditor().setText(String.format("%02d", Integer.parseInt(minuteSpinnerEdit.getEditor().getText())));
                                    }
                                });

                                Text temp2 = new Text(":");
                                temp2.getStyleClass().addAll("normalText", "purpleText", "boldText");
                                HBoxTimeEdit.getChildren().clear();
                                HBoxTimeEdit.getChildren().add(hourSpinnerEdit);
                                HBoxTimeEdit.getChildren().add(temp2);
                                HBoxTimeEdit.getChildren().add(minuteSpinnerEdit);

                                //Bus
                                index = -1;
                                int busId = bt.getBusId();
                                try {
                                    loadAvailableBusEdit(busId);
                                } catch (SQLException ex) {
                                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                for (int i = 0; i < cbBusEdit.getItems().size(); i++) {

                                    Bus busOfTrip = cbBusEdit.getItems().get(i) instanceof Bus ? (Bus) cbBusEdit.getItems().get(i) : null;
                                    if (busId == busOfTrip.getId()) {
                                        index = i;
                                        break;
                                    }
                                }
                                cbBusEdit.getSelectionModel().select(index);

                                // Surcharge
                                String surcharge = bt.getSurcharge() != 0 ? String.valueOf(bt.getSurcharge() * 1000) : "0";
                                txtSurchargeEdit.setText(surcharge);

                                tabEditBusTrip.setVisible(true);
                                tabViewBusTrip.setVisible(false);
                            }
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Xác nhận sửa");
        al.setHeaderText("Bạn có chắc chắn muốn sửa không?");

        al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> rs = al.showAndWait();
        if (rs.get() == ButtonType.NO) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đủ thông tin", ButtonType.OK);
        Route route;
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

        List<Spinner> temp = new ArrayList<>();
        for (Node node : HBoxTimeEdit.getChildren()) {
            if (node instanceof Spinner) {
                Spinner a = (Spinner) node;
                temp.add(a);
            }
        }
        LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of((int) temp.get(0).getValue(), (int) temp.get(1).getValue()));

        if (dDateTime.isBefore(LocalDateTime.now()) || dDateTime.isEqual(LocalDateTime.now())) {
            alert.setContentText("Thời điểm xuất phát không thể nhỏ hơn thời điểm hiện tại");
            alert.show();
            return;
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
        BusTrip busTrip = new BusTrip(idBusTrip, route.getId(), dDateTime, bus.getId(), surcharge, route.getTotalTime());

        if (!bts.isValidBusTrip(busTrip)) {
            alert.setContentText("Xe đã chạy chuyến khác cùng thời gian");
            alert.show();
            return;
        }
        ValidResult result = bts.isValidBusTripWithBeforeTrip(busTrip);
        if (result.getResult() == -1) {

            TextArea textArea = new TextArea(result.getFailureReason());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane gridPane = new GridPane();
            gridPane.add(textArea, 0, 0);
            alert.getDialogPane().setContent(gridPane);
            alert.show();
            return;
        } else if (result.getResult() == 0) {
            Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
            TextArea textArea = new TextArea(result.getFailureReason());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane gridPane = new GridPane();
            gridPane.add(textArea, 0, 0);
            warning.getDialogPane().setContent(gridPane);
            warning.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> resultChoose = warning.showAndWait();
            if (resultChoose.isPresent() && resultChoose.get() == ButtonType.NO) {
                return;
            }
        }
        ValidResult result2 = bts.isValidBusTripWithAfterTrip(busTrip);
        if (result.getResult() == -1) {
            TextArea textArea = new TextArea(result2.getFailureReason());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane gridPane = new GridPane();
            gridPane.add(textArea, 0, 0);
            alert.getDialogPane().setContent(gridPane);
            alert.show();
            return;

        } else if (result.getResult() == 0) {
            Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
            TextArea textArea = new TextArea(result2.getFailureReason());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            GridPane gridPane = new GridPane();
            gridPane.add(textArea, 0, 0);
            warning.getDialogPane().setContent(gridPane);
            warning.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> resultChoose = warning.showAndWait();
            if (resultChoose.isPresent() && resultChoose.get() == ButtonType.NO) {
                return;
            }
        }
        if (bts.editBusTrip(busTrip)) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Sửa thành công");
            alert.show();
            loadAvailableBusEdit(bus.getId());
            int index = -1;
            for (int i = 0; i < cbBusEdit.getItems().size(); i++) {
                Bus busOfTrip = cbBusEdit.getItems().get(i) instanceof Bus ? (Bus) cbBusEdit.getItems().get(i) : null;

                if (bus.getId() == busOfTrip.getId()) {
                    index = i;
                    break;
                }
            }

            cbBusEdit.getSelectionModel().select(index);
            loadAvailableBus();
        } else {
            alert.setContentText("Sửa không thành công");
            alert.show();
        }
        filterBusTrip();

    }

    public void loadAvailableBus() throws SQLException {
        if (combxRoute.getSelectionModel().getSelectedItem() != null && departureDate.getValue() != null) {
            Route r = combxRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) combxRoute.getSelectionModel().getSelectedItem() : null;
            LocalDate dDate = departureDate.getValue();
            LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue()));
            BusTrip testBusTrip = new BusTrip(r.getId(), dDateTime, -1, 0, r.getTotalTime());
            testBusTrip.setDepartureId(r.getDepartureId());
            testBusTrip.setDestinationId(r.getDestinationId());
            List<Bus> listBus = bs.getAvailableBuses(testBusTrip);
            Bus oldSelect = null;
            combxBus.setDisable(false);
            if (combxBus.getItems() != null) {
                if (combxBus.getSelectionModel().getSelectedItem() instanceof Bus) {
                    oldSelect = (Bus) combxBus.getSelectionModel().getSelectedItem();
                }
                combxBus.getItems().clear();
            }
            combxBus.setItems(FXCollections.observableList(listBus));
            if (oldSelect != null) {
                for (int i = 0; i < combxBus.getItems().size(); i++) {
                    Object o = combxBus.getItems().get(i);
                    Bus temp = o instanceof Bus ? (Bus) o : null;
                    if (temp != null && temp.getId() == oldSelect.getId()) {
                        combxBus.getSelectionModel().select(i);
                        break;
                    }
                }
            }
        } else {
            combxBus.setDisable(true);
            combxBus.setItems(null);
        }
    }

    private void loadAvailableBusEdit(int BusId) throws SQLException {
        if (cbEditRoute.getSelectionModel().getSelectedItem() != null && departureDateEdit.getValue() != null) {
            Route r = cbEditRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) cbEditRoute.getSelectionModel().getSelectedItem() : null;
            LocalDate dDate = departureDateEdit.getValue();
            List<Spinner> temp = new ArrayList<>();
            for (Node node : HBoxTimeEdit.getChildren()) {
                if (node instanceof Spinner) {
                    Spinner a = (Spinner) node;
                    temp.add(a);
                }
            }
            LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of((int) temp.get(0).getValue(), (int) temp.get(1).getValue()));
            BusTrip testBusTrip = new BusTrip(r.getId(), dDateTime, -1, 0, r.getTotalTime());
            testBusTrip.setDepartureId(r.getDepartureId());
            testBusTrip.setDestinationId(r.getDestinationId());
            testBusTrip.setId(txtIDBusTrip.getText());
            List<Bus> listBus = bs.getAvailableBuses(testBusTrip);
            Bus oldSelect = null;
            cbBusEdit.setDisable(false);
            if (cbBusEdit.getItems() != null) {
                if (cbBusEdit.getSelectionModel().getSelectedItem() instanceof Bus) {
                    oldSelect = (Bus) cbBusEdit.getSelectionModel().getSelectedItem();
                }
                cbBusEdit.getItems().clear();
            }
            cbBusEdit.setItems(FXCollections.observableList(listBus));
            if (oldSelect != null) {
                for (int i = 0; i < cbBusEdit.getItems().size(); i++) {
                    Object o = cbBusEdit.getItems().get(i);
                    Bus busTemp = o instanceof Bus ? (Bus) o : null;
                    if (busTemp != null && busTemp.getId() == oldSelect.getId()) {
                        cbBusEdit.getSelectionModel().select(i);
                        break;
                    }
                }
            }
        } else {
            cbBusEdit.setDisable(true);
            cbBusEdit.setItems(null);
        }
    }

    public void loadAvailableBusEdit() throws SQLException {
        if (cbEditRoute.getSelectionModel().getSelectedItem() != null && departureDateEdit.getValue() != null) {
            int BusId = bts.getBusTripById(txtIDBusTrip.getText()).getBusId();
            Route r = cbEditRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) cbEditRoute.getSelectionModel().getSelectedItem() : null;
            LocalDate dDate = departureDateEdit.getValue();
            List<Spinner> temp = new ArrayList<>();
            for (Node node : HBoxTimeEdit.getChildren()) {

                if (node instanceof Spinner) {
                    Spinner a = (Spinner) node;
                    temp.add(a);
                }
            }
            if (temp.size() > 1) {
                LocalDateTime dDateTime = LocalDateTime.of(dDate, LocalTime.of((int) temp.get(0).getValue(), (int) temp.get(1).getValue()));
                BusTrip testBusTrip = new BusTrip(r.getId(), dDateTime, -1, 0, r.getTotalTime());
                testBusTrip.setDepartureId(r.getDepartureId());
                testBusTrip.setDestinationId(r.getDestinationId());
                testBusTrip.setId(txtIDBusTrip.getText());
                List<Bus> listBus = bs.getAvailableBuses(testBusTrip);
                Bus oldSelect = null;
                cbBusEdit.setDisable(false);
                if (cbBusEdit.getItems() != null) {
                    if (cbBusEdit.getSelectionModel().getSelectedItem() instanceof Bus) {
                        oldSelect = (Bus) cbBusEdit.getSelectionModel().getSelectedItem();
                    }
                    cbBusEdit.getItems().clear();
                }
                cbBusEdit.setItems(FXCollections.observableList(listBus));
                if (oldSelect != null) {
                    for (int i = 0; i < cbBusEdit.getItems().size(); i++) {
                        Object o = cbBusEdit.getItems().get(i);
                        Bus busTemp = o instanceof Bus ? (Bus) o : null;
                        if (busTemp != null && busTemp.getId() == oldSelect.getId()) {
                            cbBusEdit.getSelectionModel().select(i);
                            break;
                        }
                    }
                }
            }

        } else {
            cbBusEdit.setDisable(true);
            cbBusEdit.setItems(null);
        }
    }
    @FXML
    private DatePicker filterDepartureDate;
    @FXML
    private ComboBox filterDeparture;
    @FXML
    private ComboBox filterDestination;

    public void filterBusTrip() throws SQLException {
        LocalDate dptDate = filterDepartureDate.getValue();
        Location l1 = null;
        Location l2 = null;
        Object location1 = filterDeparture.getSelectionModel().getSelectedItem();
        Object location2 = filterDestination.getSelectionModel().getSelectedItem();
        if (location1 != null && location1 instanceof Location) {
            l1 = (Location) location1;
        }
        if (location2 != null && location2 instanceof Location) {
            l2 = (Location) location2;
        }
        int dptId = l1 != null ? l1.getId() : -1;
        int dstId = l2 != null ? l2.getId() : -1;
        loadTableBusTripData(dptDate, dptId, dstId);
    }

    //Route
    @FXML
    private ComboBox cbRouteDeparture;
    @FXML
    private ComboBox cbRouteDestination;
    @FXML
    private TextField txtRoutePrice;
    @FXML
    private TextField txtRouteTime;
    @FXML
    private TableView tbRoute;

    public void loadTableRouteColumns() {
        TableColumn col = new TableColumn("ID");
        col.setCellValueFactory(new PropertyValueFactory("id"));
        col.setPrefWidth(100);

        TableColumn colDepartureID = new TableColumn("Điểm xuất phát");
        colDepartureID.setCellValueFactory(new PropertyValueFactory("departureName"));
        colDepartureID.setMinWidth(250);

        TableColumn colDestinationID = new TableColumn("Điểm đến");
        colDestinationID.setCellValueFactory(new PropertyValueFactory("destinationName"));
        colDestinationID.setMinWidth(250);

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

        TableColumn colTime = new TableColumn("Số phút dự kiến");
        colTime.setCellValueFactory(new PropertyValueFactory("totalTime"));
        colTime.setMinWidth(200);
        this.tbRoute.getColumns().setAll(col, colDepartureID, colDestinationID, colPrice, colTime);
    }

    public void loadTableRouteData() throws SQLException {
        List<Route> listRoutes = rs.getRoutes("", "");

        tbRoute.getItems().clear();
        tbRoute.setItems(FXCollections.observableList(listRoutes));
    }

    public void addRoute() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        if (!cbRouteDeparture.getSelectionModel().isEmpty() && !cbRouteDestination.getSelectionModel().isEmpty() && !txtRoutePrice.getText().isBlank() && !txtRouteTime.getText().isBlank()) {
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Xác nhận thêm");
            al.setHeaderText("Bạn có chắc chắn muốn thêm không?");

            al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> r = al.showAndWait();
            if (r.get() == ButtonType.NO) {
                return;
            }
            Location l1 = cbRouteDeparture.getSelectionModel().getSelectedItem() instanceof Location ? (Location) cbRouteDeparture.getSelectionModel().getSelectedItem() : null;
            Location l2 = cbRouteDestination.getSelectionModel().getSelectedItem() instanceof Location ? (Location) cbRouteDestination.getSelectionModel().getSelectedItem() : null;
            if (l1 != null && l2 != null) {
                if (l1.getId() != l2.getId()) {
                    Route oldRoute = rs.getRoute("-1", l1.getId(), l2.getId());
                    if (oldRoute == null) {
                        double price = Double.parseDouble(txtRoutePrice.getText().replaceAll(",", ""));
                        int minute = Integer.parseInt(txtRouteTime.getText());
                        Route newRoute = new Route(l1.getId(), l2.getId(), (double) price / 1000, minute);
                        if (rs.addRoute(newRoute)) {
                            alert.setContentText("Thêm thành công");
                            alert.show();
                            loadTableRouteData();
                            loadListRoute();

                            filterBusTrip();

                        } else {
                            alert.setContentText("Thêm không thành công");
                            alert.show();
                        }
                    } else {
                        alert.setContentText("Đã tồn tại chuyến xe từ " + l1.getName() + " đến " + l2.getName());
                        alert.show();
                    }
                } else {
                    alert.setContentText("Điểm xuất phát và điểm đến phải khác nhau");
                    alert.show();
                }
            } else {
                alert.setContentText("Đã có lỗi xảy ra, vui lòng kiểm tra việc chọn địa điểm");
                alert.show();
            }
        } else {
            alert.setContentText("Hãy nhập thông tin đầy đủ");
            alert.show();
        }
    }

    public void selectTableRoute() {
        Route selectedRoute = tbRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) tbRoute.getSelectionModel().getSelectedItem() : null;
        if (selectedRoute != null) {
            for (Object location : cbRouteDeparture.getItems()) {
                Location item = location instanceof Location ? (Location) location : null;
                if (item != null && item.getId() == selectedRoute.getDepartureId()) {
                    cbRouteDeparture.getSelectionModel().select(location);
                    break;
                }
            }

            for (Object location : cbRouteDestination.getItems()) {
                Location item = location instanceof Location ? (Location) location : null;
                if (item != null && item.getId() == selectedRoute.getDestinationId()) {
                    cbRouteDestination.getSelectionModel().select(location);
                    break;
                }
            }

            txtRoutePrice.setText(String.valueOf(selectedRoute.getPrice() * 1000));
            txtRouteTime.setText(String.valueOf(selectedRoute.getTotalTime()));
        }
    }

    public void editRoute() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        if (tbRoute.getSelectionModel().getSelectedItem() != null) {
            Route selectedRoute = tbRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) tbRoute.getSelectionModel().getSelectedItem() : null;
            if (selectedRoute != null) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Xác nhận sửa");
                al.setHeaderText("Bạn có chắc chắn muốn sửa không?");

                al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> r = al.showAndWait();
                if (r.get() == ButtonType.NO) {
                    return;
                }
                if (!cbRouteDeparture.getSelectionModel().isEmpty() && !cbRouteDestination.getSelectionModel().isEmpty() && !txtRoutePrice.getText().isBlank() && !txtRouteTime.getText().isBlank()) {
                    Location l1 = cbRouteDeparture.getSelectionModel().getSelectedItem() instanceof Location ? (Location) cbRouteDeparture.getSelectionModel().getSelectedItem() : null;
                    Location l2 = cbRouteDestination.getSelectionModel().getSelectedItem() instanceof Location ? (Location) cbRouteDestination.getSelectionModel().getSelectedItem() : null;
                    if (l1 != null && l2 != null) {
                        if (l1.getId() != l2.getId()) {
                            Route oldRoute = rs.getRoute(selectedRoute.getId(), l1.getId(), l2.getId());
                            if (oldRoute == null) {
                                double price = Double.parseDouble(txtRoutePrice.getText().replaceAll(",", ""));
                                int minute = Integer.parseInt(txtRouteTime.getText());
                                Route editRoute = new Route(selectedRoute.getId(), l1.getId(), l2.getId(), (double) price / 1000, minute);
                                if (rs.editRoute(editRoute)) {
                                    alert.setContentText("Sửa thành công");
                                    alert.show();
                                    loadTableRouteData();
                                    loadListRoute();
                                    filterBusTrip();
                                } else {
                                    alert.setContentText("Sửa không thành công");
                                    alert.show();
                                }
                            } else {
                                alert.setContentText("Đã tồn tại chuyến xe từ " + l1.getName() + " đến " + l2.getName());
                                alert.show();
                            }
                        } else {

                            alert.setContentText("Điểm xuất phát và điểm đến phải khác nhau");
                            alert.show();
                        }

                    } else {
                        alert.setContentText("Đã có lỗi xảy ra, vui lòng kiểm tra việc chọn địa điểm");
                        alert.show();
                    }
                } else {

                    alert.setContentText("Hãy nhập thông tin đầy đủ");
                    alert.show();
                }
            } else {
                alert.setContentText("Vui lòng chọn chuyến xe cần chỉnh sửa trước");
                alert.show();
            }
        } else {
            alert.setContentText("Vui lòng chọn chuyến xe cần chỉnh sửa trước");
            alert.show();
        }
    }

    public void deleteRoute() {
        if (tbRoute.getSelectionModel().getSelectedItem() != null) {
            Route selectedRoute = tbRoute.getSelectionModel().getSelectedItem() instanceof Route ? (Route) tbRoute.getSelectionModel().getSelectedItem() : null;
            if (selectedRoute != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa tuyến xe này không?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(res -> {
                    {
                        if (res == ButtonType.YES) {
                            try {
                                List<BusTrip> busTripOfRoute = bts.getBusTrips(null, selectedRoute.getDepartureId(), selectedRoute.getDestinationId());
                                if (busTripOfRoute.size() <= 0) {
                                    if (rs.deleteRoute(selectedRoute.getId()) == true) {
                                        Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thành công", ButtonType.OK);
                                        al.show();
                                        loadTableRouteData();
                                        loadListRoute();
                                    } else {
                                        Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thất bại", ButtonType.OK);
                                        al.show();
                                    }
                                } else {
                                    Alert al = new Alert(Alert.AlertType.WARNING, "Có chuyến xe thuộc tuyến xe này, không thể xóa");
                                    al.show();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vui lòng chọn tuyến cần xóa");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vui lòng chọn tuyến cần xóa");
            alert.show();
        }
    }

    //User
    @FXML
    private TableView tbUser;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtConfirmPassword;
    @FXML
    private TextField txtName;

    private void loadTableUserColumns() {
        TableColumn col = new TableColumn("ID");
        col.setCellValueFactory(new PropertyValueFactory("id"));
        col.setPrefWidth(100);

        TableColumn colUsername = new TableColumn("Username");
        colUsername.setCellValueFactory(new PropertyValueFactory("username"));
        colUsername.setMinWidth(250);

        TableColumn colPassword = new TableColumn("Mật khẩu");
        colPassword.setCellValueFactory(new PropertyValueFactory("password"));
        colPassword.setMinWidth(250);

        TableColumn colName = new TableColumn("Họ tên");
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colName.setMinWidth(300);

        this.tbUser.getColumns().setAll(col, colUsername, colPassword, colName);
    }

    private UserService us = new UserService();

    private void loadTableUserData() throws SQLException {
        List<User> listUsers = us.getUsers();
        tbUser.getItems().clear();
        tbUser.setItems(FXCollections.observableList(listUsers));
    }

    private void selectTableUser() {
        User selectedUser = tbUser.getSelectionModel().getSelectedItem() instanceof User ? (User) tbUser.getSelectionModel().getSelectedItem() : null;
        if (selectedUser != null) {
            txtUsername.setText(selectedUser.getUsername());
            txtPassword.setText(selectedUser.getPassword());
            txtConfirmPassword.setText(selectedUser.getPassword());
            txtName.setText(selectedUser.getName());
        }
    }

    public void deleteUser() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa không?");

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            if (tbUser.getSelectionModel().getSelectedItem() != null) {
                User selectedUser = tbUser.getSelectionModel().getSelectedItem() instanceof User ? (User) tbUser.getSelectionModel().getSelectedItem() : null;
                if (selectedUser != null) {
                    if (us.getAmountTicketByUserId(selectedUser.getId()) > 0) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Nhân viên đã thực hiện các giao dịch đặt/bán vé, không thể xóa");
                        al.showAndWait();
                        return;
                    }
                    if (us.deleteUser(selectedUser.getId())) {
                        Alert al = new Alert(Alert.AlertType.INFORMATION, "Xóa thành công");
                        al.showAndWait();
                        loadTableUserData();
                    }

                } else {
                    Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn user cần xóa");
                    warning.show();
                }
            } else {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn user cần xóa");
                warning.show();
            }
        }
    }

    public void addUser() throws SQLException {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Xác nhận thêm");
        al.setHeaderText("Bạn có chắc chắn muốn thêm không?");

        al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> r = al.showAndWait();
        if (r.get() == ButtonType.YES) {
            if (txtUsername.getText().isBlank() || txtPassword.getText().isBlank() || txtConfirmPassword.getText().isBlank() || txtName.getText().isBlank()) {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin");
                warning.show();
            } else {
                User check = us.getUserByUsername(txtUsername.getText());
                if (check == null) {
                    if (txtPassword.getText().equals(txtConfirmPassword.getText())) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn thêm user này");
                        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> result = confirm.showAndWait();
                        if (result.get() == ButtonType.YES) {
                            User u = new User(txtUsername.getText().toLowerCase(), txtPassword.getText(), formatString(txtName.getText()), "staff");
                            if (us.addUser(u)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công");
                                alert.show();
                                loadTableUserData();
                            } else {
                                Alert warning = new Alert(Alert.AlertType.WARNING, "Thêm không thành công");
                                warning.show();
                            }
                        }
                    } else {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Mật khẩu và mật khẩu xác nhận không giống nhau");
                        warning.show();
                    }

                } else {
                    Alert warning = new Alert(Alert.AlertType.WARNING, "Username đã tồn tại không thể thêm");
                    warning.show();
                }
            }
        }
    }

    public void editUser() throws SQLException {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setTitle("Xác nhận sửa");
        al.setHeaderText("Bạn có chắc chắn muốn sửa không?");

        al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> r = al.showAndWait();
        if (r.get() == ButtonType.YES) {
            if (tbUser.getSelectionModel().getSelectedItem() != null) {
                User selectedUser = tbUser.getSelectionModel().getSelectedItem() instanceof User ? (User) tbUser.getSelectionModel().getSelectedItem() : null;
                if (selectedUser != null) {
                    if (txtUsername.getText().isBlank() || txtPassword.getText().isBlank() || txtConfirmPassword.getText().isBlank() || txtName.getText().isBlank()) {
                        Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin");
                        warning.show();
                    } else {
                        User check = us.getUserByUsername(selectedUser.getId(), txtUsername.getText());
                        if (check == null) {
                            if (txtPassword.getText().equals(txtConfirmPassword.getText())) {
                                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn sửa user này không?");
                                confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                                Optional<ButtonType> result = confirm.showAndWait();
                                if (result.get() == ButtonType.YES) {
                                    User u = new User(selectedUser.getId(), txtUsername.getText().toLowerCase(), txtPassword.getText(), formatString(txtName.getText()), "staff");
                                    if (u.getPassword().equals(selectedUser.getPassword())) {
                                        if (us.editUserWithoutPass(u)) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sửa thành công");
                                            alert.show();
                                            loadTableUserData();
                                        } else {
                                            Alert warning = new Alert(Alert.AlertType.WARNING, "Sửa không thành công");
                                            warning.show();
                                        }
                                    } else {
                                        if (us.editUser(u)) {
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sửa thành công");
                                            alert.show();
                                            loadTableUserData();
                                        } else {
                                            Alert warning = new Alert(Alert.AlertType.WARNING, "Sửa không thành công");
                                            warning.show();
                                        }
                                    }

                                }
                            } else {
                                Alert warning = new Alert(Alert.AlertType.WARNING, "Mật khẩu và mật khẩu xác nhận không giống nhau");
                                warning.show();
                            }

                        } else {
                            Alert warning = new Alert(Alert.AlertType.WARNING, "Username đã tồn tại");
                            warning.show();
                        }
                    }
                } else {
                    Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn user cần sửa");
                    warning.show();
                }
            } else {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Vui lòng chọn user cần sửa");
                warning.show();
            }
        }
    }

    public void logOut() throws IOException {
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setHeaderText("Bạn có chắc chắn muốn đăng xuất không?");
        al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> r = al.showAndWait();
        if (r.get() == ButtonType.YES) {
            CurrentUser.getInstance().setUser(null);
            App.setRoot("login");
        }
    }

    @FXML
    private LineChart chart1;
    @FXML
    private PieChart chart2;
    @FXML
    private Text txtTotal;
    @FXML
    private Text txtTotalSales;

    private TicketService ts = new TicketService();

    public void loadChart() throws SQLException {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        if (chart1 != null) {
            chart1.getData().clear();
            LocalDate date = LocalDate.now().withDayOfMonth(1);
            LocalDate end = date.plusMonths(1);
            XYChart.Series series = new XYChart.Series();
            List<StatisticalValue> datas = ts.getMonthlyRevenue(year, month);

            List<StatisticalValue> chartDatas = new ArrayList<>();
            while (date.isBefore(end)) {
                StatisticalValue temp = new StatisticalValue(date.toString(), 0);
                for (StatisticalValue data : datas) {
                    if (data.getLabel().equals(date.toString())) {
                        temp.setValue(data.getValue());
                    }
                }
                chartDatas.add(temp);
                date = date.plusDays(1);
            }

            int i = 0;
            series.setName("Doanh thu");
            for (StatisticalValue data : chartDatas) {
                series.getData().add(new XYChart.Data(data.getLabel().substring(data.getLabel().lastIndexOf("-") + 1), data.getValue()));
            }
            chart1.getData().add(series);
            series.getNode().setStyle("-fx-stroke: #ac7ef1");
        }

        if (chart2 != null) {
            chart2.getData().clear();
            StatisticalValue soldSeat = ts.getAvergePercentageSeat(year, month);
            PieChart.Data dataSold = new PieChart.Data(soldSeat.getLabel(), soldSeat.getValue());
            PieChart.Data dataEmpty = new PieChart.Data("Tỷ lệ ghế trống", 100 - soldSeat.getValue());
            chart2.getData().addAll(dataEmpty, dataSold);
        }

        List<Number> totals = ts.getTotalTicket(year, month);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtTotalSales.setText(currencyFormat.format(totals.get(0)));
        txtTotal.setText(totals.get(1).toString());
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
