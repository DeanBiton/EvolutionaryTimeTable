package Engine.DTO;

import Engine.Evolution.Selection.Tournament;

public class DTOTournament implements DTOSelection{

    private String name;
    private Tournament tournament;
    private int elitism;

    public DTOTournament(Tournament tournament) {
        this.tournament = tournament;
        this.name = tournament.getName();
        this.elitism = tournament.getElitism();

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
