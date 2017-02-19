package serviceWebsite.beans;

import serviceDatabase.Entities.ActivityTable;
import serviceWebsite.enums.ActivityTypeEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Klasa posiada metody umożliwiające generowanie wykresów dot. aktywności użytkownika
 */
@ManagedBean(name = "activityChart")
public class ActivityChart implements Serializable {

    /**
     * Instancja bean
     */
    @ManagedProperty(value = "#{editor}")
    MainBean editor;
    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel generalDurationModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private PieChartModel averageDurationModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel dayDurationModel;


    /**
     * Czas, który użytkownik spędził 'na nogach' podany w milisekundach.
     */
    private long onFootTime = 0L;

    /**
     * Czas, który użytkownik spędził 'chodząc' podany w milisekundach.
     */
    private long walkingTime = 0L;

    /**
     * Czas, który użytkownik spędził 'w pojeździe' podany w milisekundach.
     */
    private long inVehicleTime = 0L;

    /**
     * Czas, który użytkownik spędził 'biegając' podany w milisekundach.
     */
    private long runningTime = 0L;

    /**
     * Czas, który użytkownik spędził 'w bezruchu' podany w milisekundach.
     */
    private long stillTime = 0L;

    /**
     * Czas trwania innych czynności podany w milisekundach.
     */
    private long otherTime = 0L;

    /**
     * Obiekt reprezentujący date w określonym formacie.
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    /**
     * Wywołanie sekwencji metod generujących wykresy.
     */
    @PostConstruct
    public void init() {
        createActivityCharts();
    }

    /**
     * Metoda wywołuje funckje odpowiedzialne za stworzenie odpowiednych wykresów.
     */
    private void createActivityCharts() {
        createGeneralDurationModel();
        createAverageDurationModel();
        createDayDurationModel();
    }

    /**
     * Metoda tworzy model danych, który okresla ogólny czas trwania poszczególnych aktywności.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initGeneralDurationModel() {
        BarChartModel model = new BarChartModel();

        // pobierz czas danych aktywności
        Set<ActivityTable> result = editor.getUser().getActivities();
        if (result != null) {
            for (ActivityTable a : result) {
                if (a.getType().equals(ActivityTypeEnum.OnFoot.name())) {
                    onFootTime = onFootTime + a.getTotalTime();
                } else if (a.getType().equals(ActivityTypeEnum.InVehicle.name())) {
                    inVehicleTime = inVehicleTime + a.getTotalTime();
                } else if (a.getType().equals(ActivityTypeEnum.Walking.name())) {
                    walkingTime = walkingTime + a.getTotalTime();
                } else if (a.getType().equals(ActivityTypeEnum.Running.name())) {
                    runningTime = runningTime + a.getTotalTime();
                } else if (a.getType().equals(ActivityTypeEnum.Still.name())) {
                    stillTime = stillTime + a.getTotalTime();
                } else if (a.getType().equals(ActivityTypeEnum.Unknown.name()) || a.getType().equals(ActivityTypeEnum.Tilting.name())) {
                    otherTime = otherTime + a.getTotalTime();
                }

            }
        }

        // zbuduj słupek dot. 'bycia na nogach'
        ChartSeries onFoot = new ChartSeries();
        onFoot.setLabel(" OnFoot");
        long time = onFootTime / 60;
        System.out.println(" czas przed dzieleniem: " + onFootTime + "po podzieleniu " + onFootTime / 60 + " ze zmiennej " + time);
        onFoot.set("onfoot", time);

        // zbuduj słupek dot. 'chodzenia'
        ChartSeries walking = new ChartSeries();
        walking.setLabel("Chodzenie");
        walking.set(" chodzenie", walkingTime / 60);

        // zbuduj słupek dot. 'biegania'
        ChartSeries running = new ChartSeries();
        running.setLabel(" bieganie");
        running.set(" bieganie", runningTime / 60);

        // zbuduj słupek dot. 'bezruchu'
        ChartSeries still = new ChartSeries();
        still.setLabel("Bezruch");
        still.set(" bezruch", stillTime / 60);

        // zbuduj słupek dot. 'bycia w pojezdzie'
        ChartSeries inVehicle = new ChartSeries();
        inVehicle.setLabel(" w Pojezdzie");
        inVehicle.set("w Pojezdzie", inVehicleTime / 60);

        // zbuduj słupek dot. inncyh aktywności - nieznanych i trzęsienia komórką
        ChartSeries other = new ChartSeries();
        other.setLabel(" Inne");
        other.set("inne", otherTime / 60);


        // dodaj do modelu
        model.addSeries(onFoot);
        model.addSeries(walking);
        model.addSeries(running);
        model.addSeries(still);
        model.addSeries(inVehicle);
        model.addSeries(other);

        return model;

    }


    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createGeneralDurationModel() {

        // generuj model
        generalDurationModel = initGeneralDurationModel();

        // ustaw własciwosci wykresu
        generalDurationModel.setTitle(" Zestawienie wykonywanych aktywności ogólnie");
        generalDurationModel.setLegendPosition("se");
        generalDurationModel.setBarWidth(100);
        generalDurationModel.setBarPadding(30);

        // os x - wlasciwosci
        Axis x = generalDurationModel.getAxis(AxisType.X);
        x.setLabel("Rodzaj aktywności");
        //os y - wlasciwosci
        Axis y = generalDurationModel.getAxis(AxisType.Y);
        y.setLabel("Czas trwania aktywności [min]");
        y.setMin(0);
    }

    /**
     * Metoda tworzy wykres kołowy, który określa średni czas trwania poszczególnych aktywności w ciągu dnia.
     *
     */
    private void createAverageDurationModel() {
        int days;
        long firstDate = getEditor().getUser().getActivityList().get(0).getDateStart().getTime();
        long lastDate = getEditor().getUser().getActivityList().get(getEditor().getUser().getActivityList().size() - 1).getDateStart().getTime();

        days = 1 + (int) (lastDate - firstDate) / 1000 / 60 / 60 / 24;

        averageDurationModel = new PieChartModel();
        averageDurationModel.set("onFoot", onFootTime / 60 / days);
        System.out.println("pie " + onFootTime / 60 / days);
        averageDurationModel.set("chodzenie", walkingTime / 60 / days);
        averageDurationModel.set("bieganie", runningTime / 60 / days);
        averageDurationModel.set("w pojezdzie", inVehicleTime / 60 / days);
        averageDurationModel.set(" bezruch", stillTime / 60 / days);
        averageDurationModel.set("inne", otherTime / 60 / days);
        averageDurationModel.setTitle("Przeciętna ilosc czasu spędzana na poszczególnych aktywnościach w ciągujednego dnia");
        averageDurationModel.setLegendPosition("e");
        //averageDurationModel.setDiameter(150);
    }

