/*
 * File: MouseReporter.java
 * -----------------------------
 * Output the location of the mouse to a label on the
 * screen. Change the color of the label to red when
 * the mouse touches it.
 */
package edu.cis;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;
import acm.program.*;

public class MouseReporter extends GraphicsProgram {

	// A constant for the x value of the label
	private static final int INDENT = 20;

	// This variable is visible to the entire program
	// It is called an "instance" variable
	private GLabel label = new GLabel("");

	public void run() {
		// this code already adds the label to the screen!
		// run it to see what it does.
		label.setFont("Courier-24");
		label.setColor(Color.BLUE);

		// this setLabel method takes in a "String" 
		// you can concatenate integers and commas as such:
		label.setLabel(0 + "," + 0);

		// add the label to the screen!
		add(label, INDENT, getHeight()/2);

	}

	public static void main(String[] args) {
		new MouseReporter().start();
	}

	public void mousePressed(MouseEvent e) {


	}

	//Create a setUpBricks() method that creates each GRect, places it at the correct coordinate and adds it to the canvas .

	public void mouseMoved(MouseEvent e) {
		GLabel l = getElementAt(INDENT, getHeight()/2);
		l.setLabel(e.getX() + ", " + e.getY());

		if (contains(e.getX(),e.getY(),l)) {

			l.setColor(Color.RED);

		} else {

			l.setColor(Color.BLUE);

		}
	}

	public boolean contains(int x, int y, GLabel l) {
		if (x > l.getX() && x < l.getX() + l.getWidth()) {

			if (y < l.getY() && y > l.getY() - l.getHeight()) {

				return true;

			}

		}

		return false;

	}

}
