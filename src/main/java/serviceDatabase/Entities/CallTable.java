package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Encja CallTable
 */
@Entity
public class CallTable implements Serializable {

    private static final long serialVersionUID = 7337068139899961053L;

    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Obiekt reprezentuje kolumne  callType w bazie danych.
     */
    @Column
    String callType;

    /**
     * Obiekt reprezentuje kolumne duration w bazie danych.
     */
    @Column
    int duration;

    /**
     * Obiekt reprezentuje kolumne callerName w bazie danych.
     */
    @Column
    String callerName;

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

    public CallTable() {
    }

    public CallTable(String callType, int duration, String callerName, Date date) {
        this.callType = callType;
        this.duration = duration;
        this.callerName = callerName;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getcallerName() {
        return callerName;
    }

    public void setNamePerson(String callerName) {
        this.callerName = callerName;
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
