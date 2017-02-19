package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Encja UserTable
 */
@Entity
public class UserTable implements Serializable {

    private static final long serialVersionUID = -2885059097486852100L;
    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Obiekt reprezentuje kolumne nick w bazie danych.
     */
    @Column
    private String nick;

    /**
     * Obiekt reprezentuje kolumne email w bazie danych.
     */
    @Column
    private String email;

    /**
     * Obiekt reprezentuje kolumne password w bazie danych.
     */
    @Column
    private String password;

    // nadanie relacji od strony użytkownika (jeden uzytkownik moze miec wiele wszystkiego)
    /**
     * Okresla relację tabeli ActivityTable z tabelą UserTable
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<ActivityTable> activities;

    /**
     * Okresla relację tabeli CallTable z tabelą UserTable
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<CallTable> calls;

    /**
     * Okresla relację tabeli LocationTable z tabelą UserTable
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<LocationTable> locations;

    /**
     * Okresla relację tabeli ProcessTable z tabelą UserTable
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<ProcessTable> process;

    /**
     * Okresla relację tabeli SmsTable z tabelą UserTable
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<SmsTable> sms;


    // niewidoczne dla Hibernate
    @Transient
    private List<CallTable> callList;

    @Transient
    private List<SmsTable> smsList;

    @Transient
    private List<ActivityTable> activityList;

    @Transient
    private List<ProcessTable> processList;

    @Transient
    private List<LocationTable> locationList;



    public UserTable() {
    }

    public UserTable(String nick, String email, String password) {
        this.nick = nick;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ActivityTable> getActivities() {
        return activities;
    }

    public void setActivities(Set<ActivityTable> activities) {
        this.activities = activities;
    }

    public Set<CallTable> getCalls() {
        return calls;
    }

    public void setCalls(Set<CallTable> calls) {
        this.calls = calls;
    }

    public Set<LocationTable> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationTable> locations) {
        this.locations = locations;
    }

    public Set<ProcessTable> getProcess() {
        return process;
    }

    public void setProcess(Set<ProcessTable> process) {
        this.process = process;
    }

    public Set<SmsTable> getSms() {
        return sms;
    }

    public void setSms(Set<SmsTable> sms) {
        this.sms = sms;
    }


    /**
     * Metoda porządkuje listę według daty
     * @return Uporządkowana lista wg. daty
     */
    public List<CallTable> getCallList() {
        if (this.callList == null) {
            this.callList = new ArrayList<>();
            this.callList.addAll(this.getCalls());
            this.callList.sort(new Comparator<CallTable>() {
                @Override
                public int compare(CallTable o1, CallTable o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        return callList;
    }

    /**
     * Metoda porządkuje listę według daty
     * @return Uporządkowana lista wg. daty
     */
    public List<SmsTable> getSmsList() {
        if (this.smsList == null) {
            this.smsList = new ArrayList<>();
            this.smsList.addAll(this.getSms());
            this.smsList.sort(new Comparator<SmsTable>() {
                @Override
                public int compare(SmsTable o1, SmsTable o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        return smsList;
    }

    /**
     * Metoda porządkuje listę według daty
     * @return Uporządkowana lista wg. daty
     */
    public List<ActivityTable> getActivityList() {
        if (this.activityList == null) {
            this.activityList = new ArrayList<>();
            this.activityList.addAll(this.getActivities());
            this.activityList.sort(new Comparator<ActivityTable>() {
                @Override
                public int compare(ActivityTable o1, ActivityTable o2) {
                    return o1.getDateStart().compareTo(o2.getDateStart());
                }
            });
        }
        return activityList;
    }

    /**
     * Metoda porządkuje listę według daty
     * @return Uporządkowana lista wg. daty
     */
    public List<ProcessTable> getProcessList() {
        if (this.processList == null) {
            this.processList = new ArrayList<>();
            this.processList.addAll(this.getProcess());
            this.processList.sort(new Comparator<ProcessTable>() {
                @Override
                public int compare(ProcessTable o1, ProcessTable o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }
        return processList;
    }


    /**
     * Metoda porządkuje listę według daty
     * @return Uporządkowana lista wg. daty
     */
    public List<LocationTable> getLocationList() {
        if (this.locationList == null) {
            this.locationList = new ArrayList<>();
            this.locationList.addAll(this.getLocations());
            this.locationList.sort(new Comparator<LocationTable>() {
                @Override
                public int compare(LocationTable o1, LocationTable o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        return locationList;
    }
}
