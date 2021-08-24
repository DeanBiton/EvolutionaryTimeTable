import DTO.DTOSchoolHoursData;
import DTO.DTOTupleGroupWithFitnessDetails;
import Evolution.Evolutionary;
import Evolution.MySolution.TupleGroup;
import Evolution.SchoolHourUIAdapter;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter implements SchoolHourUIAdapter {

    private Consumer<Double> updateBestFitness;
    private Consumer<DTOTupleGroupWithFitnessDetails> updaterBestSolution;
    private Runnable algorithmEnded;
    private Consumer<Integer> updateGenerationNumber;
    private Consumer<Long> updateTime;

    public UIAdapter(Consumer<Double> _updateFitness, Runnable _algorithmEnded, Consumer<Integer> _updateGenerationNumber,
                     Consumer<Long> _updateTime, Consumer<DTOTupleGroupWithFitnessDetails> _updaterBestSolution)
    {
        updateBestFitness = _updateFitness;
        algorithmEnded = _algorithmEnded;
        updateGenerationNumber=_updateGenerationNumber;
        updateTime=_updateTime;
        updaterBestSolution = _updaterBestSolution;
    }

    public void updateTime(Long seconds)
    {
        Platform.runLater(()->updateTime.accept(seconds));
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
