import DTO.DTOSchoolHoursData;
import DTO.DTOTuple;
import DTO.DTOTupleGroupWithFitnessDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private XYChart.Series dataBestSeries;
    private XYChart.Series dataCurrentSeries;
    //AreaChart chartFitness;
    private LineChart<Double, Integer> chartFitness;

    private MainController mainController;
    private SchoolHourManager manager;

    private int numberOfTeachers;
    private int numberOfClassrooms;

    private int currentTeacherID;
    private int currentClassroomID;

    private DTOTupleGroupWithFitnessDetails bestSolution;

    private GridPane GPTeacher;
    private GridPane GPClassroom;
    private Object currentTeacherSolutionView;
    private Object currentClassroomSolutionView;
    private int cellWidth = 50;
    private int cellHeight = 50;

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
        SPRaw.setContent(null);
        setupTeacherTab(data);
        setupClassroomTab(data);
        SPTeacher.setContent(GPTeacher);
        SPClassroom.setContent(GPClassroom);
    }

    private void setupTeacherTab(DTOSchoolHoursData data)
    {
        GPTeacher = new GridPane();
        MenuButton chooseID = new MenuButton();
        chooseID.setText("Choose ID");
        for (int i = 1; i <= data.getTeachers().size(); i++) {
            String string = "  " + i + "  ";
            MenuItem menuItem = new MenuItem(string);
            int finalI = i;
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooseID.setText(menuItem.getText());
                    currentTeacherID = finalI;
                    presentTeacherBestSolution();
                }
            });

            chooseID.getItems().add(menuItem);
        }

        GPTeacher.add(chooseID,0,0);
    }

    private void setupClassroomTab(DTOSchoolHoursData data)
    {
        GPClassroom = new GridPane();
        MenuButton chooseID = new MenuButton();
        chooseID.setText("Choose ID");
        for (int i = 1; i <= data.getClassrooms().size(); i++) {
            String string = "  " + i + "  ";
            MenuItem menuItem = new MenuItem(string);
            int finalI = i;
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooseID.setText(menuItem.getText());
                    currentClassroomID = finalI;
                    presentClassroomBestSolution();
                }
            });

            chooseID.getItems().add(menuItem);
        }

        GPClassroom.add(chooseID,0,0);
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

    private void presentTeacherBestSolution()
    {
        GridPane gridPane = createTeacherTable(true);
        if(currentTeacherSolutionView != null)
        {
            GPTeacher.getChildren().remove(1);
        }
        currentTeacherSolutionView = gridPane;
        GPTeacher.add(gridPane,0,1);
    }

    private GridPane createTeacherTable(boolean isTeacherTable)
    {
        GridPane gridPane = new GridPane();
        gridPane.gridLinesVisibleProperty().setValue(true);
        Label label = new Label();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("DAY").append(System.lineSeparator()).append("----------").append(System.lineSeparator()).append("HOUR");
        label.setText(stringBuilder.toString());
        gridPane.add(label,0,0);
        label.setMinSize(cellWidth,cellHeight);

        for (int i = 1; i <= bestSolution.getSchoolHoursData().getNumberOfDays(); i++)
        {
            label = new Label();
            stringBuilder  = new StringBuilder();
            stringBuilder.append(System.lineSeparator()).append("       "+ i+ "      ").append(System.lineSeparator());
            label.setText(stringBuilder.toString());
            gridPane.add(label, i, 0);
        }

        for (int i = 1; i <= bestSolution.getSchoolHoursData().getNumberOfHoursInADay(); i++)
        {
            label = new Label();
            stringBuilder  = new StringBuilder();
            stringBuilder.append(System.lineSeparator()).append("       "+ i+ "      ").append(System.lineSeparator());
            label.setText(stringBuilder.toString());
            gridPane.add(label, 0, i);
        }

        List<DTOTuple> dtoTuples;

        if(isTeacherTable)
        {
            System.out.println(currentTeacherID);
            dtoTuples = bestSolution.getDtoTuples().stream().filter(dtoTuple -> dtoTuple.getTeacher().getNameId() == currentTeacherID).collect(Collectors.toList());
        }
        else
        {
            System.out.println(currentClassroomID);
            dtoTuples = bestSolution.getDtoTuples().stream().filter(dtoTuple -> dtoTuple.getClassroom().getId() == currentClassroomID).collect(Collectors.toList());
        }

        for(int i = 1; i <= bestSolution.getSchoolHoursData().getNumberOfDays(); i++)
        {
            for (int j = 1; j <= bestSolution.getSchoolHoursData().getNumberOfHoursInADay(); j++)
            {
                VBox vBox = new VBox();
                int finalI = i;
                int finalJ = j;

                 List<DTOTuple> cellDTOTuples = dtoTuples.stream().
                        filter(dtoTuple -> dtoTuple.getDay() == finalI).
                        filter(dtoTuple -> dtoTuple.getHour() == finalJ).
                        collect(Collectors.toList());

                for(int k = 0; k < cellDTOTuples.size(); k++)
                {
                    DTOTuple dtoTuple = cellDTOTuples.get(k);
                    label = new Label();
                    stringBuilder  = new StringBuilder();
                    stringBuilder.append("    ");
                    if(isTeacherTable)
                    {
                        stringBuilder.append(dtoTuple.getClassroom().getId());
                    }
                    else
                    {
                        stringBuilder.append(dtoTuple.getTeacher().getNameId());
                    }

                    stringBuilder.append(",").append(dtoTuple.getSubject().getId());
                    label.setText(stringBuilder.toString());
                    vBox.getChildren().add(label);
                }

                gridPane.add(vBox, i, j);
            }
        }

        return gridPane;
    }

    private void presentClassroomBestSolution()
    {
        GridPane gridPane = createTeacherTable(false);
        if(currentClassroomSolutionView != null)
        {
            GPClassroom.getChildren().remove(1);
        }
        currentClassroomSolutionView = gridPane;
        GPClassroom.add(gridPane,0,1);
    }
    public void addfitnesstochart(double fitness,int generation)
    {

        dataCurrentSeries.getData().add(new XYChart.Data( generation, fitness));
        dataBestSeries.getData().add(new XYChart.Data( generation,bestSolution.getFitness()));
    }
    public void createDiagram()
    {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("generations");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("fitness");

        chartFitness = new LineChart(xAxis, yAxis);
        SPDiagram.setContent(chartFitness);
        chartFitness.prefWidthProperty().bind(SPDiagram.widthProperty());
        dataBestSeries = new XYChart.Series();
        dataBestSeries.setName("best");

        dataCurrentSeries = new XYChart.Series();
        dataCurrentSeries.setName("current");

        yAxis.setForceZeroInRange(false);

        chartFitness.getData().add(dataBestSeries);
        chartFitness.getData().add(dataCurrentSeries);
    }
    public void resetDiagram() {



    }
}
