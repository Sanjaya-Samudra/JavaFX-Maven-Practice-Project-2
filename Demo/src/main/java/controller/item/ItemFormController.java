package controller.item;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colUnitPrize;

    @FXML
    private TableView<Item> tblItems;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtUnitPrice;
    ItemService service = ItemController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrize.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        tblItems.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal != null) {
                addValueToText(newVal);
            }
        });

        loadTable();
    }
    private void addValueToText(Item newVal) {
        txtItemCode.setText(newVal.getItemCode());
        txtDescription.setText(newVal.getDescription());
        txtPackSize.setText(newVal.getPackSize());
        txtUnitPrice.setText(newVal.getUnitPrice().toString());
        txtQty.setText(newVal.getQty().toString());
    }
    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (
                service.addItem(
                        new Item(
                                txtItemCode.getText(),
                                txtDescription.getText(),
                                txtPackSize.getText(),
                                Double.parseDouble(txtUnitPrice.getText()),
                                Integer.parseInt(txtQty.getText())
                        )
                )
        ) {
            new Alert(Alert.AlertType.INFORMATION, "Item Added!!").show();
            loadTable();

        } else {
            new Alert(Alert.AlertType.ERROR, "Item Not Added!!").show();

        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (service.deleteItem(txtItemCode.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Item Deleted!!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Item Not Deleted!!").show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Item item = service.searchItem(txtItemCode.getText());
        txtDescription.setText(item.getDescription());
        txtPackSize.setText(item.getPackSize());
        txtQty.setText(item.getQty().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

        if (
                service.updateItem(
                        new Item(
                                txtItemCode.getText(),
                                txtDescription.getText(),
                                txtPackSize.getText(),
                                Double.parseDouble(txtUnitPrice.getText()),
                                Integer.parseInt(txtQty.getText())
                        )
                )
        ) {
            new Alert(Alert.AlertType.INFORMATION, "Item Updated !!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Item Not Updated !!").show();

        }
    }

    private void loadTable() {
        tblItems.setItems(service.getAllItems());
    }


}