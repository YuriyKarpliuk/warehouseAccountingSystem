package yurii.karpliuk.warehouseAccounting.Model;



import java.io.Serializable;

public class ProductCategory  implements Serializable {
    private Integer id;
    private  String categoryName;


    public ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
