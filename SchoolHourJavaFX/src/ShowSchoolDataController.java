import DTO.*;
import Evolution.MySolution.Subject;
import Evolution.MySolution.Teacher;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.util.Callback;
import sun.util.resources.cldr.wae.CalendarData_wae_CH;

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

    @FXML
    private double TVSubjectsHeight;
    private double TVSubjectsWidth;


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

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));
        //TVSubjectsWidth = subjectsIDColumn.getWidth() + nameColumn.getWidth();
        //tableView.setMinWidth(TVSubjectsWidth);
        return tableView;
    }

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

        TableColumn<SubjectWithHoursDemanded, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SubjectWithHoursDemanded, Integer> subjectsHoursColumn = new TableColumn<>("Hours demanded");
        subjectsHoursColumn.setCellValueFactory(new PropertyValueFactory<>("hoursDemanded"));

        tableView.getColumns().add(subjectsIDColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(subjectsHoursColumn);

        dtoSubjects.forEach(dtoSubject -> tableView.getItems().add(new SubjectWithHoursDemanded(dtoSubject, dtoClassroom.getSubjectId2WeeklyHours().get(dtoSubject.getId()))));
        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        return tableView;
    }

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
        resizeColumnsWidth(TVRules);
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

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));
        //TVSubjectsWidth = subjectsIDColumn.getWidth() + nameColumn.getWidth();
        //tableView.setMinWidth(TVSubjectsWidth);

        return tableView;
    }

    public static void resizeColumnsWidth( TableView<?> table )
    {
        table.getColumns().get(0).setPrefWidth(table.getColumns().get(0).getPrefWidth() * 2);
    }

    public static void autoResizeColumns( TableView<?> table )
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 10.0d );
        } );
    }

    ChangeListener<Number> TVSubjectsWidthSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            //if(newValue.doubleValue() >= TVSubjectsWidth + mainController.getSPMenu().getWidth())
            SPSubjects.setPrefWidth(mainController.getCenterPrefWidth());
            if(mainController.getCenterPrefWidth() >= TVSubjectsWidth)
            {
                TVSubjects.setPrefWidth(mainController.getCenterPrefWidth());
            }
            /*else
            {
                TVSubjects.setPrefWidth(TVSubjectsWidth);
            }
                System.out.println("scene width: " + newValue.doubleValue());
                System.out.println("menu width: " + mainController.getSPMenu().getWidth());
                System.out.println("TVSubjectsWidthPref: " + TVSubjectsWidth);
            System.out.println("TVSubjectsRealWidth: " + TVSubjects.getWidth());*/
          //  }
        }
    };

    ChangeListener<Number> TVSubjectsHeightSet =  new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if(newValue.doubleValue() >= TVSubjectsHeight + 130)
            {
                SPSubjects.setPrefHeight(newValue.doubleValue());
            }
            else
            {
                SPSubjects.setPrefHeight(newValue.doubleValue() - 130);
            }
        }
    };

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
        //scene.heightProperty().addListener(TVSubjectsHeightSet);
        //scene.widthProperty().addListener(TVSubjectsWidthSet);
    }
}

