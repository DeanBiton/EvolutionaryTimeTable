package DTO;

import Evolution.MySolution.MyMutation.*;
import java.util.ArrayList;
import java.util.List;

public class DTOMutations {

    private List<DTOFlipping> flippings;
    private List<DTOSizer> sizers;


    public List<DTOFlipping> getFlippings() {
        return flippings;
    }

    public List<DTOSizer> getSizers() {
        return sizers;
    }

    public DTOMutations(List<Mutation> mutations) {
        flippings = new ArrayList<>();
        sizers = new ArrayList<>();

        for (Mutation mutation : mutations) {
            if (mutation instanceof Flipping)
                flippings.add(new DTOFlipping((Flipping) mutation));
            else if(mutation instanceof Sizer)
                sizers.add(new DTOSizer((Sizer) mutation));
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Mutations:");
        stringBuilder.append(System.lineSeparator());
        if(!flippings.isEmpty())
        {
            stringBuilder.append("Filpping Mutations:");
            stringBuilder.append(System.lineSeparator());
            for (DTOFlipping dtoFlipping : flippings)
            {
                stringBuilder.append(dtoFlipping.toString());
            }
        }

        if(!sizers.isEmpty())
        {
            stringBuilder.append("Sizer Mutations:");
            stringBuilder.append(System.lineSeparator());
            for (DTOSizer dtoSizer : sizers)
            {
                stringBuilder.append(dtoSizer.toString());
            }
        }

        return stringBuilder.toString();
    }
}