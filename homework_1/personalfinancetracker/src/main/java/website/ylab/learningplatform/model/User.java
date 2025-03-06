package website.ylab.learningplatform.model;

public class User {
    private static long counter = 0;
    private final long id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(String name, String email, String password) {
        this.id = ++counter;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    // getters / setters / toString()

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Object getPassword() {
        return password;
    }
}
