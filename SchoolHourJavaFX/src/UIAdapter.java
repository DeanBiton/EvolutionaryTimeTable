import Evolution.SchoolHourUIAdapter;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter implements SchoolHourUIAdapter {

    private Consumer<Double> updateBestFitness;
    private Runnable algorithmEnded;
    private Consumer<Integer> updateGenerationNumber;
    private Consumer<Long> updateTime;

    public UIAdapter(Consumer<Double> _updateFitness, Runnable _algorithmEnded, Consumer<Integer> _updateGenerationNumber,Consumer<Long> _updateTime)
    {
        updateBestFitness = _updateFitness;
        algorithmEnded = _algorithmEnded;
        updateGenerationNumber=_updateGenerationNumber;
        updateTime=_updateTime;
    }

    public void updateTime(Long seconds)
    {
        Platform.runLater(()->updateTime.accept(seconds));
    }

    public void updateGenerationNumber(Integer current)
    {
        Platform.runLater(()->updateGenerationNumber.accept(current));
    }

    public void updateBestFitness(Double fitness)
    {
        Platform.runLater(
                () -> {
                    updateBestFitness.accept(fitness);
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
