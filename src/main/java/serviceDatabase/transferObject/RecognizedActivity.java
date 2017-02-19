package serviceDatabase.transferObject;

/**
 * Klasa określa strukturę danych dotyczących aktywności użytkownika przesłanych na serwer z aplikacji
 */
public class RecognizedActivity {


    /**
     * Typ aktywności
     */
    public String activityType;
    /**
     *  Czas rozpoczęcia aktywności
     */
    public long dataStart;
    /**
     * Całkowity czas trwania aktywności
     */
    public long totalTime;

    public RecognizedActivity() {
    }

    public RecognizedActivity(String activityType, long dataStart, long totalTime) {
        this.activityType = activityType;
        this.dataStart = dataStart;
        this.totalTime = totalTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityyType(String activityType) {
        this.activityType = activityType;
    }

    public long getDataStart() {
        return dataStart;
    }

    public void setDataStart(long dataStart) {
        this.dataStart = dataStart;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
