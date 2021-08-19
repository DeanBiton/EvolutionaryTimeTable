package Evolution;

public interface SchoolHourUIAdapter {

    void updateTime(Long seconds);
    void updateGenerationNumber(Integer current);
    void updateBestFitness(Double fitness);
    void algorithmEnded();
}
