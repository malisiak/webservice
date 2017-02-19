package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Encja ActivityTable
 */
@Entity
public class ActivityTable implements Serializable {


    private static final long serialVersionUID = 1594417835708163292L;
    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Obiekt reprezentuje kolumne Type w bazie danych.
     */
    @Column
    private String type;

    /**
     * Obiekt reprezentuje kolumne  dateStart w bazie danych.
     */
    @Column
    private Date dateStart;

    /**
     * Obiekt reprezentuje kolumne totalTime w bazie danych.
     */
    @Column
    private long totalTime;

    /**
     * Okresla relację tabeli z tabelą UserTable
     */
    @ManyToOne
    private UserTable user;


    public ActivityTable(String type, Date dateStart, long totalTime) {
        this.type = type;
        this.totalTime = totalTime;
        this.dateStart = dateStart;
    }

    public ActivityTable() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }


}