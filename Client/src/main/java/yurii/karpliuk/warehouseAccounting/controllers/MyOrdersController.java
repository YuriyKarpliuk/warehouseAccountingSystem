package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.TransactionRowData;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyOrdersController implements Initializable {
    @FXML
    private TableView<TransactionRowData> myOrdersTable;

    @FXML
    private TableColumn<TransactionRowData, String> productName;

    @FXML
    private TableColumn<TransactionRowData, Timestamp> date;

    @FXML
    private TableColumn<TransactionRowData, Integer> totalPrice;

    @FXML
    private TableColumn<TransactionRowData, Integer> quantity;
    @FXML
    private TableColumn<TransactionRowData, String> category;
    @FXML
    private TableColumn<TransactionRowData, String> description;

    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    private Client connection = Client.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection.sendMessage("myOrdersList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No orders found!");
            alert.showAndWait();
        }
    }

    public void updateTable() {
        ArrayList<TransactionRowData> dataList = (ArrayList<TransactionRowData>) connection.readObject();
        ObservableList<TransactionRowData> data = FXCollections.observableArrayList(dataList);

        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("stock"));
        category.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        myOrdersTable.setItems(data);


        FilteredList<TransactionRowData> filteredList = new FilteredList<>(data);

        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate fromD = fromDate.getValue();
                    LocalDate toD = toDate.getValue();

                    // get final values != null
                    final LocalDate finalFrom = fromD == null ? LocalDate.MIN : fromD;
                    final LocalDate finalTo = toD == null ? LocalDate.MAX : toD;

                    // values for openDate need to be in the interval [finalMin, finalMax]
                    return ti -> !finalFrom.isAfter(ti.getDate().toLocalDateTime().toLocalDate()) && !finalTo.isBefore(ti.getDate().toLocalDateTime().toLocalDate());
                },
                fromDate.valueProperty(),
                toDate.valueProperty()));

        myOrdersTable.setItems(filteredList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("myOrdersList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No orders found!");
            alert.showAndWait();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("back");
        if (connection.readMessage().equals("Good")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customerMainPage.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                stage.setTitle("Customer Main Page");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest((WindowEvent we)->{
                    connection.sendMessage("shutdown");
                });
                // Hide this current window (if this is what you want)
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("User not active!");
            alert.showAndWait();
        }
    }
}
