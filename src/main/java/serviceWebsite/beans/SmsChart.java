package serviceWebsite.beans;

import serviceDatabase.Entities.SmsTable;
import serviceWebsite.enums.SmsTypeEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Klasa posiada metody umożliwiające generowanie wykresów dot. wiadomości SMS
 */
@ManagedBean(name = "smsChart")
public class SmsChart implements Serializable {

    /**
     * Instancja bean
     */
    @ManagedProperty(value = "#{editor}")
    MainBean editor;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel generalAmountModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel averageAmountModel;

    /**
     * Obiekt reprezentujący model danych, umożliwiający stworzenie wykresu
     */
    private BarChartModel dayAmountModel;

    /**
     * Liczba odebranych wiadomości
     */
    private long amountOfInboxSms = 0L;

    /**
     * Liczba wysłanych wiadomości
     */
    private long amountOfOutboxSms = 0L;

    /**
     * Obiekt reprezentujący date w określonym formacie.
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    /**
     * Wywołanie sekwencji metod generujących wykresy.
     */
    @PostConstruct
    public void init() {
        createSmsCharts();
    }

    /**
     * Metoda wywołuje funckje odpowiedzialne za stworzenie odpowiednych wykresów.
     */
    private void createSmsCharts() {
        createGeneralAmountModel();
        createAverageAmountModel();
        createDayAmountModel();
    }

    /**
     * Metoda tworzy model danych, który określa ogólną ilość odebranych i wysłanych wiadomości SMS
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initGeneralAmountModel() {
        BarChartModel model = new BarChartModel();

        Set<SmsTable> result = editor.getUser().getSms();
        if (result != null) {
            for (SmsTable s : result) {
                if (s.getSmsType().equals(SmsTypeEnum.Inbox.name())) {
                    amountOfInboxSms++;
                } else if (s.getSmsType().equals(SmsTypeEnum.Outbox.name())) {
                    amountOfOutboxSms++;
                }
            }

            ChartSeries inbox = new ChartSeries();
            inbox.setLabel("Sms-y przychodzące");
            inbox.set(" ", amountOfInboxSms);

            ChartSeries outbox = new ChartSeries();
            outbox.setLabel("Sms-y wychodzące");
            outbox.set(" ", amountOfOutboxSms);


            model.addSeries(inbox);
            model.addSeries(outbox);
        } else {

        }
        return model;

    }

    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createGeneralAmountModel() {

        // generuj model
        generalAmountModel = initGeneralAmountModel();

        // ustaw własciwosci wykresu
        generalAmountModel.setTitle(" Zestawienie ilości przychodzących i wychodzących sms-ów");
        generalAmountModel.setLegendPosition("se");
        generalAmountModel.setBarWidth(100);
        generalAmountModel.setBarPadding(30);

        // os x - wlasciwosci
        Axis x = generalAmountModel.getAxis(AxisType.X);
        x.setLabel("Typ smsu");
        //os y - wlasciwosci
        Axis y = generalAmountModel.getAxis(AxisType.Y);
        y.setLabel("Ilość sms-ów");
        y.setMin(0);
    }

    /**
     * Metoda tworzy model danych, który określa przeciętna ilość odebranych i wysłanych wiadomości SMS w ciągu dnia
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initAverageAmountModel() {

        BarChartModel model = new BarChartModel();

        //liczba dni
        int days;
        // data rekordu z bazy z pierwszym wysłanym/bądz odebranym sms
        long firstDate = getEditor().getUser().getSmsList().get(0).getDate().getTime();

        // data rekordu z bazy z ostatnim wysłanym/bądz odebranym sms
        long lastDate = getEditor().getUser().getSmsList().get(getEditor().getUser().getSmsList().size() - 1).getDate().getTime();
        days = 1 + (int) (lastDate - firstDate) / 1000 / 60 / 60 / 24;

        ChartSeries inbox = new ChartSeries();
        inbox.setLabel("Sms przychodzące");
        inbox.set(" ", amountOfInboxSms / days);

        ChartSeries outbox = new ChartSeries();
        outbox.setLabel("Sms wychodzące");
        outbox.set(" ", amountOfOutboxSms / days);

        // dodaj do modelu
        model.addSeries(inbox);
        model.addSeries(outbox);
        return model;

    }

    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createAverageAmountModel() {

        // generuj model
        averageAmountModel = initAverageAmountModel();

        // ustaw własciwosci wykresu
        averageAmountModel.setTitle(" Przeciętna ilość wysyłanych i odebranych sms-ów jednego dnia");
        averageAmountModel.setLegendPosition("se");
        averageAmountModel.setBarWidth(100);
        averageAmountModel.setBarPadding(30);

        // os x - wlasciwosci
        Axis x = averageAmountModel.getAxis(AxisType.X);
        x.setLabel("Typ smsu");
        //os y - wlasciwosci
        Axis y = averageAmountModel.getAxis(AxisType.Y);
        y.setLabel("Ilość sms-ów");
        y.setMin(0);
    }

    /**
     * Metoda tworzy model danych, który określa ilość odebranych i wysłanych wiadomości SMS w ciągu dnia w określonym przedziale czasu.
     * Model reprezentuje wykres słupkowy.
     * @return Model danych.
     */
    private BarChartModel initDayAmountModel() {
        BarChartModel model = new BarChartModel();
        Long inboxAmount = 0L;
        Long outboxAmount = 0L;

        ChartSeries inbox = new ChartSeries();
        inbox.setLabel("Sms przychodzące");

        ChartSeries outbox = new ChartSeries();
        outbox.setLabel("Sms wychodzące");

        Date date = null;
        //pobierz czas połączeń na poszczególne dni
        for (SmsTable record : getEditor().getUser().getSmsList()) {
            if (date == null) {
                date = record.getDate();
                if (record.getSmsType().equals(SmsTypeEnum.Inbox.name())) {
                    inboxAmount++;
                } else {
                    outboxAmount++;
                }
            } else {
                if (DateUtils.isSameDay(date, record.getDate())) {
                    if (record.getSmsType().equals(SmsTypeEnum.Inbox.name())) {
                        inboxAmount++;
                    } else {
                        outboxAmount++;
                    }
                } else {
                    outbox.set(dateFormat.format(date), outboxAmount);
                    inbox.set(dateFormat.format(date), inboxAmount);
                    date = record.getDate();
                    inboxAmount = 0L;
                    outboxAmount = 0L;
                    if (record.getSmsType().equals(SmsTypeEnum.Inbox.name())) {
                        inboxAmount++;
                    } else {

                        outboxAmount++;
                    }
                }
            }
        }
        outbox.set(dateFormat.format(date), outboxAmount);
        inbox.set(dateFormat.format(date), inboxAmount);


        // dodaj do modelu
        model.addSeries(inbox);
        model.addSeries(outbox);
        return model;

    }

