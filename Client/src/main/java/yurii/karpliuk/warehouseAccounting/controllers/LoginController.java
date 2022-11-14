package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.User;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ImageView loginImageView;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Client connection = Client.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File loginFile = new File("src/main/resources/images/warehouseIcon.png");
        Image loginImage = new Image(loginFile.toURI().toString());
        loginImageView.setImage(loginImage);

    }

    public void registerButtonOnAction(ActionEvent actionEvent) {
        if (connection.getClientSock() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Connection refused to given hostname and port!");
            alert.setContentText("Connection refused to  127.0.0.1!");
            alert.showAndWait();
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load(), 650.4, 430));
                stage.setTitle("Registration");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest((WindowEvent we) -> {
                    connection.sendMessage("shutdown");
                    System.exit(0);
                });
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void signInButtonOnAction(ActionEvent actionEvent) {
        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields cannot be empty");
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        } else {
            if (connection.getClientSock() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Connection refused to given hostname and port!");
                alert.setContentText("Connection refused to  127.0.0.1!");
                alert.showAndWait();
            } else {
                User user = new User(username.getText(), password.getText());
                connection.sendMessage("SignIn");
                connection.sendObject(user);
                if (connection.readMessage().equals("Good")) {
                    Object object = connection.readObject();
                    if (object.equals("Supplier")) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("supplierMainPage.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                            stage.setTitle("Supplier Main Page");
                            stage.setResizable(false);
                            stage.show();
                            stage.setOnCloseRequest((WindowEvent we) -> {
                                connection.sendMessage("shutdown");
                                System.exit(0);
                            });
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (object.equals("Customer")) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customerMainPage.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                            stage.setTitle("Customer Main Page");
                            stage.setResizable(false);
                            stage.show();
                            stage.setOnCloseRequest((WindowEvent we) -> {
                                connection.sendMessage("shutdown");
                                System.exit(0);
                            });
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminMain.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                            stage.setTitle("Admin Main Page");
                            stage.setResizable(false);
                            stage.show();
                            stage.setOnCloseRequest((WindowEvent we) -> {
                                connection.sendMessage("shutdown");
                                System.exit(0);
                            });
                            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            ex.getMessage();
                        }
                    }
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setTitle("Error!");
                    alert1.setHeaderText("User not found or wrong password!");
                    alert1.setContentText("User with username: " + username.getText() + " not registrated!");
                    alert1.showAndWait();
                }
            }
        }
    }
}




