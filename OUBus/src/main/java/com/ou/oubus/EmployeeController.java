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
import com.ou.utils.CurrentUser;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private TextField txtCusName;
    @FXML
    private TextField txtCusPhone;
    @FXML
    private TextField txtSearchCustomer;
    @FXML
    private AnchorPane tabViewTicket;
    @FXML
    private AnchorPane tabChangeTicket;

    private GridPane selectedTab;
    private final List<Button> btn = new ArrayList<>();
    private final List<Button> btnEdit = new ArrayList<>();

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

            LocationService ls = new LocationService();
            List<Location> listloc = ls.getLocations(null);
            listloc.add(null);
            cbDeparture.setItems(FXCollections.observableList(listloc));
            cbDestination.setItems(FXCollections.observableList(listloc));
            this.txtSearchCustomer.textProperty().addListener(e -> {
                try {
                    loadTableTicketData(txtSearchCustomer.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            txtCusName.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\p{L}\\s]*")) {
                    txtCusName.setText(newValue.replaceAll("[^\\p{L}\\s]", ""));
                }
            });

            txtCusPhone.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("^\\d{10}$")) {
                    txtCusPhone.setText(newValue.replaceAll("[^0-9]", ""));
                    if (txtCusPhone.getText().length() > 10) {
                        txtCusPhone.setText(txtCusPhone.getText().substring(0, 10));
                    }
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
                    Button b2 = (Button) App.getScene().lookup("#seat" + seats.get(i).getName());
                    btn.add(b);
                    btnEdit.add(b2);
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
        tabChangeTicket.setVisible(false);
        tabViewTicket.setVisible(true);
    }

    public void signOut(Event e) throws IOException {
        currentUser.setUser(null);
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
        List<BusTrip> lbt = bts.getBusTripsEmployee(departureDate, departureId, destinationId);

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
            }
        }
        txtCusName.setText("");
        txtCusPhone.setText("");
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
            listSelectedSeat.remove(btnSeat);
        } else {
            btnSeat.getStyleClass().add("selectedSeat");
            listSelectedSeat.add(btnSeat);
        }
    }

    public void confirmBuy(ActionEvent e) throws SQLException, IOException {
        if (listSelectedSeat.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn ghế nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        List<Ticket> tickets = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận mua vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            LocalDateTime departureTime = busTrip.getDepartureTime();

            if (departureTime.isAfter(LocalDateTime.now().plusMinutes(5))) {
                if (txtCusName.getText().isBlank() || txtCusPhone.getText().isBlank()) {
                    Alert al = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đầy đủ thông tin khách hàng", ButtonType.OK);
                    al.showAndWait();
                    return;
                }
                if (txtCusPhone.getText().length() < 10) {
                    Alert al = new Alert(Alert.AlertType.ERROR, "Số điện thoại chưa đúng định dạng", ButtonType.OK);
                    al.showAndWait();
                    return;
                }
                Customer c = cs.getCustomer(txtCusName.getText(), txtCusPhone.getText());
                if (c == null) {
                    c = new Customer(txtCusName.getText(), txtCusPhone.getText());
                    if (!cs.addCustomer(c)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra", ButtonType.OK);
                        al.showAndWait();
                        return;
                    }
                }
                for (int i = 0; i < listSelectedSeat.size(); i++) {
                    Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "purchased", busTrip.getPrice() + busTrip.getSurcharge());
                    if (!ts.addTicket(t)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                        al.show();
                    }
                    tickets.add(ts.getTicket(t.getId()));
                }
                Alert al = new Alert(Alert.AlertType.CONFIRMATION, "Mua vé thành công, bạn có muốn xuất vé? ");
                al.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> rs = al.showAndWait();
                if (rs.get() == ButtonType.YES) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("exportTicket.fxml"));
                    Parent root = loader.load();
                    TicketExportController tec = loader.getController();
                    tec.setTickets(tickets);
                    tec.loadTickets();
                    App.setRoot(root);

                }
                tabChooseBuy.setVisible(true);
                tabComfirmBuy.setVisible(false);
                loadTableTicketData(txtSearchCustomer.getText());
            } else {
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Còn 5 phút nữa là xe khởi hành, bạn không thể mua vé");
                al.show();
            }
        }
    }

    public void confirmBook(ActionEvent e) throws SQLException {
        if (listSelectedSeat.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn ghế nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận đặt vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            LocalDateTime departureTime = busTrip.getDepartureTime();

            if (departureTime.isAfter(LocalDateTime.now().plusMinutes(60))) {
                if (txtCusName.getText().isBlank() || txtCusPhone.getText().isBlank()) {
                    Alert al = new Alert(Alert.AlertType.ERROR, "Vui lòng điền đầy đủ thông tin khách hàng", ButtonType.OK);
                    al.showAndWait();
                    return;
                }
                if (txtCusPhone.getText().length() < 10) {
                    Alert al = new Alert(Alert.AlertType.ERROR, "Số điện thoại chưa đúng định dạng", ButtonType.OK);
                    al.showAndWait();
                    return;
                }
                Customer c = cs.getCustomer(txtCusName.getText(), txtCusPhone.getText());
                if (c == null) {
                    c = new Customer(txtCusName.getText(), txtCusPhone.getText());
                    if (!cs.addCustomer(c)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra", ButtonType.OK);
                        al.showAndWait();
                        return;
                    }
                }
                for (int i = 0; i < listSelectedSeat.size(); i++) {
                    Ticket t = new Ticket(c.getId(), busTrip.getId(), ss.getSeat(listSelectedSeat.get(i).getId()).getId(), currentUser.getUser().getId(), "booked", busTrip.getPrice() + busTrip.getSurcharge());
                    if (!ts.addTicket(t)) {
                        Alert al = new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra, không thể thêm");
                        al.show();
                    }
                }
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Đặt vé thành công");
                al.showAndWait();
                tabChooseBuy.setVisible(true);
                tabComfirmBuy.setVisible(false);
                loadTableTicketData(txtSearchCustomer.getText());
            } else {
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Còn 5 phút nữa là xe khởi hành, bạn không thể mua vé");
                al.show();
            }
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

        TableColumn Time = new TableColumn("Thời gian giao dịch");
        Time.setCellValueFactory(new PropertyValueFactory("Time"));
        Time.setPrefWidth(300);

        this.tbTicket.getColumns().setAll(col, colCusName, colPhone, colSeatName, colStaffName, colStatus, colPrice, colDeparture, colDestination, colDepartureTime, Time);
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
                } else if (selectedItem.getTime().plusMinutes(30).isAfter(LocalDateTime.now())) {
                    ts.changeStatusToBuy(selectedItem);
                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Thành công");
                    al.show();
                    loadTableTicketData(txtSearchCustomer.getText());
                } else {
                    Alert al = new Alert(Alert.AlertType.INFORMATION, "Không thể đặt vé vì thời gian đặt quá 30 phút");
                    al.show();
                }
            }
        }
    }
    @FXML
    private TextField CustomerNameOfTicket;
    @FXML
    private TextField PhoneNameOfTicket;
    @FXML
    private ComboBox cbBusTripOfTicket;

    @FXML
    private Ticket ticketEditing;
    @FXML
    private ComboBox cbDeparture;
    @FXML
    private ComboBox cbDestination;
    @FXML
    private DatePicker dpDepartureDate;

    public void loadBusTripEditing() throws SQLException {
        Object item = cbDeparture.getSelectionModel().getSelectedItem();
        Location l1 = item instanceof Location ? (Location) item : null;
        int departureId = l1 != null ? l1.getId() : -1;

        Object item2 = cbDestination.getSelectionModel().getSelectedItem();
        Location l2 = item2 instanceof Location ? (Location) item2 : null;
        int destinationId = l2 != null ? l2.getId() : -1;

        loadComboBusTripData(dpDepartureDate.getValue(), departureId, destinationId);
        for (int i = 0; i < cbBusTripOfTicket.getItems().size(); i++) {
            Object item3 = cbBusTripOfTicket.getItems().get(i);
            BusTrip bt = item3 instanceof BusTrip ? (BusTrip) item3 : null;
            if (bt != null && bt.getId().equals(newBusTripId)) {
                cbBusTripOfTicket.getSelectionModel().select(i);
                break;
            }
        }
    }

    public void editSeat() throws SQLException {
        if (tbTicket.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn vé nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        ticketEditing = tbTicket.getSelectionModel().getSelectedItem() instanceof Ticket ? (Ticket) tbTicket.getSelectionModel().getSelectedItem() : null;
        if (ticketEditing != null) {
            CustomerNameOfTicket.setText(ticketEditing.getCusName());
            PhoneNameOfTicket.setText(ticketEditing.getCusPhone());
            newBusTripId = ticketEditing.getBusTripId();

            loadBusTripEditing();
            for (Button b : btnEdit) {
                b.getStyleClass().remove("selectedSeat");
                b.setDisable(false);
            }

            List<Ticket> tickets = ts.getTicketsByBusTrip(ticketEditing.getBusTripId());
            for (Ticket t : tickets) {
                Seat s = ss.getSeat(t.getSeatId());
                Button b = (Button) App.getScene().lookup("#seat" + s.getName());
                b.setDisable(true);
            }

            Seat s = ss.getSeat(ticketEditing.getSeatId());
            Button b = (Button) App.getScene().lookup("#seat" + s.getName());
            b.setDisable(false);

            b.getStyleClass().add("selectedSeat");
            selectSeatEdit = b;

        }
        tabViewTicket.setVisible(false);
        tabChangeTicket.setVisible(true);

    }

    private void loadComboBusTripData(LocalDate departureDate, int departureId, int destinationId) throws SQLException {
        BusTripService bts = new BusTripService();
        List<BusTrip> lbt = bts.getBusTripsEmployee(departureDate, departureId, destinationId);

        cbBusTripOfTicket.getItems().clear();
        cbBusTripOfTicket.setItems(FXCollections.observableList(lbt));
    }

    public void changeSeat(ActionEvent e) {
        Button btnSeat = (Button) e.getSource();
        if (selectSeatEdit != null) {
            selectSeatEdit.getStyleClass().remove("selectedSeat");
        }
        selectSeatEdit = btnSeat;
        selectSeatEdit.getStyleClass().add("selectedSeat");

    }

    public void turnBack(ActionEvent e) {
        tabViewTicket.setVisible(true);
        tabChangeTicket.setVisible(false);
    }

    private String newBusTripId;
    private Button selectSeatEdit = null;

    public void changeBusTrip() throws SQLException {
        Object selection = cbBusTripOfTicket.getSelectionModel().getSelectedItem();
        BusTrip selectedBusTrip = selection instanceof BusTrip ? (BusTrip) selection : null;
        if (selectedBusTrip != null && !selectedBusTrip.getId().equals(newBusTripId)) {
            newBusTripId = selectedBusTrip.getId();
            List<Ticket> tickets = ts.getTicketsByBusTrip(newBusTripId);
            for (Button b : btnEdit) {
                b.getStyleClass().remove("selectedSeat");
                b.setDisable(false);
            }

            for (Ticket t : tickets) {
                Seat s = ss.getSeat(t.getSeatId());
                Button b = (Button) App.getScene().lookup("#seat" + s.getName());
                b.setDisable(true);
            }

            if (selectedBusTrip.getId().equals(ticketEditing.getBusTripId())) {
                Seat s = ss.getSeat(ticketEditing.getSeatId());
                Button b = (Button) App.getScene().lookup("#seat" + s.getName());
                b.setDisable(false);

                b.getStyleClass().add("selectedSeat");
                selectSeatEdit = b;
            } else {
                selectSeatEdit = null;
            }
        }
    }

    public void confirmEdit() throws SQLException {
        if (selectSeatEdit == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn ghế nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Xác nhận đổi vé?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            BusTripService bts = new BusTripService();
            BusTrip bt = bts.getBusTripById(newBusTripId);
            LocalDateTime departureTime = bt.getDepartureTime();
//            if (CustomerNameOfTicket.getText().isBlank() || PhoneNameOfTicket.getText().isBlank()) {
//                Alert al = new Alert(Alert.AlertType.WARNING, "Vui lòng điền đầy đủ thông tin", ButtonType.OK);
//                al.showAndWait();
//                return;
//            }
            if ("booked".equals(ticketEditing.getStatus()) && !departureTime.isAfter(LocalDateTime.now().plusMinutes(60))) {
                Alert al = new Alert(Alert.AlertType.WARNING, "Bạn không thể đặt xe trong 60 phút trước giờ khởi hành", ButtonType.OK);
                al.showAndWait();
                return;
            }
            if ("purchased".equals(ticketEditing.getStatus()) && !departureTime.isAfter(LocalDateTime.now().plusMinutes(5))) {
                Alert al = new Alert(Alert.AlertType.WARNING, "Bạn không thể đổi vé trong 5 phút trước giờ khởi hành", ButtonType.OK);
                al.showAndWait();
                return;
            }
//            Customer c = cs.getCustomer(CustomerNameOfTicket.getText(), PhoneNameOfTicket.getText());
//            if (c == null || !c.getId().equals(ticketEditing.getCustomerId())) {
//                int count = ts.getAmountTicketOfCustomer(ticketEditing.getCustomerId());
//                if (count == 0) {
//                    cs.deleteCustomer(ticketEditing.getCustomerId());
//                }
//            }
//            if (c == null) {
//                c = new Customer(CustomerNameOfTicket.getText(), PhoneNameOfTicket.getText());
//                if (!cs.addCustomer(c)) {
//                    Alert al = new Alert(Alert.AlertType.WARNING, "Đã có lỗi xảy ra", ButtonType.OK);
//                    al.showAndWait();
//                    return;
//                }
//            }
            ticketEditing.setStaffId(CurrentUser.getInstance().getUser().getId());
            ticketEditing.setSeatId(ss.getSeat(selectSeatEdit.getId().replaceAll("seat", "")).getId());
            ticketEditing.setBusTripId(newBusTripId);
//            ticketEditing.setCustomerId(c.getId());
            if (ts.editTicket(ticketEditing)) {
                Alert al = new Alert(Alert.AlertType.INFORMATION, "Đổi thành công", ButtonType.OK);
                al.showAndWait();
                loadTableTicketData(txtSearchCustomer.getText());
                tabViewTicket.setVisible(true);
                tabChangeTicket.setVisible(false);
            } else {
                Alert al = new Alert(Alert.AlertType.WARNING, "Đã có lỗi xảy ra, có thể đã qua 30 phút kể từ lần đặt vé đầu tiên", ButtonType.OK);
                al.showAndWait();
            }

        }

    }

    public void exportTicket(ActionEvent e) throws IOException {
        if (tbTicket.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa chọn vé nào", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("exportTicket.fxml"));
        Parent root = loader.load();
        TicketExportController c = loader.getController();
        Object o = tbTicket.getSelectionModel().getSelectedItem();
        Ticket t = o instanceof Ticket ? (Ticket) o : null;
        List<Ticket> tickets = new ArrayList<>();
        if (t != null) {
            tickets.add(t);
            c.setTickets(tickets);
            c.loadTickets();
            App.setRoot(root);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Đã có lỗi xảy ra", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
