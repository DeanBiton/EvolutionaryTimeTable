import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    SchoolHourManager manager;

    BooleanProperty isXMLLoaded;

    private Stage primaryStage;


    @FXML
    private Button loadXML;
    @FXML
    private Button RunAlgorithm;
    @FXML
    private TextArea BestSolutionFitness;

    public Controller() {
        manager = new SchoolHourManager();

        isXMLLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    void RunAlgorithm(ActionEvent event) {
        List<EndCondition> endConditions = new ArrayList<>();
        int printEveryThisNumberOfGenerations = 10;
        endConditions.add(new NumberOfGenerations(1000));
        manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations);
    }

    @FXML
    void loadXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("xml files", "*.xml"));
        //File fileToLoadFrom = fileChooser.showOpenDialog(loadXML.getContextMenu());

        File fileToLoadFrom = fileChooser.showOpenDialog(primaryStage);
        try {
            manager.LoadXML(fileToLoadFrom);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        isXMLLoaded.setValue(true);
    }

    @FXML
    private void initialize() {
        RunAlgorithm.disableProperty().bind(isXMLLoaded.not());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
