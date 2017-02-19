package serviceWebsite.beans;

import serviceDatabase.Entities.ProcessTable;
import serviceWebsite.enums.ProcessTypeEnum;
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
 * Klasa posiada metody umożliwiające generowanie wykresów dot. używanych przez użytkownika aplikacji
 */
@ManagedBean(name = "processChart")
public class ProcessChart implements Serializable {

    /**
     * Instancja bean
     */
    @ManagedProperty(value = "#{editor}")
    MainBean editor;
    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel generalTimeModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private PieChartModel averageTimeModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel dayTimeModel;

    /**
     * Czas używania aplikacji Facebook podany w milisekundach.
     */
    private long facebookTime = 0L;

    /**
     * Czas używania aplikacji Messenger podany w milisekundach.
     */
    private long messengerTime = 0L;

    /**
     * Czas używania aplikacji kamera podany w milisekundach.
     */
    private long cameraTime = 0L;

    /**
     * Czas używania aplikacji odtwarzacz muzyczny podany w milisekundach.
     */
    private long musicTime = 0L;

    /**
     * Czas używania aplikacji przeglądarka internetowa podany w milisekundach.
     */
    private long websiteTime = 0L;

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
     * Metoda tworzy model danych, który określa ogólny czas używania poszczególnych aplikacji.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initGeneralDurationModel() {
        BarChartModel model = new BarChartModel();

        Set<ProcessTable> result = editor.getUser().getProcess();
        if (result != null) {
            for (ProcessTable p : result) {
                if (p.getProcessName().equals(ProcessTypeEnum.Facebook.name())) {
                    facebookTime = facebookTime + p.getTimeInForeground();
                } else if (p.getProcessName().equals(ProcessTypeEnum.Messenger.name())) {
                    messengerTime = messengerTime + p.getTimeInForeground();
                } else if (p.getProcessName().equals(ProcessTypeEnum.Camera.name())) {
                    cameraTime = cameraTime + p.getTimeInForeground();
                } else if (p.getProcessName().equals(ProcessTypeEnum.MusicPlayer.name())) {
                    musicTime = musicTime + p.getTimeInForeground();
                } else if (p.getProcessName().equals(ProcessTypeEnum.Website.name())) {
                    websiteTime = websiteTime + p.getTimeInForeground();
                }

            }

            ChartSeries facebook = new ChartSeries();
            facebook.setLabel("facebook");
            facebook.set("facebook", facebookTime / 60);


            ChartSeries messenger = new ChartSeries();
            messenger.setLabel("massenger");
            messenger.set("messenger", messengerTime / 60);

            ChartSeries camera = new ChartSeries();
            camera.setLabel("aparat");
            camera.set("aparat", cameraTime / 60);

            ChartSeries website = new ChartSeries();
            website.setLabel("Przeglądarka");
            website.set("przeglądarka", websiteTime / 60);

            ChartSeries musicPlayer = new ChartSeries();
            musicPlayer.setLabel("odtwarzacz muzyczny");
            musicPlayer.set("odtwarzacz muzyczny", musicTime / 60);


            // dodaj do modelu
            model.addSeries(facebook);
            model.addSeries(messenger);
            model.addSeries(website);
            model.addSeries(camera);
            model.addSeries(musicPlayer);

        }
        return model;
    }

    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createGeneralDurationModel() {

        // generuj model
        generalTimeModel = initGeneralDurationModel();

        // ustaw własciwosci wykresu
        generalTimeModel.setTitle("Zestawienie czasu korzystanie z innych aplikacji");
        generalTimeModel.setLegendPosition("se");
        generalTimeModel.setBarWidth(100);
        generalTimeModel.setBarPadding(5);

        // os x - wlasciwosci
        Axis x = generalTimeModel.getAxis(AxisType.X);
        x.setLabel("Rodzaj aplikacji");
        //os y - wlasciwosci
        Axis y = generalTimeModel.getAxis(AxisType.Y);
        y.setLabel("Czas korzystania z aplikacji [min]");
        y.setMin(0);
    }

    /**
     * Metoda tworzy wykres kołowy, który określa średni czas używania poszczególnych aplikacji w ciągu dnia.
     */
    private void createAverageDurationModel() {
        int days;
        long firstDate = getEditor().getUser().getProcessList().get(0).getDate().getTime();
        long lastDate = getEditor().getUser().getActivityList().get(getEditor().getUser().getProcessList().size() - 1).getDateStart().getTime();

        days = 1 + (int) (lastDate - firstDate) / 1000 / 60 / 60 / 24;

        averageTimeModel = new PieChartModel();
        averageTimeModel.set("Facebook", facebookTime / 60 / days);
        averageTimeModel.set("Przeglądarka", websiteTime / 60 / days);
        averageTimeModel.set("Kamera", cameraTime / 60 / days);
        averageTimeModel.set("Odtwarzacz muzyczny", musicTime / 60 / days);
        averageTimeModel.setTitle("Przeciętna ilosc czasu spędzana na używaniu danej aplkacji");
        averageTimeModel.setLegendPosition("e");
    }

