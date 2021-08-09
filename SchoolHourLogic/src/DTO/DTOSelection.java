package DTO;

import Evolution.Selection.Selection;
import Evolution.Selection.Truncation;

public interface DTOSelection {

    public static DTOSelection getDTOSelection(Selection selection)
    {
        DTOSelection dtoSelection = null;

        if(selection instanceof Truncation)
        {
            dtoSelection = new DTOTruncation((Truncation) selection);
        }

        return dtoSelection;
    }

    public static String getDTOSelectionToString(DTOSelection dtoSelection) {
        StringBuilder stringBuilder = new StringBuilder();

        if(dtoSelection instanceof DTOTruncation)
        {
            stringBuilder.append(((DTOTruncation)dtoSelection).toString());
        }

        return stringBuilder.toString();
    }
}
