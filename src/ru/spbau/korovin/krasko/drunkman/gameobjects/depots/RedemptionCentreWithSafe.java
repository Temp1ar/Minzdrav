package ru.spbau.korovin.krasko.drunkman.gameobjects.depots;

import ru.aptu.softwaredesign.drunkengame.gameobjects.depots.RedemptionCentre;
import ru.aptu.softwaredesign.drunkengame.gameobjects.persons.Beggar;

/**
 * Created by IntelliJ IDEA.
 * User: Temp1ar
 * Date: 14.05.12
 * Time: 23:41
 */
public class RedemptionCentreWithSafe extends RedemptionCentre {
    private int beggarBudget = 0;
    private int turnsToRevertBeggar = 0;

    public void nextIteration() {
        if (turnsToRevertBeggar == 0 && field.isFree(appearance)) {
            field.addObject(new Beggar(appearance, appearance, new Runnable() {
                public void run() {
                    turnsToRevertBeggar = 1;
                    beggarBudget++;
                }
            }), appearance);
            turnsToRevertBeggar = -1;
        } else if (turnsToRevertBeggar > 0) {
            turnsToRevertBeggar--;
        }
    }

    public int getBalance() {
        return beggarBudget;
    }
}
