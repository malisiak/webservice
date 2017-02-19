package serviceDatabase.transferObject;

/**
 * Klasa okresla strukturę danych dotyczacych połączeń przesłanych na serwer z aplikacji
 */
public class CallLog {

    /**
     * Typ połączenia
     */
    private String callType;
    /**
     * czas trwania rozmowy
     */
    private int duration;
    /**
     * Imię rozmówcy
     */
    private String callerName;
    /**
     * Data
     */
    private long date;

    public CallLog() {
    }

    public CallLog(String callType, int duration, String callerName, long date) {
        this.callType = callType;
        this.duration = duration;
        this.callerName = callerName;
        this.date = date;
    }

    public String getCallType(){
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

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
