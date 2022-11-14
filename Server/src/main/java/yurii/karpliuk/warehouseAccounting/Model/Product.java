package yurii.karpliuk.warehouseAccounting.Model;


import java.io.Serializable;


public class Product implements Serializable {
    private Integer id;
    private String productName;
    private Integer price;
    private Integer  stock;
    private String  description;
    private String imgUrl;


    private  Integer pCode;
    private  String  productCategory;


    public Product( String productName, Integer price, Integer stock, String description,  String productCategory,String imgUrl) {
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productCategory = productCategory;
        this.imgUrl=imgUrl;

    }

    public Product(String productName,String description,String productCategory,Integer price,Integer stock,String imgUrl) {
        this.productName = productName;
        this.description = description;
        this.productCategory = productCategory;
        this.price = price;
        this.stock = stock;
        this.imgUrl=imgUrl;
    }
    public Product(Integer id,String productName,String description,String productCategory,Integer price,Integer stock,String imgUrl,Integer pCode) {
        this.id= id;
        this.productName = productName;
        this.description = description;
        this.productCategory = productCategory;
        this.price = price;
        this.stock = stock;
        this.pCode = pCode;
        this.imgUrl=imgUrl;
    }

    public Product(String productName,String description,String productCategory) {
        this.productName = productName;
        this.description = description;
        this.productCategory = productCategory;
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


    public Integer getpCode() {
        return pCode;
    }

    public void setpCode(Integer pCode) {
        this.pCode = pCode;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }



}
