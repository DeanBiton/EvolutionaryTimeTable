package Algorithm;

import Engine.DTO.DTOAlgorithmContidions;
import Engine.DTO.DTOTupleGroupWithFitnessDetails;
import Engine.SchoolHourManager;

public class DTOAlgorithmStatus {

    DTOAlgorithmContidions dtoAlgorithmContidions;
    SchoolHourManager.algorithmStatus algorithmStatus;
    Double maximumFitnessFound;

    public DTOAlgorithmStatus(SchoolHourManager manager) {
        dtoAlgorithmContidions = manager.getAlgorithmConditions();
        algorithmStatus = manager.getAlgorithmStatus();
        maximumFitnessFound = manager.getMaximumFitnessFound();
    }
}
