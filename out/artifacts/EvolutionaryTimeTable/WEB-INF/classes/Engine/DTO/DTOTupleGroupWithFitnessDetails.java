package Engine.DTO;

import Engine.Evolution.MySolution.TupleGroup;
import Engine.Evolution.Rule;

import java.util.*;

public class DTOTupleGroupWithFitnessDetails extends DTOTupleGroup{

    private List<DTORule> rules;
    private double hardRulesAverage;
    private double softRulesAverage;

    public DTOTupleGroupWithFitnessDetails(TupleGroup tupleGroup, DTOSchoolHoursData schoolHoursData) {
        super(tupleGroup, schoolHoursData);
        rules = new ArrayList<>();
        hardRulesAverage = 0;
        softRulesAverage = 0;
        getRulesScore();
    }

    private void getRulesScore()
    {
        List<Rule> rules = new ArrayList<>();
        int numberOfHardRules = 0;
        int numberOfSoftRules = 0;
        double SumOfHardRulesScore = 0;
        double SumOfSoftRulesScore = 0;

        for(Rule rule : tupleGroup.getData().getRules()) {
            double score = tupleGroup.fitnessOfRule(rule.getType());
            if(rule.getImplementationLevel() == Rule.RuleImplementationLevel.Hard)
            {
                numberOfHardRules++;
                SumOfHardRulesScore += score;
            }
            else
            {
                numberOfSoftRules++;
                SumOfSoftRulesScore += score;
            }

            this.rules.add(new DTORule(rule, score));
        }

        if(numberOfHardRules > 0)
        {
            hardRulesAverage += SumOfHardRulesScore / numberOfHardRules;
            hardRulesAverage = (double) Math.round(hardRulesAverage * 10) / 10;
        }
        if(numberOfSoftRules > 0)
        {
            softRulesAverage += SumOfSoftRulesScore / numberOfSoftRules;
            softRulesAverage = (double) Math.round(softRulesAverage * 10) / 10;
        }
    }

    public List<DTORule> getRules() {
        return rules;
    }

    public double getHardRulesAverage() {
        return hardRulesAverage;
    }

    public double getSoftRulesAverage() {
        return softRulesAverage;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
