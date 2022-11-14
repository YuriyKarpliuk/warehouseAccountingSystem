package yurii.karpliuk.warehouseAccounting.Model;

import java.io.Serializable;

public class ProductsRowData implements Serializable {
    private Integer id;
    private String productName;
    private Integer price;
    private Integer stock;
    private String description;
    private String imgUrl;
    private Integer code;
    private String productCategory;

    private String supplierName;

    public ProductsRowData(Integer id, String productName, Integer price, Integer stock, String description, String imgUrl, Integer code, String productCategory, String supplierName) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imgUrl = imgUrl;
        this.code = code;
        this.productCategory = productCategory;
        this.supplierName = supplierName;
    }
    public ProductsRowData(String productName,String productCategory, Integer price, Integer stock, String description, String imgUrl) {
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imgUrl = imgUrl;
        this.productCategory = productCategory;
    }

    public ProductsRowData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
