package edu.pedorenko.petri_object_model.statistics;

import java.util.HashMap;
import java.util.Map;

public class PetriObjectModelStatistics {

    private Map<Long, PetriObjectStatistics> petriObjectsStatistics = new HashMap<>();

    public PetriObjectModelStatistics() {
    }

    public void putPetriObjectStatistics(long petriObjectId, PetriObjectStatistics petriObjectStatistics) {
        petriObjectsStatistics.put(petriObjectId, petriObjectStatistics);
    }

    public PetriObjectStatistics getPetriObjectStatisticsByPetriObjectId(long petriObjectId) {
        return petriObjectsStatistics.get(petriObjectId);
    }

    public void doStatistics(double timeDelta) {
        for (PetriObjectStatistics petriObjectStatistics : petriObjectsStatistics.values()) {
            petriObjectStatistics.doStatistics(timeDelta);
        }
    }

    public String toString() {
        return "PetriObjectModelStatistics{" +
                "petriObjectsStatistics=" + petriObjectsStatistics +
                '}';
    }
}
