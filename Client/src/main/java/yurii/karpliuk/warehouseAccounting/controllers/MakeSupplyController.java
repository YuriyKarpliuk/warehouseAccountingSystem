package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.Product;
import yurii.karpliuk.warehouseAccounting.Model.Transaction;
import yurii.karpliuk.warehouseAccounting.Model.TransactionItem;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MakeSupplyController implements Initializable {
    @FXML
    private ImageView image;

    private String imgUrl;

    private Client connection = Client.getInstance();
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private TextField productName;
    @FXML
    private TextField price;
    @FXML
    private TextField quantity;
    @FXML
    private TextField description;


    public void makeSupplyOnAction(ActionEvent actionEvent) {
        if (productName.getText().trim().isEmpty() || price.getText().trim().isEmpty() || quantity.getText().trim().isEmpty() || description.getText().trim().isEmpty() || imgUrl.isEmpty() || categoryChoiceBox.getValue().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields cannot be empty");
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        } else {
            Product newProduct = new Product(productName.getText(), description.getText(), categoryChoiceBox.getValue(), Integer.parseInt(price.getText()), Integer.parseInt(quantity.getText()), imgUrl);
            TransactionItem transactionItem = new TransactionItem(Integer.parseInt(quantity.getText()), Integer.parseInt(price.getText()) * Integer.parseInt(quantity.getText()), newProduct);
            Transaction transaction = new Transaction("Supply", new Timestamp(new Date().getTime()));
            transaction.addItem(transactionItem);
            transaction.setTotalQuantity(transaction.getTransactionItems());
            transaction.setTotalPrice(transaction.getTransactionItems());

            connection.sendMessage("makeSupply");
            connection.sendObject(newProduct);
            connection.sendObject(transaction);
            connection.sendObject(transactionItem);

            if (connection.readMessage().equals("Good")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Supply created");
                alert.setHeaderText("Supply transaction completed successfully");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
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
                alert.setTitle("Error while creating supply transaction!");
                alert.showAndWait();
            }
        }
    }

    public void addImageOnAction() {
        try {
            FileChooser fileChooser = new FileChooser();
            File f = fileChooser.showOpenDialog(null);
            if (f != null) {
                imgUrl = f.getAbsolutePath();
                File imageFile = new File(f.getAbsolutePath());
                Image productImg = new Image(imageFile.toURI().toString());
                image.setImage(productImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
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
            // Hide this current window (if this is what you want)
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("getCategories");
        if (connection.readMessage().equals("Good")) {
            ArrayList<String> dataList = (ArrayList<String>) connection.readObject();
            categoryChoiceBox.getItems().addAll(dataList);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error while getting categories!");
            alert.showAndWait();
        }
    }
}
