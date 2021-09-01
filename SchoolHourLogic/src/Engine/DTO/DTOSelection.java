package Engine.DTO;

import Engine.Evolution.Selection.RouletteWheel;
import Engine.Evolution.Selection.Selection;
import Engine.Evolution.Selection.Tournament;
import Engine.Evolution.Selection.Truncation;

public interface DTOSelection {

    public int getElitism();
    public String getName();
    public static DTOSelection getDTOSelection(Selection selection)
    {
        DTOSelection dtoSelection = null;

        if(selection instanceof Truncation)
        {
            dtoSelection = new DTOTruncation((Truncation) selection);
        }
        else if(selection instanceof Tournament)
        {
            dtoSelection = new DTOTournament((Tournament) selection);
        }
        else if(selection instanceof RouletteWheel)
        {
            dtoSelection = new DTORouletteWheel((RouletteWheel) selection);
        }

        return dtoSelection;
    }

    public static String getDTOSelectionToString(DTOSelection dtoSelection) {
        StringBuilder stringBuilder = new StringBuilder();

        if(dtoSelection instanceof DTOTruncation)
        {
            stringBuilder.append(((DTOTruncation)dtoSelection).toString());
        }
        else if(dtoSelection instanceof DTOTournament)
        {
            stringBuilder.append(((DTOTournament)dtoSelection).toString());
        }
        else if(dtoSelection instanceof DTORouletteWheel)
        {
            stringBuilder.append(((DTORouletteWheel)dtoSelection).toString());
        }

        return stringBuilder.toString();
    }
}
