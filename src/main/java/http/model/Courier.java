package http.model;

public class Courier {

    private String login;
    private String password;
    private String firstName;
    public Long id;

    public Courier (String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {
    }
}
