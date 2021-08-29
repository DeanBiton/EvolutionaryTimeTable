package Evolution;

public interface SchoolHourUIAdapter {

    void updateTime(Long seconds);
    void updateGenerationNumber(Integer current);
    void updateBestSolution(Evolutionary bestSolution);
    void algorithmEnded();
    void addThisGenBestSolution(Evolutionary thisGenBestSolution, Integer generation);
}
