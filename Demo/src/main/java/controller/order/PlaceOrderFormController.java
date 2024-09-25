package controller.order;

import controller.customer.CustomerController;
import controller.item.ItemController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.CartTm;
import model.Customer;
import model.Item;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    public TextField txtCustomerAddress;

    public TextField txtUnitPrice;

    @FXML
    private Button btnPlaceOrderOnAction;

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
            if(t1!=null){
                searchCustomer(t1);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            System.out.println(t1);
            if(t1!=null){
                searchItem(t1);
            }
        });
    }

    private void loadItemCodes() {
        ObservableList<String> idsList = FXCollections.observableArrayList();
        ObservableList<Item> allItems = ItemController.getInstance().getAllItems();

        allItems.forEach(obj->{
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

        if(itemStock < qty){
            new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
        }else{
            CartTm cartTm = new CartTm(itemCode, itemDesc, qty, unitPrice, total);
            cartTms.add(cartTm);
            tblCart.setItems(cartTms);
        }

    }

    private void searchCustomer(String id){
        Customer customer = CustomerController.getInstance().searchCustomer(id);
        System.out.println(customer);
        txtCustomerName.setText(customer.getName());
        txtCustomerAddress.setText(customer.getAddress());

    }

    private void searchItem(String id){
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

    private void loadCustomerIds(){
        List<String> allCustomerIds = CustomerController.getInstance().getAllCustomerIds();
        ObservableList<String> objects = FXCollections.observableArrayList();
        allCustomerIds.forEach(id->{
            objects.add(id);
        });

        cmbCustomerId.setItems(objects);
    }


    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        new Alert(Alert.AlertType.INFORMATION, "Sorry! Button Not Set Yet").show();
    }
}