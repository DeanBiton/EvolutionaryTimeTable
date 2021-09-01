package Engine.Evolution;
import Engine.Evolution.MySolution.Crossover.Crossover;
import Engine.Evolution.MySolution.SchoolHourData;
import Engine.Evolution.MySolution.TupleGroup;
import Engine.Evolution.Selection.Selection;

import java.util.List;
public class SchoolHourEvolutionaryAlgorithm extends EvolutionaryAlgorithm{

    private SchoolHourData data;

    public SchoolHourEvolutionaryAlgorithm(int _initialPopulation, Selection _selection, Crossover _crossover, SchoolHourData _data)
    {
        super(_initialPopulation,_selection,_crossover);
        data=_data;
    }

    public SchoolHourEvolutionaryAlgorithm(EvolutionaryAlgorithmData _evolutionaryAlgorithmData, SchoolHourData data) {
        super(_evolutionaryAlgorithmData);
        this.data = data;
    }

    @Override
    protected Evolutionary createEvolutionaryInstance() {
        return new TupleGroup(data);
    }

    @Override
    protected List<Evolutionary> crossover(Evolutionary parent1, Evolutionary parent2) {
        if(!(parent1 instanceof TupleGroup) || !(parent2 instanceof TupleGroup))
        {
            throw new RuntimeException("Evolutionary must be instance of TupleGroup");
        }

        return eaData.getCrossover().crossover((TupleGroup) parent1, (TupleGroup) parent2);
    }

}
