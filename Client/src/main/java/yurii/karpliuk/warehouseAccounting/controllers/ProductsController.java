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

public class ProductsController implements Initializable {
    @FXML
    private TableView<ProductsWithImage> productsTable;

    @FXML
    private TableColumn<ProductsWithImage, Integer> id;

    @FXML
    private TableColumn<ProductsWithImage, String> name;

    @FXML
    private TableColumn<ProductsWithImage, String> category;

    @FXML
    private TableColumn<ProductsWithImage, String> image;
    @FXML
    private TableColumn<ProductsWithImage, String> description;
    @FXML
    private TableColumn<ProductsWithImage, Integer> price;
    @FXML
    private TableColumn<ProductsWithImage, Integer> quantity;
    @FXML
    private TableColumn<ProductsWithImage, String> supplierName;
    @FXML
    private TableColumn<ProductsWithImage, Integer> code;
    @FXML
    private TextField keywordField;
    @FXML
    private Client connection = Client.getInstance();

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
                // Hide this current window (if this is what you want)
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection.sendMessage("productsList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
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
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("stock"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        category.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        image.setCellValueFactory(new PropertyValueFactory<>("img"));

        productsTable.setItems(data);

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
                    if (product.getSupplierName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    }
                    if (product.getProductCategory().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                }));
        SortedList<ProductsWithImage> productSortedList = new SortedList<>(filteredList);
        productSortedList.comparatorProperty().bind(productsTable.comparatorProperty());
        productsTable.setItems(productSortedList);
    }

    public void refreshButtonOnAction(ActionEvent actionEvent) {
        connection.sendMessage("productsList");
        if (connection.readMessage().equals("Good")) {
            updateTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found!");
            alert.setHeaderText("No products found!");
            alert.showAndWait();
        }
    }
}
