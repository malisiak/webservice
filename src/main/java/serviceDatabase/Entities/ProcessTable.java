package serviceDatabase.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Encja processTable
 */
@Entity
public class ProcessTable implements Serializable{
    private static final long serialVersionUID = -8949455404661486591L;

    /**
     * Obiekt reprezentuje kolumne Id w bazie danych. Adnotacja @GeneratedValue automatycznie nadaje id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * Obiekt reprezentuje kolumne processName w bazie danych.
     */
    @Column
    String processName;

    /**
     * Obiekt reprezentuje kolumne TimeInForeground w bazie danych.
     */
    @Column
    int TimeInForeground;

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

    /**
     * Pole niewidoczne dla Hibernate. Wykorzystywane przy tworzeniu etykiety dnia.
     */
    @Transient
    private String timeOfADay;

    public ProcessTable() {
    }

    public ProcessTable(String processName, int timeInForeground, Date date) {
        this.processName = processName;
        TimeInForeground = timeInForeground;
        this.date = date;
    }
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getTimeInForeground() {
        return TimeInForeground;
    }

    public void setTimeInForeground(int timeInForeground) {
        TimeInForeground = timeInForeground;
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


    /**
     * Metoda przydziela etykietę pory dnia.
     * @return Etykieta dnia.
     */
    public String getTimeOfADay() {
        if (this.timeOfADay == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);

            if (hour >=8 && hour <9) {
                this.timeOfADay = "8/9";
            }
            else if (hour >=9 && hour <10) {
                this.timeOfADay = "9/10";
            }
            else if (hour >=10 && hour <11) {
                this.timeOfADay = "10/11";
            }
            else if (hour >=11 && hour <12) {
                this.timeOfADay = "11/12";
            }
            else if (hour >=12 && hour <13) {
                this.timeOfADay = "12/13";
            }
            else if (hour >=13 && hour <14) {
                this.timeOfADay = "13/14";
            }
            else if (hour >=14 && hour <15) {
                this.timeOfADay = "14/15";
            }
            else if (hour >=15 && hour <16) {
                this.timeOfADay = "15/16";
            }
            else if (hour >=16 && hour <17) {
                this.timeOfADay = "16/17";
            }
            else if (hour >=17 && hour <18) {
                this.timeOfADay = "17/18";
            }
            else if (hour >=18 && hour <19) {
                this.timeOfADay = "18/19";
            }
            else if (hour >=19 && hour <20) {
                this.timeOfADay = "19/20";
            }
            else if (hour >=20 && hour <21) {
                this.timeOfADay = "20/21";
            }
            else if (hour >=21 && hour <22) {
                this.timeOfADay = "21/22";
            }
            else if (hour >=22 && hour <23) {
                this.timeOfADay = "22/23";
            }
            else if (hour >=23 && hour <24) {
                this.timeOfADay = "23/24";
            }
            else if (hour >=0 && hour <3) {
                this.timeOfADay = "0/3";
            }
            else if (hour >=3 && hour <6) {
                this.timeOfADay = "3/6";
            }
            else if (hour >=6 && hour <8) {
                this.timeOfADay = "6/8";
            }

        }
        return timeOfADay;
    }
}
