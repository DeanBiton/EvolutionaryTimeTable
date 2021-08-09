package Evolution;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.EndConditionGetterClass;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.Selection.Selection;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public abstract class EvolutionaryAlgorithm implements Serializable {

    public EvolutionaryAlgorithmData eaData;

    public  EvolutionaryAlgorithm(int _initialPopulation, Selection _selection, Crossover _crossover)
    {
        eaData = new EvolutionaryAlgorithmData(_initialPopulation,_selection,_crossover);
    }

    public EvolutionaryAlgorithm(EvolutionaryAlgorithmData _evolutionaryAlgorithmData)
    {
        this.eaData=_evolutionaryAlgorithmData;
    }

    public boolean isActivated(){ return eaData.getBestSolution()!=null;}

    public void runAlgorithm (List<EndCondition> endConditions, int printEveryThisNumberOfGenerations) {
        List<Evolutionary> generation = createFirstGeneration();
        eaData.setBestSolution(null);
        eaData.initEveryGenAndItsBestSolution();
        int currentGeneration = 0;
        eaData.setEndConditionAlgorithm(endConditions);
        boolean EndConditionIsMet=false;
        Evolutionary thisGenBestSolution;

        while (! EndConditionIsMet)
        {
            currentGeneration++;

             thisGenBestSolution = generation.stream().max(Evolutionary::compare).get();
            if(eaData.getBestSolution() == null)
            {
                eaData.setBestSolution(thisGenBestSolution);
            }
            else
            {
                if(thisGenBestSolution.fitness() > eaData.getBestSolution().fitness())
                {
                    eaData.setBestSolution(thisGenBestSolution);
                }
            }

            if(Thread.currentThread().isInterrupted())
            {
                break;
            }

            if(currentGeneration % printEveryThisNumberOfGenerations == 0)
            {
                synchronized (eaData.getEveryGenAndItsBestSolution())
                {
                    eaData.getEveryGenAndItsBestSolution().add(new Pair(currentGeneration, thisGenBestSolution.fitness()));
                }
            }

            generation = getNextGen(generation);
            if(currentGeneration == 10)
            {
                generation.get(0).fitness();
            }

            EndConditionGetterClass endConditionGetterClass=new EndConditionGetterClass(currentGeneration,eaData.getBestSolution().fitness());
            EndConditionIsMet= eaData.getEndConditionAlgorithm().stream().anyMatch(t-> t.checkCondition(endConditionGetterClass));
        }
    }

    protected List<Evolutionary> createFirstGeneration() {
        List<Evolutionary> generation = new ArrayList<>(eaData.getInitialPopulation());

        for(int i = 0; i < eaData.getInitialPopulation(); i++)
        {
            generation.add(createEvolutionaryInstance());
        }

        generation.forEach(Evolutionary::random);

        return generation;
    }

    protected List<Evolutionary> getNextGen(List<Evolutionary> currentGen) {
        List<Evolutionary> parents = eaData.getSelection().selection(currentGen);
        List<Evolutionary> children = new ArrayList<>();

        for(int i = 0; i < parents.size(); i+=2)
        {
            List<Evolutionary> twoNewChildren;
            if(i != parents.size() - 1)
            {
                twoNewChildren = crossover(parents.get(i), parents.get(i+1));
            }
            else
            {
                twoNewChildren = crossover(parents.get(i), parents.get(i));
            }

            if(twoNewChildren.size() != 2)
            {
                throw new RuntimeException("number of children is not 2");
            }

            children.addAll(twoNewChildren);
        }

        children.forEach(Evolutionary::mutation);
        return children;
    }

    protected abstract Evolutionary createEvolutionaryInstance();

    protected abstract List<Evolutionary> crossover(Evolutionary parent1, Evolutionary parent2);
}

