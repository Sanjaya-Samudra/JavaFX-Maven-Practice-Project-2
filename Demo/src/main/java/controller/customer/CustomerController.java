package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerService {

    private static CustomerController instance;

    private CustomerController(){}

    public static CustomerController getInstance(){
        return instance==null?instance=new CustomerController():instance;
    }
    @Override
    public boolean addCustomer(Customer customer) {
        String SQL = "INSERT INTO Customer values(?,?,?,?,?,?,?,?,?)";

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1, customer.getId());
            psTm.setObject(2, customer.getName());
            psTm.setObject(3, customer.getTitle());
            psTm.setObject(4, customer.getDob());
            psTm.setObject(5, customer.getSalary());
            psTm.setObject(6, customer.getAddress());
            psTm.setObject(7, customer.getCity());
            psTm.setObject(8, customer.getPostalCode());
            psTm.setObject(9, customer.getProvince());

            return psTm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
            while (resultSet.next()) {
                // Get the date and handle potential null values
                LocalDate dateOfBirth = null;
                java.sql.Date sqlDate = resultSet.getDate(4);
                if (sqlDate != null) {
                    dateOfBirth = sqlDate.toLocalDate();
                }
                observableList.add(
                        new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                //resultSet.getDate(4).toLocalDate(),
                                dateOfBirth,
                                resultSet.getDouble(5),
                                resultSet.getString(6),
                                resultSet.getString(7),
                                resultSet.getString(8),
                                resultSet.getString(9)
                        )
                );
            }
            return observableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean updateCustomer(Customer customer) {

        String SQL = "UPDATE Customer SET CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? WHERE CustID=?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1, customer.getTitle());
            psTm.setObject(2, customer.getName());
            psTm.setObject(3, customer.getDob());
            psTm.setObject(4, customer.getSalary());
            psTm.setObject(5, customer.getAddress());
            psTm.setObject(6, customer.getCity());
            psTm.setObject(7, customer.getProvince());
            psTm.setObject(8, customer.getPostalCode());
            psTm.setObject(9, customer.getId());
            return psTm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCustomer(String id) {
        try {
            return DBConnection.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM Customer WHERE CustID='" + id + "'") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Customer searchCustomer(String id) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE CustID=?", id);
            while (resultSet.next()){
                return  new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getDouble(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<String> getAllCustomerIds(){
        ArrayList<String> cusIdsList = new ArrayList<>();
        ObservableList<Customer> allCustomers = getAllCustomers();

        allCustomers.forEach(obj->{
            cusIdsList.add(obj.getId());
        });

        return cusIdsList;
    }
}