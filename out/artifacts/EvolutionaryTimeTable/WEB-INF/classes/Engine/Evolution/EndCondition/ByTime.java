package Engine.Evolution.EndCondition;

import Engine.ShowException;

public class ByTime implements EndCondition{
    private int minutes;

    public ByTime(int minutes) {
        if(minutes<=0)
            throw new ShowException("End condition byTime - parameter is not positive");

        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean checkCondition(EndConditionGetterClass endConditionGetterClass) {
        if(minutes*60>endConditionGetterClass.getTime())
            return false;
        else
            return true;
    }
}
