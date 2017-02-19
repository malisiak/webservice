package serviceDatabase.transferObject;

/**
 * Klasa określa strukturę danych dotyczących użytkownika przesłanych na serwer z aplikacji
 */
public class User {

    /**
     * Imię użytkownika
     */
    private String name;
    /**
     * Adres e-mail użytkownika
     */
    private String email;
    /**
     * Hasło użytkownika
     */
    private String password;

    public User(String email) {
        this.email = email;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
