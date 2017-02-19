package serviceDatabase.Lists;

/**
 * Klasa reprezentująca wektor pomiarowy
 */
public class WekaAtribute {

    /**
     * Obiekt reprezentujący etykietę pory dnia.
     */
    private String label;
    /**
     * Obiekt reprezentujący etykietę lokalizacji (miaso i ulica)
     */
    private String location;
    /**
     * Obiekt reprezentujący etykietę używanej palikacji
     */
    private String prcoessName;

    public WekaAtribute(String label, String location, String processName) {
        this.label = label;
        this.location = location;
        this.prcoessName = processName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrcoessName() {
        return prcoessName;
    }

    public void setPrcoessName(String prcoessName) {
        this.prcoessName = prcoessName;
    }
}
