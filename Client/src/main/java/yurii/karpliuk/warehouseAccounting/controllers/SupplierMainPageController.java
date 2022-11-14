package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;

public class SupplierMainPageController {
    private Client connection=Client.getInstance();
    public void makeSupplyButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("makeSupply.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 650, 430));
            stage.setTitle("Supply transaction");
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
    public void MySupplyButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mySupplyTransactions.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 848, 564));
            stage.setTitle("Supply Transactions");
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
    public void logoutButtonOnAction(ActionEvent actionEvent){
        connection.sendMessage("LogOut");
        if (connection.readMessage().equals("Good")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                stage.setTitle("Login");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest((WindowEvent we)->{
                    connection.sendMessage("shutdown");
                });
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (
                    IOException e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("User not found!");
            alert.setContentText("User isn't active!");
            alert.showAndWait();
        }
    }
}
