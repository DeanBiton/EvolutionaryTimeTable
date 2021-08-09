package Evolution;

import Evolution.EndCondition.EndCondition;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.MySolution.Crossover.CrossoverSelector;
import Evolution.Selection.Selection;
import Evolution.Selection.SelectionSelector;
import Xml.JAXBClasses.ETTCrossover;
import Xml.JAXBClasses.ETTEvolutionEngine;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EvolutionaryAlgorithmData implements Serializable {

    private int initialPopulation;
    private Selection selection;
    private Crossover crossover;
    private Evolutionary bestSolution;
    private List<Pair<Integer, Double>> everyGenAndItsBestSolution;
    private List<EndCondition> endCondition;


    public EvolutionaryAlgorithmData(ETTEvolutionEngine ettEvolutionEngine)
    {
         selection=ETTgetSelection(ettEvolutionEngine);
         crossover = ETTgetCrossover(ettEvolutionEngine.getETTCrossover());
         initialPopulation=ettEvolutionEngine.getETTInitialPopulation().getSize();
        this.endCondition = new ArrayList<>();
        bestSolution=null;
    }
    private Selection ETTgetSelection(ETTEvolutionEngine ettEvolutionEngine)
    {
        String name = ettEvolutionEngine.getETTSelection().getType();
        String configuration= ettEvolutionEngine.getETTSelection().getConfiguration();
        Selection selection;
        try{
            SelectionSelector ss= SelectionSelector.valueOf(name);
            selection=ss.create(configuration);
        } catch (IllegalArgumentException e)
        {
            throw new RuntimeException("Selection "+ name+" doesnt exists");
        }
        return selection;
    }

    private Crossover ETTgetCrossover(ETTCrossover ettCrossover)
    {
        String crossovername= ettCrossover.getName();
        int cuttingPoints=ettCrossover.getCuttingPoints();
        String configuration=ettCrossover.getConfiguration();
        return CrossoverSelector.valueOf(crossovername).create(cuttingPoints,configuration);
    }

    public EvolutionaryAlgorithmData(int initialPopulation, Selection selection, Crossover crossover) {
        this.initialPopulation = initialPopulation;
        this.selection = selection;
        this.crossover = crossover;
        this.endCondition = new ArrayList<>();
        bestSolution=null;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(int initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    public Evolutionary getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Evolutionary bestSolution) {
        this.bestSolution = bestSolution;
    }

    public List<Pair<Integer, Double>> getEveryGenAndItsBestSolution() {
        return everyGenAndItsBestSolution;
    }

    public void initEveryGenAndItsBestSolution() {
        this.everyGenAndItsBestSolution = new ArrayList<>();
    }

    public List<EndCondition> getEndConditionAlgorithm() {
        return endCondition;
    }

    public void setEndConditionAlgorithm(List<EndCondition> endCondition) {
        this.endCondition = endCondition;
    }
}
