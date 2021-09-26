package Engine.Evolution;
import Engine.DTO.DTOAlgorithmContidions;
import Engine.Evolution.EndCondition.*;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.MyMutation.Mutation;
import Engine.Evolution.Selection.Selection;
import javafx.util.Pair;
import org.apache.commons.lang3.time.StopWatch;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class EvolutionaryAlgorithm implements Serializable {

    protected EvolutionaryAlgorithmData eaData;
    private boolean suspended = false;
    private StopWatch stopWatch = new StopWatch();
    private boolean isSettingAvailable = true;
    private EndConditionGetterClass endConditionGetterClass= new EndConditionGetterClass(0,0,0);

    public  EvolutionaryAlgorithm(int _initialPopulation, Selection _selection, Crossover _crossover)
    {
        eaData = new EvolutionaryAlgorithmData(_initialPopulation,_selection,_crossover);
    }

    public EvolutionaryAlgorithm(EvolutionaryAlgorithmData _evolutionaryAlgorithmData)
    {
        this.eaData=_evolutionaryAlgorithmData;
    }

    public boolean isActivated(){ return eaData.getBestSolution()!=null;}

    public void runAlgorithm () {
        isSettingAvailable = false;
        LocalDateTime time1 = LocalDateTime.now();
        stopWatch.reset();
        stopWatch.start();
        List<Evolutionary> generation = createFirstGeneration();
        eaData.setBestSolution(null);
        eaData.initEveryGenAndItsBestSolution();
        int currentGeneration = 0;
        boolean EndConditionIsMet=false;

        Evolutionary thisGenBestSolution;

        while (! EndConditionIsMet)
        {
            synchronized(this) {
                while(suspended) {
                    try {
                        isSettingAvailable = true;
                        stopWatch.suspend();
                        wait();
                        stopWatch.resume();
                        isSettingAvailable = false;
                    } catch (InterruptedException e) {
                        suspended=false;
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if(Thread.currentThread().isInterrupted())
            {
                break;
            }
            currentGeneration++;
            //uiAdapter.updateGenerationNumber(currentGeneration);

            generation.get(0).fitness();
            thisGenBestSolution = generation.stream().max(Evolutionary::compare).get();
            if(eaData.getBestSolution() == null)
            {
                eaData.setBestSolution(thisGenBestSolution);
                //uiAdapter.updateBestSolution(thisGenBestSolution);
            }
            else
            {
                if(thisGenBestSolution.fitness() > eaData.getBestSolution().fitness())
                {
                    eaData.setBestSolution(thisGenBestSolution);
                    //uiAdapter.updateBestSolution(thisGenBestSolution);
                }
            }

            if(currentGeneration % eaData.getShowEveryGeneration() == 0)
            {
                synchronized (eaData.getEveryGenAndItsBestSolution())
                {
                    //eaData.getEveryGenAndItsBestSolution().add(new Pair(currentGeneration, thisGenBestSolution.fitness()));
                    //System.out.print("currentGeneration= "+currentGeneration);
                    //System.out.println(", thisGenBestSolution.fitness= " +thisGenBestSolution.fitness());
                    //uiAdapter.addThisGenBestSolution(thisGenBestSolution, currentGeneration);
                }
            }

            generation = getNextGen(generation);
            if(currentGeneration == 10)
            {
                generation.get(0).fitness();
            }

             endConditionGetterClass.update(currentGeneration,eaData.getBestSolution().fitness(),(int)stopWatch.getTime(TimeUnit.SECONDS));
            EndConditionIsMet= eaData.getEndConditionAlgorithm().stream().anyMatch(t-> t.checkCondition(endConditionGetterClass));
            //uiAdapter.updateTime(stopWatch.getTime(TimeUnit.SECONDS));

        }
        isSettingAvailable = true;
        LocalDateTime time2 = LocalDateTime.now();

        //System.out.println("Minutes: " + Duration.between(time1, time2).toMinutes() + ", Seconds: " + (Duration.between(time1, time2).getSeconds() - Duration.between(time1, time2).toMinutes() * 60));
        stopWatch.stop();
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
        List<Evolutionary> children = new ArrayList<>();
        List<Evolutionary> parents = eaData.getSelection().selection(currentGen, children);

        if(parents.size() % 2 != 0)
        {
            throw new RuntimeException("number of parents is not even.");
        }

        for(int i = 0; i < parents.size(); i+=2)
        {
            List<Evolutionary> twoNewChildren;
            twoNewChildren = crossover(parents.get(i), parents.get(i+1));

            if(twoNewChildren.size() != 2)
            {
                throw new RuntimeException("number of children is not 2.");
            }

            children.addAll(twoNewChildren);
        }

        if((currentGen.size() - eaData.getSelection().getElitism()) % 2 == 1)
        {
            children.remove(children.size() - 1);
        }

        synchronized (eaData.getMutations()) {
            children.forEach(Evolutionary::mutation);
        }

        return children;
    }

    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notifyAll();
    }

    protected abstract Evolutionary createEvolutionaryInstance();

    protected abstract List<Evolutionary> crossover(Evolutionary parent1, Evolutionary parent2);

    public int getInitialPopulation() {
        synchronized (eaData.getInitialPopulation())
        {
            return eaData.getInitialPopulation().intValue();
        }
    }

    public void setInitialPopulation(int initialPopulation) {
        synchronized (eaData.getInitialPopulation())
        {
            if(isSettingAvailable)
            {
                eaData.setInitialPopulation(initialPopulation);
            }
            else
            {
                throw new RuntimeException("can't set initialPopulation while the algorithm is running.");
            }
        }
    }

    public Selection getSelection() {
        synchronized (eaData.getSelection())
        {
            return eaData.getSelection();
        }
    }

    public void setSelection(Selection selection) {
        synchronized (eaData.getSelection())
        {
            if(isSettingAvailable)
            {
                eaData.setSelection(selection);
            }
            else
            {
                throw new RuntimeException("can't set selection while the algorithm is running.");
            }
        }
    }

    public Crossover getCrossover() {
        synchronized (eaData.getCrossover())
        {
            return eaData.getCrossover();
        }
    }

    public void setCrossover(Crossover crossover) {
        synchronized (eaData.getCrossover())
        {
            if(isSettingAvailable)
            {
                eaData.setCrossover(crossover);
            }
            else
            {
                throw new RuntimeException("can't set crossover while the algorithm is running.");
            }
        }
    }

    public void setMutations(List<Mutation> mutations) {
        synchronized (eaData.getMutations())
        {
            if(isSettingAvailable)
            {
                eaData.setMutations(mutations);
            }
            else
            {
                throw new RuntimeException("can't set mutations while the algorithm is running.");
            }
        }
    }

    public Evolutionary getBestSolution() {
        return eaData.getBestSolution();
    }

    public List<Pair<Integer, Double>> getEveryGenAndItsBestSolution() {
        return eaData.getEveryGenAndItsBestSolution();
    }

    public List<EndCondition> getEndConditionAlgorithm() {
        return eaData.getEndConditionAlgorithm();
    }

    public  EndConditionGetterClass getEndCondtionsStatus() {return endConditionGetterClass; }
    
    public void setEndConditions(List<EndCondition> endConditions) {
        synchronized (eaData.getEndConditionAlgorithm())
        {
            if(isSettingAvailable)
            {
                eaData.setEndConditionAlgorithm(endConditions);
            }
            else
            {
                throw new RuntimeException("can't set end conditions while the algorithm is running.");
            }
        }
    }

    public DTOAlgorithmContidions getDTOAlgorithmEndConditions()
    {
        double fitness = -1;
        int numberOfGeneration = -1;
        int time = -1;

        for(EndCondition endCondition : eaData.getEndConditionAlgorithm())
        {
            if(endCondition instanceof ByFitness)
                fitness = ((ByFitness)endCondition).getFitness();
            else if(endCondition instanceof NumberOfGenerations)
                numberOfGeneration = ((NumberOfGenerations)endCondition).getNumberOfGenerations();
            else
                time = ((ByTime)endCondition).getMinutes();
        }

        return new DTOAlgorithmContidions(new EndConditionGetterClass(numberOfGeneration, fitness, time));
    }

    public Integer getShowEveryGeneration() {
        synchronized (eaData.getShowEveryGeneration())
        {
            return eaData.getShowEveryGeneration();
        }
    }

    public void setShowEveryGeneration(Integer showEveryGeneration) {
        synchronized (eaData.getShowEveryGeneration())
        {
            if(isSettingAvailable)
            {
                eaData.setShowEveryGeneration(showEveryGeneration);
            }
            else
            {
                throw new RuntimeException("can't set showEveryGeneration while the algorithm is running.");
            }
        }
    }
}
