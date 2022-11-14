package yurii.karpliuk.warehouseAccounting.Model;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Transaction implements Serializable {
    private Integer id;
    private String transactionType;
    private Timestamp date;

    private Integer userId;
    private Integer totalPrice=0;
    private  Integer totalQuantity=0;
    private List<TransactionItem> transactionItems = new ArrayList<>();

    public Transaction(String transactionType, Timestamp date) {
        this.transactionType = transactionType;
        this.date = date;
    }


    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(List<TransactionItem> transactionItems) {
        for(int i=0;i<transactionItems.size();i++){
            this.totalPrice +=transactionItems.get(i).getTotalPrice();

        }
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(List<TransactionItem> transactionItems) {
        for(int i=0;i<transactionItems.size();i++){
            this.totalQuantity +=transactionItems.get(i).getQuantity();
        }
    }

    public void addItem(TransactionItem transactionItem){
        transactionItems.add(transactionItem);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }
}
