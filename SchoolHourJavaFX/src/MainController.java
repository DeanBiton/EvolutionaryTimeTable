import Evolution.EndCondition.EndCondition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private MenuItem MIDefaultSkin;

    @FXML
    private MenuItem MIDarkSkin;

    @FXML
    private MenuItem MIUglySkin;


    @FXML
    private GridPane GPCenter;

    // Algorithm Buttons
    @FXML
    private ScrollPane SPTop;
    @FXML
    private AnchorPane APTop;
    @FXML
    private GridPane GPOptions;
    @FXML
    private MenuButton MBSkins;
    @FXML
    private ToggleButton TBAnimation;
    @FXML
    private Button BTNLoadXML;
    @FXML
    private Button BTNRunAlgorithm;
    @FXML
    private Button BTNPauseResume;
    @FXML
    private Button BTNStop;
    private double SPTopMinWidth;

    //Menu
    @FXML
    private ScrollPane SPMenu;
    @FXML
    private AnchorPane APMenu;
    @FXML
    private VBox VBoxMenu;
    @FXML
    private Button BTNSetConditions;
    @FXML
    private Button BTNAlgorithmSettings;
    @FXML
    private Button BTNShowSchoolData;
    @FXML
    private Button BTNBestSolution;

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
    private ShowBestSolutionController showBestSolutionController;
    private Node showBestSolutionScene;

    private BooleanProperty isXMLLoaded;
    private BooleanProperty isAlgorithmActive;
    private BooleanProperty isAlgorithmAlive;

    //screen size Related
    double menuButtonHeight = 70;
    double menuButtonWidth = 170;

    public MainController() throws Exception{
        manager = new SchoolHourManager();
        isXMLLoaded = new SimpleBooleanProperty(false);
        isAlgorithmActive = new SimpleBooleanProperty(false);
        isAlgorithmAlive = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() throws Exception{
        // Algorithm Buttons
        BTNRunAlgorithm.disableProperty().setValue(true);
        BTNPauseResume.disableProperty().bind(isAlgorithmActive.not());
        BTNStop.disableProperty().bind(isAlgorithmAlive.not());

        initializeMenu();

        //creating Controllers
        createChooseEndConditionsController();
        createAlgorithmSettingsController();
        createShowSchoolDataController();
        createShowBestSolutionController();
        createViewAlgorithmController();
    }

    public void initializeComponentsSizes()
    {
        initializeAPTopSizes();
        initializeVBoxSizes();
        showBestSolutionController.initializeSizes();
        initializeSkins();
    }

    private void initializeSkins()
    {
        MBSkins.setText(MIDefaultSkin.getText());
        MIDarkSkin.setOnAction(event -> {MBSkins.setText(MIDarkSkin.getText());
            getScene().getStylesheets().clear();
            getScene().getStylesheets().add("DarkTheme.css");
        });

        MIDefaultSkin.setOnAction(event -> { MBSkins.setText(MIDefaultSkin.getText());
            getScene().getStylesheets().clear();
            getScene().getStylesheets().add("DefaultTheme.css");
           });
    }
    private void initializeAPTopSizes()
    {
        AnchorPane.setTopAnchor(GPOptions, 5.0);
        AnchorPane.setRightAnchor(GPOptions, 5.0);

        AnchorPane.setTopAnchor(BTNLoadXML, SPTop.getHeight()/2 - BTNLoadXML.getHeight() / 2);
        AnchorPane.setTopAnchor(BTNRunAlgorithm, SPTop.getHeight()/2 - BTNRunAlgorithm.getHeight() / 2);
        AnchorPane.setTopAnchor(BTNPauseResume, SPTop.getHeight()/2 - BTNPauseResume.getHeight() / 2);
        AnchorPane.setTopAnchor(BTNStop, SPTop.getHeight()/2 - BTNStop.getHeight() / 2);

        setLeftAPTop();
        getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setLeftAPTop();
            }
        });
    }

    private void setLeftAPTop()
    {
        SPTopMinWidth = BTNLoadXML.getWidth() + BTNRunAlgorithm.getWidth() + BTNPauseResume.getWidth() + BTNStop.getWidth() + GPOptions.getWidth();
        SPTopMinWidth = SPTopMinWidth + SPTopMinWidth / 8;
        double lengthBetweenControls;

        if (getScene().getWidth() <= SPTopMinWidth)
        {
            SPTop.setPrefWidth(getScene().getWidth());
            APTop.setPrefWidth(SPTopMinWidth);
            lengthBetweenControls = 5;
        }
        else
        {
            SPTop.setPrefWidth(getScene().getWidth() + 5);
            APTop.setPrefWidth(getScene().getWidth());

            lengthBetweenControls = (APTop.getPrefWidth() - (BTNLoadXML.getWidth() +
                    BTNRunAlgorithm.getWidth() + BTNPauseResume.getWidth() + BTNStop.getWidth() + GPOptions.getWidth()))/5 - 5;
        }

        AnchorPane.setLeftAnchor(BTNLoadXML, lengthBetweenControls);
        AnchorPane.setLeftAnchor(BTNRunAlgorithm, AnchorPane.getLeftAnchor(BTNLoadXML) + BTNLoadXML.getWidth() + lengthBetweenControls);
        AnchorPane.setLeftAnchor(BTNPauseResume, AnchorPane.getLeftAnchor(BTNRunAlgorithm) + BTNRunAlgorithm.getWidth() + lengthBetweenControls);
        AnchorPane.setLeftAnchor(BTNStop, AnchorPane.getLeftAnchor(BTNPauseResume) + BTNPauseResume.getWidth() + lengthBetweenControls);
    }

    private void initializeVBoxSizes() {
        BTNSetConditions.setPrefSize(menuButtonWidth, menuButtonHeight);
        BTNAlgorithmSettings.setPrefSize(menuButtonWidth, menuButtonHeight);
        BTNShowSchoolData.setPrefSize(menuButtonWidth, menuButtonHeight);
        BTNBestSolution.setPrefSize(menuButtonWidth, menuButtonHeight);

        VBoxMenu.setPrefHeight(menuButtonHeight * 4);

        SPMenu.setPrefWidth(menuButtonWidth + 2);
        SPMenu.setPrefWidth(400);
        SPMenu.setPrefHeight(menuButtonHeight * 4);

        APMenu.setPrefWidth(menuButtonWidth);
        APMenu.setPrefHeight(menuButtonHeight * 4);

        AnchorPane.setTopAnchor(VBoxMenu, 0.0);
        setMenuButtonsWidth();

        getScene().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setMenuButtonsWidth();
            }
        });
    }

    public void setMenuButtonsWidth()
    {
        double currentMenuButtonWidth = menuButtonWidth + 2;
        if(SPMenu.getHeight() < 283) // menuButtonHeight * 4
        {
           currentMenuButtonWidth += 13;
        }


       // BTNSetConditions.setPrefWidth(currentMenuButtonWidth);
        //BTNViewAlgorithm.setPrefWidth(currentMenuButtonWidth);
        //BTNShowSchoolData.setPrefWidth(currentMenuButtonWidth);
        //BTNBestSolution.setPrefWidth(currentMenuButtonWidth);

        //VBoxMenu.setPrefWidth(currentMenuButtonWidth); //*
        SPMenu.setPrefWidth(currentMenuButtonWidth); // affects the actual width
        //APMenu.setPrefWidth(currentMenuButtonWidth);
        //SPMenu.setPrefWidth(menuButtonWidth + 2); // affects the actual width
    }

    private void initializeMenu()
    {
/*
        //SPMenu initialize
        SPMenu.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        SPMenu.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        SPMenu.setPrefSize(200, 60);
        VBoxMenu.setMinSize(100,200);
        VBoxMenu.setMinSize(100,200);

        //SPMenu.setFitToHeight(true);
        //SPMenu.setPrefSize(borderPane.getLeft().getLayout
        // X(), borderPane.getLeft().getLayoutY());
        BTNSetConditions.setMinSize(190, 60);
        BTNSetConditions.setPrefSize(190, 60);
        BTNSetConditions.setMaxSize(190, 60);
*/
        // Menu Buttons
        BTNSetConditions.disableProperty().bind(Bindings.or(isXMLLoaded.not(), isAlgorithmAlive));
        BTNShowSchoolData.disableProperty().setValue(true);
        BTNAlgorithmSettings.disableProperty().setValue(true);
        BTNBestSolution.disableProperty().setValue(true);
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
    private void loadXML(ActionEvent event) throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileToLoadFrom = fileChooser.showOpenDialog(primaryStage);
        try {
            manager.LoadXML(fileToLoadFrom);
            isXMLLoaded.setValue(true);
            ChooseEndConditionsButton(new ActionEvent());
            BTNSetConditions.disableProperty().bind(isAlgorithmAlive);
            BTNAlgorithmSettings.disableProperty().setValue(true);
            resetApp();
        } catch (src.ShowException e) {
            error(e.getMessage());
           // System.out.println(e.getMessage());
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void resetApp() {
        BTNRunAlgorithm.disableProperty().setValue(true);
        BTNShowSchoolData.disableProperty().setValue(false);
        algorithmSettingsController.resetScene(manager.getDataAndAlgorithmSettings().getDtoEvolutionaryAlgorithmSettings());
        showSchoolDataController.resetScene();
        showBestSolutionController.resetScene();

        if(borderPane.getBottom() != null)
        {
            borderPane.getChildren().remove(viewAlgorithmScene);
        }
    }

    @FXML
    private void RunAlgorithm(ActionEvent event) {
        UIAdapter uiAdapter = createUIAdapter();

        manager.runEvolutionaryAlgorithm(endConditions, printEveryThisNumberOfGenerations, uiAdapter);
        isAlgorithmActive.setValue(true);
        isAlgorithmAlive.setValue(true);
        BTNRunAlgorithm.disableProperty().setValue(true);
        BTNAlgorithmSettings.disableProperty().setValue(false);
        BTNBestSolution.disableProperty().setValue(false);
        if(borderPane.getCenter() == chooseEndConditionsScene)
        {
            AlgorithmSettingsButton(new ActionEvent());  //change scene to viewAlgorithm
        }

        if(borderPane.getBottom() == null)
        {
           borderPane.setBottom(viewAlgorithmScene);
        }

        showBestSolutionController.createDiagram();
    }

    public void setAlgorithmParameters(List<EndCondition> _endConditions, int _printEveryThisNumberOfGenerations,ConditionPairs conditionPairs)
    {
        viewAlgorithmController.setConditionPairs(conditionPairs);
        endConditions = _endConditions;
        printEveryThisNumberOfGenerations = _printEveryThisNumberOfGenerations;
        BTNRunAlgorithm.disableProperty().setValue(false);
    }

    @FXML
    private void PauseResume(ActionEvent event) {

        if(manager.isSuspended())
        {
            algorithmSettingsController.setNewSettings();
            manager.resume();
            BTNPauseResume.setText("Pause");
        }
        else
        {
            manager.suspend();
            BTNPauseResume.setText("Resume");
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
                    BTNRunAlgorithm.disableProperty().setValue(false);
                    isAlgorithmAlive.setValue(false);
                    BTNPauseResume.setText("Pause");
                },
                current->{
                    viewAlgorithmController.updateGenerationNUmber(current);
                },
                seconds->{
                    viewAlgorithmController.updateTime(seconds);
                },
                bestSolution->{
                    showBestSolutionController.setDTOTupleGroupWithFitnessDetails(bestSolution);
                },
                (fitness,generation) -> {
                    showBestSolutionController.addfitnesstochart(fitness, generation);
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
        //showSchoolDataController.initializeSizes();
    }

    private void createAlgorithmSettingsController()
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("AlgorithmSettings");

        try {
            algorithmSettingsScene = fxmlLoader.load();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        algorithmSettingsController = fxmlLoader.getController();
        algorithmSettingsController.setMainController(this);
        algorithmSettingsController.setManager(manager);
    }

    private void createShowBestSolutionController()
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("ShowBestSolution");

        try {
            showBestSolutionScene = fxmlLoader.load();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        BorderPane.setAlignment(showBestSolutionScene, Pos.TOP_LEFT);

        showBestSolutionController = fxmlLoader.getController();
        showBestSolutionController.setMainController(this);
        showBestSolutionController.setManager(manager);
    }

    private void createViewAlgorithmController()
    {
        FXMLLoader fxmlLoader = getSceneFXMLLoader("ViewAlgorithm");

        try {
            viewAlgorithmScene = fxmlLoader.load();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        //BorderPane.setAlignment(viewAlgorithmScene, Pos.TOP_LEFT);
        viewAlgorithmController = fxmlLoader.getController();
        viewAlgorithmController.setMainController(this);
        viewAlgorithmController.setManager(manager);
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
    private void AlgorithmSettingsButton(ActionEvent event)
    {
        borderPane.getChildren().remove(algorithmSettingsScene);
        borderPane.setCenter(algorithmSettingsScene);
    }

    @FXML
    private void ShowBestSolutionButton(ActionEvent event)
    {
        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(showBestSolutionScene);
    }

    // Gets
    public Scene getScene()
    {
        return primaryStage.getScene();
    }

    public BorderPane getBorderPane() { return borderPane;}

    public ScrollPane getSPMenu() {return SPMenu;}

    public double getCenterPrefWidth()
    {
        return getScene().getWidth() - SPMenu.getWidth();
    }

    public double getCenterPrefHeight()
    {
        double height = getScene().getHeight() - SPTop.getHeight();

        if(borderPane.getBottom() != null)
        {
            height -= viewAlgorithmController.getSPViewAlgorithm().getHeight();
        }

        return height;
    }

    public double getCenterWindowWidth()
    {
        return Screen.getPrimary().getBounds().getWidth() - SPMenu.getWidth();
    }

    public double getCenterWindowHeight()
    {
        return Screen.getPrimary().getBounds().getHeight() - SPTop.getHeight();
    }

    private void error(String errorString)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        //alert.setHeaderText("Look, an Error Dialog");
        alert.setContentText(errorString);

        alert.showAndWait();
    }
}
