package developers.bmsce.mank.com.foodorder.Model;

public class User {

    private String Name;
    private String Password;
    private String phone;
    private String IsStaff;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String name, String password, String phone) {
        Name = name;
        Password = password;
        this.phone = phone;
        IsStaff = "false";
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public User() {

    }
    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = "false";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
