package Evolution.Selection;

import Evolution.HelperFunc;

import java.util.ArrayList;

public enum SelectionSelector {
    Truncation {
        @Override
        public Selection create(String configuration) {
            ArrayList<String> parameters = HelperFunc.getParameters(configuration);
            if (parameters.size() != 1)
                throw new RuntimeException("number of parametrs incorrect for Selection Truncation");

            int topPercent;
            try {
                topPercent = Integer.parseInt(parameters.get(0));
            } catch (Exception e) {
                throw new RuntimeException("parametrs incorrect for Selection Truncation");
            }
                return new Truncation(topPercent, 0);
        }
    };

    public abstract Selection create(String configuration);

    }


