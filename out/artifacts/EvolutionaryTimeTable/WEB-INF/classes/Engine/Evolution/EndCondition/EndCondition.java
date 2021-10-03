package Engine.Evolution.EndCondition;
import java.io.Serializable;

public interface EndCondition extends Serializable {
    public boolean checkCondition(EndConditionGetterClass endConditionGetterClass);
}
