package edu.covenant.safety.scotsgaurd.Model;

public class User {

    public static final int STUDENT = 0;
    public static final int STUDENT_WORKER = 1;
    public static final int ADMIN = 2;

    private String id;
    private String username;
    private String bannerID;
    private String password;

    private int accountType;

    public User(String username, String password, String bannerID, int accountType) {
        this.username = username;
        this.bannerID = bannerID;
        this.password = password;
        this.accountType = accountType;
    }

    public User() {

    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBannerID() {
        return bannerID;
    }

    public void setBannerID(String bannerID) {
        this.bannerID = bannerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
