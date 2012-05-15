package ru.spbau.korovin.krasko.drunkman;

import ru.aptu.softwaredesign.drunkengame.fields.Field2D;
import ru.aptu.softwaredesign.drunkengame.fields.HexagonalField;
import ru.aptu.softwaredesign.drunkengame.fields.RectangularField;
import ru.aptu.softwaredesign.drunkengame.gameobjects.depots.Bar;
import ru.aptu.softwaredesign.drunkengame.gameobjects.depots.PoliceStation;
import ru.aptu.softwaredesign.drunkengame.gameobjects.fieldobjects.Column;
import ru.aptu.softwaredesign.drunkengame.gameobjects.fieldobjects.Light;
import ru.spbau.korovin.krasko.drunkman.gameobjects.depots.HealthcareMinistry;
import ru.spbau.korovin.krasko.drunkman.gameobjects.depots.RedemptionCentreWithSafe;

import java.awt.*;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Temp1ar
 * Date: 14.05.12
 * Time: 23:31
 */
public class Main {
    private static TreeSet<Integer> turns = new TreeSet<Integer>(Arrays.asList(205, 305, 505));

    public static void main(String[] args) {
        Dimension dimension = new Dimension(15, 15);
        boolean hexField = (args.length != 0);
        Field2D field = !hexField ? new RectangularField(dimension) : new HexagonalField(dimension);

        field.addObject(new Column(), new Point(7, 7));

        Light light = new Light();
        field.addObject(light, new Point(2, 9));

        field.addDepot(new PoliceStation(light), new Point(3, 14));
        field.addDepot(new Bar(), new Point(0, 9));

        RedemptionCentreWithSafe redemptionCentre = new RedemptionCentreWithSafe();
        field.addDepot(redemptionCentre, new Point(14, 4));

        int picturesNeeded = 40;
        HealthcareMinistry ministry = new HealthcareMinistry(picturesNeeded);
        if (hexField) {
            field.addDepot(ministry, new Point(13 - 4, 14 - 2));
        } else {
            field.addDepot(ministry, new Point(13, 14));
        }

        int i = 0;
        boolean deadlock = false;
        while (!deadlock && !ministry.areEvidenceCollected()) {
            field.nextIteration();
            if (turns.contains(i) || i % 1000 == 0) {
                System.out.println(field);
            }

            if(i == 20000) {
                deadlock = true;
            }

            i++;
        }

        int balance = redemptionCentre.getBalance();

        if (!deadlock) {
            System.out.println("After " + i + " iterations bar has been closed. " +
                    "Beggar accumulated " + balance + " dollars.");
        } else {
            System.out.println("Inspector can't take " + picturesNeeded +
                    " pictures because of deadlock state. " +
                    "Beggar accumulated " + balance + " dollars.");
        }

        if (balance > 8000) {
            System.out.println("Beggar can afford yacht for 8000.");
        } else if (balance > 1000) {
            System.out.println("Beggar can afford yacht for 1000.");
        } else if (balance > 200) {
            System.out.println("Beggar can afford yacht for 200.");
        } else {
            System.out.println("Beggar can't afford yacht.");
        }

    }
}