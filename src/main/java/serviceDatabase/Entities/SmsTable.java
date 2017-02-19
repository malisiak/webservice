package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Encja SmsTable
 */
@Entity
public class SmsTable implements Serializable {
    private static final long serialVersionUID = -2351884461919770330L;

    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * Obiekt reprezentuje kolumne smsType w bazie danych.
     */
    @Column
    String smsType;

    /**
     * Obiekt reprezentuje kolumne personName w bazie danych.
     */
    @Column
    String personName;

    /**
     * Obiekt reprezentuje kolumne date w bazie danych.
     */
    @Column
    Date date;

    /**
     * Okresla relację tabeli z tabelą UserTable
     */
    @ManyToOne
    private UserTable user;

    public SmsTable() {

    }

    public SmsTable(String smsType, String personName, Date date) {
        this.smsType = smsType;
        this.personName = personName;
        this.date = date;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }
}
