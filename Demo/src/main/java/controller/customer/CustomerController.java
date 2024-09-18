package controller.customer;

import db.DBConnection;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerController implements CustomerService{
    @Override
    public boolean addCustomer(Customer customer) {

        try {
            String SQL = "INSERT INTO Customer values(?,?,?,?,?,?,?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,customer.getId());
            psTm.setObject(2,customer.getTitle());
            psTm.setObject(3,customer.getName());
            psTm.setObject(4,customer.getDob());
            psTm.setObject(5,customer.getSalary());
            psTm.setObject(6,customer.getAddress());
            psTm.setObject(7,customer.getCity());
            psTm.setObject(8,customer.getProvince());
            psTm.setObject(9,customer.getPostalCode());

            return psTm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public boolean updateCustomer(Customer customer) {

        String SQL = "UPDATE customer SET CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, PostalCode=? WHERE CustID=?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,customer.getTitle());
            psTm.setObject(2,customer.getName());
            psTm.setObject(3,customer.getDob());
            psTm.setObject(4,customer.getSalary());
            psTm.setObject(5,customer.getAddress());
            psTm.setObject(6,customer.getCity());
            psTm.setObject(7,customer.getProvince());
            psTm.setObject(8,customer.getPostalCode());
            psTm.setObject(9,customer.getId());

            return psTm.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deleteCustomer(String id) {
        try {
            return DBConnection.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM customer WHERE CustID='" + id + "'") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public Customer searchCustomer(String id) {
        return null;
    }
}