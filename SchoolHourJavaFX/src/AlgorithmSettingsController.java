import DTO.*;
import Evolution.MySolution.Crossover.AspectOriented;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.MySolution.Crossover.DayTimeOriented;
import Evolution.MySolution.MyMutation.Flipping;
import Evolution.MySolution.MyMutation.Mutation;
import Evolution.MySolution.MyMutation.Sizer;
import Evolution.Selection.RouletteWheel;
import Evolution.Selection.Selection;
import Evolution.Selection.Tournament;
import Evolution.Selection.Truncation;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmSettingsController {


    @FXML
    private GridPane MutationsGP;
    @FXML
    private MenuButton OrientationMenuBTN;

    @FXML
    private MenuItem ClassMenu;

    @FXML
    private MenuItem TeacherMenu;
    @FXML
    private Label crossoverParameter;
    @FXML
    private Spinner<Integer> separators;
    @FXML
    private MenuButton CrossoverMenuBTN;

    @FXML
    private MenuItem DayTimeOrientedMenu;

    @FXML
    private MenuItem AspectOrientedMenu;
    @FXML
    private Label selectionParameter;
    @FXML
    private Spinner<Integer> elitism;
    @FXML
    private Label selectionSliderLabel;
    @FXML
    private Slider selectionSlider;
    @FXML
    private MenuButton SelectionMenuBTN;

    @FXML
    private MenuItem TruncationMenu;

    @FXML
    private MenuItem RouletteMenu;

    @FXML
    private MenuItem TournamentMenu;

    @FXML
    private GridPane SettingsGridPane;

    private List<Slider> sizerProbability;
    private List<Spinner<Integer>> sizerTotalTuples;
    private List<Slider> flippingProbability;
    private List<Spinner<Integer>> flippingMaxTupples;
    private List<MenuButton> flippingComponnent;

    private MainController mainController;
    private SchoolHourManager manager;
    public AlgorithmSettingsController() {
    }

    public void setMainController(MainController _mainController)
    {
        mainController = _mainController;
    }

    public void setManager(SchoolHourManager _manager)
    {
        manager = _manager;
    }

    public void resetScene(DTOEvolutionaryAlgorithmSettings DTOeaData) {
        //selection
        for (MenuItem m : SelectionMenuBTN.getItems()) {
            if (m.getUserData() == DTOeaData.getDtoSelection().getName()) {
                {
                    selectionSlider.setValue(getSelectionExtraValue(DTOeaData.getDtoSelection()));
                    setSpinnerSelection(DTOeaData.getInitialPopulation());
                    elitism.getValueFactory().setValue(DTOeaData.getDtoSelection().getElitism());

                    m.fire();
                    break;
                }
            }
        }
        //crossover
        for (MenuItem m : CrossoverMenuBTN.getItems()) {
          //  System.out.println(DTOeaData.getDtoCrossover().getName());
            if (m.getUserData() == DTOeaData.getDtoCrossover().getName()) {
                {
                   // selectionSlider.setValue(getSelectionExtraValue(DTOeaData.getDtoSelection()));
                    setSpinnerCrossover(100);
                    separators.getValueFactory().setValue(DTOeaData.getDtoCrossover().getNumberOfSeparators());

                    // ((CheckMenuItem)OrientationMenuBTN.getItems().get(0)).setSelected(true);
                    m.fire();

                    if(m.getUserData()=="AspectOriented")
                    {
                        for(MenuItem o: OrientationMenuBTN.getItems())
                        {
                            //System.out.println(((AspectOriented)(DTOeaData.getDtoCrossover())).getOrientationType());
                            if(o.getUserData()==((AspectOriented)(DTOeaData.getDtoCrossover())).getOrientationType())
                            {
                                o.fire();
                            }
                        }
                    }
                    break;
                }
            }

        }

        initializeMutations(DTOeaData.getDtoMutations());
        focusTraversable();


    }
    private double getSelectionExtraValue(DTOSelection selection)
    {
        switch (selection.getName()) {
            case "Truncation":
                return ((DTOTruncation)selection).getTopPercent();
            case "Tournament":
                return ((DTOTournament)selection).getPte();
            default:
                return 0;

        }
    }


    private void focusTraversable()
    {
        SettingsGridPane.focusTraversableProperty().bind(SettingsGridPane.mouseTransparentProperty().not());
        SelectionMenuBTN.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        elitism.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        selectionSlider.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        CrossoverMenuBTN.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        separators.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        OrientationMenuBTN.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());

        MutationsGP.focusTraversableProperty().bind(SettingsGridPane.focusTraversableProperty());
        sizerProbability.forEach(sp->sp.focusTraversableProperty().bind(MutationsGP.focusTraversableProperty()));
        sizerTotalTuples.forEach(sp->sp.focusTraversableProperty().bind(MutationsGP.focusTraversableProperty()));
        flippingProbability.forEach(sp->sp.focusTraversableProperty().bind(MutationsGP.focusTraversableProperty()));
        flippingMaxTupples.forEach(sp->sp.focusTraversableProperty().bind(MutationsGP.focusTraversableProperty()));
        flippingComponnent.forEach(sp->sp.focusTraversableProperty().bind(MutationsGP.focusTraversableProperty()));
    }

    private void setSpinnerCrossover(int numOfSeparators)
    {
        SpinnerValueFactory<Integer> sv= new SpinnerValueFactory.IntegerSpinnerValueFactory(1,numOfSeparators);
        separators.setValueFactory(sv) ;
        TextFormatter<Integer> myTextFormatter= new TextFormatter<Integer>(sv.getConverter(),sv.getValue());
        separators.getEditor().setTextFormatter(myTextFormatter);
        sv.valueProperty().bindBidirectional(myTextFormatter.valueProperty());
    }
    private void setSpinnerSelection(int population)
    {
        SpinnerValueFactory<Integer> sv= new SpinnerValueFactory.IntegerSpinnerValueFactory(0,population);
        elitism.setValueFactory(sv) ;
        TextFormatter<Integer> myTextFormatter= new TextFormatter<Integer>(sv.getConverter(),sv.getValue());
        elitism.getEditor().setTextFormatter(myTextFormatter);
        sv.valueProperty().bindBidirectional(myTextFormatter.valueProperty());
    }

    private Spinner<Integer> createSpinner(int min,int max)
    {
        Spinner<Integer> spinner = new Spinner<>();


        SpinnerValueFactory<Integer> sv= new SpinnerValueFactory.IntegerSpinnerValueFactory(min,max);
        spinner.setValueFactory(sv) ;
        TextFormatter<Integer> myTextFormatter= new TextFormatter<Integer>(sv.getConverter(),sv.getValue());
        spinner.getEditor().setTextFormatter(myTextFormatter);
        sv.valueProperty().bindBidirectional(myTextFormatter.valueProperty());
        spinner.setPrefWidth(60);

        return spinner;
    }
    @FXML
    private void initialize()
    {
        initializeSelection();
        initializeCrossover();
    }

    private MenuButton createFlippingComponent(int numberforID)
    {
        MenuButton component= new MenuButton();
        component.setPrefWidth(45);
        //component.setMinWidth(Region.USE_COMPUTED_SIZE);
       // component.setMaxWidth(Region.USE_COMPUTED_SIZE);
        component.setId("FlippingComponent"+numberforID);
        List<Character> components = Arrays.asList('S', 'T','C','H','D');
        for(int i=0;i<components.size();i++)
        {
            MenuItem menuItem= new MenuItem(components.get(i).toString());
            menuItem.setId("Component"+components.get(i).toString()+numberforID);
            menuItem.setOnAction(event -> {component.setText(menuItem.getText());});
            component.getItems().add(menuItem);
        }
        return component;

    }

    private Slider createSlider(double min,double max)
    {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setMinWidth(200);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.5);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(0.1);

        return slider;
    }
    private void initializeMutations(DTOMutations mutations)
    {
        sizerProbability=new ArrayList<>(mutations.getSizers().size());
        sizerTotalTuples= new ArrayList<>(mutations.getSizers().size());
        flippingProbability=new ArrayList<>(mutations.getFlippings().size());
        flippingMaxTupples=new ArrayList<>(mutations.getFlippings().size());
        flippingComponnent=new ArrayList<>(mutations.getFlippings().size());
        int i=0;
        for (int nsizers=0;nsizers<mutations.getSizers().size();i++,nsizers++)
        {
            MutationsGP.add(new Label("Type: Sizer"),0,i);

            MutationsGP.add(new Label("Probability:"),1,i);
            Slider slider =createSlider(0,1);
            slider.setValue(mutations.getSizers().get(nsizers).getProbability());

            Label valueSlider= new Label();
            valueSlider.textProperty().bind(slider.valueProperty().asString());
            valueSlider.setMaxWidth(40);
            valueSlider.setMinWidth(40);
          // valueSlider.tooltipProperty().bind(valueSlider.);
            MutationsGP.add(valueSlider,3,i);
            sizerProbability.add(slider);
            MutationsGP.add(slider,2,i);
            MutationsGP.add(new Label("Total tupples:"),4,i);
            Spinner spinner= createSpinner(-1000,1000);
            spinner.getValueFactory().setValue(mutations.getSizers().get(nsizers).getTotalTuples());
            sizerTotalTuples.add(spinner);
            MutationsGP.add(spinner,5,i);
        }

        for (int nFlippings=0;nFlippings<mutations.getFlippings().size();i++,nFlippings++)
        {

            MutationsGP.add(new Label("Type: Flipping"),0,i);

            Label label=new Label("Probability:");
            //label.setAlignment(Pos.TOP_CENTER);
            MutationsGP.add(label,1,i);
            Slider slider = createSlider(0,1);
            slider.setValue(mutations.getFlippings().get(nFlippings).getProbability());

            Label valueSlider= new Label();
            valueSlider.textProperty().bind(slider.valueProperty().asString());
            valueSlider.setMaxWidth(40);
            valueSlider.setMinWidth(40);
            MutationsGP.add(valueSlider,3,i);
            flippingProbability.add(slider);
            MutationsGP.add(slider,2,i);
            MutationsGP.add(new Label("Max tupples:"),4,i);
            Spinner spinner= createSpinner(0,Integer.MAX_VALUE);
            spinner.getValueFactory().setValue(mutations.getFlippings().get(nFlippings).getMaxTuples());
            flippingMaxTupples.add(spinner);
            MutationsGP.add(spinner,5,i);
            MutationsGP.add(new Label("Component:"),6,i);
            MenuButton component= createFlippingComponent(nFlippings);
            flippingComponnent.add(component);
            Integer finalNFlippings = nFlippings;
            component.getItems().stream().filter(comp-> comp.getText().equals(mutations.getFlippings().get(finalNFlippings).getFlippingComponent().toString())).forEach(MenuItem::fire);

            MutationsGP.add(component,7,i);
        }
        MutationsGP.getColumnConstraints().get(0).setPrefWidth(80);

    }

    private void initializeCrossover()
    {

        crossoverParameter.setText("");
        setSpinnerCrossover(1);
        DayTimeOrientedMenu.setOnAction(event ->
        {
            CrossoverMenuBTN.setText(DayTimeOrientedMenu.getText());
            crossoverParameter.setText("");
            OrientationMenuBTN.setVisible(false);
        });

        AspectOrientedMenu.setOnAction(event ->
        {

            CrossoverMenuBTN.setText(AspectOrientedMenu.getText());
            crossoverParameter.setText("Orientation:");
            OrientationMenuBTN.setVisible(true);

        });

        TeacherMenu.setOnAction(event ->
        {
            OrientationMenuBTN.setText(TeacherMenu.getText());
        });

        ClassMenu.setOnAction(event ->
        {
            OrientationMenuBTN.setText(ClassMenu.getText());

        });

        OrientationMenuBTN.getItems().get(0).fire();



        TeacherMenu.setUserData("TEACHER");
        ClassMenu.setUserData("CLASS");

        DayTimeOrientedMenu.setUserData("DayTimeOriented");
        AspectOrientedMenu.setUserData("AspectOriented");

    }




    ChangeListener<Number> integerJumps = (obs, oldval, newVal) ->
            selectionSlider.setValue(Math.round(newVal.doubleValue()));

        private void initializeSelection()
        {
            setSpinnerSelection(0);
            selectionSliderLabel.textProperty().bind(selectionSlider.valueProperty().asString());
            selectionSliderLabel.visibleProperty().bind(selectionSlider.visibleProperty());
            // SettingsGridPane.add(selectionSlider,5,0);
            //selectionSliderLabel.textProperty().bind(selectionSlider.valueProperty().asString());
            //SelectionMenuBTN.setOnAction(event-> System.out.println("check"));
            TruncationMenu.setOnAction(event ->
            {
                selectionSlider.valueProperty().removeListener(integerJumps);
                selectionParameter.setText("Top Percent:");
                SelectionMenuBTN.setText(TruncationMenu.getText());
                selectionSlider.setMin(1);
                selectionSlider.setMax(100);
                selectionSlider.setShowTickLabels(true);
                selectionSlider.setShowTickMarks(true);
                selectionSlider.setMajorTickUnit(49);
                selectionSlider.setMinorTickCount(5);
                selectionSlider.setBlockIncrement(10);
                // selectionSlider.valueProperty().addListener((obs, oldval, newVal) ->
                //       selectionSlider.setValue(Math.round(newVal.doubleValue())));
                selectionSlider.valueProperty().addListener( integerJumps);
                selectionSlider.setVisible(true);

            });
            RouletteMenu.setOnAction(event ->
            {
                selectionParameter.setText("");
                selectionSlider.setVisible(false);
                SelectionMenuBTN.setText(RouletteMenu.getText());
            });
            TournamentMenu.setOnAction(event ->
            {
                selectionParameter.setText("Pte:");
                selectionSlider.valueProperty().removeListener(integerJumps);
                SelectionMenuBTN.setText(TournamentMenu.getText());
                selectionSlider.setMin(0);
                selectionSlider.setMax(1);
                selectionSlider.setShowTickLabels(true);
                selectionSlider.setShowTickMarks(true);
                selectionSlider.setMajorTickUnit(0.5);
                selectionSlider.setMinorTickCount(1);
                selectionSlider.setBlockIncrement(0.1);
                selectionSlider.setVisible(true);

            });
            TruncationMenu.setUserData("Truncation");
            RouletteMenu.setUserData("RouletteWheel");
            TournamentMenu.setUserData("Tournament");
        }

        public void setNewSettings()
        {
            Selection selection=null;
            switch (SelectionMenuBTN.getText())
            {
                case "Truncation":
                    selection=new Truncation((int)selectionSlider.getValue(),elitism.getValue());
                    break;
                case "Tournament":
                    selection=new Tournament(elitism.getValue(),selectionSlider.getValue());
                    break;
                case "Roulette Wheel":
                    selection=new RouletteWheel(elitism.getValue());
                    break;

            }
            Crossover crossover=null;

            switch (CrossoverMenuBTN.getText())
            {
                case "Day Time Oriented":
                    crossover=new DayTimeOriented(separators.getValue());
                    break;
                case "Aspect Oriented":
                    switch (OrientationMenuBTN.getText())
                    {
                        case "Class":
                            crossover=new AspectOriented(separators.getValue(), AspectOriented.OrientationType.CLASS);
                            break;
                        case "Teacher":
                            crossover=new AspectOriented(separators.getValue(), AspectOriented.OrientationType.TEACHER);
                            break;
                    }
            }

            List<Mutation> mutations=new ArrayList<>();

            //sizer
            for (int i=0;i<sizerProbability.size();i++)
            {
                mutations.add(new Sizer(sizerProbability.get(i).getValue(),sizerTotalTuples.get(i).getValue()));
            }
            //flipping
            for (int i=0;i<flippingProbability.size();i++)
            {
                mutations.add(new Flipping(flippingProbability.get(i).getValue(),flippingMaxTupples.get(i).getValue(),Flipping.FlippingComponent.valueOf(flippingComponnent.get(i).getText())));
            }


            //sets
            manager.setSelection(selection);
            manager.setCrossover(crossover);
            manager.setMutations(mutations);
        }

        public void disableScreen()
        {

            MutationsGP.setMouseTransparent(true);
            SettingsGridPane.setMouseTransparent(true);
        }

        public void enableScreen()
        {
            MutationsGP.setMouseTransparent(false);
            SettingsGridPane.setMouseTransparent(false);

        }

    };


