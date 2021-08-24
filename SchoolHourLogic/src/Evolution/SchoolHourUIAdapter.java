package Evolution;

public interface SchoolHourUIAdapter {

    void updateTime(Long seconds);
    void updateGenerationNumber(Integer current);
    void updateBestSolution(Evolutionary bestSolution);
    void algorithmEnded();
    void addFitnessToChart(Double fitness, Integer generation);
}
