package yurii.karpliuk.warehouseAccounting.Model;

import java.io.Serializable;
import java.sql.Timestamp;

public class SuppliesTransactions implements Serializable {
    private  Integer id;
    private Timestamp date;
    private  String  supplierEmail;
    private Integer totalPrice;
    private Integer quantity;
    private String  productName;

    public SuppliesTransactions(Integer id, Timestamp date, String supplierEmail, Integer totalPrice, Integer quantity, String productName) {
        this.id = id;
        this.date = date;
        this.supplierEmail = supplierEmail;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.productName = productName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
