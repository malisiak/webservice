package serviceWebsite.beans;

import serviceDatabase.Entities.LocationTable;
import serviceDatabase.Lists.WekaAtribute;
import serviceWebsite.enums.ProcessTypeEnum;
import weka.core.*;
import weka.classifiers.trees.J48;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa posiada metody umozliwiające przeprowadzenie algorytmu drzewa decyzyjnego.
 */
@ManagedBean(name = "weka")
public class WekaDecisionTree {

    /**
     * Instancja bean
     */
    @ManagedProperty(value = "#{editor}")
    MainBean editor;


    /**
     * Lista reprezentująca wektor pomiarowy.
     */
    List<WekaAtribute> wekaAtributes = new ArrayList<>();
    /**
     * Wartość zwracana na stronę internetową
     */
    String result = "Brak wygenerowanych danych";


    /**
     * Metoda generuje drzewo decyzyjne i wyswietla je na stronie internetowej.
     * @return Rezultat, przeprowadzonego algorytmu drzewa decyzyjnego.
     */
    public String getDecisionTree() {

        UtilsWeka uw = new UtilsWeka();
        //Przygotuj liste z danymi
        wekaAtributes = uw.prepareData(editor.getUser().getProcessList(), editor.getUser().getLocationList());

        // Stworzenie etykiety pory dnia
        FastVector time = new FastVector(19);
        for (int i = 1; i < 17; i++) {
            time.addElement((7 + i) + "/" + (8 + i));
        }
        time.addElement("0/3");
        time.addElement("3/6");
        time.addElement("6/8");
        Attribute timeAttribute = new Attribute("czas", time);

        // Przygotowanie danych do stworzenia atrybutu lokalizacji, tworzona jest lista z naza miasta i ulicą, a nastepnie przekształcanajest na Zbiór, by uniknąc duplikatów.
        List<String> streetName = new ArrayList<>();
        for (LocationTable l: editor.getUser().getLocationList()){
            streetName.add(uw.getStreetName(l.getLatitude(), l.getLongitude()));
        }
        Set<String> streetAtributeList = new HashSet<>(streetName);

      // Stworzenie atrybutu lokalizacji i okreslenie wartości
        FastVector location = new FastVector(streetAtributeList.size());
        for (String locationAtributte: streetAtributeList){
            location.addElement(locationAtributte);
        }
        Attribute locationAttribute = new Attribute("lokalizacja", location);

        // Stworzenie atrybutu apliakcji i okreslenie wartości
        FastVector process = new FastVector(5);
        process.addElement(ProcessTypeEnum.Facebook.name());
        process.addElement(ProcessTypeEnum.Messenger.name());
        process.addElement(ProcessTypeEnum.Camera.name());
        process.addElement(ProcessTypeEnum.MusicPlayer.name());
        process.addElement(ProcessTypeEnum.Website.name());
        Attribute processAttribute = new Attribute("aplikacja", process);


        FastVector vector = new FastVector(3);
        vector.addElement(timeAttribute);
        vector.addElement(locationAttribute);
        vector.addElement(processAttribute);

        Instances data = new Instances("Data", vector, wekaAtributes.size());
        data.setClassIndex(data.numAttributes() - 2);

        // zbudowanie danych
        Instance iData = new SparseInstance(3);
        for (WekaAtribute w : wekaAtributes) {
            iData.setValue((Attribute) vector.elementAt(0), w.getLabel());
            iData.setValue((Attribute) vector.elementAt(1), w.getLocation());
            iData.setValue((Attribute) vector.elementAt(2), w.getPrcoessName());
            data.add(iData);
        }

// stworzenie drzewa edycyjnego
        try {

            J48 tree = new J48();
            String[] options = new String[1];
            options[0] = "-U";
            tree.setOptions(options);
            // tu byla data
            tree.buildClassifier(data);

            result = "" + tree;

            //Print tree
            System.out.println(tree);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // odpowiednie przygotowanie danych w celu wyświetlenia
        result = result.replace("|", "<br/> | ");
        result = result.replace("\nczas = ", "<br/> czas = ");
        result = result.replace("J48 unpruned tree\n------------------\n", " WYNIKI PRZETWARZANIA DANYCH Z UŻYCIEM ALGORYTMU DRZEWA DECYZYJNEGO<br/>");
        result = result.replace("\n\n", "<br/>");
        result = result.replace("\n\n", "<br/>");
        result = result.replace("\n\nNumber of Leaves : \t5\n\nSize of the tree : \t6\n", "<br/>");

        return result;

    }

    // settery, gettery
    public MainBean getEditor() {
        return editor;
    }

    public void setEditor(MainBean editor) {
        this.editor = editor;
    }
}
