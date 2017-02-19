package serviceWebsite.beans;

import serviceDatabase.Entities.CallTable;
import serviceWebsite.enums.CallTypeEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Klasa posiada metody umożliwiające generowanie wykresów dot. połączeń telefonicznych użytkownika
 */
@ManagedBean(name = "callChart")
public class CallChart implements Serializable {

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
     * Czas trwania połączeń przychodzących
     */
    long incomingTime = 0L;

    /**
     * Czas trwania połączeń wychodzących
     */
    long outcomingTime = 0L;

    /**
     * Obiekt reprezentujący date w określonym formacie.
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    /**
     * Wywołanie sekwencji metod generujących wykresy.
     */
    @PostConstruct
    public void init() {
        createCallCharts();
    }


    /**
     * Metoda wywołuje funckje odpowiedzialne za stworzenie odpowiednych wykresów.
     */
    private void createCallCharts() {
        createGeneralDurationModel();
        createAverageDurationModel();
        createDayDurationModel();

    }

    /**
     * Metoda tworzy model danych, który okresla ogólny czas trwania połączeń przychodzących i wychodzących.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych
     */
    private BarChartModel initGeneralDurationModel() {
        BarChartModel model = new BarChartModel();


        // pobierz dane na temat czasu trwania rozmowy
        Set<CallTable> result = editor.getUser().getCalls();
        if (result != null) {
            for (CallTable c : result) {
                if (c.getCallType().equals(CallTypeEnum.Incoming.name())) {
                    incomingTime = incomingTime + c.getDuration();
                } else if (c.getCallType().equals(CallTypeEnum.Outcoming.name())) {
                    outcomingTime = outcomingTime + c.getDuration();
                }
            }

            // zbuduj słupek dot. połączeń przychodzących
            ChartSeries incoming = new ChartSeries();
            incoming.setLabel("Połączenia przychodzące");
            incoming.set(" ", incomingTime / 60);

            // zbuduj słupek dot. połączeń wychodzących
            ChartSeries outcoming = new ChartSeries();
            outcoming.setLabel("Połączenia wychodzące");
            outcoming.set(" ", outcomingTime / 60);

            // dodaj do modelu
            model.addSeries(incoming);
            model.addSeries(outcoming);
        } else {
            System.out.println("Jestem nulem");
        }
        return model;

    }


    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createGeneralDurationModel() {

        // generuj model
        generalDurationModel = initGeneralDurationModel();

        // ustaw własciwosci wykresu
        generalDurationModel.setTitle(" Zestawienie czasu rozmów przychodzących i wychodzących");
        generalDurationModel.setLegendPosition("se");
        generalDurationModel.setBarWidth(100);
        generalDurationModel.setBarPadding(30);

        // os x - wlasciwosci
        Axis x = generalDurationModel.getAxis(AxisType.X);
        x.setLabel("Typ połączenia");
        //os y - wlasciwosci
        Axis y = generalDurationModel.getAxis(AxisType.Y);
        y.setLabel("Czas trwania połączenia [min]");
        y.setMin(0);
    }


    /**
     * Metoda tworzy wykres kołowy, który określa średni czas trwania połączeń przychodzących i wychodzących w ciągu dnia.
     */
    private void createAverageDurationModel() {
        averageDurationModel = new PieChartModel();
        int days = returnAmountOfDay();

        averageDurationModel.set("Połączenia przychodzące", incomingTime / 60 / days);
        averageDurationModel.set("Połączenia wychodzące", outcomingTime / 60 / days);

        averageDurationModel.setTitle("Przeciętny czas rozmów w czasie jednego dnia");
        averageDurationModel.setLegendPosition("e");
    }


