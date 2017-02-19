package serviceWebsite.beans;

import serviceDatabase.service.DBUtil;
import serviceDatabase.Entities.UserTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 * Bean zarządzający przepływem danych na stronie internetowej
 */
@ManagedBean(name = "editor")
@SessionScoped
public class MainBean {

    /**
     * e-mail użytkownika
     */
    private String email;
    /**
     * hasło uzytkownika
     */
    private String password;
    /**
     * Obiekt reprezentujący dane na temat użytkownika
     */
    private UserTable user;

    /**
     * Zawartość strony internetowej
     */
    private String websiteContent = "callChart.xhtml";
    /**
     * Wartość określająca czy wyświetlić komunikat błędu czy  nie
     */
    private boolean showFailure = false;
    /**
     * Pole nagłówka
     */
    private String text = "Analiza danych kontekstowych";

    /**
     * Metoda umozliwiająca zalogowanie się do strony internetowej.
     * Sprawdza czy podane hasło i email istnieje w bazie danych.
     * @return Zwraca adres strony; profil uzytkownika, jeżeli hasło i adres e-mail istnieją w bazie danych; komunikat błędu w przypadku braku figurowania użytkownika w bazie.
     */
    @PostConstruct
    public String checkLogin() {
        StringBuilder hql = new StringBuilder();
        hql.append(" from UserTable u ");
        hql.append(" where ");
        hql.append(" u.email = :email ");
        hql.append(" and u.password = :password ");
        SessionFactory sessionFactory = DBUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql.toString());
        query.setParameter("email", this.email);
        query.setParameter("password", this.password);
        user = (UserTable) query.uniqueResult();
        session.close();
        if (!(user == null)) {
           // user = result;
            return "successfulllogin";
        } else {
            this.showFailure = true;
            return "index";
        }
    }

    /**
     * Metoda umożliwia wylogowanie się użytkownika ze swojego profilu
     * @return Zwraca adres strony, gdzie znajduje sie panel logowania
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
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

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }

    public String getWebsiteContent() {
        return websiteContent;
    }

    public void setWebsiteContent(String websiteContent) {
        this.websiteContent = websiteContent;
    }

    public boolean isShowFailure() {
        return showFailure;
    }

    public void setShowFailure(boolean showFailure) {
        this.showFailure = showFailure;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}