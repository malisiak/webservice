package serviceDatabase.transferObject;

/**
 * Klasa określa strukturę danych dotyczących aplikacji uzywanych przez użytkownika przesłanych na serwer z aplikacji
 */
public class ProcessLog {

    /**
     * Nazwa procesu aktywności
     */
    private String procesName;
    /**
     * Czas używania aktywności
     */
    private int timeInForeground;
    /**
     * Data używania aktywności
     */
    private long date;

    public ProcessLog() {
    }

    public ProcessLog(String procesName, int timeInForeground, long date) {
        this.procesName = procesName;
        this.timeInForeground = timeInForeground;
        this.date = date;
    }

    public String getProcesName() {
        return procesName;
    }

    public void setProcesName(String procesName) {
        this.procesName = procesName;
    }

    public int getTimeInForeground() {
        return timeInForeground;
    }

    public void setTimeInForeground(int timeInForeground) {
        this.timeInForeground = timeInForeground;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
