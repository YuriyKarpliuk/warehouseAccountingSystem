package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.User;
import yurii.karpliuk.warehouseAccounting.Network.Client;
import yurii.karpliuk.warehouseAccounting.RegExValidators.EmailValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    private Client connection= Client.getInstance();
    private EmailValidator emailValidator = new EmailValidator();

    @FXML
    private ChoiceBox<String> userTypeChoiceBox;

    @FXML
    private TextField username;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("getRoles");
        if (connection.readMessage().equals("Good")) {
            ArrayList<String> dataList = (ArrayList<String>) connection.readObject();
            userTypeChoiceBox.getItems().addAll(dataList);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error while getting roles!");
            alert.showAndWait();
        }
    }


    public void SignUpButtonOnAction(ActionEvent actionEvent) {

        if (username.getText().trim().isEmpty() || firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty() || email.getText().trim().isEmpty() || password.getText().trim().isEmpty() || userTypeChoiceBox.getValue().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields cannot be empty");
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        } else {
            User newUser = new User(username.getText(), firstName.getText(), lastName.getText(), email.getText(), password.getText(), userTypeChoiceBox.getValue());

            if (emailValidator.validate(email.getText())) {
                connection.sendMessage("SignUp");
                connection.sendObject(newUser);
                if (connection.readMessage().equals("Good")) {
                    if (userTypeChoiceBox.getValue().equals("Customer")){
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
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (userTypeChoiceBox.getValue().equals("Supplier")) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("supplierMainPage.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                            stage.setTitle("Supplier Main Page");
                            stage.setResizable(false);
                            stage.show();
                            stage.setOnCloseRequest((WindowEvent we)->{
                                connection.sendMessage("shutdown");
                            });
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("User with such username/email already exists!");
                    alert.setContentText("Error!\nChange username/email or sign in with such username/email!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Email isn't valid!");
                alert.setContentText("Error!\nCheck if your email is valid!");
                alert.showAndWait();
            }
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
            stage.setTitle("Login");
            stage.show();
            stage.setOnCloseRequest((WindowEvent we)->{
                connection.sendMessage("shutdown");
            });
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