    /**
     * Metoda tworzy wykres na podstawie otrzymanego modelu danych z funckji initGeneralDurationModel(). W funckji określane są własności osi oraz legendy.
     */
    private void createDayAmountModel() {
        // generuj model
        dayAmountModel = initDayAmountModel();

        // ustaw wlasciwosci wykresu
        dayAmountModel.setTitle(" Ilość sms w poszczególnych dniach");
        dayAmountModel.setLegendPosition("se");
        dayAmountModel.setBarWidth(10);
        dayAmountModel.setBarPadding(5);

        // os x - wlasciwosci
        Axis x = dayAmountModel.getAxis(AxisType.X);
        x.setLabel("Data");

        // os y - wlasciwosci
        Axis y = dayAmountModel.getAxis(AxisType.Y);
        y.setLabel("Ilość smsów");
        y.setMin(0);
    }


    //gettery, settery


    public MainBean getEditor() {
        return editor;
    }

    public void setEditor(MainBean editor) {
        this.editor = editor;
    }

    public BarChartModel getGeneralAmountModel() {
        return generalAmountModel;
    }

    public void setGeneralAmountModel(BarChartModel generalAmountModel) {
        this.generalAmountModel = generalAmountModel;
    }

    public BarChartModel getAverageAmountModel() {
        return averageAmountModel;
    }

    public void setAverageAmountModel(BarChartModel averageAmountModel) {
        this.averageAmountModel = averageAmountModel;
    }

    public BarChartModel getDayAmountModel() {
        return dayAmountModel;
    }

    public void setDayAmountModel(BarChartModel dayAmountModel) {
        this.dayAmountModel = dayAmountModel;
    }
}
