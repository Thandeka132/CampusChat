package User;

public class User {
    private final String userName;
    private String status;

    public User(String userName, String status) {
        this.userName = userName;
        this.status = "Online";
    }

    public String getUserName() {
        return userName;
    }
}
