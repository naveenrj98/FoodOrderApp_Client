package developers.bmsce.mank.com.foodorder.Model;

public class Token {

    private String token;
    private boolean isServerToken;

    public Token() {
    }

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", isServerToken=" + isServerToken +
                '}';
    }
}
