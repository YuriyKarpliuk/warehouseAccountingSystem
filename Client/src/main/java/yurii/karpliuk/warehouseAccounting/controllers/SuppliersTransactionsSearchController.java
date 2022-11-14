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
import yurii.karpliuk.warehouseAccounting.Model.SuppliesTransactions;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuppliersTransactionsSearchController implements Initializable {
    @FXML
    private TableView<SuppliesTransactions> suppliesTable;

    @FXML
    private TableColumn<SuppliesTransactions, Integer> id;

    @FXML
    private TableColumn<SuppliesTransactions, Timestamp> date;

    @FXML
    private TableColumn<SuppliesTransactions, String> email;

    @FXML
    private TableColumn<SuppliesTransactions, Integer> price;
    @FXML
    private TableColumn<SuppliesTransactions, String> quantity;
    @FXML
    private TableColumn<SuppliesTransactions, String> productName;

    @FXML
    private DatePicker from;
    @FXML
    private DatePicker to;
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
        connection.sendMessage("suppliesTransactions");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No supplies found!");
            alert.showAndWait();
        }
    }

    public void updateTable() {
        ArrayList<SuppliesTransactions> dataList = (ArrayList<SuppliesTransactions>) connection.readObject();
        ObservableList<SuppliesTransactions> data = FXCollections.observableArrayList(dataList);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        email.setCellValueFactory(new PropertyValueFactory<>("supplierEmail"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        suppliesTable.setItems(data);


        FilteredList<SuppliesTransactions> filteredList = new FilteredList<>(data);

        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate fromD = from.getValue();
                    LocalDate toD = to.getValue();

                    // get final values != null
                    final LocalDate finalFrom = fromD == null ? LocalDate.MIN : fromD;
                    final LocalDate finalTo = toD == null ? LocalDate.MAX : toD;

                    // values for openDate need to be in the interval [finalMin, finalMax]
                    return ti -> !finalFrom.isAfter(ti.getDate().toLocalDateTime().toLocalDate()) && !finalTo.isBefore(ti.getDate().toLocalDateTime().toLocalDate());
                },
                from.valueProperty(),
                to.valueProperty()));

        suppliesTable.setItems(filteredList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("suppliesTransactions");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No supplies found!");
            alert.showAndWait();
        }
    }
}
