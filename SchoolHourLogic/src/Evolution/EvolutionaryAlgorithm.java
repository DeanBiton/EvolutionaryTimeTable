package Evolution;
import Evolution.EndCondition.EndCondition;
import Evolution.EndCondition.EndConditionGetterClass;
import Evolution.MySolution.Crossover.Crossover;
import Evolution.Selection.Selection;
import javafx.util.Pair;
import org.apache.commons.lang3.time.StopWatch;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class EvolutionaryAlgorithm implements Serializable {

    public EvolutionaryAlgorithmData eaData;
    private boolean suspended=false;
    private StopWatch stopWatch= new StopWatch();

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
        LocalDateTime time1 = LocalDateTime.now();
        stopWatch.start();
        List<Evolutionary> generation = createFirstGeneration();
        eaData.setBestSolution(null);
        eaData.initEveryGenAndItsBestSolution();
        int currentGeneration = 0;
        eaData.setEndConditionAlgorithm(endConditions);
        boolean EndConditionIsMet=false;
        Evolutionary thisGenBestSolution;

        while (! EndConditionIsMet)
        {
            synchronized(this) {
                while(suspended) {
                    try {
                        stopWatch.suspend();
                        wait();
                        stopWatch.resume();
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

            generation.get(0).fitness();
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

            if(currentGeneration % printEveryThisNumberOfGenerations == 0)
            {
                synchronized (eaData.getEveryGenAndItsBestSolution())
                {
                    //eaData.getEveryGenAndItsBestSolution().add(new Pair(currentGeneration, thisGenBestSolution.fitness()));
                    System.out.print("currentGeneration= "+currentGeneration);
                    System.out.println(", thisGenBestSolution.fitness= " +thisGenBestSolution.fitness());
                }
            }

            generation = getNextGen(generation);
            if(currentGeneration == 10)
            {
                generation.get(0).fitness();
            }

            EndConditionGetterClass endConditionGetterClass=new EndConditionGetterClass(currentGeneration,eaData.getBestSolution().fitness(),(int)stopWatch.getTime(TimeUnit.MINUTES));
            EndConditionIsMet= eaData.getEndConditionAlgorithm().stream().anyMatch(t-> t.checkCondition(endConditionGetterClass));

        }
        LocalDateTime time2 = LocalDateTime.now();

        System.out.println("Minutes: " + Duration.between(time1, time2).toMinutes() + ", Seconds: " + (Duration.between(time1, time2).getSeconds() - Duration.between(time1, time2).toMinutes() * 60));
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

        children.forEach(Evolutionary::mutation);
        return children;
    }

    protected abstract Evolutionary createEvolutionaryInstance();

    protected abstract List<Evolutionary> crossover(Evolutionary parent1, Evolutionary parent2);


    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notifyAll();
    }
}


