package Engine.DTO;

import Engine.Evolution.Rule;

public class DTORule {

    private Rule rule;

    private String name;
    private Rule.RuleImplementationLevel implementationLevel;
    private String etc;
    private Double score;

    public DTORule(Rule rule) {
        this.rule = rule;
        this.name = rule.getType().name();
        this.implementationLevel = rule.getImplementationLevel();

        if(name.equals("Sequentiality"))
        {
            etc = "Total hour :" + rule.getType().getTotalHours();
        }
        else
        {
            etc = "";
        }
    }

    public DTORule(Rule rule, double score)
    {
        this(rule);
        this.score = score;
    }
    public final Rule.RuleType getRuleType() {
        return rule.getType();
    }

    public final Rule.RuleImplementationLevel getRuleImplementationLevel() {
        return implementationLevel;
    }

    public String getName() {
        return name;
    }

    public Rule.RuleImplementationLevel getImplementationLevel() {
        return implementationLevel;
    }

    public String getEtc() {
        return etc;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return rule.toString();
    }
}