    /**
     * Metoda tworzy model danych, który okresla czas trwania poszczególnych aktywności w ciągu dnia w określonym przedziale czau.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initDayDurationModel() {
        BarChartModel model = new BarChartModel();
        Long timeOnFoot = 0L;
        Long timeInVehicle = 0L;
        Long timeWalking = 0L;
        Long timeRunning = 0L;
        Long timeStill = 0L;
        Long timeUnknown = 0L;

        ChartSeries onFoot = new ChartSeries();
        onFoot.setLabel(" OnFoot");


        ChartSeries walking = new ChartSeries();
        walking.setLabel("Chodzenie");

        ChartSeries running = new ChartSeries();
        running.setLabel(" bieganie");

        ChartSeries still = new ChartSeries();
        still.setLabel("Bezruch");

        ChartSeries inVehicle = new ChartSeries();
        inVehicle.setLabel(" w Pojezdzie");


        ChartSeries other = new ChartSeries();
        other.setLabel(" Inne");

        Date date = null;
        //pobierz czas aktywnosci w poszczególne dni
        for (ActivityTable record : getEditor().getUser().getActivityList()) {
            if (date == null) {
                date = record.getDateStart();
                if (record.getType().equals(ActivityTypeEnum.OnFoot.name())) {
                    timeOnFoot = timeOnFoot + record.getTotalTime();
                } else if (record.getType().equals(ActivityTypeEnum.InVehicle.name())) {
                    timeInVehicle = timeInVehicle + record.getTotalTime();
                } else if (record.getType().equals(ActivityTypeEnum.Walking.name())) {
                    timeWalking = timeWalking + record.getTotalTime();
                } else if (record.getType().equals(ActivityTypeEnum.Running.name())) {
                    timeRunning = timeRunning + record.getTotalTime();
                } else if (record.getType().equals(ActivityTypeEnum.Still.name())) {
                    timeStill = timeStill + record.getTotalTime();
                } else if (record.getType().equals(ActivityTypeEnum.Unknown.name()) || record.getType().equals(ActivityTypeEnum.Tilting.name())) {
                    timeUnknown = timeUnknown + record.getTotalTime();
                }
            } else {
                if (DateUtils.isSameDay(date, record.getDateStart())) {
                    if (record.getType().equals(ActivityTypeEnum.OnFoot.name())) {
                        timeOnFoot = timeOnFoot + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.InVehicle.name())) {
                        timeInVehicle = timeInVehicle + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Walking.name())) {
                        timeWalking = timeWalking + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Running.name())) {
                        timeRunning = timeRunning + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Still.name())) {
                        timeStill = timeStill + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Unknown.name()) || record.getType().equals(ActivityTypeEnum.Tilting.name())) {
                        timeUnknown = timeUnknown + record.getTotalTime();
                    }

                } else {
                    onFoot.set(dateFormat.format(date), timeOnFoot / 60);
                    walking.set(dateFormat.format(date), timeWalking / 60);
                    running.set(dateFormat.format(date), timeRunning / 60);
                    inVehicle.set(dateFormat.format(date), timeInVehicle / 60);
                    still.set(dateFormat.format(date), timeStill / 60);
                    other.set(dateFormat.format(date), timeUnknown / 60);
                    date = record.getDateStart();
                    timeOnFoot = 0L;
                    timeInVehicle = 0L;
                    timeWalking = 0L;
                    timeRunning = 0L;
                    timeStill = 0L;
                    timeUnknown = 0L;
                    if (record.getType().equals(ActivityTypeEnum.OnFoot.name())) {
                        timeOnFoot = timeOnFoot + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.InVehicle.name())) {
                        timeInVehicle = timeInVehicle + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Walking.name())) {
                        timeWalking = timeWalking + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Running.name())) {
                        timeRunning = timeRunning + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Still.name())) {
                        timeStill = timeStill + record.getTotalTime();
                    } else if (record.getType().equals(ActivityTypeEnum.Unknown.name()) || record.getType().equals(ActivityTypeEnum.Tilting.name())) {
                        timeUnknown = timeUnknown + record.getTotalTime();
                    }
                }
            }
        }
        onFoot.set(dateFormat.format(date), timeOnFoot / 60);
        walking.set(dateFormat.format(date), timeWalking / 60);
        running.set(dateFormat.format(date), timeRunning / 60);
        inVehicle.set(dateFormat.format(date), timeInVehicle / 60);
        still.set(dateFormat.format(date), timeStill / 60);
        other.set(dateFormat.format(date), timeUnknown / 60);


        // dodaj do modelu
        model.addSeries(onFoot);
        model.addSeries(walking);
        model.addSeries(running);
        model.addSeries(inVehicle);
        model.addSeries(still);
        model.addSeries(other);
        return model;

    }

    /**
     *
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initDayDurationModel(). W funckji określane są własności osi oraz legendy.
     *
     */
    private void createDayDurationModel() {
        // generuj model
        dayDurationModel = initDayDurationModel();

        // ustaw wlasciwosci wykresu
        dayDurationModel.setTitle("Czas wykonywania poszczególnych czynnosci w ciągu dnia");
        dayDurationModel.setLegendPosition("se");
        dayDurationModel.setBarWidth(10);
        dayDurationModel.setBarPadding(2);

        // os x - wlasciwosci
        Axis x = dayDurationModel.getAxis(AxisType.X);
        x.setLabel("Data");

        // os y - wlasciwosci
        Axis y = dayDurationModel.getAxis(AxisType.Y);
        y.setLabel("Czas wykonywania czynnosci [min]");
        y.setMin(0);
    }


    //gettery, settery

    public MainBean getEditor() {
        return editor;
    }

    public void setEditor(MainBean editor) {
        this.editor = editor;
    }

    public BarChartModel getGeneralDurationModel() {
        return generalDurationModel;
    }

    public void setGeneralDurationModel(BarChartModel generalDurationModel) {
        this.generalDurationModel = generalDurationModel;
    }

    public PieChartModel getAverageDurationModel() {
        return averageDurationModel;
    }

    public void setAverageDurationModel(PieChartModel averageDurationModel) {
        this.averageDurationModel = averageDurationModel;
    }

    public BarChartModel getDayDurationModel() {
        return dayDurationModel;
    }

    public void setDayDurationModel(BarChartModel dayDurationModel) {
        this.dayDurationModel = dayDurationModel;
    }

}
