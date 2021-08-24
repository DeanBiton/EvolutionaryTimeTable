package DTO;

import Evolution.Selection.Tournament;
import Evolution.Selection.Truncation;

public class DTOTournament implements DTOSelection{

    private Tournament tournament;

    public DTOTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public final double getPte() {
        return tournament.getPte();
    }

    @Override
    public int getElitism() {
        return tournament.getElitism();
    }

    @Override
    public String getName() {
        return tournament.getName();
    }

    @Override
    public String toString() {
        return tournament.toString();
    }
}