    /**
     * Metoda tworzy model danych, który okresla czas trwania połączeń przychodzących i wychodzących w ciągu dnia w określonym przedziale czau.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych
     */
    private BarChartModel initDayDurationModel() {
        BarChartModel model = new BarChartModel();
        long incomingTimeDay = 0L;
        long outcomingTimeDay = 0L;

        ChartSeries incoming = new ChartSeries();
        incoming.setLabel("Połączenia przychodzące");

        ChartSeries outcoming = new ChartSeries();
        outcoming.setLabel("Połączenia wychodzące");


        //pobierz czas połączeń na poszczególne dni
        java.util.Date date = null;
        for (CallTable record : getEditor().getUser().getCallList()) {
            if (date == null) {
                date = record.getDate();
                if (record.getCallType().equals(CallTypeEnum.Incoming.name())) {
                    incomingTimeDay = incomingTimeDay + record.getDuration();
                } else {
                    outcomingTimeDay = outcomingTimeDay + record.getDuration();
                }
            } else {
                if (DateUtils.isSameDay(date, record.getDate())) {
                    if (record.getCallType().equals(CallTypeEnum.Incoming.name())) {
                        incomingTimeDay = incomingTimeDay + record.getDuration();
                    } else {
                        outcomingTimeDay = outcomingTimeDay + record.getDuration();
                    }
                }
                else{
                    incoming.set(dateFormat.format(date), incomingTimeDay / 60 );
                    outcoming.set(dateFormat.format(date), outcomingTimeDay / 60);
                    date = record.getDate();
                    incomingTimeDay = 0L;
                    outcomingTimeDay =0L;
                    if (record.getCallType().equals(CallTypeEnum.Incoming.name())) {
                        incomingTimeDay = incomingTimeDay + record.getDuration();
                    } else {
                        outcomingTimeDay = outcomingTimeDay + record.getDuration();
                    }

                }
            }
            incoming.set(dateFormat.format(date), incomingTimeDay / 60 );
            outcoming.set(dateFormat.format(date), outcomingTimeDay / 60);


        }

        // dodaj do modelu
        model.addSeries(incoming);
        model.addSeries(outcoming);
        return model;
    }


    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initDayDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createDayDurationModel() {
        // generuj model
        dayDurationModel = initDayDurationModel();

        // ustaw wlasciwosci wykresu
        dayDurationModel.setTitle(" Czas rozmów w poszczególnych dniach");
        dayDurationModel.setLegendPosition("se");
        dayDurationModel.setBarWidth(10);
        dayDurationModel.setBarPadding(30);

        // os x - wlasciwosci
        Axis x = dayDurationModel.getAxis(AxisType.X);
        x.setLabel("Data połączenia");

        // os y - wlasciwosci
        Axis y = dayDurationModel.getAxis(AxisType.Y);
        y.setLabel("Czas trwania połączenia [min]");
        y.setMin(0);
    }

    /**
     * Metoda oblicza ilość dni, które upłynęły od pierwszego połączenia zarejestrowanego przez system  do ostatniego.
     * @return Ilość dni.
     */
    public int returnAmountOfDay() {
        Set<CallTable> result = editor.getUser().getCalls();
        long days = 0L;
        long firstDay = 0L;
        long lastDay = 0L;
        boolean flag = true;

        if (result.size() > 1) {
            for (CallTable c : result) {

                if (flag) {
                    firstDay = c.getDate().getTime();
                    flag = false;
                    continue;
                }

                if (firstDay > c.getDate().getTime()) {
                    firstDay = c.getDate().getTime();
                } else {
                    if (lastDay < c.getDate().getTime()) {
                        lastDay = c.getDate().getTime();
                    }
                }

            }
        } else {
            return 1;
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println("first call " + s.format(new Date(firstDay)) + " " + firstDay);
        System.out.println("last call " + s.format(new Date(lastDay)) + " " + lastDay);

        days = ((lastDay - firstDay) / 1000 / 60 / 60 / 24);

        // zaaokraglenie
        return (int) (days + 1);
    }


    // Getter, Setter

    public BarChartModel getGeneralDurationModel() {
        return generalDurationModel;
    }

    public void setGeneralDurationModel(BarChartModel generalDurationModel) {
        this.generalDurationModel = generalDurationModel;
    }

    public MainBean getEditor() {
        return editor;
    }

    public void setEditor(MainBean editor) {
        this.editor = editor;
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
