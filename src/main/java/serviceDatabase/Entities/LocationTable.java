package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Encja CallTable
 */
@Entity
public class LocationTable implements Serializable {
    private static final long serialVersionUID = -494209820470795053L;

    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * Obiekt reprezentuje kolumne latitude w bazie danych.
     */
    @Column
    double latitude;

    /**
     * Obiekt reprezentuje kolumne longitude w bazie danych.
     */
    @Column
    double longitude;

    /**
     * Obiekt reprezentuje kolumne  date w bazie danych.
     */
    @Column
    Date date;

    /**
     * Obiekt reprezentuje kolumne  provider w bazie danych.
     */
    @Column
    String provider;

    /**
     * Obiekt reprezentuje kolumne speed w bazie danych.
     */
    float speed;

    /**
     * Okresla relację tabeli z tabelą UserTable
     */
    @ManyToOne
    private UserTable user;

    public LocationTable() {
    }

    public LocationTable(double latitude, double longitude, Date date, String provider, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.provider = provider;
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }


}
