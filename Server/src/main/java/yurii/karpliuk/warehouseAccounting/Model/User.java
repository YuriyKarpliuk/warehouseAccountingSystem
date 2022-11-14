package yurii.karpliuk.warehouseAccounting.Model;


import java.io.Serializable;


public class User implements Serializable {
    private Integer id;
    private  String username;
    private  String firstName;
    private  String lastName;
    private  String email;
    private String password;
    private String uRole;

    private Boolean isActive;

    public User(String username, String firstName, String lastName, String email, String password, String uRole) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.uRole = uRole;
    }
    public User(){

    }
    public User(Integer id,String firstName,String lastName,String username,String email){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.email=email;
    }


    public User(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getuRole() {
        return uRole;
    }

    public void setuRole(String uRole) {
        this.uRole = uRole;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
