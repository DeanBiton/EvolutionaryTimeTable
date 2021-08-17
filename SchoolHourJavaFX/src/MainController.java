import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button loadXML;
    @FXML
    private Button RunAlgorithm;
    @FXML
    private TextArea BestSolutionFitness;
    @FXML
    private Button PauseResume;
    @FXML
    private Button BTNStop;

    private SchoolHourManager manager;
    private Stage primaryStage;
    private List<EndCondition> endConditions;
    private int printEveryThisNumberOfGenerations;

    private ChooseEndConditionsController chooseEndConditionsController;

    private BooleanProperty isXMLLoaded;
    private BooleanProperty isAlgorithmActive;
    private BooleanProperty isAlgorithmAlive;
    public MainController() throws Exception{
        manager = new SchoolHourManager();
        isXMLLoaded = new SimpleBooleanProperty(false);
        isAlgorithmActive = new SimpleBooleanProperty(false);
        isAlgorithmAlive = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() throws Exception{
        RunAlgorithm.disableProperty().setValue(true);
        PauseResume.disableProperty().bind(isAlgorithmActive.not());
        //PauseResume.visibleProperty().bind(isAlgorithmActive);
        BTNStop.disableProperty().bind(isAlgorithmAlive.not());
    }

    private FXMLLoader getSceneFXMLLoader(String fxmlFileName) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(fxmlFileName + ".fxml");
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    private Parent getFirstAppRoot(FXMLLoader fxmlLoader) throws IOException {
        return (Parent) fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAlgorithmParameters(List<EndCondition> _endConditions, int _printEveryThisNumberOfGenerations)
    {
        endConditions = _endConditions;
        printEveryThisNumberOfGenerations = _printEveryThisNumberOfGenerations;
        RunAlgorithm.disableProperty().setValue(false);
    }

    @FXML
    private void RunAlgorithm(ActionEvent event) {
        UIAdapter uiAdapter = createUIAdapter();

        manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations, uiAdapter);
        isAlgorithmActive.setValue(true);
        RunAlgorithm.disableProperty().setValue(true);
        isAlgorithmAlive.setValue(true);
    }

    @FXML
    private void loadXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileToLoadFrom = fileChooser.showOpenDialog(primaryStage);
        try {
            manager.LoadXML(fileToLoadFrom);
            isXMLLoaded.setValue(true);
            createChooseEndConditionsController();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void PauseResume(ActionEvent event) {

        if(manager.isSuspended())
        {
            manager.resume();
            PauseResume.setText("Pause");
        }
        else
        {
            manager.suspend();
            PauseResume.setText("Resume");
        }
    }

    @FXML
    private void Stop(ActionEvent event)
    {
        manager.stopAlgorithm();
    }

    private UIAdapter createUIAdapter()
    {
        return new UIAdapter(
                fitness-> {
                    BestSolutionFitness.setText(fitness.toString());
                },

                () -> {
                    isAlgorithmActive.setValue(false);
                    RunAlgorithm.disableProperty().setValue(false);
                    isAlgorithmAlive.setValue(false);
                    PauseResume.setText("Pause");
                }
        );
    }

    private void createChooseEndConditionsController() throws Exception
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("ChooseEndConditions");

        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(fxmlLoader.load());
        chooseEndConditionsController = fxmlLoader.getController();
        chooseEndConditionsController.setMainController(this);
        chooseEndConditionsController.setManager(manager);
    }

}
