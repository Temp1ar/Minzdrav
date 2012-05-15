package ru.spbau.korovin.krasko.drunkman.gameobjects.persons;

import ru.aptu.softwaredesign.drunkengame.gameobjects.persons.DrunkMan;
import ru.aptu.softwaredesign.drunkengame.gameobjects.persons.TargetTraveler;

import java.awt.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Temp1ar
 * Date: 15.05.12
 * Time: 0:05
 */
public class HealthcareInspector extends TargetTraveler {
    private final static int RADIUS = 2;
    private static final int NEW_TARGET_PERIOD = 20;

    private Random random = new Random();
    private int targetResetIterations = 0;

    private HashSet<DrunkMan> picturedDrunkMen = new HashSet<>();
    private final int picturesNeeded;

    public HealthcareInspector(Point origin, Point target, Runnable callback, int picturesNeeded) {
        super(origin, target, callback);
        this.picturesNeeded = picturesNeeded;
    }

    private void setNewRandomTarget() {
        int x, y;
        do {
            x = random.nextInt(field.getDimensions().width);
            y = random.nextInt(field.getDimensions().height);
            target = new Point(x, y);
        } while (!field.onField(x, y));
    }

    public void nextIteration() {
        if (!carrying && (++targetResetIterations > NEW_TARGET_PERIOD
                || field.getCoordinates(this).equals(target))) {
            targetResetIterations = 0;
            setNewRandomTarget();
        }

        if(!carrying) {
            findDrunkMen();
        }

        super.nextIteration();
    }

    @Override
    public char getSymbol() {
        return '^';
    }

    @Override
    public void interact(DrunkMan drunkMan) {
        if (drunkMan.getState() != DrunkMan.State.WALK) {
            picturedDrunkMen.add(drunkMan);
            if (picturedDrunkMen.size() >= picturesNeeded) {
                target = origin;
                carrying = true;
            }
        }
    }

    public void findDrunkMen() {
        class VisitedPoint {
            Point point;
            int dist;

            VisitedPoint(Point point, int dist) {
                this.point = point;
                this.dist = dist;
            }
        }

        Set<Point> visited = new HashSet<Point>();
        Queue<VisitedPoint> queue = new LinkedList<VisitedPoint>();

        queue.add(new VisitedPoint(field.getCoordinates(this), 0));
        visited.add(field.getCoordinates(this));

        while (!queue.isEmpty()) {
            VisitedPoint current = queue.poll();
            if (current.dist < RADIUS) {
                for (Point neighbor : field.getAvailableTurns(current.point)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(new VisitedPoint(neighbor, current.dist + 1));
                    }
                }
            }
            if (!field.isFree(current.point)) {
                if(field.getObjectAt(current.point) != this) {
                    field.getObjectAt(current.point).initInteraction(this);
                }
            }
        }
    }
}
