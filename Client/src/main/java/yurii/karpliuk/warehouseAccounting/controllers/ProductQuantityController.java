package yurii.karpliuk.warehouseAccounting.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import yurii.karpliuk.warehouseAccounting.Model.ProductsRowData;
import yurii.karpliuk.warehouseAccounting.Model.ProductsWithImage;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;

public class ProductQuantityController {
    private ProductsRowData productsRowData;
    private ProductsWithImage productsWithImage;
    @FXML
    private TextField quantity;
    @FXML
    private Client connection=Client.getInstance();
    public void quantityOkButtonOnAction(ActionEvent actionEvent) throws IOException {
        if(quantity.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Quantity cannot be empty");
            alert.setContentText("Please enter quantity of product!");
            alert.showAndWait();
        }else {
            productsRowData = new ProductsRowData();
            productsRowData.setId(productsWithImage.getId());
            productsRowData.setCode(productsWithImage.getCode());
            productsRowData.setSupplierName(productsWithImage.getSupplierName());
            productsRowData.setProductName(productsWithImage.getProductName());
            productsRowData.setProductCategory(productsWithImage.getProductCategory());
            productsRowData.setDescription(productsWithImage.getDescription());
            productsRowData.setImgUrl(productsWithImage.getImgUrl());
            productsRowData.setStock(Integer.parseInt(quantity.getText()));
            productsRowData.setPrice(productsWithImage.getPrice()* productsRowData.getStock());

            connection.sendMessage("cartItem");
            connection.sendObject(productsRowData);
            if (connection.readMessage().equals("Good")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Item successfully added to cart!");
                alert.showAndWait();
            }  else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Check if product is in stock!");
            alert.showAndWait();
        }
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        }

    }
    public void quantityCancelButtonOnAction(ActionEvent actionEvent) {
        // Hide this current window (if this is what you want)
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

    }

    public void setProductsWithImage(ProductsWithImage productsWithImage) {
        this.productsWithImage = productsWithImage;
    }
}
