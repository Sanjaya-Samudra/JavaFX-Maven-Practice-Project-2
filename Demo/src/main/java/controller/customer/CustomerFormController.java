package controller.customer;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalCode;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;
    @FXML
    private ComboBox<String> cmbTitle;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colDob;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colSalary;

    @FXML
    private DatePicker dateDob;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtSalary;

    CustomerService service = CustomerController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        loadTable();

        ObservableList<String> titleList = FXCollections.observableArrayList();
        titleList.add("Mr.");
        titleList.add("Miss.");
        titleList.add("Ms.");
        cmbTitle.setItems(titleList);

//        ----------------------------------------------------------------
        System.out.println("TEST 01");
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                addValueToText(newVal);
            }
        });
    }

    private void addValueToText(Customer newVal) {
        txtId.setText(newVal.getId());
        txtName.setText(newVal.getName());
        txtSalary.setText("" + newVal.getSalary());
        txtAddress.setText(newVal.getAddress());
        dateDob.setValue(newVal.getDob());
        cmbTitle.setValue(newVal.getTitle());
        txtCity.setText(newVal.getCity());
        txtProvince.setText(newVal.getProvince());
        txtPostalCode.setText(newVal.getPostalCode());
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if(
                service.addCustomer(
                        new Customer(
                                txtId.getText(),
                                txtName.getText(),
                                cmbTitle.getValue(),
                                dateDob.getValue(),
                                Double.parseDouble(txtSalary.getText()),
                                txtAddress.getText(),
                                txtCity.getText(),
                                txtPostalCode.getText(),
                                txtProvince.getText()
                        )
                )
        ){
            new Alert(Alert.AlertType.INFORMATION, "Customer Added!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Customer Not Added!").show();
        }

    }

    private void loadTable() {
        tblCustomers.setItems(service.getAllCustomers());
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if (service.deleteCustomer(txtId.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Deleted!!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Customer Not Deleted!!").show();
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) {

        if(
                service.updateCustomer(
                        new Customer(
                                txtId.getText(),
                                cmbTitle.getValue(),
                                txtName.getText(),
                                dateDob.getValue(),
                                Double.parseDouble(txtSalary.getText()),
                                txtAddress.getText(),
                                txtCity.getText(),
                                txtPostalCode.getText(),
                                txtProvince.getText()
                        )
                )
        ){
            new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").show();
            loadTable();
        }else {
            new Alert(Alert.AlertType.INFORMATION, "Customer Not Updated!").show();
        }

    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        Customer customer = service.searchCustomer(txtId.getText());
        if (customer==null){
            new Alert(Alert.AlertType.ERROR,"No Customer Found").show();
        }else {
            txtAddress.setText(customer.getAddress());
            txtCity.setText((customer.getCity()));
            txtName.setText(customer.getName());
            txtSalary.setText(Double.toString(customer.getSalary()));
            txtProvince.setText(customer.getProvince());
            txtPostalCode.setText(customer.getPostalCode());
            cmbTitle.setValue(customer.getTitle());
            dateDob.setValue(customer.getDob());
        }
    }
}