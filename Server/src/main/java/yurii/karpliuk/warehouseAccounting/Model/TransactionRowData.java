package yurii.karpliuk.warehouseAccounting.Model;

import java.io.Serializable;
import java.sql.Timestamp;

public class TransactionRowData implements Serializable {
    private  Integer code;
    private  String  productCategory;
    private String productName;
    private Integer price;
    private Integer  stock;
    private String  description;
    private Timestamp date;

    public TransactionRowData(Integer code, String productCategory, String productName, Integer price, Integer stock, String description, Timestamp date) {
        this.code = code;
        this.productCategory = productCategory;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.date = date;
    }
    public TransactionRowData(String productCategory, String productName, Integer price, Integer stock, String description, Timestamp date) {
        this.productCategory = productCategory;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.date = date;
    }



    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

}
