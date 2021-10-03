package Engine.DTO;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class DTOEveryGenAndItsBestSolution {

    private final List<Pair<Integer, Double>> everyGenAndItsBestSolution;

    public DTOEveryGenAndItsBestSolution(List<Pair<Integer, Double>> _everyGenAndItsBestSolution, boolean copyFullList) {

        int length = _everyGenAndItsBestSolution.size();
        everyGenAndItsBestSolution = new ArrayList<>();

        if(copyFullList)
        {
            for(Pair<Integer,Double> pair : _everyGenAndItsBestSolution)
            {
                everyGenAndItsBestSolution.add(new Pair(pair.getKey().intValue(), pair.getValue().doubleValue()));
            }
        }
        else
        {
            int i = (length < 10) ? 0 : length - 10;
            Pair<Integer,Double> newPair;
            for(;i < length; i++)
            {
                synchronized (_everyGenAndItsBestSolution)
                {
                    newPair= new Pair(_everyGenAndItsBestSolution.get(i).getKey().intValue(), _everyGenAndItsBestSolution.get(i).getValue().doubleValue());
                }

                everyGenAndItsBestSolution.add(newPair);
            }
        }
    }

    public List<Pair<Integer, Double>> getEveryGenAndItsBestSolution() {
        return everyGenAndItsBestSolution;
    }
}
