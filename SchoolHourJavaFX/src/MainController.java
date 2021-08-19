import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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
    @FXML
    private VBox VBoxMenu;
    @FXML
    private Button BTNSetConditions;
    @FXML
    private Button BTNViewAlgorithm;
    @FXML
    private Button BTNShowSchoolData;

    private BorderPane algorithmBorderPane;

    private SchoolHourManager manager;
    private Stage primaryStage;
    private List<EndCondition> endConditions;
    private int printEveryThisNumberOfGenerations;

    private ChooseEndConditionsController chooseEndConditionsController;
    private Node chooseEndConditionsScene;
    private ShowSchoolDataController showSchoolDataController;
    private Node showSchoolDataScene;
    private ViewAlgorithmController viewAlgorithmController;
    private Node viewAlgorithmScene;
    private AlgorithmSettingsController algorithmSettingsController;
    private Node algorithmSettingsScene;


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
        // Algorithm Buttons
        RunAlgorithm.disableProperty().setValue(true);
        PauseResume.disableProperty().bind(isAlgorithmActive.not());
        //PauseResume.visibleProperty().bind(isAlgorithmActive);
        BTNStop.disableProperty().bind(isAlgorithmAlive.not());

        // Menu Buttons
        BTNSetConditions.disableProperty().bind(Bindings.or(isXMLLoaded.not(), isAlgorithmAlive));
        BTNShowSchoolData.disableProperty().setValue(true);

        //creating Controllers
        createChooseEndConditionsController();
        createViewAlgorithmController();
        createShowSchoolDataController();
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

    // Algorithm Buttons
    @FXML
    private void loadXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileToLoadFrom = fileChooser.showOpenDialog(primaryStage);
        try {
            manager.LoadXML(fileToLoadFrom);
            isXMLLoaded.setValue(true);
            borderPane.getChildren().remove(borderPane.getCenter());
            borderPane.setCenter(chooseEndConditionsScene);
            resetApp();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void resetApp() {
        RunAlgorithm.disableProperty().setValue(true);
        BTNShowSchoolData.disableProperty().setValue(false);
        showSchoolDataController.resetScene();
    }

    @FXML
    private void RunAlgorithm(ActionEvent event) {
        UIAdapter uiAdapter = createUIAdapter();

        manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations, uiAdapter);
        isAlgorithmActive.setValue(true);
        RunAlgorithm.disableProperty().setValue(true);
        isAlgorithmAlive.setValue(true);
    }

    public void setAlgorithmParameters(List<EndCondition> _endConditions, int _printEveryThisNumberOfGenerations,ConditionPairs conditionPairs)
    {
        viewAlgorithmController.setConditionPairs(conditionPairs);
        endConditions = _endConditions;
        printEveryThisNumberOfGenerations = _printEveryThisNumberOfGenerations;
        RunAlgorithm.disableProperty().setValue(false);
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
                    viewAlgorithmController.updateBestFitness(fitness);
                },

                () -> {
                    isAlgorithmActive.setValue(false);
                    RunAlgorithm.disableProperty().setValue(false);
                    isAlgorithmAlive.setValue(false);
                    PauseResume.setText("Pause");
                },
                current->{
                    viewAlgorithmController.updateGenerationNUmber(current);
                },
                seconds->{
                    viewAlgorithmController.updateTime(seconds);
                }
        );
    }

    // Create Controllers
    private void createChooseEndConditionsController() throws Exception
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("ChooseEndConditions");
        chooseEndConditionsScene = fxmlLoader.load();
        BorderPane.setAlignment(chooseEndConditionsScene, Pos.TOP_LEFT);

        chooseEndConditionsController = fxmlLoader.getController();
        chooseEndConditionsController.setMainController(this);
        chooseEndConditionsController.setManager(manager);
    }

    private void createShowSchoolDataController() throws Exception
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("ShowSchoolData");
        showSchoolDataScene = fxmlLoader.load();
        BorderPane.setAlignment(showSchoolDataScene, Pos.TOP_LEFT);

        showSchoolDataController = fxmlLoader.getController();
        showSchoolDataController.setMainController(this);
        showSchoolDataController.setManager(manager);
    }

    private void createViewAlgorithmController()
    {
        algorithmBorderPane = new BorderPane();
        algorithmBorderPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        algorithmBorderPane.setMinWidth(Region.USE_COMPUTED_SIZE);
        algorithmBorderPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        algorithmBorderPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        //algorithmBorderPane.setMaxHeight(Region.);

        FXMLLoader fxmlLoader = getSceneFXMLLoader("ViewAlgorithm");

        try {
            viewAlgorithmScene = fxmlLoader.load();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        BorderPane.setAlignment(viewAlgorithmScene, Pos.TOP_LEFT);
        viewAlgorithmController = fxmlLoader.getController();
        viewAlgorithmController.setMainController(this);
        viewAlgorithmController.setManager(manager);

        fxmlLoader = getSceneFXMLLoader("AlgorithmSettings");

        try {
            algorithmSettingsScene = fxmlLoader.load();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        BorderPane.setAlignment(algorithmSettingsScene, Pos.TOP_LEFT);
        algorithmSettingsController = fxmlLoader.getController();
        algorithmSettingsController.setMainController(this);
        algorithmSettingsController.setManager(manager);

        algorithmBorderPane.setTop(algorithmSettingsScene);
        algorithmBorderPane.setBottom(viewAlgorithmScene);
    }

    // Menu Buttons
    @FXML
    private void ChooseEndConditionsButton(ActionEvent event)
    {
        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(chooseEndConditionsScene);
    }

    @FXML
    private void ShowSchoolDataButton(ActionEvent event)
    {
        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(showSchoolDataScene);
    }

    @FXML
    private void ViewAlgorithmButton(ActionEvent event)
    {
        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(algorithmBorderPane);
    }


}
