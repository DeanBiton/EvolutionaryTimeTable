import DTO.DTOClassroom;
import DTO.DTOSchoolHoursData;
import DTO.DTOSubject;
import DTO.DTOTeacher;
import Evolution.MySolution.Subject;
import Evolution.MySolution.Teacher;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShowSchoolDataController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab TabSubjects;
    @FXML
    private Tab TabTeachers;
    @FXML
    private Tab TabClassrooms;
    @FXML
    private ScrollPane SPSubjects;
    @FXML
    private ScrollPane SPTeachers;
    @FXML
    private ScrollPane SPClassrooms;

    private MainController mainController;
    private SchoolHourManager manager;

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    public void resetScene()
    {
        DTOSchoolHoursData dtoSchoolHoursData = manager.getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        updateSubjects(dtoSchoolHoursData);
        updateTeachers(dtoSchoolHoursData);
        updateClassrooms(dtoSchoolHoursData);
    }

    private void updateSubjects(DTOSchoolHoursData dtoSchoolHoursData) {
        TableView tableView = getSubjectsTable(dtoSchoolHoursData.getSubjects().values());

        SPSubjects.setContent(tableView);
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

        return tableView;
    }

    private void updateTeachers(DTOSchoolHoursData dtoSchoolHoursData)
    {
        GridPane gridPane = new GridPane();

        gridPane.add(new Label(), 0, 0);
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
            gridPane.add(label, 0, teacherNumber[0]);
            TableView tableView = getTeacherSubjectsTable(dtoTeacher, dtoSchoolHoursData);

            gridPane.add(tableView, 0, teacherNumber[0] + 1);
            gridPane.add(new Label(), 0, teacherNumber[0] + 2);

            teacherNumber[0] += 3;
        });

        SPTeachers.setContent(gridPane);
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
}

