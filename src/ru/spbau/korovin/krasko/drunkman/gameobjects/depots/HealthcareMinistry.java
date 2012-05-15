package ru.spbau.korovin.krasko.drunkman.gameobjects.depots;

import ru.aptu.softwaredesign.drunkengame.gameobjects.depots.PersonDepot;
import ru.spbau.korovin.krasko.drunkman.gameobjects.persons.HealthcareInspector;

/**
 * Created by IntelliJ IDEA.
 * User: Temp1ar
 * Date: 14.05.12
 * Time: 23:58
 */
public class HealthcareMinistry extends PersonDepot {
    private boolean inspectorOnMap = false;
    private boolean evidenceCollected = false;
    private final int picturesNeeded;

    public HealthcareMinistry(int picturesNeeded) {
        super();
        this.picturesNeeded = picturesNeeded;
    }

    @Override
    public void nextIteration() {
        if (inspectorOnMap) {
            return;
        }
        if (!evidenceCollected && field.isFree(appearance)) {
            inspectorOnMap = true;
            field.addObject(new HealthcareInspector(appearance, appearance, new Runnable() {
                public void run() {
                    inspectorOnMap = false;
                    evidenceCollected = true;
                }
            }, picturesNeeded), appearance);
        }
    }

    @Override
    public char getSymbol() {
        return 'М';
    }

    public boolean areEvidenceCollected() {
        return evidenceCollected;
    }


}
