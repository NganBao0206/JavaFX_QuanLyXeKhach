/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ou.oubus;

import com.ou.pojo.BusTrip;
import com.ou.pojo.Customer;
import com.ou.pojo.Location;
import com.ou.pojo.Seat;
import com.ou.pojo.Ticket;
import com.ou.service.BusTripService;
import com.ou.service.CurrentUser;
import com.ou.service.CustomerService;
import com.ou.service.LocationService;
import com.ou.service.SeatService;
import com.ou.service.TicketService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 */
public class EmployeeController implements Initializable {

    @FXML
    private TableView tbBusTrip;
    @FXML
    private TableView tbTicket;
    @FXML
    private ComboBox cbStart;
    @FXML
    private ComboBox cbEnd;
    @FXML
    private DatePicker date;
    @FXML
    private AnchorPane tabChooseBuy;
    @FXML
    private AnchorPane tabComfirmBuy;
    @FXML
    private GridPane tabBuyTicket;
    @FXML
    private Text txtStartLocation;
    @FXML
    private Text txtEndLocation;
    @FXML
    private Text txtPrice;
    @FXML
    private Text txtTime;
    @FXML
    private VBox vbCus;
    @FXML
    private TextField txtSearchCustomer;

    private GridPane selectedTab;
    private final List<Button> btn = new ArrayList<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            loadTableBusTripColumns();
            loadTableTicketColumns();
            loadTableBusTripData(null, -1, -1);
            loadTableTicketData(null);
            getLocations();
            this.txtSearchCustomer.textProperty().addListener(e -> {
                try {
                    loadTableTicketData(txtSearchCustomer.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(() -> {
            try {
                List<Seat> seats = ss.getSeats();

                for (int i = 0; i < seats.size(); i++) {
                    Button b = (Button) App.getScene().lookup("#" + seats.get(i).getName());
                    btn.add(b);
                }
                selectedTab = tabBuyTicket;

            } catch (SQLException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public void changeTab(ActionEvent e) {
        Button currentBtn = (Button) e.getTarget();
        String name = currentBtn.getId().replace("btn", "");
        selectedTab.setVisible(false);
        GridPane currentTab = (GridPane) App.getScene().lookup("#tab" + name);
        selectedTab = currentTab;
        selectedTab.setVisible(true);
    }

    public void signOut(Event e) throws IOException {
        App.setRoot("login");
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
        colDeparture.setPrefWidth(150);

        TableColumn colDestination = new TableColumn("Nơi đến");
        colDestination.setCellValueFactory(new PropertyValueFactory("destinationName"));
        colDestination.setPrefWidth(150);

        TableColumn colDepartureTime = new TableColumn("Giờ xuất phát");
        colDepartureTime.setCellValueFactory(new PropertyValueFactory("departureTime"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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

        TableColumn colPrice = new TableColumn("Giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));

        TableColumn colSurcharge = new TableColumn("Phụ phí");
        colSurcharge.setCellValueFactory(new PropertyValueFactory("surcharge"));
        this.tbBusTrip.getColumns().setAll(col, colRouteID, colDeparture, colDestination, colDepartureTime, colBusID, colPrice, colSurcharge);
    }

    private void loadTableBusTripData(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
        BusTripService bts = new BusTripService();
        List<BusTrip> lbt = bts.getBusTrips(departureDate, departureId, destinationId);

        this.tbBusTrip.getItems().clear();
        this.tbBusTrip.setItems(FXCollections.observableList(lbt));
    }

    private void getLocations() throws SQLException {
        LocationService ls = new LocationService();
        List<Location> l = ls.getLocations();

        this.cbStart.setItems(FXCollections.observableList(l));
        this.cbEnd.setItems(FXCollections.observableList(l));
    }

    public void searchBusTrip() throws SQLException {
        LocalDate dptDate = date.getValue();
        Location l1 = null;
        Location l2 = null;
        Object location1 = cbStart.getSelectionModel().getSelectedItem();
        Object location2 = cbEnd.getSelectionModel().getSelectedItem();
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

    public void loadSeatByBusTrip() throws SQLException {

//        Reset seat
        for (Button b : btn) {
            b.getStyleClass().remove("selectedSeat");
            b.setDisable(false);
        }

        List<Ticket> tickets = ts.getTicketsByBusTrip(busTrip.getId());
        for (Ticket t : tickets) {
            Seat s = ss.getSeat(t.getSeatId());
            Button b = (Button) App.getScene().lookup("#" + s.getName());
            b.setDisable(true);
        }

    }

    public void refresh() throws SQLException {
        cbStart.getSelectionModel().select(null);
        cbEnd.getSelectionModel().select(null);
        date.setValue(null);
        loadTableBusTripData(null, -1, -1);
    }

    public void buyTicket() throws SQLException {
        if (tbBusTrip.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn chuyến xe", ButtonType.OK);
            alert.showAndWait();
            return;
        } else {

            BusTrip bt = tbBusTrip.getSelectionModel().getSelectedItem() instanceof BusTrip ? (BusTrip) tbBusTrip.getSelectionModel().getSelectedItem() : null;
            if (bt != null) {
                txtStartLocation.setText(bt.getDepartureName());
                txtEndLocation.setText(bt.getDestinationName());
                String text = String.format("%.0f", bt.getPrice() * 1000);
                txtPrice.setText(text + "đ");
                LocalDateTime departureTime = bt.getDepartureTime();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String departureTimeString = departureTime.format(dateTimeFormatter);
                txtTime.setText(departureTimeString);
                busTrip = bt;
                loadSeatByBusTrip();
                listSelectedSeat.clear();
                vbCus.getChildren().clear();

            }
        }
        tabChooseBuy.setVisible(false);
        tabComfirmBuy.setVisible(true);
    }

    private BusTrip busTrip;
    private final CustomerService cs = new CustomerService();
    private final TicketService ts = new TicketService();
    private final CurrentUser currentUser = CurrentUser.getInstance();

    public void goBackChooseBuy() {
        tabChooseBuy.setVisible(true);
        tabComfirmBuy.setVisible(false);
    }

    private final List<Button> listSelectedSeat = new ArrayList<>();
    private final SeatService ss = new SeatService();

    public void chooseSeat(ActionEvent e) {
        Button btnSeat = (Button) e.getSource();
        if (listSelectedSeat.contains(btnSeat)) {
            btnSeat.getStyleClass().remove("selectedSeat");
            VBox removedBox = (VBox) App.getScene().lookup("#customer-" + btnSeat.getId());
            listSelectedSeat.remove(btnSeat);
            vbCus.getChildren().remove(removedBox);
        } else {
            btnSeat.getStyleClass().add("selectedSeat");
            listSelectedSeat.add(btnSeat);
            VBox boxCustomer = new VBox();
            boxCustomer.setId("customer-" + btnSeat.getId());
            HBox row1 = new HBox();
            row1.setAlignment(Pos.CENTER_LEFT);
            Text t1 = new Text("Khách hàng ngồi ghế: " + btnSeat.getId());
            t1.getStyleClass().add("purpleText");
            row1.getChildren().add(t1);
            row1.getStyleClass().addAll("normalText");
            boxCustomer.getChildren().add(row1);

            HBox row2 = new HBox();
            Text t2 = new Text("Họ tên: ");
            t2.setWrappingWidth(130);
            row2.getChildren().add(t2);
            row2.getStyleClass().add("normalText");
            TextField tfName = new TextField();
            tfName.setId("name" + btnSeat.getId());
            row2.getChildren().add(tfName);
            boxCustomer.getChildren().add(row2);

            HBox row3 = new HBox();
            Text t3 = new Text("Số điện thoại: ");
            t3.setWrappingWidth(130);
            row3.getChildren().add(t3);
            TextField tfPhone = new TextField();
            row3.getStyleClass().add("normalText");
            tfPhone.setId("phone" + btnSeat.getId());
            row3.getChildren().add(tfPhone);
            boxCustomer.getChildren().add(row3);
            boxCustomer.setStyle("-fx-spacing: 20");
            vbCus.getChildren().add(boxCustomer);
        }
    }

    public void confirmBuy(ActionEvent e) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận mua vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            List<TextField> nameCus = new ArrayList<>();
            List<TextField> phoneCus = new ArrayList<>();
            for (Button seat : listSelectedSeat) {
                TextField tfName = (TextField) App.getScene().lookup("#name" + seat.getId());
                TextField tfPhone = (TextField) App.getScene().lookup("#phone" + seat.getId());
                if (tfName.getText().isBlank() || tfPhone.getText().isBlank()) {
                    Alert al = new Alert(Alert.AlertType.WARNING, "Vui lòng điền đầy đủ thông tin khách hàng");
                    al.showAndWait();
                    return;
                } else {
                    nameCus.add(tfName);
                    phoneCus.add(tfPhone);

                }
            }
            for (int i = 0; i < listSelectedSeat.size(); i++) {
                Customer c = cs.getCustomer(nameCus.get(i).getText(), phoneCus.get(i).getText());
                if (c != null) {

                    Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "purchased", busTrip.getPrice() + busTrip.getSurcharge());
                    if (!ts.addTicket(t)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                        al.show();
                    } else {
                        Alert al = new Alert(Alert.AlertType.INFORMATION, "Mua vé thành công");
                        al.show();
                        tabChooseBuy.setVisible(true);
                        tabComfirmBuy.setVisible(false);
                        loadTableTicketData(null);
                    }

                } else {
                    c = new Customer(nameCus.get(i).getText(), phoneCus.get(i).getText());
                    if (cs.addCustomer(c)) {
                        Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "purchased", busTrip.getPrice() + busTrip.getSurcharge());

                        if (!ts.addTicket(t)) {
                            Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                            al.show();
                        }
                    }
                }
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Mua vé thành công");
                al.show();
                tabChooseBuy.setVisible(true);
                tabComfirmBuy.setVisible(false);
                loadTableTicketData(null);
            }
            loadTableTicketData(txtSearchCustomer.getText());
        }
    }

    public void confirmBook(ActionEvent e) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận đặt vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            List<TextField> nameCus = new ArrayList<>();
            List<TextField> phoneCus = new ArrayList<>();
            for (Button seat : listSelectedSeat) {
                TextField tfName = (TextField) App.getScene().lookup("#name" + seat.getId());
                TextField tfPhone = (TextField) App.getScene().lookup("#phone" + seat.getId());
                if (tfName.getText().isBlank() || tfPhone.getText().isBlank()) {
                    Alert al = new Alert(Alert.AlertType.WARNING, "Vui lòng điền đầy đủ thông tin khách hàng");
                    al.showAndWait();
                    return;
                } else {
                    nameCus.add(tfName);
                    phoneCus.add(tfPhone);

                }
            }
            for (int i = 0; i < listSelectedSeat.size(); i++) {
                Customer c = cs.getCustomer(nameCus.get(i).getText(), phoneCus.get(i).getText());

                if (c != null) {
                    System.out.println(c.getName());
                    Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "booked", busTrip.getPrice() + busTrip.getSurcharge());
                    if (!ts.addTicket(t)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                        al.show();
                    }
                } else {
                    c = new Customer(nameCus.get(i).getText(), phoneCus.get(i).getText());
                    if (cs.addCustomer(c)) {
                        Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "booked", busTrip.getPrice() + busTrip.getSurcharge());
                        System.out.println(currentUser.getUser().getId());
                        if (!ts.addTicket(t)) {
                            Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                            al.show();
                        } else {
                            Alert al = new Alert(Alert.AlertType.INFORMATION, "Đặt vé thành công");
                            al.show();
                            tabChooseBuy.setVisible(true);
                            tabComfirmBuy.setVisible(false);
                            loadTableTicketData(null);
                        }
                    }
                }
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Đặt vé thành công");
                al.show();
                tabChooseBuy.setVisible(true);
                tabComfirmBuy.setVisible(false);
                loadTableTicketData(null);
            }
            loadTableTicketData(txtSearchCustomer.getText());
        }
    }

    private void loadTableTicketColumns() {

        TableColumn col = new TableColumn("ID");
        col.setCellValueFactory(new PropertyValueFactory("id"));
        col.setPrefWidth(50);

        TableColumn colCusName = new TableColumn("Tên khách hàng");
        colCusName.setCellValueFactory(new PropertyValueFactory("cusName"));
        colCusName.setPrefWidth(300);

        TableColumn colPhone = new TableColumn("SDT");
        colPhone.setCellValueFactory(new PropertyValueFactory("cusPhone"));
        colPhone.setPrefWidth(100);

        TableColumn colSeatName = new TableColumn("Tên ghế");
        colSeatName.setCellValueFactory(new PropertyValueFactory("seatName"));
        colSeatName.setPrefWidth(100);

        TableColumn colStaffName = new TableColumn("Tên nhân viên đặt");
        colStaffName.setCellValueFactory(new PropertyValueFactory("staffName"));
        colStaffName.setPrefWidth(200);

        TableColumn colStatus = new TableColumn("Trạng thái");
        colStatus.setCellValueFactory(new PropertyValueFactory("Status"));
        colStatus.setPrefWidth(150);

        TableColumn colDeparture = new TableColumn("Nơi xuất phát");
        colDeparture.setCellValueFactory(new PropertyValueFactory("DepartureName"));
        colDeparture.setPrefWidth(300);

        TableColumn colDestination = new TableColumn("Nơi đến");
        colDestination.setCellValueFactory(new PropertyValueFactory("DestinationName"));
        colDestination.setPrefWidth(300);

        TableColumn colDepartureTime = new TableColumn("Thời gian xuất phát");
        colDepartureTime.setCellValueFactory(new PropertyValueFactory("DepartureTime"));
        colDepartureTime.setPrefWidth(300);

        TableColumn colPrice = new TableColumn("Giá");
        colPrice.setCellValueFactory(new PropertyValueFactory("TicketPrice"));
        colPrice.setPrefWidth(150);

        this.tbTicket.getColumns().setAll(col, colCusName, colPhone, colSeatName, colStaffName, colStatus, colPrice, colDeparture, colDestination, colDepartureTime);
    }

    private void loadTableTicketData(String keyword) throws SQLException {
        List<Ticket> lt = ts.getTickets(keyword);
        this.tbTicket.getItems().clear();
        this.tbTicket.setItems(FXCollections.observableList(lt));
    }

    public void delTicket() throws SQLException {
        if (tbTicket.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn vé nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận hủy vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Ticket selectedItem = (Ticket) tbTicket.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.getStatus().equals("purchased")) {
                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Không thể hủy vé đã mua");
                    al.show();
                    return;
                }
                String id = selectedItem.getId();
                if (ts.deleteTicket(id) == true) {
                    Alert announce = new Alert(Alert.AlertType.INFORMATION, "Hủy vé thành công");
                    tbTicket.getItems().remove(selectedItem);
                    announce.showAndWait();
                    Platform.runLater(() -> {
                        try {
                            List<Seat> seats = ss.getSeats();
                            for (int i = 0; i < seats.size(); i++) {
                                Button b = (Button) App.getScene().lookup("#" + seats.get(i).getName());
                                btn.add(b);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }

            }

        }
    }

    public void changeBookToBuy() throws SQLException {
        if (tbTicket.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn vé nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận mua vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Ticket selectedItem = (Ticket) tbTicket.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.getStatus().equals("purchased")) {
                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Vé đã mua");
                    al.show();
                    return;
                }
                ts.changeStatusToBuy(selectedItem);
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Thành công");
                al.show();
                loadTableTicketData(txtSearchCustomer.getText());
            }
        }
    }

}
