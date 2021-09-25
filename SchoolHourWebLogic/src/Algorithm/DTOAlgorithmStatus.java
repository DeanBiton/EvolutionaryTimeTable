package Algorithm;

import Engine.DTO.DTOAlgorithmContidions;
import Engine.DTO.DTOTupleGroupWithFitnessDetails;
import Engine.SchoolHourManager;

public class DTOAlgorithmStatus {

    DTOAlgorithmContidions dtoAlgorithmContidions;
    String algorithmStatus;

    public DTOAlgorithmStatus(SchoolHourManager manager) {
        dtoAlgorithmContidions = manager.getAlgorithmConditions();
    }
}
