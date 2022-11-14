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
import yurii.karpliuk.warehouseAccounting.Model.SalesTransactions;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SaleTransactionsSearchController implements Initializable {
    @FXML
    private TableView<SalesTransactions> salesTable;

    @FXML
    private TableColumn<SalesTransactions, Integer> id;

    @FXML
    private TableColumn<SalesTransactions, Timestamp> date;

    @FXML
    private TableColumn<SalesTransactions, String> customerEmail;

    @FXML
    private TableColumn<SalesTransactions, Integer> totalPrice;
    @FXML
    private TableColumn<SalesTransactions, String> quantity;
    @FXML
    private TableColumn<SalesTransactions, String> productName;

    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    private Client connection = Client.getInstance();

    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminMain.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
            stage.setTitle("Admin Main Page");
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("salesTransactions");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No sales found!");
            alert.showAndWait();
        }
    }

    public void updateTable() {
        ArrayList<SalesTransactions> dataList = (ArrayList<SalesTransactions>) connection.readObject();
        ObservableList<SalesTransactions> data = FXCollections.observableArrayList(dataList);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        salesTable.setItems(data);


        FilteredList<SalesTransactions> filteredList = new FilteredList<>(data);

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

        salesTable.setItems(filteredList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("salesTransactions");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No sales found!");
            alert.showAndWait();
        }
    }
}
