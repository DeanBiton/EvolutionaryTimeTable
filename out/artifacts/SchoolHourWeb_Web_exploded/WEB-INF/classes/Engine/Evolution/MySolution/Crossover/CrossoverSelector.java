package Engine.Evolution.MySolution.Crossover;

import Engine.ShowException;
import Engine.Evolution.HelperFunc;

import java.util.ArrayList;

public enum CrossoverSelector {
    DayTimeOriented{
        @Override
        public Crossover create(int cuttingPoints, String configuration) {
            if(configuration!=null)
                throw new ShowException("parametrs incorrect for crossover DayTimeOriented");

            return new DayTimeOriented(cuttingPoints);
        }
    },
    AspectOriented{
        @Override
        public Crossover create(int cuttingPoints, String configuration){
            if (configuration == null)
            {
                throw new ShowException("parameters incorrect for crossover AspectOriented");
            }

            ArrayList<String> parameters = HelperFunc.getParameters(configuration);

            if(parameters.size()!=1)
            {
                throw new ShowException("number of parameters incorrect for mutation Flipping");
            }

            Engine.Evolution.MySolution.Crossover.AspectOriented.OrientationType orientationType;
            try {
                orientationType =  Engine.Evolution.MySolution.Crossover.AspectOriented.OrientationType.valueOf(parameters.get(0));
            }
            catch (Exception ex)
            {
                throw new ShowException("parameters incorrect for crossover AspectOriented");
            }

            return new AspectOriented(cuttingPoints, orientationType);
        }
    };



    public abstract Crossover create(int cuttingPoints, String configuration);
}
