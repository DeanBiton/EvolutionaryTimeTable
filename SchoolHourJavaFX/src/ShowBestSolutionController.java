import DTO.DTOTupleGroupWithFitnessDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.sql.SQLOutput;

public class ShowBestSolutionController {
    @FXML
    private Scene scene;

    @FXML
    private BorderPane BPBestSolution;

    @FXML
    private ScrollPane SPTuples;
    @FXML
    private TabPane TPTuples;

    @FXML
    private ScrollPane SPRules;

    private MainController mainController;
    private SchoolHourManager manager;

    private double SPRulesHeightPercentage = 40.0; // number between 0 and 100

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    public void initializeSizes()
    {
        scene = mainController.getScene();
        //SPTuples.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //SPTuples.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //SPRules.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //SPRules.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        BPBestSolution.setPrefWidth(mainController.getCenterPrefWidth());
        SPTuples.setPrefWidth(mainController.getCenterPrefWidth());
        SPRules.setPrefWidth(mainController.getCenterPrefWidth());

        BPBestSolution.setPrefHeight(mainController.getCenterPrefHeight());
        SPRules.setPrefHeight(BPBestSolution.getPrefHeight() * SPRulesHeightPercentage / 100);
        SPTuples.setPrefHeight(BPBestSolution.getPrefHeight() - SPRules.getPrefHeight());

        scene.heightProperty().addListener(HeightSet);
        scene.widthProperty().addListener(WidthSet);

        BorderPane.setAlignment(SPTuples, Pos.TOP_LEFT);
        BorderPane.setAlignment(SPRules, Pos.TOP_LEFT);
    }

    ChangeListener<Number> WidthSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            BPBestSolution.setPrefWidth(mainController.getCenterPrefWidth());
            SPTuples.setPrefWidth(mainController.getCenterPrefWidth());
            SPRules.setPrefWidth(mainController.getCenterPrefWidth());
        }
    };

    ChangeListener<Number> HeightSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            BPBestSolution.setPrefHeight(mainController.getCenterPrefHeight());
            SPRules.setPrefHeight(BPBestSolution.getPrefHeight() * SPRulesHeightPercentage / 100);
            SPTuples.setPrefHeight(BPBestSolution.getPrefHeight() - SPRules.getPrefHeight());
        }
    };

    public void presentBestSolution(DTOTupleGroupWithFitnessDetails dtoTupleGroupWithFitnessDetails)
    {

    }
}
