package yurii.karpliuk.warehouseAccounting.Model;

public class ClientInfo{
    private String clientIpPort;
    private User user;

    public void setClientIpPort(String clientIpPort) {
        this.clientIpPort = clientIpPort;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClientIpPort() {
        return clientIpPort;
    }

}
