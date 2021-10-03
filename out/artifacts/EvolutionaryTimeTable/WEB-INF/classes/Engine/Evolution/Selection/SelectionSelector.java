package Engine.Evolution.Selection;

import Engine.ShowException;
import Engine.Evolution.HelperFunc;

import java.util.ArrayList;

public enum SelectionSelector {
    Truncation {
        @Override
        public Selection create(String configuration, int elitism) {
            ArrayList<String> parameters = HelperFunc.getParameters(configuration);
            if (parameters.size() != 1)
                throw new ShowException("number of parametrs incorrect for Selection Truncation");

            int topPercent;
            try {
                topPercent = Integer.parseInt(parameters.get(0));
            } catch (Exception e) {
                throw new ShowException("parametrs incorrect for Selection Truncation");
            }
                return new Truncation(topPercent, elitism);
        }
    },

    RouletteWheel{
        @Override
        public Selection create(String configuration, int elitism) {
            if (configuration!=null)
                throw new ShowException("number of parametrs incorrect for Selection RouletteWheel");

            return new RouletteWheel(elitism);
        }
    },

    Tournament{
        @Override
        public Selection create(String configuration, int elitism) {
            ArrayList<String> parameters = HelperFunc.getParameters(configuration);
            if (parameters.size() != 1)
                throw new ShowException("number of parametrs incorrect for Selection Tournament");
            double pte;
            try {
                pte = Double.parseDouble(parameters.get(0));
            } catch (Exception e) {
                throw new ShowException("parametrs incorrect for Selection Tournament");
            }
            return new Tournament(elitism,pte);
        }
    };
    public abstract Selection create(String configuration, int elitism);

    }


