import Evolution.SchoolHourUIAdapter;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter implements SchoolHourUIAdapter {

    private Consumer<Double> updateBestFitness;
    private Runnable algorithmEnded;

    public UIAdapter(Consumer<Double> _updateFitness, Runnable _algorithmEnded)
    {
        updateBestFitness = _updateFitness;
        algorithmEnded = _algorithmEnded;
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
