package Engine.Evolution.MySolution.MyMutation;

import Engine.ShowException;
import Engine.Evolution.HelperFunc;

import java.util.ArrayList;

public enum MutationSelector {

    Flipping {
        @Override
        public Mutation create(double probability, String configuration) {

            ArrayList<String> parameters = HelperFunc.getParameters(configuration);
            if(parameters.size()!=2)
                throw new ShowException("number of parameters incorrect for mutation Flipping");

            int maxTupples;
            Flipping.FlippingComponent component;
            try {
                maxTupples = Integer.parseInt(parameters.get(0));
                component = Engine.Evolution.MySolution.MyMutation.Flipping.FlippingComponent.valueOf(parameters.get(1));
            } catch (Exception e)
            {
                throw new ShowException("parameters incorrect for mutation Flipping");
            }
            return new Flipping(probability,maxTupples, component);
        }
    },
    Sizer{
        @Override
        public Mutation create(double probability, String configuration)
        {
            ArrayList<String> parameters = HelperFunc.getParameters(configuration);
            if(parameters.size()!=1)
                throw new ShowException("number of parameters incorrect for mutation Flipping");

            int totalTuples;
            try {
                totalTuples = Integer.parseInt(parameters.get(0));
            }
            catch (Exception e)
            {
                throw new ShowException("parameters incorrect for mutation Sizer");
            }

            return new Sizer(probability, totalTuples);
        }
    }
    ;
    public abstract Mutation create(double probability, String configuration);

}