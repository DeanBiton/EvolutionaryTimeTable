import DTO.DTORule;
import DTO.DTOSchoolHoursData;
import DTO.DTOTuple;
import DTO.DTOTupleGroupWithFitnessDetails;
import Evolution.Rule;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowBestSolutionController {

    @FXML
    private Scene scene;
    @FXML
    private TabPane tabPane;
    @FXML
    private ScrollPane SPRaw;
    @FXML
    private ScrollPane SPTeacher;
    @FXML
    private ScrollPane SPClassroom;
    @FXML
    private ScrollPane SPRules;
    @FXML
    private ScrollPane SPDiagram;
    private XYChart.Series dataBestSeries;
    private XYChart.Series dataCurrentSeries;
    private LineChart<Double, Integer> chartFitness;

    private MainController mainController;
    private SchoolHourManager manager;

    private int currentTeacherID;
    private int currentClassroomID;

    private DTOTupleGroupWithFitnessDetails bestSolution;

    private GridPane GPTeacher;
    private GridPane GPClassroom;
    private Object currentTeacherSolutionView;
    private Object currentClassroomSolutionView;
    private final int cellWidth = 160;
    private final int cellHeight = 50;
    private final int teacherClassroomTableVGap = 10;
    private final int teacherClassroomTablePadding = 10;

    public TabPane getTabPane() {
        return tabPane;
    }

    // first time created
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
    }

    // Every Load
    public void resetScene()
    {
        DTOSchoolHoursData data = manager.getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        SPRaw.setContent(null);
        setupTeacherTab(data);
        setupClassroomTab(data);
        SPTeacher.setContent(GPTeacher);
        SPClassroom.setContent(GPClassroom);
        currentTeacherID = 0;
        currentClassroomID = 0;
    }

    private void setupTeacherTab(DTOSchoolHoursData data)
    {
        GPTeacher = new GridPane();
        GridPane gridPane = new GridPane();
        Label label = new Label("Teacher:    ");
        MenuButton chooseID = getChooseIDMenuButton(data, true);
        GridPane cellExample = getCellExample(true);

        GPTeacher.setVgap(teacherClassroomTableVGap);
        GPTeacher.setPadding(new Insets(teacherClassroomTablePadding));
        gridPane.add(label,0,0);
        gridPane.add(chooseID,1,0);
        GPTeacher.add(gridPane,0,0);
        GPTeacher.add(cellExample, 0,1);
    }

    private void setupClassroomTab(DTOSchoolHoursData data)
    {
        GPClassroom = new GridPane();
        GridPane gridPane = new GridPane();
        Label label = new Label("Classroom:    ");
        MenuButton chooseID = getChooseIDMenuButton(data, false);
        GridPane cellExample = getCellExample(false);

        GPClassroom.setVgap(teacherClassroomTableVGap);
        GPClassroom.setPadding(new Insets(teacherClassroomTablePadding));
        gridPane.add(label,0,0);
        gridPane.add(chooseID,1,0);
        GPClassroom.add(gridPane,0,0);
        GPClassroom.add(cellExample, 0,1);
    }

    private MenuButton getChooseIDMenuButton(DTOSchoolHoursData data, boolean isTeacherTab)
    {
        MenuButton chooseID = new MenuButton();
        int chooseIDNumber;
        chooseID.setText("");

        if(isTeacherTab)
        {
            chooseIDNumber = data.getTeachers().size();
            setupTeacherChooseID(chooseID, chooseIDNumber, data);
        }
        else
        {
            chooseIDNumber = data.getClassrooms().size();
            setupClassroomChooseID(chooseID, chooseIDNumber, data);
        }

        return chooseID;
    }

    private void setupTeacherChooseID(MenuButton chooseID, int chooseIDNumber, DTOSchoolHoursData data)
    {
        for (int i = 1; i <= chooseIDNumber; i++) {
            String string = "  " + i + "  " + data.getTeachers().get(i).getName();
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
    }

    private void setupClassroomChooseID(MenuButton chooseID, int chooseIDNumber, DTOSchoolHoursData data)
    {
        for (int i = 1; i <= chooseIDNumber; i++) {
            String string = "  " + i + "  " + data.getClassrooms().get(i).getName();
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
    }

    private GridPane getCellExample(boolean isTeacherTab)
    {
        GridPane gridPane = new GridPane();
        gridPane.gridLinesVisibleProperty().setValue(true);
        Label label = new Label();

        label.setMinSize(cellWidth, cellHeight);
        if(isTeacherTab)
        {
            label.setText("< Classroom ID Classroom Name, Subject ID Subject Name >");
        }
        else
        {
            label.setText("< Teacher ID Teacher Name, Subject ID Subject Name >");
        }

        gridPane.add(label,0,0);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    // every new best solution
    public void setDTOTupleGroupWithFitnessDetails(DTOTupleGroupWithFitnessDetails dtoTupleGroupWithFitnessDetails)
    {
        this.bestSolution = dtoTupleGroupWithFitnessDetails;
        presentRawBestSolution();
        if(currentTeacherID != 0)
        {
            presentTeacherBestSolution();
        }

        if(currentClassroomID != 0)
        {
            presentClassroomBestSolution();
        }

        presentRulesBestSolution();
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
                    Label label = new Label();
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
                    label.setText(stringBuilder.toString());
                    vBox.getChildren().add(label);
                }
        );

        return vBox;
    }

    public class RuleView
    {
        private String name;
        private Rule.RuleImplementationLevel implementationLevel;
        private String etc;
        private Double score;

        public RuleView(DTORule dtoRule, double _score)
        {
            name = dtoRule.getName();
            implementationLevel = dtoRule.getImplementationLevel();
            etc = dtoRule.getEtc();
            score = (Math.floor(_score * 1000)) / 1000;
        }

        public String getName() {
            return name;
        }

        public Rule.RuleImplementationLevel getImplementationLevel() {
            return implementationLevel;
        }

        public String getEtc() {
            return etc;
        }

        public Double getScore() {
            return score;
        }
    }

    private void presentRulesBestSolution() {
        GridPane gridPane = new GridPane();

        gridPane.add(createRulesTable(),0,0);
        SPRules.setContent(gridPane);
    }

    private TableView createRulesTable()
    {
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RuleView, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<RuleView, Integer> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("implementationLevel"));

        TableColumn<RuleView, Integer> etcColumn = new TableColumn<>("Etc.");
        etcColumn.setCellValueFactory(new PropertyValueFactory<>("etc"));

        TableColumn<RuleView, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(etcColumn);
        tableView.getColumns().add(scoreColumn);

        List<RuleView> rulesView = new ArrayList<>();
        bestSolution.getRulesScores().forEach((dtoRule, score) -> rulesView.add(new RuleView(dtoRule, score)));
        rulesView.sort(new Comparator<RuleView>() {
            @Override
            public int compare(RuleView o1, RuleView o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        rulesView.forEach(ruleView -> tableView.getItems().add(ruleView));

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));
        resizeColumnsWidth(tableView);
        return tableView;
    }

    public static void resizeColumnsWidth( TableView<?> table )
    {
        table.getColumns().get(0).setPrefWidth(table.getColumns().get(0).getPrefWidth() * 2);
    }

    // every new best solution and new click on choose ID
    private void presentTeacherBestSolution()
    {
        GridPane gridPane = createTeacherOrClassroomTable(true);
        if(currentTeacherSolutionView != null)
        {
            GPTeacher.getChildren().remove(2);
        }
        currentTeacherSolutionView = gridPane;
        GPTeacher.add(gridPane,0,2);
    }

    private void presentClassroomBestSolution()
    {
        GridPane gridPane = createTeacherOrClassroomTable(false);
        if(currentClassroomSolutionView != null)
        {
            GPClassroom.getChildren().remove(2);
        }
        currentClassroomSolutionView = gridPane;
        GPClassroom.add(gridPane,0,2);
    }

    private GridPane createTeacherOrClassroomTable(boolean isTeacherTable)
    {
        GridPane gridPane = new GridPane();
        gridPane.gridLinesVisibleProperty().setValue(true);
        gridPane.getStyleClass().add("SolutionGrid");

        addHourDayCell(gridPane);
        addUpperRow(gridPane);
        addLeftCol(gridPane);
        addCells(gridPane, isTeacherTable);

        return gridPane;
    }

    private void addHourDayCell(GridPane gridPane)
    {
        Label label = new Label();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("DAY").append(System.lineSeparator()).append("----------").append(System.lineSeparator()).append("HOUR");

        label.setText(stringBuilder.toString());
        label.setAlignment(Pos.CENTER);
        gridPane.add(label,0,0);
        label.setMinSize(cellWidth,cellHeight);

      /*//INSERT LINE CODE HERE
        Line line = new Line();
        line.setStartX(label.getLayoutX());
        line.setStartY(label.getLayoutY());
        line.setEndX(label.getLayoutX());
        line.setEndY(label.getLayoutY() + cellHeight / 2);
        Group group = new Group(line);*/
    }

    private void addUpperRow(GridPane gridPane)
    {
        Label label;
        StringBuilder stringBuilder;

        for (int i = 1; i <= bestSolution.getSchoolHoursData().getNumberOfDays(); i++)
        {
            label = new Label();
            label.setMinSize(cellWidth,cellHeight);
            stringBuilder  = new StringBuilder();
            stringBuilder.append(System.lineSeparator()).append(i).append(System.lineSeparator());
            label.setText(stringBuilder.toString());
            label.setAlignment(Pos.CENTER);
            gridPane.add(label, i, 0);
        }
    }

    private void addLeftCol(GridPane gridPane)
    {
        Label label;
        StringBuilder stringBuilder;

        for (int i = 1; i <= bestSolution.getSchoolHoursData().getNumberOfHoursInADay(); i++)
        {
            label = new Label();
            label.setMinSize(cellWidth,cellHeight);
            stringBuilder  = new StringBuilder();
            stringBuilder.append(System.lineSeparator()).append(i).append(System.lineSeparator());
            label.setText(stringBuilder.toString());
            label.setAlignment(Pos.CENTER);
            gridPane.add(label, 0, i);
        }
    }

    private void addCells(GridPane gridPane, boolean isTeacherTable)
    {
        Label label;
        StringBuilder stringBuilder;
        List<DTOTuple> dtoTuples;

        if(isTeacherTable)
        {
            dtoTuples = bestSolution.getDtoTuples().stream().filter(dtoTuple -> dtoTuple.getTeacher().getNameId() == currentTeacherID).collect(Collectors.toList());
        }
        else
        {
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
                    if(isTeacherTable)
                    {
                        stringBuilder.append(dtoTuple.getClassroom().getId()).append(" ").append(dtoTuple.getClassroom().getName());
                    }
                    else
                    {
                        stringBuilder.append(dtoTuple.getTeacher().getNameId()).append(" ").append(dtoTuple.getTeacher().getName());
                    }

                    stringBuilder.append(", ").append(dtoTuple.getSubject().getId()).append(" ").append(dtoTuple.getSubject().getName());
                    label.setText(stringBuilder.toString());
                    vBox.getChildren().add(label);
                }

                vBox.setAlignment(Pos.CENTER);
                gridPane.add(vBox, i, j);
            }
        }

    }

    /// Diagram

    // activates every Run
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
}
