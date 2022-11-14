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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Main;
import yurii.karpliuk.warehouseAccounting.Model.ProductsRowData;
import yurii.karpliuk.warehouseAccounting.Model.ProductsWithImage;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MakeOrderController implements Initializable {
    @FXML
    private TableView<ProductsWithImage> searchProducts;
    @FXML
    private TableColumn<ProductsWithImage, String> colName;
    @FXML
    private TableColumn<ProductsWithImage, String> categoryCol;
    @FXML
    private TableColumn<ProductsWithImage, String> descriptionCol;
    @FXML
    private TableColumn<ProductsWithImage, Integer> priceCol;
    @FXML
    private TableColumn<ProductsWithImage, Integer> stockCol;
    @FXML
    private TableColumn<ProductsWithImage, Integer> codeCol;
    @FXML
    private TableColumn<ProductsWithImage, String> imageCol;

    @FXML
    private TextField keywordField;
    @FXML
    private Client connection = Client.getInstance();


    public void CartButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("cart.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load(), 848, 564));
            stage.setTitle("Cart");
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

    public void backButtonOnAction(ActionEvent actionEvent) {
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
    }

    public void addToCartButtonOnAction(ActionEvent actionEvent) {
        ProductsWithImage productsWithImage = searchProducts.getSelectionModel().getSelectedItem();
        if (productsWithImage != null) {
            if (productsWithImage.getStock() != 0) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("productQuantity.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(fxmlLoader.load(), 300, 130));
                    stage.setTitle("Quantity");
                    stage.show();
                    stage.setOnCloseRequest((WindowEvent we)->{
                        connection.sendMessage("shutdown");
                    });
                    ProductQuantityController productQuantityController = fxmlLoader.getController();
                    productQuantityController.setProductsWithImage(productsWithImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Product isn't in stock!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Please choose which you want to add to cart!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("productsList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No products found!");
            alert.showAndWait();
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
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("img"));

        searchProducts.setItems(data);
        FilteredList<ProductsWithImage> filteredList = new FilteredList<>(data, b -> true);
        keywordField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(product -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if (product.getProductName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if (product.getDescription().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }

                    if (product.getProductCategory().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }));
        SortedList<ProductsWithImage> productsRowDataSortedList = new SortedList<>(filteredList);
        productsRowDataSortedList.comparatorProperty().bind(searchProducts.comparatorProperty());
        searchProducts.setItems(productsRowDataSortedList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("productsList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No products found!");
            alert.showAndWait();
        }
    }

}
