import Evolution.EndCondition.ByFitness;
import Evolution.EndCondition.ByTime;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class ChooseEndConditionsController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label LBChooseEndConditions;
    @FXML
    private CheckBox CBByFitness;
    @FXML
    private CheckBox CBByTime;
    @FXML
    private CheckBox CBByGenerationNumber;
    @FXML
    private TextField TFByFitness;
    @FXML
    private TextField TFByGenerationNumber;
    @FXML
    private TextField TFByTime;
    @FXML
    private TextField TFShowEveryGeneration;
    @FXML
    private Button BTNsetConditions;


    private BooleanProperty isFitnessGood;
    private BooleanProperty isFitnessParamGood;
    private BooleanProperty isGenerationNumberGood;
    private BooleanProperty isGenerationNumberParameterGood;
    private BooleanProperty isTimeGood;
    private BooleanProperty isTimeParameterGood;
    private BooleanProperty isEveryGenerationGood;
    private BooleanBinding isSetActive;

    private MainController mainController;
    private SchoolHourManager manager;



    public ChooseEndConditionsController() {
        isFitnessGood=new SimpleBooleanProperty(false);
        isFitnessParamGood=new SimpleBooleanProperty(false);

        isGenerationNumberGood=new SimpleBooleanProperty(false);
        isGenerationNumberParameterGood=new SimpleBooleanProperty(false);

        isTimeGood=new SimpleBooleanProperty(false);
        isTimeParameterGood=new SimpleBooleanProperty(false);

        isEveryGenerationGood=new SimpleBooleanProperty(false);
        }

    private void ByFitnessInit(){
        TFByFitness.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    Double fitness= Double.parseDouble(newValue);
                    isFitnessParamGood.setValue((fitness>0&&fitness<=100)?true:false);
                    TFByFitness.setStyle((fitness>0&&fitness<=100)?"-fx-text-inner-color: green;":"-fx-text-inner-color: red;");
                }catch (NumberFormatException e){
                    isFitnessParamGood.setValue(false);
                    TFByFitness.setStyle("-fx-text-inner-color: red;");
                }
            }
        });
        isFitnessGood.bind(Bindings.or(isFitnessParamGood,CBByFitness.selectedProperty().not()));
    }
    private void ByGenerationNumberInit(){
        TFByGenerationNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    Integer generationNumber= Integer.parseInt(newValue);
                    isGenerationNumberParameterGood.setValue((generationNumber>=100)?true:false);
                    TFByGenerationNumber.setStyle((generationNumber>=100)?"-fx-text-inner-color: green;":"-fx-text-inner-color: red;");
                }catch (NumberFormatException e){
                    isGenerationNumberParameterGood.setValue(false);
                    TFByGenerationNumber.setStyle("-fx-text-inner-color: red;");
                }
            }
        });
        isGenerationNumberGood.bind(Bindings.or(isGenerationNumberParameterGood,CBByGenerationNumber.selectedProperty().not()));

    }
    private void ByTimeInit(){
        TFByTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    Integer time= Integer.parseInt(newValue);
                    isTimeParameterGood.setValue((time>0)?true:false);
                    TFByTime.setStyle((time>0)?"-fx-text-inner-color: green;":"-fx-text-inner-color: red;");
                }catch (NumberFormatException e){
                    isTimeParameterGood.setValue(false);
                    TFByTime.setStyle("-fx-text-inner-color: red;");
                }
            }
        });
        isTimeGood.bind(Bindings.or(isTimeParameterGood,CBByTime.selectedProperty().not()));
    }
    private void ShowEveryGenerationInit(){
        TFShowEveryGeneration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    Integer everyGen= Integer.parseInt(newValue);
                    isEveryGenerationGood.setValue((everyGen>0)?true:false);
                    TFShowEveryGeneration.setStyle((everyGen>0)?"-fx-text-inner-color: green;":"-fx-text-inner-color: red;");
                }catch (NumberFormatException e){
                    isEveryGenerationGood.setValue(false);
                    TFShowEveryGeneration.setStyle("-fx-text-inner-color: red;");
                }
            }
        });
    }


    @FXML
    private void initialize()
    {
        TFByFitness.disableProperty().bind(CBByFitness.selectedProperty().not());
        TFByGenerationNumber.disableProperty().bind(CBByGenerationNumber.selectedProperty().not());
        TFByTime.disableProperty().bind(CBByTime.selectedProperty().not());

        ShowEveryGenerationInit();
        ByFitnessInit();
        ByGenerationNumberInit();
        ByTimeInit();

        isSetActive=isEveryGenerationGood.and(isFitnessGood.and(isGenerationNumberGood.and(isTimeGood).and(CBByFitness.selectedProperty().or(CBByGenerationNumber.selectedProperty().or(CBByTime.selectedProperty())))));
        BTNsetConditions.disableProperty().bind(isSetActive.not());
    }

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    @FXML
    private void setConditions()
    {
        List<EndCondition> endConditions = new ArrayList<>();
        ConditionPairs conditionPairs=new ConditionPairs();
        if(CBByFitness.isSelected())
        {

            endConditions.add(new ByFitness(Double.parseDouble(TFByFitness.getText())));
            conditionPairs.setFitness(Double.parseDouble(TFByFitness.getText()));
        }
        if(CBByGenerationNumber.isSelected())
        {
            endConditions.add(new NumberOfGenerations(Integer.parseInt(TFByGenerationNumber.getText())));
            conditionPairs.setNumberOfGeneration(Integer.parseInt(TFByGenerationNumber.getText()));
        }
        if(CBByTime.isSelected())
        {
            endConditions.add(new ByTime(Integer.parseInt(TFByTime.getText())));
            conditionPairs.setTimeSeconds(Integer.parseInt(TFByTime.getText())*60);

        }

        int printEveryThisNumberOfGenerations = Integer.parseInt(TFShowEveryGeneration.getText());

        mainController.setAlgorithmParameters(endConditions, printEveryThisNumberOfGenerations,conditionPairs);
    }
}
