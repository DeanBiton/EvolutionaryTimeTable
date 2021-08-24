package Evolution.MySolution.Crossover;
import Evolution.Evolutionary;
import Evolution.MySolution.Tuple;
import Evolution.MySolution.TupleGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayTimeOriented extends Crossover{

    public DayTimeOriented(int _numberOfSeperators) {
        super(_numberOfSeperators);
    }

    @Override
    public List<List<Tuple>> crossoverInitialize(TupleGroup parent) {

        int DH = parent.getData().getNumberOfDays()*parent.getData().getNumberOfHoursInADay();
        List<List<Tuple>> tupleLists = new ArrayList<>(DH);

        for(int i = 0; i < DH; i++)
        {
            List<Tuple> lst = new ArrayList<>();
            tupleLists.add(lst);
        }

        for(Tuple tuple:parent.getTuples())
        {
            int location = ((tuple.getDay()-1) * parent.getData().getNumberOfHoursInADay()) + tuple.getHour() - 1;
            tupleLists.get(location).add(tuple);
        }

        return tupleLists;
    }

    @Override
    public String getName() {
        return "DayTimeOriented";
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("DayTimeOriented");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Number of separators: ");
        stringBuilder.append(numberOfSeparators);
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
