package yurii.karpliuk.warehouseAccounting.Model;


import java.io.Serializable;

public class TransactionItem implements Serializable {
    private Integer id;
    private Integer quantity;
    private Integer totalPrice;

    private Transaction transaction;

    private Product product;

    public TransactionItem(Integer quantity, Integer totalPrice, Product product) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
