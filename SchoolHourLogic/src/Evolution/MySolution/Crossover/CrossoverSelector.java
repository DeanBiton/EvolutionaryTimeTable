package Evolution.MySolution.Crossover;

import Evolution.HelperFunc;

import java.util.ArrayList;

public enum CrossoverSelector {
    DayTimeOriented{
        @Override
        public Crossover create(int cuttingPoints, String configuration) {
            if(configuration!=null)
                throw new RuntimeException("parametrs incorrect for crossover DayTimeOriented");

            return new DayTimeOriented(cuttingPoints);
        }
    },
    AspectOriented{
        @Override
        public Crossover create(int cuttingPoints, String configuration){
            if (configuration == null)
            {
                throw new RuntimeException("parameters incorrect for crossover AspectOriented");
            }

            ArrayList<String> parameters = HelperFunc.getParameters(configuration);

            if(parameters.size()!=1)
            {
                throw new RuntimeException("number of parameters incorrect for mutation Flipping");
            }

            Evolution.MySolution.Crossover.AspectOriented.OrientationType orientationType;
            try {
                orientationType =  Evolution.MySolution.Crossover.AspectOriented.OrientationType.valueOf(parameters.get(0));
            }
            catch (Exception ex)
            {
                throw new RuntimeException("parameters incorrect for crossover AspectOriented");
            }

            return new AspectOriented(cuttingPoints, orientationType);
        }
    };



    public abstract Crossover create(int cuttingPoints, String configuration);
}
