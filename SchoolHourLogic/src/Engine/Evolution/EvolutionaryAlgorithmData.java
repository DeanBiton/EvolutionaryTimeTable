package Engine.Evolution;

import Engine.Evolution.MySolution.Crossover.AspectOriented;
import Engine.Evolution.Selection.RouletteWheel;
import Engine.Evolution.Selection.Truncation;
import Engine.ShowException;
import Engine.Evolution.EndCondition.EndCondition;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.Crossover.CrossoverSelector;
import Engine.Evolution.MySolution.MyMutation.Mutation;
import Engine.Evolution.Selection.Selection;
import Engine.Evolution.Selection.SelectionSelector;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EvolutionaryAlgorithmData implements Serializable {

    private Integer initialPopulation;
    private Selection selection;
    private Crossover crossover;
    private Evolutionary bestSolution;
    private List<Pair<Integer, Double>> everyGenAndItsBestSolution;
    private List<EndCondition> endCondition;
    private List<Mutation> mutations;

    public EvolutionaryAlgorithmData(List<Mutation> mutations) {
        this.mutations = mutations;
        initialPopulation = 10; // ettEvolutionEngine.getETTInitialPopulation().getSize();
        selection = new Truncation(10, 20); // ETTgetSelection(ettEvolutionEngine);
        crossover = new AspectOriented(0, AspectOriented.OrientationType.TEACHER); // ETTgetCrossover(ettEvolutionEngine.getETTCrossover());
        this.endCondition = new ArrayList<>();
        bestSolution = null;
    }

    /*
    private Selection ETTgetSelection(ETTEvolutionEngine ettEvolutionEngine) {
        String name = ettEvolutionEngine.getETTSelection().getType();
        String configuration = ettEvolutionEngine.getETTSelection().getConfiguration();
        int elitism = ettEvolutionEngine.getETTSelection().getETTElitism() != null ? ettEvolutionEngine.getETTSelection().getETTElitism() : 0;
        if (elitism > initialPopulation)
            throw new ShowException( "elitism is above initial population");
        Selection selection;
        try {
            SelectionSelector ss = SelectionSelector.valueOf(name);
            selection = ss.create(configuration, elitism);
        } catch (IllegalArgumentException e) {
            throw new ShowException("Selection " + name + " doesnt exists");
        }
        return selection;
    }

    private Crossover ETTgetCrossover(ETTCrossover ettCrossover) {
        String crossovername = ettCrossover.getName();
        int cuttingPoints = ettCrossover.getCuttingPoints();
        String configuration = ettCrossover.getConfiguration();
        return CrossoverSelector.valueOf(crossovername).create(cuttingPoints, configuration);
    }
*/

    public EvolutionaryAlgorithmData(int initialPopulation, Selection selection, Crossover crossover) {
        this.initialPopulation = initialPopulation;
        this.selection = selection;
        this.crossover = crossover;
        this.endCondition = new ArrayList<>();
        bestSolution = null;
    }

    public Integer getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(int initialPopulation) {
        if(initialPopulation <= 0)
        {
            throw new RuntimeException("Initial population must be a positive integer");
        }

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

    public List<Mutation> getMutations() {
        return mutations;
    }

    public void setMutations(List<Mutation> mutations) {
        this.mutations.clear();
        this.mutations.addAll(mutations);
    }
}
