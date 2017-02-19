package serviceDatabase.transferObject;
/**
 * Klasa określa strukturę danych dotyczących wiadomości SMS użytkownika przesłanych na serwer z aplikacji
 */
public class SmsLog {

    /**
     * Typ wiadomości SMS
     */
    private String smsType;
    /**
     * Imię osoby, od/do której została wysłana wiadomość
     */
    private String personName;
    /**
     * Data wysłania/odebrania wiadomości
     */
    private long date;

    public SmsLog() {

    }

    public SmsLog(String smsType, String personName, long date) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


}
