package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.User;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {
    @FXML
    private TableView<User> suppliersTable;

    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, String> firstName;

    @FXML
    private TableColumn<User, String> lastName;

    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TextField suppliersSearch;
    @FXML
    private TableColumn<User, String> email;
    private Client connection = Client.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection.sendMessage("suppliersList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No customers found!");
            alert.showAndWait();
        }
    }

    public void updateTable() {
        ArrayList<User> dataList = (ArrayList<User>) connection.readObject();
        ObservableList<User> data = FXCollections.observableArrayList(dataList);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        suppliersTable.setItems(data);
        FilteredList<User> filteredList = new FilteredList<>(data, b -> true);
        suppliersSearch.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(user -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if (user.getUsername().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if (user.getFirstName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if (user.getLastName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if (user.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }));
        SortedList<User> userSortedList = new SortedList<>(filteredList);
        userSortedList.comparatorProperty().bind(suppliersTable.comparatorProperty());
        suppliersTable.setItems(userSortedList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("suppliersList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No customers found!");
            alert.showAndWait();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("back");
        if (connection.readMessage().equals("Good")) {
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
