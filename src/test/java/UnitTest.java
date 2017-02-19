package test;


import serviceDatabase.Entities.LocationTable;
import serviceDatabase.Entities.ProcessTable;
import serviceDatabase.Lists.WekaAtribute;
import serviceWebsite.beans.UtilsWeka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.*;

/**
 * Created by Martyna on 01.12.2016.
 */
public class UnitTest {


    @Test
    public void getStreetNameTest() {

        double lat = 40.714224;
        double lon = -73.961452;

        UtilsWeka uw = new UtilsWeka();
        String testResult = uw.getStreetName(lat, lon);
        String properResult = "Brooklyn, Bedford Ave";
        assertEquals(properResult, testResult);
    }

    @Test
    public void prepareDataTest() {

        UtilsWeka uw = new UtilsWeka();
        List<ProcessTable> p = new ArrayList<>();
        List<LocationTable> l = new ArrayList<>();
        p.add(new ProcessTable("Messenger", 25, new Date(1480639433)));
        l.add(new LocationTable(54.3790512, 18.6196503, new Date(1480639439), "fused", 0.0f));
        l.add(new LocationTable(54.3790512, 18.6196500, new Date(1480639000), "fused", 0.0f));

        List<WekaAtribute> properList = new ArrayList<>();
        properList.add(new WekaAtribute("3/6", "Gdańsk, Do Studzienki", "Messenger"));
        List<WekaAtribute> testList = uw.prepareData(p, l);


        for (int i = 0; i < properList.size(); i++) {
            assertEquals(properList.get(i).getLabel(), testList.get(i).getLabel());
            assertEquals(properList.get(i).getLocation(), testList.get(i).getLocation());
            assertEquals(properList.get(i).getPrcoessName(), testList.get(i).getPrcoessName());
        }
    }

}
