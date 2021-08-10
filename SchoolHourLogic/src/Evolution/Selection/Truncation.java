package Evolution.Selection;
import Evolution.*;
import java.util.*;

public class Truncation extends Selection{

    private int topPercent;

    public Truncation(int _topPercent, int _elitism) {
        super(_elitism);
        if(_topPercent > 100 || _topPercent < 0)
        {
            throw new RuntimeException("topPercent must be between 0 and 100 in Selection Trunction");
        }

        this.topPercent = _topPercent;
    }

    public int getTopPercent() {
        return topPercent;
    }

    @Override
    public List<Evolutionary> selection(List<Evolutionary> currentGen) {

        Collections.sort(currentGen, Collections.reverseOrder(Evolutionary::compare));

        int numberOfSolutions = (int) Math.floor(currentGen.size() * topPercent / 100);
        List<Evolutionary> parents= new ArrayList<>(currentGen.size());

        if(numberOfSolutions == 0)
            numberOfSolutions = 1;

        for (int i = 0; i < currentGen.size(); i++)
        {
            Evolutionary parent = currentGen.get(HelperFunc.getRandomNumber(0,numberOfSolutions-1));
            if(i % 2 == 1)
            {
                if(parent != parents.get(i - 1) || numberOfSolutions == 1)
                {
                    parents.add(parent);
                }
                else
                {
                    i--;
                }
            }
            else
            {
                parents.add(parent);
            }
        }

        return parents;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Truncation");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Top percent: ");
        stringBuilder.append(topPercent);
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
