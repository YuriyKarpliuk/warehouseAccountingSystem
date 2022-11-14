package yurii.karpliuk.warehouseAccounting.database;


import yurii.karpliuk.warehouseAccounting.Model.*;

import java.sql.*;
import java.util.ArrayList;


public class DBWorker {
    private static DBWorker db = null;
    private static final String URL = "jdbc:mysql://localhost:3306/warehouseacounting?allowPublicKeyRetrieval=true&&autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "2003";
    private PreparedStatement preparedStatement;
    private Statement statement;
    private Connection connection;
    private int userid = 0;
    private ArrayList<User> customers;
    private ArrayList<User> suppliers;
    private ArrayList<String> categories;
    private ArrayList<String> roles;
    private ArrayList<TransactionRowData> myTransactions;
    private ArrayList<ProductsRowData> products;
    private ArrayList<ProductsRowData> cartItems;
    private ArrayList<SuppliesTransactions> suppliesTransactions;
    private ArrayList<SalesTransactions> salesTransactions;
    private ArrayList<TransactionRowData> myOrders;


    public Connection getConnection() {
        return connection;
    }

    public DBWorker() {

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            System.out.println("Error while loading driver");
        }
    }

    public static DBWorker getInstance() {
        if (db == null) {
            db = new DBWorker();
        }
        return db;
    }

    public boolean SignUp(String username, String password) {
        try {
            preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE users.username=? and users.uPassword=md5(?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userid = rs.getInt("id");
                setIsActive(userid, true);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean logOut(String username) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE users.username=?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userid = rs.getInt("id");
                setIsActive(userid, false);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setIsActive(Integer id, Boolean isActive) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE users SET isActive=? WHERE id=?");
            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username, String email) {
        try {
            preparedStatement = connection.prepareStatement("SELECT *  from users where users.username = ? or users.email=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean RegisterNewUser(String username, String firstName, String lastName, String email, String password, String uRole) {
        try {
            if (userExists(username, email)) {
                return false;
            } else {
                Boolean isActive = true;
                preparedStatement = connection.prepareStatement("INSERT INTO users(username, firstname, lastname, email,uPassword,uRole,isActive) VALUES (?,?,?,?,md5(?),?,?)");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, uRole);
                preparedStatement.setString(6, password);
                preparedStatement.setBoolean(7, isActive);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public Integer getCategoryId(String categoryName) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM productCategories WHERE categoryName= ?");
        preparedStatement.setString(1, categoryName);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return null;
        }
    }

    public String getCategoryName(Integer categoryId) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM productCategories WHERE id= ?");
        preparedStatement.setInt(1, categoryId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getString("categoryName");
        } else {
            return null;
        }
    }

    public String getSupplierName(Integer userId) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id= ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getString("username");
        } else {
            return null;
        }
    }

    public String getSupplierEmail(Integer userId) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id= ?");
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getString("email");
        } else {
            return null;
        }
    }

    public Integer getProductId(Integer code) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE pCode= ?");
        preparedStatement.setInt(1, code);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return null;
        }
    }
    public Integer getProductStockByCode(Integer code) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE pCode= ?");
        preparedStatement.setInt(1, code);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("quantity");
        } else {
            return null;
        }
    }

    public Integer getProductIdByName(String pName) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE productName= ?");
        preparedStatement.setString(1, pName);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return null;
        }
    }

    public Integer getTransactionId(Timestamp timestamp) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE tDate= ?");
        preparedStatement.setTimestamp(1, timestamp);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return null;
        }
    }

    public boolean addProduct(Product product, Integer categoryId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO product(productName,quantity, price,pCode,pDescription,imgUrl,categoryId) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getStock());
            preparedStatement.setInt(3, product.getPrice());
            preparedStatement.setInt(4, product.getpCode());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setString(6, product.getImgUrl());
            preparedStatement.setInt(7, categoryId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addTransactionItem(TransactionItem transactionItem, Integer transactionId, Integer productId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO transactionItem(totalPrice, quantity, transactionId, productId) VALUES (?,?,?,?)");
            preparedStatement.setInt(1, transactionItem.getTotalPrice());
            preparedStatement.setInt(2, transactionItem.getQuantity());
            preparedStatement.setInt(3, transactionId);
            preparedStatement.setInt(4, productId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createTransaction(Transaction transaction, Integer userId) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO transactions(tDATE, totalPrice,quantity,userId,transactionType) VALUES (?,?,?,?,?)");
            preparedStatement.setTimestamp(1, transaction.getDate());
            preparedStatement.setInt(2, transaction.getTotalPrice());
            preparedStatement.setInt(3, transaction.getTotalQuantity());
            preparedStatement.setInt(4, userId);
            preparedStatement.setString(5, transaction.getTransactionType());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean clearCart(Integer userId) {
        try {
            preparedStatement = connection.prepareStatement("delete from  cartItem where userId=?");
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProductStock(String productName, Integer quantity) {
        try {
            preparedStatement = connection.prepareStatement("update  product set quantity=quantity - ? where productName=?");
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, productName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public User getUserInfo(String username) throws SQLException {
        User user = new User();
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username= ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setEmail(rs.getString("email"));
            user.setuRole(rs.getString("uRole"));
            user.setPassword(rs.getString("uPassword"));
        }
        return user;
    }

    public ArrayList<User> searchCustomers() {
        customers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select id,firstName,lastName, username,email from users where uRole=?");
            preparedStatement.setString(1, "Customer");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                customers.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return customers;
    }
    public ArrayList<User> searchSuppliers() {
        suppliers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select id,firstName,lastName, username,email from users where uRole=?");
            preparedStatement.setString(1, "Supplier");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                suppliers.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return suppliers;
    }

    public ArrayList<ProductsRowData> searchProducts() {
        products = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select p.id,p.productName,p.quantity, p.price,p.categoryId,p.pDescription,p.pCode,p.imgUrl,t2.userId from product p  join transactionitem t on p.id = t.productId join transactions t2 on t2.id = t.transactionId join users u on t2.userId = u.id where u.uRole=? ");
            preparedStatement.setString(1, "Supplier");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                products.add(new ProductsRowData(rs.getInt("id"), rs.getString("productName"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("pDescription"), rs.getString("imgUrl"), rs.getInt("pCode"), getCategoryName(rs.getInt("categoryId")), getSupplierName(rs.getInt("userId"))));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return products;
    }

    public boolean pCodeExists(Integer pcode){
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM product where pCode=?");
            preparedStatement.setInt(1, pcode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userid = rs.getInt("id");
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ProductsRowData> searchCartItems() {
        cartItems = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select p.productName,p.categoryId,p.pDescription,p.imgUrl,c.userId,c.price,c.quantity from cartitem c join product p on c.productId = p.id");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cartItems.add(new ProductsRowData(rs.getString("productName"), getCategoryName(rs.getInt("categoryId")), rs.getInt("price"), rs.getInt("quantity"), rs.getString("pDescription"), rs.getString("imgUrl")));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return cartItems;
    }

    public ArrayList<TransactionRowData> searchMyTransaction(Integer userid) {
        myTransactions = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select p.productName,t2.tDate,p.price, p.quantity,p.categoryId,p.pCode,p.pDescription from product p join transactionitem t on p.id = t.productId join transactions t2 on t2.id = t.transactionId where t2.userId=? and t2.transactionType=?");
            preparedStatement.setInt(1, userid);
            preparedStatement.setString(2, "Supply");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                myTransactions.add(new TransactionRowData(rs.getInt("pCode"), getCategoryName(rs.getInt("categoryId"))
                        , rs.getString("productName"), rs.getInt("price"), rs.getInt("quantity"), rs.getString("pDescription"), rs.getTimestamp("tDate")));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return myTransactions;
    }

    public ArrayList<TransactionRowData> searchMyOrders(Integer userid) {
        myOrders = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select p.productName,t2.tDate,t.totalPrice, t.quantity,p.categoryId,p.pDescription from product p join transactionitem t on p.id = t.productId join transactions t2 on t2.id = t.transactionId where t2.userId=? and t2.transactionType=?");
            preparedStatement.setInt(1, userid);
            preparedStatement.setString(2, "Order");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                myOrders.add(new TransactionRowData(getCategoryName(rs.getInt("categoryId"))
                        , rs.getString("productName"), rs.getInt("totalPrice"), rs.getInt("quantity"), rs.getString("pDescription"), rs.getTimestamp("tDate")));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return myOrders;
    }

    public ArrayList<SuppliesTransactions> searchSuppliesTransaction() {
        suppliesTransactions = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select t.id,t.tDate,t.userId, t.totalPrice,t.quantity,p.productName from transactions t join transactionitem t1 on t.id = t1.transactionId join product p on t1.productId = p.id where transactionType=?");
            preparedStatement.setString(1,"Supply");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                suppliesTransactions.add(new SuppliesTransactions(rs.getInt("id"), rs.getTimestamp("tDate")
                        , getSupplierEmail(rs.getInt("userId")), rs.getInt("totalPrice"), rs.getInt("quantity"), rs.getString("productName")));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return suppliesTransactions;
    }
    public ArrayList<SalesTransactions> searchSalesTransaction() {
        salesTransactions = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select t.id,t.tDate,t.userId, t1.totalPrice,t1.quantity,p.productName from transactions t join transactionitem t1 on t.id = t1.transactionId join product p on t1.productId = p.id where transactionType=?");
           preparedStatement.setString(1,"Order");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                salesTransactions.add(new SalesTransactions(rs.getInt("id"), rs.getTimestamp("tDate")
                        , getSupplierEmail(rs.getInt("userId")), rs.getInt("totalPrice"), rs.getInt("quantity"), rs.getString("productName")));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return salesTransactions;
    }


    public ArrayList<String> searchCategories() {
        categories = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("select categoryName from productCategories");
            while (rs.next()) {
                categories.add(rs.getString("categoryName"));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return categories;
    }
    public ArrayList<String> searchRoles() {
        roles = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("select roleName from roles where id between 1 and 2");
            while (rs.next()) {
                roles.add(rs.getString("roleName"));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return roles;
    }



    public boolean addOrderItem(ProductsRowData productsRowData, Integer productId, Integer userId) throws SQLException {
        try {
            if(productsRowData.getStock()<getProductStockByCode(productsRowData.getCode())) {
                preparedStatement = connection.prepareStatement("INSERT INTO cartItem(price, quantity, productId,userId) VALUES (?,?,?,?)");
                preparedStatement.setInt(1, productsRowData.getPrice());
                preparedStatement.setInt(2, productsRowData.getStock());
                preparedStatement.setInt(3, productId);
                preparedStatement.setInt(4, userId);
                preparedStatement.executeUpdate();
                return true;
            }else{
                return  false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
