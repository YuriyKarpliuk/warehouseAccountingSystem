package yurii.karpliuk.warehouseAccounting.Model;

import java.io.Serializable;
import java.sql.Timestamp;

public class SalesTransactions implements Serializable {
    private  Integer id;
    private Timestamp date;
    private  String  customerEmail;
    private Integer totalPrice;
    private Integer quantity;
    private String  productName;

    public SalesTransactions(Integer id, Timestamp date, String customerEmail, Integer totalPrice, Integer quantity, String productName) {
        this.id = id;
        this.date = date;
        this.customerEmail = customerEmail;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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
