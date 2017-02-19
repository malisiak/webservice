package serviceWebsite.beans;

import serviceDatabase.Entities.LocationTable;
import serviceDatabase.Entities.ProcessTable;
import serviceDatabase.Lists.WekaAtribute;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Klasa zawiera metody pomocniczne dla klay WekaDecisiontree, które umozliwiają przypisanie nazwy miasta i ulicy z współrzędnych geograficznych  oraz metodę przygotowywującą wektor pomairowy.
 */
public class UtilsWeka {

    /**
     * Adres strony internetowej, umożliwiającej przekształcenie współrzędnych geograficznych na m.in nazwę miasta i ulicy
     */
    private final String GOOGLE_API_ADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    /**
     * Lista reprezentująca wektor pomiarowy.
     */
    List<WekaAtribute> wekaAtributes = new ArrayList<>();

    /**
     * Metoda umozliwia odczytanie nazwy miasta oraz nazwy ulicy na podstawie przesłanych współrzędnych geograficznych.
     * @param lat Szerokość geograficzna.
     * @param lon Długość geograficzna.
     * @return Wartość tekstowa przechowywująca nazwę miasta oraz ulicy.
     */
    public String getStreetName(double lat, double lon) {
        StringBuilder text = new StringBuilder();
        try {
            String url = GOOGLE_API_ADDRESS + lat + "," + lon + "&sensor=true";
            Document data = Jsoup.connect(url).ignoreContentType(true).get();
            String json = data.select("body").text();
            JSONObject jsonFromBody = new JSONObject(json);
            if (jsonFromBody.getJSONArray("results") != null) {
                JSONArray jsonResultArray = jsonFromBody.getJSONArray("results");
                if (jsonResultArray.length() > 0 && jsonResultArray.getJSONObject(0).getString("formatted_address") != null) {

                    text.append(jsonResultArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(3).getString("short_name"));
                    text.append(", ");
                    text.append(jsonResultArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(1).getString("short_name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    /**
     * Metoda przygotowuje wektor pomiarowy. Zapisuje do listy odpowiednią etykietę pory dnia, lokalizację (nazwe miasta oraz ulicy) oraz aplikację używaną w tym czasie przez użytkownika
     * @param pList Lista zawierająca dane o używanych przez użytkownika aplikacjach.
     * @param lList Lista zawierająca dane na temat lokalizacji.
     * @return Zwraca przygotowany wektor pomairowy.
     */
    public List<WekaAtribute> prepareData(List<ProcessTable> pList, List<LocationTable> lList) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        for (ProcessTable p : pList) {

            for (LocationTable l : lList) {

                if (DateUtils.isSameDay(p.getDate(), l.getDate())) {
                    cal1.setTime(p.getDate());
                    cal2.setTime(l.getDate());
                    // sprawdz czy te same godziny
                    if (cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)) {
                        int minutePros = cal1.get(Calendar.MINUTE);
                        int minuteLoc = cal2.get(Calendar.MINUTE);
                        if (String.valueOf(minutePros).length() == 1) {
                            minutePros = 0;
                            if (String.valueOf(minuteLoc).length() == 1) {
                                minuteLoc = 0;
                                wekaAtributes.add(new WekaAtribute(p.getTimeOfADay(), getStreetName(l.getLatitude(), l.getLongitude()), p.getProcessName()));
                                break;

                            } else {
                                continue;
                            }
                        }
                        if (minutePros / 10 == minuteLoc / 10) {
                            if (minutePros/10 < 5) {
                                wekaAtributes.add(new WekaAtribute(p.getTimeOfADay(), getStreetName(l.getLatitude(), l.getLongitude()), p.getProcessName()));
                                break;
                            } else {
                                wekaAtributes.add(new WekaAtribute(p.getTimeOfADay(), getStreetName(l.getLatitude(),l.getLongitude()), p.getProcessName()));
                                break;
                            }

                        }

                    }

                }
            }
        }
return wekaAtributes;
    }
}