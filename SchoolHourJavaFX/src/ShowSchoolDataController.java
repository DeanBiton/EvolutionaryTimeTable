import DTO.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShowSchoolDataController {

    @FXML
    private Scene scene;
    @FXML
    private TabPane tabPane;

    // Subjects
    @FXML
    private Tab TabSubjects;
    @FXML
    private ScrollPane SPSubjects;
    @FXML
    private TableView TVSubjects;

    // Teachers
    @FXML
    private Tab TabTeachers;
    @FXML
    private ScrollPane SPTeachers;
    @FXML
    private GridPane GPTeachers;

    // Classroom
    @FXML
    private Tab TabClassrooms;
    @FXML
    private ScrollPane SPClassrooms;

    // Rules
    @FXML
    private Tab TabRules;
    @FXML
    private ScrollPane SPRules;
    @FXML
    private GridPane GPRules;
    @FXML
    private TableView TVRules;

    private double TVSubjectsHeight;

    private MainController mainController;
    private SchoolHourManager manager;

    @FXML
    private void initialize()
    {
        initTabAnimations();
    }

    private void initTabAnimations()
    {
        TabClassrooms.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if(isSelected)
                mainController.doFade(TabClassrooms.getContent());
        });

        TabRules.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if(isSelected)
                mainController.doFade(TabRules.getContent());
        });

        TabSubjects.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if(isSelected)
                mainController.doFade(TabSubjects.getContent());
        });

        TabTeachers.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if(isSelected)
                mainController.doFade(TabTeachers.getContent());
        });
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    public void setScene()
    {
        scene = mainController.getScene();
    }

    public void resetScene()
    {
        setScene();
        DTOSchoolHoursData dtoSchoolHoursData = manager.getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        updateSubjects(dtoSchoolHoursData);
        updateTeachers(dtoSchoolHoursData);
        updateClassrooms(dtoSchoolHoursData);
        updateRules(dtoSchoolHoursData);
        initializeSizes();
    }

    // Subjects
    private void updateSubjects(DTOSchoolHoursData dtoSchoolHoursData) {
        TVSubjects = getSubjectsTable(dtoSchoolHoursData.getSubjects().values());
        AnchorPane.setLeftAnchor(TVSubjects,0.0);
        AnchorPane.setTopAnchor(TVSubjects,0.0);
        TVSubjectsHeight = TVSubjects.getPrefHeight();
        SPSubjects.setContent(TVSubjects);
    }

    private TableView getSubjectsTable(Collection<DTOSubject> dtoSubjects)
    {
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DTOSubject, Integer> subjectsIDColumn = new TableColumn<>("Subject ID");
        subjectsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<DTOSubject, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().add(subjectsIDColumn);
        tableView.getColumns().add(nameColumn);

        dtoSubjects.forEach(dtoSubject -> tableView.getItems().add(dtoSubject));

        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(40));

        tableView.setMaxHeight(Region.USE_PREF_SIZE);
        return tableView;
    }

    // Teachers
    private void updateTeachers(DTOSchoolHoursData dtoSchoolHoursData)
    {
        GPTeachers = new GridPane();

        GPTeachers.add(new Label(), 0, 0);
        final int[] teacherNumber = {1};
        dtoSchoolHoursData.getTeachers().values().forEach(dtoTeacher ->
        {
            Label label = new Label();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Teacher ID: ");
            stringBuilder.append(dtoTeacher.getNameId());
            stringBuilder.append("  |  Teacher Name: ");
            stringBuilder.append(dtoTeacher.getName());
            label.setText(stringBuilder.toString());
            GPTeachers.add(label, 0, teacherNumber[0]);
            TableView tableView = getTeacherSubjectsTable(dtoTeacher, dtoSchoolHoursData);

            GPTeachers.add(tableView, 0, teacherNumber[0] + 1);
            GPTeachers.add(new Label(), 0, teacherNumber[0] + 2);

            teacherNumber[0] += 3;
        });

        AnchorPane.setTopAnchor(GPTeachers,0.0);
        SPTeachers.setContent(GPTeachers);
    }

    private TableView getTeacherSubjectsTable(DTOTeacher dtoTeacher, DTOSchoolHoursData dtoSchoolHoursData) {
        List<DTOSubject> dtoSubjects = new ArrayList<>();

        dtoTeacher.getAllSubjectsTeaching().forEach(subjectID ->
        {
            dtoSubjects.add(dtoSchoolHoursData.getSubjects().get(subjectID));
        });

        return getSubjectsTable(dtoSubjects);
    }

    // Classrooms
    private void updateClassrooms(DTOSchoolHoursData dtoSchoolHoursData)
    {
        GridPane gridPane = new GridPane();

        gridPane.add(new Label(), 0, 0);
        final int[] classroomNumber = {1};
        dtoSchoolHoursData.getClassrooms().values().forEach(dtoClassroom ->
        {
            Label label = new Label();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Classroom ID: ");
            stringBuilder.append(dtoClassroom.getId());
            stringBuilder.append("  |  Classroom Name: ");
            stringBuilder.append(dtoClassroom.getName());
            label.setText(stringBuilder.toString());
            gridPane.add(label, 0, classroomNumber[0]);
            TableView tableView = getClassroomSubjectsTable(dtoClassroom, dtoSchoolHoursData);
            resizeColumnWidth(tableView, 0, 1.2);
            resizeColumnWidth(tableView, 1, 1.2);
            resizeColumnWidth(tableView, 2, 1.8);
            gridPane.add(tableView, 0, classroomNumber[0] + 1);
            gridPane.add(new Label(), 0, classroomNumber[0] + 2);

            classroomNumber[0] += 3;
        });

        AnchorPane.setTopAnchor(gridPane,0.0);
        AnchorPane.setLeftAnchor(gridPane,0.0);
        SPClassrooms.setContent(gridPane);
    }

    public class SubjectWithHoursDemanded
    {
        private Integer id;
        private String name;
        private Integer hoursDemanded;

        public SubjectWithHoursDemanded(DTOSubject subject, Integer hoursDemanded) {
            this.hoursDemanded = hoursDemanded;
            this.id = subject.getId();
            this.name = subject.getName();
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getHoursDemanded() {
            return hoursDemanded;
        }
    }

    private TableView getClassroomSubjectsTable(DTOClassroom dtoClassroom, DTOSchoolHoursData dtoSchoolHoursData) {
        List<DTOSubject> dtoSubjects = new ArrayList<>();

        dtoClassroom.getSubjectId2WeeklyHours().keySet().forEach(subjectID ->
        {
            dtoSubjects.add(dtoSchoolHoursData.getSubjects().get(subjectID));
        });

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SubjectWithHoursDemanded, Integer> subjectsIDColumn = new TableColumn<>("Subject ID");
        subjectsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<SubjectWithHoursDemanded, String> nameColumn = new TableColumn<>("Name         ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SubjectWithHoursDemanded, Integer> subjectsHoursColumn = new TableColumn<>("Hours demanded");
        subjectsHoursColumn.setCellValueFactory(new PropertyValueFactory<>("hoursDemanded"));

        tableView.getColumns().add(subjectsIDColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(subjectsHoursColumn);

        dtoSubjects.forEach(dtoSubject -> tableView.getItems().add(new SubjectWithHoursDemanded(dtoSubject, dtoClassroom.getSubjectId2WeeklyHours().get(dtoSubject.getId()))));
        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(40));

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        return tableView;
    }

    // Rules
    private void updateRules(DTOSchoolHoursData dtoSchoolHoursData)
    {
        GPRules = new GridPane();

        Label label = new Label();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Hard rules weight = " + dtoSchoolHoursData.getHardRulesWeight());
        stringBuilder.append(System.lineSeparator());
        label.setText(stringBuilder.toString());
        GPRules.add(label,0,0);

        TVRules = getRulesTable(dtoSchoolHoursData);
        resizeColumnWidth(TVRules,0,  3.2);
        GPRules.add(TVRules,0,1);

        SPRules.setContent(GPRules);
    }

    private TableView getRulesTable(DTOSchoolHoursData dtoSchoolHoursData)
    {
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DTORule, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DTORule, Integer> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("implementationLevel"));

        TableColumn<DTORule, Integer> etcColumn = new TableColumn<>("Etc.");
        etcColumn.setCellValueFactory(new PropertyValueFactory<>("etc"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(typeColumn);
        tableView.getColumns().add(etcColumn);

        dtoSchoolHoursData.getRules().forEach(dtoRule -> tableView.getItems().add(dtoRule));

        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(40));

        return tableView;
    }

    // sizes
    public static void resizeColumnWidth( TableView<?> table ,int columnNumber, double sizeMultiplayer)
    {
        table.getColumns().get(columnNumber).setPrefWidth(table.getColumns().get(columnNumber).getPrefWidth() * sizeMultiplayer);
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
        tabPane.setPrefWidth(mainController.getCenterPrefWidth());
        tabPane.setPrefHeight(mainController.getCenterPrefHeight());
        scene.heightProperty().addListener(TPHeightSet);
        scene.widthProperty().addListener(TPWidthSet);
    }
}

