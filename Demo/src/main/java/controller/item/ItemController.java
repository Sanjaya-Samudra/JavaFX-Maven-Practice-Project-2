package controller.item;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemController implements ItemService {
    private static ItemController instance;
    private ItemController(){}

    public static ItemController getInstance(){
        return instance==null?instance=new ItemController():instance;
    }
    @Override
    public boolean addItem(Item item) {
        String SQl = "INSERT INTO Item values(?,?,?,?,?)";
        try {
            Object execute = CrudUtil.execute(SQl,
                    item.getItemCode(),
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQty()
            );
            System.out.println(execute);
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Item> getAllItems() {
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM Item";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL);

            while (resultSet.next()) {
                itemObservableList.add(new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                ));
            }
            return itemObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateItem(Item item) {
        String SQL = "UPDATE Item SET Description=?, PackSize=?, UnitPrice=?, QtyOnHand=?  WHERE ItemCode=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1, item.getDescription());
            psTm.setObject(2, item.getPackSize());
            psTm.setObject(3, item.getUnitPrice());
            psTm.setObject(4, item.getQty());
            psTm.setObject(5, item.getItemCode());
            return psTm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteItem(String id) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            return connection.createStatement().executeUpdate("DELETE FROM Item WHERE ItemCode ='" + id + "'") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item searchItem(String id) {

        String SQL = "SELECT * FROM Item WHERE ItemCode='" + id + "'";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            ResultSet resultSet = psTm.executeQuery();
            while (resultSet.next()) {
                return new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    private void sample(Object... args) {

    }
}