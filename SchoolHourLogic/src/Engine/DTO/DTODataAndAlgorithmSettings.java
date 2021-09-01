package Engine.DTO;

public class DTODataAndAlgorithmSettings {

    private DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings;
    private DTOSchoolHoursData dtoSchoolHoursData;

    public DTODataAndAlgorithmSettings(DTOEvolutionaryAlgorithmSettings dtoEvolutionaryAlgorithmSettings, DTOSchoolHoursData dtoSchoolHoursData) {
        this.dtoEvolutionaryAlgorithmSettings = dtoEvolutionaryAlgorithmSettings;
        this.dtoSchoolHoursData = dtoSchoolHoursData;
    }

    public DTOEvolutionaryAlgorithmSettings getDtoEvolutionaryAlgorithmSettings() {
        return dtoEvolutionaryAlgorithmSettings;
    }

    public DTOSchoolHoursData getDtoSchoolHoursData() {
        return dtoSchoolHoursData;
    }
}
