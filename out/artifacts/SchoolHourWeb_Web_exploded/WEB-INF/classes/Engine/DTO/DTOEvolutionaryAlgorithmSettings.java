package Engine.DTO;

public class DTOEvolutionaryAlgorithmSettings {

    private int initialPopulation;
    private DTOMutations dtoMutations;
    private DTOSelection dtoSelection;
    private DTOCrossover dtoCrossover;

    public DTOEvolutionaryAlgorithmSettings(int initialPopulation, DTOMutations dtoMutations, DTOSelection dtoSelection, DTOCrossover dtoCrossover) {
        this.initialPopulation = initialPopulation;
        this.dtoMutations = dtoMutations;
        this.dtoSelection = dtoSelection;
        this.dtoCrossover = dtoCrossover;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public DTOMutations getDtoMutations() {
        return dtoMutations;
    }

    public DTOSelection getDtoSelection() {
        return dtoSelection;
    }

    public DTOCrossover getDtoCrossover() {
        return dtoCrossover;
    }
}
