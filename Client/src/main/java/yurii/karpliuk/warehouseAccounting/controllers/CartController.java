package yurii.karpliuk.warehouseAccounting.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.*;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private TableView<ProductsWithImage> cartTable;
    @FXML
    private TableColumn<ProductsWithImage, String> nameCol;
    @FXML
    private TableColumn<ProductsWithImage, String> categoryCol;
    @FXML
    private TableColumn<ProductsWithImage, String> descriptionCol;
    @FXML
    private TableColumn<ProductsWithImage, Integer> priceCol;
    @FXML
    private TableColumn<ProductsWithImage, Integer> quantityCol;
    @FXML
    private TableColumn<ProductsWithImage, String> imageCol;
    @FXML
    private Client connection = Client.getInstance();

    public void ConfirmOrderButtonOnAction(ActionEvent actionEvent) {
        if (cartTable.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Your cart is empty!");
            alert.setContentText("Please add at least one item to make order!");
            alert.showAndWait();
        } else {

            Transaction transaction = new Transaction("Order", new Timestamp(new java.util.Date().getTime()));
            for (int i = 0; i < cartTable.getItems().size(); i++) {
                Product product = new Product(cartTable.getItems().get(i).getProductName(), cartTable.getItems().get(i).getDescription(), cartTable.getItems().get(i).getProductCategory());
                TransactionItem transactionItem = new TransactionItem(cartTable.getItems().get(i).getStock(), cartTable.getItems().get(i).getPrice(), product);
                transaction.addItem(transactionItem);
            }
            transaction.setTotalQuantity(transaction.getTransactionItems());
            transaction.setTotalPrice(transaction.getTransactionItems());
            connection.sendMessage("confirmOrder");
            connection.sendObject(transaction);
            connection.sendObject(transaction.getTransactionItems());

            if (connection.readMessage().equals("Good")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order created");
                alert.setHeaderText("Order transaction completed successfully");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customerMainPage.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
                        stage.setResizable(false);
                        stage.setTitle("Customer Main Page");
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
                alert.setTitle("Error while creating order transaction!");
                alert.showAndWait();
            }
        }
    }

    public void clearTableButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("clearCart");
        if (connection.readMessage().equals("Good")) {
           cartTable.getItems().clear();
           cartTable.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Cart is already empty!");
            alert.showAndWait();
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
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

    public void updateTable() {
        ArrayList<ProductsRowData> dataList = (ArrayList<ProductsRowData>) connection.readObject();
        ArrayList<ProductsWithImage> productsWithImages = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ProductsWithImage productsWithImage = new ProductsWithImage();

            productsWithImage.setId(dataList.get(i).getId());
            productsWithImage.setProductName(dataList.get(i).getProductName());
            productsWithImage.setProductCategory(dataList.get(i).getProductCategory());
            productsWithImage.setCode(dataList.get(i).getCode());
            productsWithImage.setImgUrl(dataList.get(i).getImgUrl());
            productsWithImage.setPrice(dataList.get(i).getPrice());
            productsWithImage.setDescription(dataList.get(i).getDescription());
            productsWithImage.setStock(dataList.get(i).getStock());
            productsWithImage.setSupplierName(dataList.get(i).getSupplierName());

            File imgFile = new File(dataList.get(i).getImgUrl());
            ImageView img = new ImageView(new Image(imgFile.toURI().toString()));
            productsWithImage.setImg(img);
            productsWithImages.add(productsWithImage);
        }
        ObservableList<ProductsWithImage> data = FXCollections.observableArrayList(productsWithImages);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("img"));
        cartTable.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("cartList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No cart items found!");
            alert.showAndWait();
        }
    }
}
