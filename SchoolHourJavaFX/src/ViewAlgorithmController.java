import Evolution.EndCondition.EndCondition;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ViewAlgorithmController {

    @FXML
    private ScrollPane SPViewAlgorithm;
    @FXML
    private GridPane gridEndCondtions;
    @FXML
    private Label generationValue;
    @FXML
    private Label fitnessValue;
    @FXML
    private Label timeValue;
    @FXML
    private Label generationCapacity;
    @FXML
    private Label fitnessCapacity;
    @FXML
    private Label timeCapacity;

    private ProgressBar progressBarFitness;
    private ProgressBar progressBarNumberOfGenerations;
    private ProgressBar progressBarTime;

    private IntegerProperty generationNumber;
    private LongProperty timeMinutes;
    private LongProperty timeSeconds;
    private DoubleProperty bestFitness;
    private StringProperty time;

    private ConditionPairs conditionPairs;


    private MainController mainController;
    private SchoolHourManager manager;
    private List<EndCondition> endConditions;
    public ViewAlgorithmController() {
        generationNumber=new SimpleIntegerProperty(0);
        timeMinutes=new SimpleLongProperty(0);
        timeSeconds=new SimpleLongProperty(0);
        bestFitness=new SimpleDoubleProperty(0);
            time=new SimpleStringProperty("00:00:00");
        progressBarFitness=null;
        progressBarNumberOfGenerations=null;
        progressBarTime=null;
    }

    public void initialize()
    {
        generationValue.textProperty().bind(generationNumber.asString());
        fitnessValue.textProperty().bind(bestFitness.asString("%.3f"));
        //time.bind(timeMinutes.asString()+":"+timeSeconds.toString());
        timeValue.textProperty().bind(time);
       //progressbar.progressProperty().bind();
    }

    public void updateGenerationNUmber(Integer current)
    {
        generationNumber.setValue(current);
    }

    public void updateBestFitness(Double fitness)
    {
        bestFitness.setValue(fitness);

    }

    public void updateTime(Long seconds)
    {
        timeSeconds.setValue(seconds);
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        String timeString = timeOfDay.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
         time.setValue(timeString);
    }
    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    public void setConditionPairs(ConditionPairs _conditionPairs) {

        this.conditionPairs= _conditionPairs;

        gridEndCondtions.getChildren().removeAll(progressBarFitness,progressBarNumberOfGenerations,progressBarTime);
        fitnessCapacity.setText("");
        generationCapacity.setText("");
        timeCapacity.setText("");
        createProgressBars();
    }

    private void createProgressBars()
    {
        double prefHeightPrograssBar= 250;
        //GridPane gridPane= new GridPane();
        if(conditionPairs.fitness!=null) {
            progressBarFitness = new ProgressBar();
            gridEndCondtions.add(progressBarFitness,3,1);
            progressBarFitness.setPrefWidth(prefHeightPrograssBar);
            progressBarFitness.setMinWidth(Region.USE_PREF_SIZE);
            progressBarFitness.progressProperty().bind(bestFitness.divide(conditionPairs.fitness));
            fitnessCapacity.setText("/ " + String.format("%.3f", conditionPairs.fitness));
        }

        if(conditionPairs.numberOfGeneration!=null) {
            progressBarNumberOfGenerations = new ProgressBar();
            gridEndCondtions.add(progressBarNumberOfGenerations,3,0);
            progressBarNumberOfGenerations.setPrefWidth(prefHeightPrograssBar);
            progressBarNumberOfGenerations.setMinWidth(Region.USE_PREF_SIZE);
            progressBarNumberOfGenerations.progressProperty().bind(generationNumber.divide(conditionPairs.numberOfGeneration*1.0));
            generationCapacity.setText("/ " + conditionPairs.numberOfGeneration.toString());
        }

        if(conditionPairs.timeSeconds!=null) {
            progressBarTime = new ProgressBar();
            gridEndCondtions.add(progressBarTime,3,2);
            progressBarTime.setPrefWidth(prefHeightPrograssBar);
            progressBarTime.setMinWidth(Region.USE_PREF_SIZE);
            progressBarTime.progressProperty().bind(timeSeconds.divide(conditionPairs.timeSeconds*1.0));
            LocalTime timeOfDay = LocalTime.ofSecondOfDay(conditionPairs.timeSeconds);
            String timeString = timeOfDay.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            timeCapacity.setText("/ " + timeString);
        }


    }

    public void initializeSizes()
    {
        initializeViewAlgorithmWidth();
        mainController.getScene().widthProperty().addListener(SPWidthSet);
    }

    ChangeListener<Number> SPWidthSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            initializeViewAlgorithmWidth();
        }
    };

    public void initializeViewAlgorithmWidth()
    {
        SPViewAlgorithm.setPrefWidth(mainController.getScene().getWidth());
        gridEndCondtions.setPrefWidth(mainController.getScene().getWidth() - 5);

        double barWidth;

        if(mainController.getSkinsButton().getText().equals("Ugly"))
        {
            barWidth = SPViewAlgorithm.getPrefWidth() - 290;
        }
        else
        {
            barWidth = SPViewAlgorithm.getPrefWidth() - 220;
        }



        if(progressBarFitness != null)
        {
            progressBarFitness.setPrefWidth(barWidth);
        }

        if(progressBarNumberOfGenerations != null)
        {
            progressBarNumberOfGenerations.setPrefWidth(barWidth);
        }

        if(progressBarTime != null)
        {
            progressBarTime.setPrefWidth(barWidth);
        }
    }
}
