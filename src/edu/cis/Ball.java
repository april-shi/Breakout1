package edu.cis;

import acm.graphics.GOval;
import stanford.cs106.util.RandomGenerator;

import java.awt.*;

public class Ball extends GOval {

    boolean isAlive = false;
    private double velocity_x;
    private double velocity_y;

    public Ball(double width, double height) {

        super(Breakout.CANVAS_WIDTH / 2 - (width / 2), Breakout.CANVAS_HEIGHT / 2 - (height / 2), width, height);
        RandomGenerator rgen = RandomGenerator.getInstance();
        if (rgen.nextBoolean(0.5)) velocity_x = -velocity_x;

    }

    public double getVelocity_x() {
        return velocity_x;
    }

    public double getVelocity_y() {
        return velocity_y;
    }

    public void setVelocity_x(double velocity_x) {
        this.velocity_x = velocity_x;
    }

    public void setVelocity_y(double velocity_y) {
        this.velocity_y = velocity_y;
    }

    //make the ball change direction when it collides with the canvas border
    public void update() {

        move(velocity_x, velocity_y);

        if (getY() < 0) {

            velocity_y = -velocity_y;

        }

        if (getX() < 0) {

            velocity_x = -velocity_x;

        }

        if (getX() + getWidth() > Breakout.CANVAS_WIDTH) {

            velocity_x = -velocity_x;

        }


    }

}
