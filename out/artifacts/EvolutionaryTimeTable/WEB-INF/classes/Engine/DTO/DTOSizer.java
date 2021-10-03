package Engine.DTO;

import Engine.Evolution.MySolution.MyMutation.Sizer;

public class DTOSizer {

    private Sizer sizer;

    public DTOSizer(Sizer sizer) {
        this.sizer = sizer;
    }

    public final int getTotalTuples(){
        return sizer.getTotalTuples();
    }

    public final double getProbability()
    {
        return sizer.getProbability();
    }

    @Override
    public String toString() {
        return sizer.toString();
    }
}
