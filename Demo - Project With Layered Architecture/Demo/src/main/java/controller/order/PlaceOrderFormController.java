package controller.order;

import controller.customer.CustomerController;
import controller.item.ItemController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import dto.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    public TextField txtCustomerAddress;
    public TextField txtUnitPrice;
    public TextField txtOrderId;


    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblOrderTime;

    @FXML
    private TableView<CartTm> tblCart;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtItemDescription;

    @FXML
    private TextField txtItemStock;

    @FXML
    private TextField txtQty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadCustomerIds();
        loadItemCodes();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            System.out.println(t1);
            if (t1 != null) {
                searchCustomer(t1);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            System.out.println(t1);
            if (t1 != null) {
                searchItem(t1);
            }
        });
    }

    private void loadItemCodes() {
        ObservableList<String> idsList = FXCollections.observableArrayList();
        ObservableList<Item> allItems = ItemController.getInstance().getAllItems();

        allItems.forEach(obj -> {
            idsList.add(obj.getItemCode());
        });

        cmbItemCode.setItems(idsList);

    }

    ObservableList<CartTm> cartTms = FXCollections.observableArrayList();

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        String itemCode = cmbItemCode.getValue();
        String itemDesc = txtItemDescription.getText();
        Integer qty = Integer.valueOf(txtQty.getText());
        Double unitPrice = Double.valueOf(txtUnitPrice.getText());
        Double total = unitPrice * qty;

        Integer itemStock = Integer.parseInt(txtItemStock.getText());

        if (itemStock < qty) {
            new Alert(Alert.AlertType.WARNING, "Invalid Qty").show();
        } else {
            cartTms.add(new CartTm(itemCode, itemDesc, qty, unitPrice, total));
            tblCart.setItems(cartTms);
            calcTotal();
        }


    }

    private void searchCustomer(String id) {
        Customer customer = CustomerController.getInstance().searchCustomer(id);
        System.out.println(customer);
        txtCustomerName.setText(customer.getName());
        txtCustomerAddress.setText(customer.getAddress());

    }

    private void searchItem(String id) {
        Item item = ItemController.getInstance().searchItem(id);
        txtItemDescription.setText(item.getDescription());
        txtItemStock.setText(item.getQty().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());

    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblOrderDate.setText(f.format(date));

//        -----------------------------------------------

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime now = LocalTime.now();
            lblOrderTime.setText(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadCustomerIds() {
        List<String> allCustomerIds = CustomerController.getInstance().getAllCustomerIds();
        ObservableList<String> objects = FXCollections.observableArrayList();
        allCustomerIds.forEach(id -> {
            objects.add(id);
        });

        cmbCustomerId.setItems(objects);
    }


    private void calcTotal(){
        Double netTotal=0.0;
        for (CartTm cartTm : cartTms){
            netTotal += cartTm.getTotal();
        }
        lblNetTotal.setText(netTotal.toString());
    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String orderId = txtOrderId.getText();
        LocalDate date = LocalDate.parse(lblOrderDate.getText());
        String customerId = cmbCustomerId.getValue();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        cartTms.forEach(obj->{
            orderDetails.add(
                    new OrderDetail(
                            txtOrderId.getText(),
                            obj.getItemCode(),
                            obj.getQty(),
                            0.0)
            );
        });
        Order order = new Order(orderId, date, customerId, orderDetails);
        try {
            new OrderController().placeOrder(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(order);
    }
}