    /**
     * Metoda tworzy model danych, który okresśla czas używania poszczególnych aplikacji w ciagu danego dnia w przedziale czasu
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initDayDurationModel() {
        BarChartModel model = new BarChartModel();
        Long timeFacebook = 0L;
        Long timeMessenger = 0L;
        Long timeCamera = 0L;
        Long timeWebsite = 0L;
        Long timeMusic = 0L;


        // zbuduj słupek dot. połączeń przychodzących
        ChartSeries facebook = new ChartSeries();
        facebook.setLabel("Facebook");

        // zbuduj słupek dot. połączeń wychodzących
        ChartSeries messenger = new ChartSeries();
        messenger.setLabel("Messenger");

        // zbuduj słupek dot. połączeń przychodzących
        ChartSeries camera = new ChartSeries();
        camera.setLabel("Aparat");

        // zbuduj słupek dot. połączeń wychodzących
        ChartSeries website = new ChartSeries();
        website.setLabel("Przeglądarka");

        // zbuduj słupek dot. połączeń przychodzących
        ChartSeries musicPlayer = new ChartSeries();
        musicPlayer.setLabel("Odtwarzacz muzyki");


        Date date = null;
        //pobierz czas połączeń na poszczególne dni
        for (ProcessTable record : getEditor().getUser().getProcessList()) {
            if (date == null) {
                date = record.getDate();
                if (record.getProcessName().equals(ProcessTypeEnum.Facebook.name())) {
                    timeFacebook = timeFacebook + record.getTimeInForeground();
                } else if (record.getProcessName().equals(ProcessTypeEnum.Messenger.name())) {
                    messengerTime = messengerTime + record.getTimeInForeground();
                } else if (record.getProcessName().equals(ProcessTypeEnum.Website.name())) {
                    websiteTime = websiteTime + record.getTimeInForeground();
                } else if (record.getProcessName().equals(ProcessTypeEnum.Camera.name())) {
                    cameraTime = cameraTime + record.getTimeInForeground();
                } else if (record.getProcessName().equals(ProcessTypeEnum.MusicPlayer.name())) {
                    musicTime = musicTime + record.getTimeInForeground();
                }

            } else {
                if (DateUtils.isSameDay(date, record.getDate())) {
                    if (record.getProcessName().equals(ProcessTypeEnum.Facebook.name())) {
                        timeFacebook = timeFacebook + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Messenger.name())) {
                        messengerTime = messengerTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Website.name())) {
                        websiteTime = websiteTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Camera.name())) {
                        cameraTime = cameraTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.MusicPlayer.name())) {
                        musicTime = musicTime + record.getTimeInForeground();
                    }

                } else {
                    facebook.set(dateFormat.format(date), timeFacebook / 60);
                    messenger.set(dateFormat.format(date), timeMessenger / 60);
                    website.set(dateFormat.format(date), timeWebsite / 60);
                    camera.set(dateFormat.format(date), timeCamera / 60);
                    musicPlayer.set(dateFormat.format(date), timeMusic / 60);
                    date = record.getDate();
                    timeFacebook = 0L;
                    timeMessenger = 0L;
                    timeWebsite = 0L;
                    timeCamera = 0L;
                    timeMusic = 0L;

                    if (record.getProcessName().equals(ProcessTypeEnum.Facebook.name())) {
                        timeFacebook = timeFacebook + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Messenger.name())) {
                        messengerTime = messengerTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Website.name())) {
                        websiteTime = websiteTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.Camera.name())) {
                        cameraTime = cameraTime + record.getTimeInForeground();
                    } else if (record.getProcessName().equals(ProcessTypeEnum.MusicPlayer.name())) {
                        musicTime = musicTime + record.getTimeInForeground();
                    }
                }
            }

        }
        facebook.set(dateFormat.format(date), timeFacebook / 60);
        messenger.set(dateFormat.format(date), timeMessenger / 60);
        website.set(dateFormat.format(date), timeWebsite / 60);
        camera.set(dateFormat.format(date), timeCamera / 60);
        musicPlayer.set(dateFormat.format(date), timeMusic / 60);
        // dodaj do modelu
        model.addSeries(facebook);
        model.addSeries(messenger);
        model.addSeries(website);
        model.addSeries(camera);
        model.addSeries(musicPlayer);

        return model;
    }

    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initDayDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createDayDurationModel() {
        // generuj model
        dayTimeModel = initDayDurationModel();

        // ustaw wlasciwosci wykresu
        dayTimeModel.setTitle("Czas korzystania z aplikacji w poszczególnych dniach");
        dayTimeModel.setLegendPosition("se");
        dayTimeModel.setBarWidth(10);
        dayTimeModel.setBarPadding(2);

        // os x - wlasciwosci
        Axis x = dayTimeModel.getAxis(AxisType.X);
        x.setLabel("Data");

        // os y - wlasciwosci
        Axis y = dayTimeModel.getAxis(AxisType.Y);
        y.setLabel("Czas korzystania z aplikacji [min]");
        y.setMin(0);
    }


    //gettery i settery

    public MainBean getEditor() {
        return editor;
    }

    public void setEditor(MainBean editor) {
        this.editor = editor;
    }

    public BarChartModel getGeneralTimeModel() {
        return generalTimeModel;
    }

    public void setGeneralTimeModel(BarChartModel generalTimeModel) {
        this.generalTimeModel = generalTimeModel;
    }

    public PieChartModel getAverageTimeModel() {
        return averageTimeModel;
    }

    public void setAverageTimeModel(PieChartModel averageTimeModel) {
        this.averageTimeModel = averageTimeModel;
    }

    public BarChartModel getDayTimeModel() {
        return dayTimeModel;
    }

    public void setDayTimeModel(BarChartModel dayTimeModel) {
        this.dayTimeModel = dayTimeModel;
    }
}
