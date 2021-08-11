package Evolution.MySolution.MyMutation;
import Evolution.HelperFunc;
import Evolution.MySolution.*;
import java.util.*;

public class Flipping extends Mutation {

    private int maxTuples;
    private FlippingComponent flippingComponent;

    public enum FlippingComponent {
        S, T, C, H, D;
    }

    public Flipping(double _probability, int _maxTuples, FlippingComponent _flippingComponent){
        super(_probability);
        maxTuples = _maxTuples;
        flippingComponent = _flippingComponent;
    }

    public int getMaxTuples() {
        return maxTuples;
    }

    public FlippingComponent getFlippingComponent() {
        return flippingComponent;
    }

    @Override
    public void mutate(TupleGroup group) {
        List<Tuple> list= new ArrayList(group.getTuples());

        if(!group.getTuples().isEmpty())
        {
            for (int i = 0; i < maxTuples; i ++)
            {
                Tuple tuple = list.get(HelperFunc.getRandomNumber(0, group.getTuples().size()-1));

                if(probability >= Math.random() || probability == 1)
                {
                    mutateTuple(tuple, group.getData());
                }
            }
        }
    }

    public void mutateTuple(Tuple tuple, SchoolHourData data) {
        int random;

        switch (flippingComponent) {
            case S:
                random = HelperFunc.getRandomNumber(1, data.getSubjects().size());
                tuple.setSubject(data.getSubjects().get(random));
                break;
            case T:
                random = HelperFunc.getRandomNumber(1, data.getTeachers().size());
                tuple.setTeacher(data.getTeachers().get(random));
                break;
            case C:
                random = HelperFunc.getRandomNumber(1, data.getClassrooms().size());
                tuple.setClassroom(data.getClassrooms().get(random));
                break;
            case H:
                random = HelperFunc.getRandomNumber(1, data.getNumberOfHoursInADay());
                tuple.setHour(random);
                break;
            case D:
                random = HelperFunc.getRandomNumber(1, data.getNumberOfDays());
                tuple.setDay(random);
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder  = new StringBuilder();

        stringBuilder.append("Probability: ");
        stringBuilder.append(probability);
        stringBuilder.append(", Max Tuples: ");
        stringBuilder.append(maxTuples);
        stringBuilder.append(", Flipping Component: ");
        stringBuilder.append(flippingComponent);
        stringBuilder.append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
