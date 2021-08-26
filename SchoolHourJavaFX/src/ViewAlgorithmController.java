import Evolution.EndCondition.EndCondition;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.time.LocalTime;
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
        fitnessValue.textProperty().bind(bestFitness.asString());
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
        //System.out.println(Duration.ofSeconds(seconds).getSeconds());
        timeSeconds.setValue(seconds);

        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
         time.setValue(timeOfDay.toString());

       // time.setValue((seconds/60)+":"+(seconds%60));
       // System.out.println(timeSeconds.getValue());
       // timeMinutes.setValue(Duration.ofSeconds(seconds).toMinutes());
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
            progressBarFitness.progressProperty().bind(bestFitness.divide(conditionPairs.fitness));
            //pane.getChildren().add(progressBarFitness);
        }

        if(conditionPairs.numberOfGeneration!=null) {
            progressBarNumberOfGenerations = new ProgressBar();
            gridEndCondtions.add(progressBarNumberOfGenerations,3,0);
            progressBarNumberOfGenerations.setPrefWidth(prefHeightPrograssBar);
            progressBarNumberOfGenerations.progressProperty().bind(generationNumber.divide(conditionPairs.numberOfGeneration*1.0));
        }

        if(conditionPairs.timeSeconds!=null) {
            progressBarTime = new ProgressBar();
            gridEndCondtions.add(progressBarTime,3,2);
            progressBarTime.setPrefWidth(prefHeightPrograssBar);
            progressBarTime.progressProperty().bind(timeSeconds.divide(conditionPairs.timeSeconds*1.0));
        }

    }

    public ScrollPane getSPViewAlgorithm() {
        return SPViewAlgorithm;
    }
}
