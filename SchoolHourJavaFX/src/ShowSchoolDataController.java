import DTO.DTOSchoolHoursData;
import DTO.DTOSubject;
import Evolution.MySolution.Subject;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowSchoolDataController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab TabSubjects;
    @FXML
    private ScrollPane SPSubjects;
    @FXML
    private Tab TabTeachers;
    @FXML
    private Tab TabClassrooms;

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
        updateSubjects();
        //
        //
    }

    private void updateSubjects() {
        DTOSchoolHoursData dtoSchoolHoursData = manager.getDataAndAlgorithmSettings().getDtoSchoolHoursData();
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DTOSubject, Integer> subjectsIDColumn = new TableColumn<>("Subject ID");
        subjectsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<DTOSubject, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().add(subjectsIDColumn);
        tableView.getColumns().add(nameColumn);

        dtoSchoolHoursData.getSubjects().values().forEach(dtoSubject -> tableView.getItems().add(dtoSubject));

       // tableView.setFixedCellSize(26);
       // tableView.setPrefHeight(tableView.getFixedCellSize() * (dtoSchoolHoursData.getSubjects().values().size() + 1));

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(26));

        tableView.setPrefWidth(SPSubjects.getPrefWidth());

        SPSubjects.setContent(tableView);
        System.out.println(tableView.getSelectionModel().getSelectedIndices().toArray().length);

    }
}

