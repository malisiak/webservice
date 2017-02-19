package serviceDatabase.service;

import serviceDatabase.transferObject.*;

import java.util.List;

/**
 *  Klasa reprezentująca dane, które zostały pobrane przez telefon komórkowy i wysyłane przez apliakcje.
 */
public class Wrapper {

    /**
     * Lista reprezentująca dane dot. aktywności
     */
    private List<RecognizedActivity> activities;

    /**
     * Lista reprezentująca dane dot. połączeń telefonicznych
     */
    private List <CallLog> calls;

    /**
     * Lista reprezentująca dane dot. wiadomości SMS
     */
    private List <SmsLog> sms;

    /**
     * Lista reprezentująca dane dot. procesów
     */
    private List <ProcessLog> process;

    /**
     * Lista reprezentująca dane dot. lokalizacji użytkownika
     */
    private List <LocationLog> location;

    /**
     * Obiekt reprezentujący dane dot. użytkownika
     */
    private User user;



// gettery i settery
// aktywności
    public List<RecognizedActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<RecognizedActivity> activities) {
        this.activities = activities;
    }

    // połączenie telefoniczne
    public List<CallLog> getCalls()
    {
        return calls;
    }

    public void setCalls(List<CallLog> calls) {
        this.calls = calls;
    }

    // wiadomości sms
    public List<SmsLog> getSms() {
        return sms;
    }

    public void setSms(List<SmsLog> sms) {
        this.sms = sms;
    }

    // processy
    public List<ProcessLog> getProcess() {
        return process;
    }

    public void setProcess(List<ProcessLog> process) {
        this.process = process;
    }

    // lokalizacja
    public List<LocationLog> getLocation() {
        return location;
    }

    public void setLocation(List<LocationLog> location) {
        this.location = location;
    }

    // użytkownik
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
