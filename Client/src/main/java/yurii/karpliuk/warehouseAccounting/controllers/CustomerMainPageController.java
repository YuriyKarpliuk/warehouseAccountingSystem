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


public class CustomerMainPageController   {
    private Client connection = Client.getInstance();
    public void makeOrderButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("makeOrder.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 848, 564));
            stage.setTitle("Products");
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
    public void MyOrdersButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("myOrders.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 848, 564));
            stage.setTitle("Orders");
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
        connection = Client.getInstance();
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
