import DTO.DTOSchoolHoursData;
import DTO.DTOTuple;
import DTO.DTOTupleGroupWithFitnessDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ShowBestSolutionController {
    @FXML
    private Scene scene;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab TabRaw;
    @FXML
    private ScrollPane SPRaw;
    @FXML
    private Tab TabTeacher;
    @FXML
    private ScrollPane SPTeacher;
    @FXML
    private Tab TabClassroom;
    @FXML
    private ScrollPane SPClassroom;
    @FXML
    private Tab TabDiagram;
    @FXML
    private ScrollPane SPDiagram;

    private MainController mainController;
    private SchoolHourManager manager;

    private int numberOfTeachers;
    private int numberOfClassrooms;

    private int currentTeacherID;
    private int currentClassroomID;

    DTOTupleGroupWithFitnessDetails bestSolution;
    Object currentRawSolutionView;

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    ChangeListener<Number> TPWidthSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            tabPane.setPrefWidth(mainController.getCenterPrefWidth());
        }
    };

    ChangeListener<Number> TPHeightSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            tabPane.setPrefHeight(mainController.getCenterPrefHeight());
        }
    };

    public void initializeSizes() {
        scene = mainController.getScene();
        tabPane.setPrefWidth(mainController.getCenterPrefWidth());
        tabPane.setPrefHeight(mainController.getCenterPrefHeight());
        scene.heightProperty().addListener(TPHeightSet);
        scene.widthProperty().addListener(TPWidthSet);
        //scene.heightProperty().addListener(TVSubjectsHeightSet);
        //scene.widthProperty().addListener(TVSubjectsWidthSet);
    }

    public void resetScene()
    {
        DTOSchoolHoursData data = manager.getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        numberOfTeachers = data.getTeachers().size();
        numberOfClassrooms = data.getClassrooms().size();
    }

    public void setDTOTupleGroupWithFitnessDetails(DTOTupleGroupWithFitnessDetails dtoTupleGroupWithFitnessDetails)
    {
        this.bestSolution = dtoTupleGroupWithFitnessDetails;
        presentRawBestSolution();
    }

    private void presentRawBestSolution()
    {
       GridPane gridPane =  new GridPane();
       gridPane.add(getRawView(), 0, 0);
       SPRaw.setContent(gridPane);
    }

    private VBox getRawView()
    {
        VBox vBox = new VBox();
        vBox.fillWidthProperty().setValue(true);
        vBox.setPrefWidth(1000);
        bestSolution.getDtoTuples().sort((o1,o2)->{
            if(false==o1.getDay().equals(o2.getDay()))
                return o1.getDay()-o2.getDay();
            else if(false==o1.getHour().equals(o2.getHour()))
                return o1.getHour()-o2.getHour();
            else if(false==o1.getClassroom().equals(o2.getClassroom()))
                return o1.getClassroom().getId()-o2.getClassroom().getId();
            else if(false==o1.getTeacher().equals(o2.getTeacher()))
                return o1.getTeacher().getNameId()-o2.getTeacher().getNameId();
            else
                return 0;
        });

        bestSolution.getDtoTuples().forEach( dtoTuple ->{
                    TextField textField = new TextField();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<Day=");
                    stringBuilder.append(dtoTuple.getDay());
                    stringBuilder.append(", Hour=");
                    stringBuilder.append(dtoTuple.getHour());
                    stringBuilder.append(", Classroom ID: ");
                    stringBuilder.append(dtoTuple.getClassroom().getId());
                    stringBuilder.append(", Teacher ID: ");
                    stringBuilder.append(dtoTuple.getTeacher().getNameId());
                    stringBuilder.append(", Subject ID: ");
                    stringBuilder.append(dtoTuple.getSubject().getId());
                    stringBuilder.append(">");
                    textField.setText(stringBuilder.toString());
                    vBox.getChildren().add(textField);
                }
        );

        return vBox;
    }

    private void setupMBChooseID()
    {

    }

}
