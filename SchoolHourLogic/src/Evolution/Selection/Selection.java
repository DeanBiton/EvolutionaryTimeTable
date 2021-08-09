package Evolution.Selection;
import Evolution.Evolutionary;

import java.io.Serializable;
import java.util.List;

public abstract class Selection implements Serializable {

    public abstract List<Evolutionary> selection(List<Evolutionary> currentGen);
}
