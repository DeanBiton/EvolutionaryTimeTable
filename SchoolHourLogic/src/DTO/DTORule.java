package DTO;

import Evolution.Rule;

public class DTORule {

    private Rule rule;

    public DTORule(Rule rule) {
        this.rule = rule;
    }

    public final Rule.RuleType getRuleType() {
        return rule.getType();
    }

    public final Rule.RuleImplementationLevel getRuleImplementationLevel() {
        return rule.getImplementationLevel();
    }

    @Override
    public String toString() {
        return rule.toString();
    }
}