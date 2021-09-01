package JavaFX;

import Engine.DTO.DTOSchoolHoursData;
import Engine.DTO.DTOTupleGroupWithFitnessDetails;
import Engine.Evolution.Evolutionary;
import Engine.Evolution.MySolution.TupleGroup;
import Engine.Evolution.SchoolHourUIAdapter;
import javafx.application.Platform;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UIAdapter implements SchoolHourUIAdapter {

    private Consumer<Double> updateBestFitness;
    private Consumer<DTOTupleGroupWithFitnessDetails> updaterBestSolution;
    private Runnable algorithmEnded;
    private Consumer<Integer> updateGenerationNumber;
    private Consumer<Long> updateTime;
    private BiConsumer<DTOTupleGroupWithFitnessDetails,Integer> addThisGenBestSolution;
    public UIAdapter(Consumer<Double> _updateFitness, Runnable _algorithmEnded, Consumer<Integer> _updateGenerationNumber,
                     Consumer<Long> _updateTime, Consumer<DTOTupleGroupWithFitnessDetails> _updaterBestSolution,BiConsumer<DTOTupleGroupWithFitnessDetails,Integer> _addThisGenBestSolution)
    {
        updateBestFitness = _updateFitness;
        algorithmEnded = _algorithmEnded;
        updateGenerationNumber=_updateGenerationNumber;
        updateTime=_updateTime;
        updaterBestSolution = _updaterBestSolution;
        addThisGenBestSolution = _addThisGenBestSolution;
    }

    public void updateTime(Long seconds)
    {
        Platform.runLater(()->updateTime.accept(seconds));
    }

    public void addThisGenBestSolution(Evolutionary bestSolution,Integer generation)
    {
        TupleGroup tupleGroup = (TupleGroup) bestSolution;
        Platform.runLater(() -> addThisGenBestSolution.accept(new DTOTupleGroupWithFitnessDetails(tupleGroup, new DTOSchoolHoursData(tupleGroup.getData())), generation));
    }

    public void updateGenerationNumber(Integer current)
    {
        Platform.runLater(()->updateGenerationNumber.accept(current));
    }

    public void updateBestSolution(Evolutionary bestSolution)
    {
        Platform.runLater(
                () -> {
                    updateBestFitness.accept(bestSolution.fitness());
                    TupleGroup tupleGroup = (TupleGroup) bestSolution;
                    updaterBestSolution.accept(new DTOTupleGroupWithFitnessDetails(tupleGroup, new DTOSchoolHoursData(tupleGroup.getData())));
                }
        );
    }

    public void algorithmEnded()
    {
        Platform.runLater(
                () -> {
                    algorithmEnded.run();
                }
        );
    }
}
