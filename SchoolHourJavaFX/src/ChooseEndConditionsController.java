import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.NumberOfGenerations;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ChooseEndConditionsController {

    @FXML
    private ScrollPane scrollPane;
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

    private MainController mainController;
    private SchoolHourManager manager;

    @FXML
    private void initialize()
    {

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
        /*input logic here*/

        List<EndCondition> endConditions = new ArrayList<>();
        int printEveryThisNumberOfGenerations = 10;
        endConditions.add(new NumberOfGenerations(1000));
        mainController.setAlgorithmParameters(endConditions, printEveryThisNumberOfGenerations);
    }
}
