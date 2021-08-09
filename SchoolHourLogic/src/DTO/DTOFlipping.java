package DTO;

import Evolution.MySolution.MyMutation.Flipping;

public class DTOFlipping {

    private Flipping flipping;

    public DTOFlipping(Flipping flipping) {
        this.flipping = flipping;
    }

    public final int getMaxTuples() {
        return flipping.getMaxTuples();
    }

    public final Flipping.FlippingComponent getFlippingComponent() {
        return flipping.getFlippingComponent();
    }

    public final double getProbability(){
        return flipping.getProbability();
    }

    @Override
    public String toString() {
        return flipping.toString();
    }
}
