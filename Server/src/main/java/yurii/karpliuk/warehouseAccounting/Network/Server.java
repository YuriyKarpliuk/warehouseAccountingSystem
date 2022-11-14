package yurii.karpliuk.warehouseAccounting.Network;

import yurii.karpliuk.warehouseAccounting.Model.*;
import yurii.karpliuk.warehouseAccounting.database.DBWorker;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements Runnable {

    private Socket factSocket;
    private DBWorker db = DBWorker.getInstance();
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;


    private HashMap<String, ClientInfo> clientInfoHashMap = new HashMap<>();

    private String messServ = null;
    boolean m_bRunThread = true;


    Server(Socket socket) throws IOException {

        try {
            this.factSocket = socket;
            inStream = new ObjectInputStream(factSocket.getInputStream());
            outStream = new ObjectOutputStream(factSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            outStream.flush();
            inStream.close();
            outStream.close();
            factSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readMessage() {
        try {
            messServ = (String) inStream.readObject();
        } catch (Exception e) {
            System.err.println("Client disconnected");

        }
        return messServ;
    }

    private void sendMessage(String messServ) {
        try {
            outStream.flush();
            outStream.writeObject(messServ);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object readObject() {
        Object object = null;
        try {
            object = inStream.readObject();
        } catch (Exception e) {
            System.out.println("No data in thread");
        }

        return object;
    }


    private void sendObject(Object object) {
        try {
            outStream.flush();
            outStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (m_bRunThread) {
//            while ((messServ = (String) inStream.readObject()) != null) {
                messServ = (String) inStream.readObject();
                switch (messServ) {
                    case "SignIn": {
                        User user = (User) readObject();
                        try {
                            if (db.SignUp(user.getUsername(), user.getPassword())) {
                                sendMessage("Good");
                                sendObject(db.getUserInfo(user.getUsername()).getuRole());
                                System.out.println("Signed in user:\n" + "\t 'id': '" + db.getUserInfo(user.getUsername()).getId() + "'\n" + "\t 'username': '" + db.getUserInfo(user.getUsername()).getUsername() + "'\n" + "\t 'firsName': '" + db.getUserInfo(user.getUsername()).getFirstName() + "'\n" + "\t 'lastName': '" + db.getUserInfo(user.getUsername()).getLastName() + "'\n" + "\t 'email': '" + db.getUserInfo(user.getUsername()).getEmail() + "'\n" + "\t 'uRole': '" + db.getUserInfo(user.getUsername()).getuRole() + "'\n" + "\t 'password': '" + db.getUserInfo(user.getUsername()).getPassword() + "'");
                                ClientInfo clientInfo = new ClientInfo();
                                clientInfo.setClientIpPort(factSocket.getInetAddress().getHostName());
                                clientInfo.setUser(user);
                                clientInfoHashMap.put(factSocket.getInetAddress().getHostAddress(),clientInfo);
                            } else {
                                sendMessage("Fail");
                                System.out.println("User not found. Wrong username or password");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    break;
                    case "SignUp": {
                        User user = (User) readObject();
                        try {
                            if (db.RegisterNewUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getuRole(), user.getPassword())) {
                                sendMessage("Good");
                                System.out.println("Signed up user:\n" + "\t 'id': '" + db.getUserInfo(user.getUsername()).getId() + "'\n" + "\t 'username': '" + db.getUserInfo(user.getUsername()).getUsername() + "'\n" + "\t 'firsName': '" + db.getUserInfo(user.getUsername()).getFirstName() + "'\n" + "\t 'lastName': '" + db.getUserInfo(user.getUsername()).getLastName() + "'\n" + "\t 'email': '" + db.getUserInfo(user.getUsername()).getEmail() + "'\n" + "\t 'uRole': '" + db.getUserInfo(user.getUsername()).getuRole() + "'\n" + "\t 'password': '" + db.getUserInfo(user.getUsername()).getPassword() + "'");
                                ClientInfo clientInfo = new ClientInfo();
                                clientInfo.setClientIpPort(factSocket.getInetAddress().getHostName());
                                clientInfo.setUser(user);
                                clientInfoHashMap.put(factSocket.getInetAddress().getHostAddress(),clientInfo);
                            } else {
                                System.out.println("User with such username/email already exists!");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "LogOut": {
                        User user = db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername());
                        try {
                            if (db.logOut(user.getUsername())) {
                                sendMessage("Good");
                                System.out.println("Logged out user:\n" + "\t 'id': '" + db.getUserInfo(user.getUsername()).getId() + "'\n" + "\t 'username': '" + db.getUserInfo(user.getUsername()).getUsername() + "'\n" + "\t 'firsName': '" + db.getUserInfo(user.getUsername()).getFirstName() + "'\n" + "\t 'lastName': '" + db.getUserInfo(user.getUsername()).getLastName() + "'\n" + "\t 'email': '" + db.getUserInfo(user.getUsername()).getEmail() + "'\n" + "\t 'uRole': '" + db.getUserInfo(user.getUsername()).getuRole() + "'\n" + "\t 'password': '" + db.getUserInfo(user.getUsername()).getPassword() + "'");

                            } else {
                                System.out.println("User isn't active so he can't be logged out!");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "customerList": {
                        try {
                            if (!db.searchCustomers().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching customers");
                                sendObject(db.searchCustomers());
                            } else {
                                System.out.println("No customers found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "suppliersList": {
                        try {
                            if (!db.searchSuppliers().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching suppliers");
                                sendObject(db.searchSuppliers());
                            } else {
                                System.out.println("Error!No suppliers found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "getCategories": {
                        try {
                            if (!db.searchCategories().isEmpty()) {
                                sendMessage("Good");
                                sendObject(db.searchCategories());
                            } else {
                                System.out.println("Error!No product categories found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "getRoles": {
                        try {
                            if (!db.searchRoles().isEmpty()) {
                                sendMessage("Good");
                                sendObject(db.searchRoles());
                            } else {
                                System.out.println("Error!No user roles found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "back": {
                        User user = db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername());
                        try {
                            if (db.userExists(user.getUsername(), user.getEmail())) {
                                sendMessage("Good");
                                System.out.println("User " + user.getUsername() + " returned to previous window");
                            } else {
                                System.out.println("User isn't active so he can't return to previous window");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "makeSupply": {
                        Product product = (Product) readObject();
                        Transaction transaction = (Transaction) readObject();
                        TransactionItem transactionItem = (TransactionItem) readObject();
                        try {
                            while (true) {
                                if (!db.pCodeExists((int) ((Math.random() * (999999 - 1000)) + 1000))) {
                                    product.setpCode((int) ((Math.random() * (999999 - 1000)) + 1000));
                                    break;
                                }
                            }
                            if (db.addProduct(product, db.getCategoryId(product.getProductCategory())) && db.createTransaction(transaction, db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId()) && db.addTransactionItem(transactionItem, db.getTransactionId(transaction.getDate()), db.getProductId(product.getpCode()))) {
                                sendMessage("Good");
                                System.out.println("Created supply transaction:\n" + "\t 'time': '" + transaction.getDate() + "'\n" + "\t 'product code': " + product.getpCode());
                            } else {
                                System.out.println("Error while creating supply transaction!");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "mySupplyList": {
                        try {
                            if (!db.searchMyTransaction(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId()).isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching his supplies");
                                sendObject(db.searchMyTransaction(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId()));
                            } else {
                                System.out.println("No supplies found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "myOrdersList": {
                        try {
                            if (!db.searchMyOrders(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId()).isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching his orders");
                                sendObject(db.searchMyOrders((db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId())));
                            } else {
                                System.out.println("No orders found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "productsList": {
                        try {
                            if (!db.searchProducts().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching products");
                                sendObject(db.searchProducts());
                            } else {
                                System.out.println("No products found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "suppliesTransactions": {
                        try {
                            if (!db.searchSuppliesTransaction().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching supplies transactions");
                                sendObject(db.searchSuppliesTransaction());
                            } else {
                                System.out.println("No supplies transactions found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "salesTransactions": {
                        try {
                            if (!db.searchSalesTransaction().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is searching sales transactions");
                                sendObject(db.searchSalesTransaction());
                            } else {
                                System.out.println("No sales transactions found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "cartItem": {
                        ProductsRowData productsRowData = (ProductsRowData) readObject();
                        try {
                            if (db.addOrderItem(productsRowData, db.getProductId(productsRowData.getCode()), db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId())) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " added order item to cart");
                            } else {
                                System.out.println("Error while adding item to cart");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "cartList": {
                        try {
                            if (!db.searchCartItems().isEmpty()) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " is checking his cart");
                                sendObject(db.searchCartItems());
                            } else {
                                System.out.println("No cart items found");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "clearCart": {
                        try {
                            if (db.clearCart(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId())) {
                                sendMessage("Good");
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " cleared his cart");
                            } else {
                                System.out.println("Error while clearing cart!");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "confirmOrder": {
                        Integer count = 1;
                        Transaction transaction = (Transaction) readObject();
                        ArrayList<TransactionItem> transactionItems = (ArrayList<TransactionItem>) readObject();
                        try {
                            for (int i = 0; i < transactionItems.size(); i++) {
                            }
                            if (db.createTransaction(transaction, db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId())) {
                                for (int i = 0; i < transactionItems.size(); i++) {
                                    if (db.addTransactionItem(transactionItems.get(i), db.getTransactionId(transaction.getDate()), db.getProductIdByName(transactionItems.get(i).getProduct().getProductName()))) {
                                        db.updateProductStock(transactionItems.get(i).getProduct().getProductName(), transactionItems.get(i).getQuantity());
                                        count++;
                                    }
                                }
                                if (count == transactionItems.size() + 1) {
                                    db.clearCart(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId());
                                    sendMessage("Good");
                                    System.out.println("Created order transaction:\n" + "\t 'time': '" + transaction.getDate() + "'\n" + "\t 'user id ': '" + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getId());
                                }
                            } else {
                                System.out.println("Error while creating order transaction!");
                                sendMessage("Bad");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case "shutdown": {
                        m_bRunThread=false;
                        try {
                            if(db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername()!=null) {
                                System.out.println("User " + db.getUserInfo(clientInfoHashMap.get(factSocket.getInetAddress().getHostAddress()).getUser().getUsername()).getUsername() + " disconnected");
                                clientInfoHashMap.remove(factSocket.getInetAddress().getHostAddress());
                                closeEverything(factSocket,inStream,outStream);
                            }else{
                                closeEverything(factSocket,inStream,outStream);
                            }

                        } catch (Exception ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        closeEverything(factSocket,inStream,outStream);
                    }
                    break;

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeEverything(factSocket,inStream,outStream);
        }

    }




    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        try {
            if (outStream != null) {
                outStream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
