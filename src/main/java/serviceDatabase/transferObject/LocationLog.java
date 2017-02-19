package serviceDatabase.transferObject;

/**
 * Klasa określa strukturę danych dotyczących lokalizacji użytkownika przesłanych na serwer z aplikacji
 */
public class LocationLog {

    /**
     * Szerokość geograficzna
     */
    double latitude;
    /**
     * Długość geograficzna
     */
    double longitude;
    /**
     * Data
     */
    long date;
    /**
     * Dostawca
     */
    String provider;
    /**
     * Prędkość
     */
    float speed;

    public LocationLog() {
    }

    public LocationLog(double latitude, double longitude, long date, String provider, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.provider = provider;
        this.speed = speed;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
