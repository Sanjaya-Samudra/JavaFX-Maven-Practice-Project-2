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
import java.time.LocalDate;
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
    private DatePicker  dateDob;

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

    List<Customer> customerList = new ArrayList<>();

    CustomerService service = new CustomerController();

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
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            System.out.println("1 : "+observableValue);
            System.out.println("OLD VAL : "+oldVal);
            System.out.println("NEW VAL : "+newVal);
            System.out.println("TEST 02");
            if (newVal!=null){
                addValueToText(newVal);
            }
        });
    }

    private void addValueToText(Customer newVal) {
        txtId.setText(newVal.getId());
        txtName.setText(newVal.getName());
        txtSalary.setText(""+newVal.getSalary());
        txtAddress.setText(newVal.getAddress());
        dateDob.setValue(newVal.getDob());
        cmbTitle.setValue(newVal.getTitle());
        txtCity.setText(newVal.getCity());
        txtProvince.setText(newVal.getCity());
        txtPostalCode.setText(newVal.getPostalCode());
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Customer customer = new Customer(
                txtId.getText(),
                txtName.getText(),
                cmbTitle.getValue(),
                txtAddress.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtCity.getText(),
                txtPostalCode.getText(),
                txtProvince.getText()
        );
        if (service.addCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION).show();
        }else {
            new Alert(Alert.AlertType.ERROR).show();
        }

    }

    private void loadTable(){
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement psTm = connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = psTm.executeQuery();


            while (resultSet.next()){

                LocalDate dob = null;
                Date dobSqlDate = resultSet.getDate("DOB");
                if (dobSqlDate != null) {
                    dob = dobSqlDate.toLocalDate();
                }

                Customer customer = new Customer(
                        resultSet.getString("CustId"),
                        resultSet.getString("CustName"),
                        resultSet.getString("CustTitle"),
                        resultSet.getString("CustAddress"),
                        dob,
                        resultSet.getDouble("salary"),
                        resultSet.getString("city"),
                        resultSet.getString("postalCode"),
                        resultSet.getString("province")
                );

                customerObservableList.add(customer);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        -------------------------------------------------------


        customerList.forEach(customer -> {
            customerObservableList.add(customer);
        });

        tblCustomers.setItems(customerObservableList);

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

        Customer customer = new Customer(
                txtId.getText(),
                txtName.getText(),
                cmbTitle.getValue(),
                txtAddress.getText(),
                dateDob.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtCity.getText(),
                txtPostalCode.getText(),
                txtProvince.getText()
        );


    }
}