package edu.cis;

import acm.graphics.*;
import acm.program.*;
import acm.util.SoundClip;

import java.io.File;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.lang.Math;
import java.util.Random;

public class Breakout extends GraphicsProgram {

	// Dimensions of the canvas, in pixels
	// These should be used when setting up the initial size of the game,
	// but in later calculations you should use getWidth() and getHeight()
	// rather than these constants for accurate size information.
	public static final double CANVAS_WIDTH = 420;
	public static final double CANVAS_HEIGHT = 600;

	// Number of bricks in each row
	public static final int NBRICK_COLUMNS = 10;

	// Number of rows of bricks
	public static final int NBRICK_ROWS = 10;

	// Separation between neighboring bricks, in pixels
	public static final double BRICK_SEP = 4;

	// Width of each brick, in pixels
	public static final double BRICK_WIDTH = Math.floor(
			(CANVAS_WIDTH - (NBRICK_COLUMNS + 1.0) * BRICK_SEP) / NBRICK_COLUMNS);

	// Height of each brick, in pixels
	public static final double BRICK_HEIGHT = 8;

	// Offset of the top brick row from the top, in pixels
	public static final double BRICK_Y_OFFSET = 70;

	// Dimensions of the paddle
	public static final double PADDLE_WIDTH = 60;
	public static final double PADDLE_HEIGHT = 10;

	// Offset of the paddle up from the bottom
	public static final double PADDLE_Y_OFFSET = 100;

	// Radius of the ball in pixels
	public static final double BALL_RADIUS = 10;

	// The ball's vertical velocity.
	public static double VELOCITY_Y = -3.0;

	// The ball's minimum and maximum horizontal velocity; the bounds of the
	// initial random velocity that you should choose (randomly +/-).
	public static final double VELOCITY_X_MIN = 1.0;
	public static final double VELOCITY_X_MAX = 3.0;

	// Animation delay or pause time between ball moves (ms)
	public static final double DELAY = 1000.0 / 60.0;

	// Number of turns
	public static final int NTURNS = 3;

	public static long FPS = 60;
	public static double VELOCITY_X = 1.0;
	public GRect paddle;

	public GRect[] bricks = new GRect[NBRICK_COLUMNS * NBRICK_ROWS];

	//A string of balls so I can put more balls
	private Ball[] balls = new Ball[2];
	//counting variable for balls
	int ballCount = 1;
	//scoring variables
	private int totalScore;
	public double scorePositionX = CANVAS_WIDTH * 0.9;
	public GLabel scoreBoard = new GLabel("" + totalScore, CANVAS_WIDTH * 0.9, 35);
	public GLabel scoreHeading = new GLabel("Score: ", CANVAS_WIDTH * 0.871, 20);
	//lives variables
	private int lives;
	public double livesPositionX = CANVAS_WIDTH * 0.1;
	public GLabel livesBoard = new GLabel("" + lives, CANVAS_WIDTH * 0.1, 35);
	public GLabel livesHeading = new GLabel("Lives: ", CANVAS_WIDTH * 0.1, 20);
	public String winText = "You Won!";
	public String loseText = "You lost";
	public Font bannerFont = new Font("Serif", Font.PLAIN, 30);
	public GLabel banner = new GLabel("", CANVAS_WIDTH / 2 - 40, CANVAS_HEIGHT / 2 - 10);
	public boolean pause = false;
	public GLabel restart = new GLabel("press r to restart", CANVAS_WIDTH / 2 - 40, CANVAS_HEIGHT / 2 + 10);
	public GLabel roundBoard = new GLabel("", CANVAS_WIDTH / 2 - 40, 35);
	public GLabel roundHeading = new GLabel("Round: ", CANVAS_WIDTH / 2 - 40, 20);
	public double roundPositionX = CANVAS_WIDTH * 0.5;
	private int round;
	//sound variables
	public SoundClip[] soundHit = new SoundClip[5];
	public SoundClip soundWin;
	public SoundClip soundlose;
	public SoundClip soundLifeLost;

	//button to switch on switch off the sound when testing
	boolean enableSound = true;
	public GImage winImage = new GImage(System.getProperty("user.dir") + "/winImage.jpg", CANVAS_WIDTH / 2 - 110 / 2, CANVAS_HEIGHT / 2 + 20);
	public GImage lostImage = new GImage(System.getProperty("user.dir") + "/lostImage.jpg", CANVAS_WIDTH / 2 - 80 / 2, CANVAS_HEIGHT / 2 + 20);
	public Random random = new Random();

	public void run() {

		// Set the window's title bar text
		setTitle("CIS Y11 CS Breakout");
		setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		setUpBricks();
		setUpPaddle();
		createBall();
		add(scoreBoard);
		add(scoreHeading);
		add(livesBoard);
		add(livesHeading);
		setLives(2);
		setScore(0);
		add(banner);
		banner.setFont(bannerFont);
		add(roundBoard);
		add(roundHeading);
		setRound(1);
		restart.setX(CANVAS_WIDTH / 2 - restart.getWidth() / 2);
		loadSounds();
		winImage.setSize(110, 80);
		lostImage.setSize(80, 80);


		// Set the canvas size.  In your code, remember to ALWAYS use getWidth()
		// and getHeight() to get the screen dimensions, not these constants!

		/* You fill this in, along with any subsidiary methods */

		play();
	}

	private void play() {

		long prevTime = 0;

		while (true) {

			//how long its been since last time in ran it
			long currentTime = System.currentTimeMillis();

			//Delay time for the ball
			if (currentTime - prevTime > DELAY) {

				prevTime = System.currentTimeMillis();

				if (!pause) {

					//do the movements & bounce off the wall
					for (Ball ball : balls) {

						if (ball.isAlive) {

							ball.update();

						}

					}

					checkForCollisions();

				}

			}

		}

	}

	//checks for the number of balls in play
	public int ballsInPlay() {

		int count = 0;
		for (Ball ball : balls) {

			if (ball.isAlive) {

				count++;

			}

		}

		return count;

	}

	//plays the sounds when you collide with something
	public void playHitSounds() {

		Random rand = new Random();
		int randomNumber = rand.nextInt(0, 5);
		playSound(soundHit[randomNumber]);

	}

	//plays sounds
	public void playSound(SoundClip sound) {

		if (enableSound) {

			sound.play();

		}

	}


	//load all sound files
	public void loadSounds() {

		File file = new File(System.getProperty("user.dir") + "/hit1.wav");
		soundHit[0] = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/hit2.wav");
		soundHit[1] = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/hit3.wav");
		soundHit[2] = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/hit4.wav");
		soundHit[3] = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/hit5.wav");
		soundHit[4] = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/winSound.wav");
		soundWin = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/loseSound.wav");
		soundlose = new SoundClip(file);
		file = new File(System.getProperty("user.dir") + "/lifelost.wav");
		soundLifeLost = new SoundClip(file);

	}


	//increase speed of the ball after a round
	public void onWonRound() {

		if (round < 3) {

			//add a sound
			setRound(round + 1);
			resetBricks();

			if (round == 3) {

				ballCount = 2;

			}
			resetBallPositions();
			for (Ball ball : balls) {

				ball.setVelocity_x(ball.getVelocity_x() * 1.25);
				ball.setVelocity_y(ball.getVelocity_y() * 1.25);

			}


		} else {

			onGameWon();
		}


	}

	//when a life is lost
	public void onLifeLost(int amount) {

		lives -= amount;
		livesBoard.setLabel("" + lives);

		if (lives < 0) {

			onGameOver();
			setLives(0);


		} else {

			playSound(soundLifeLost);
			resetBallPositions();

		}

	}

	//game over screen
	public void onGameOver() {

		banner.setLabel(loseText);
		banner.setX(CANVAS_WIDTH / 2 - banner.getWidth() / 2);
		pause = true;
		add(restart);
		playSound(soundlose);
		add(lostImage);

	}

	//game won screen
	public void onGameWon() {

		banner.setLabel(winText);
		banner.setX(CANVAS_WIDTH / 2 - banner.getWidth() / 2);
		//System.out.println("done!");
		pause = true;
		add(restart);
		playSound(soundWin);
		add(winImage);

	}

	//setter for number of lives
	private void setLives(int amount) {

		lives = amount;
		livesBoard.setLabel("" + lives);
		//make center
		livesBoard.setX(livesPositionX - livesBoard.getWidth() / 2);
		livesHeading.setX(livesPositionX - livesHeading.getWidth() / 2);

	}

	//setter for number of rounds
	private void setRound(int amount) {

		round = amount;
		roundBoard.setLabel("" + round);
		//make center
		roundBoard.setX(roundPositionX - roundBoard.getWidth() / 2);
		roundHeading.setX(roundPositionX - roundHeading.getWidth() / 2);

	}

	//reset the balls, bricks
	private void resetGame() {

		ballCount = 1;
		setScore(0);
		setLives(2);
		resetBricks();
		setRound(1);
		createBall();
		resetBallPositions();
		pause = false;
		//System.out.println("hi");
		banner.setLabel("");
		remove(restart);
		remove(lostImage);
		remove(winImage);

	}

	public void resetBricks() {

		for (int j = 0; j < NBRICK_COLUMNS; j++) {

			for (int i = 0; i < NBRICK_ROWS; i++) {

				bricks[i + j * NBRICK_COLUMNS].setX((BRICK_WIDTH + BRICK_SEP) * j + 6.2);
				bricks[i + j * NBRICK_COLUMNS].setY(i * (BRICK_HEIGHT + BRICK_SEP) + BRICK_Y_OFFSET);


			}
		}

	}

	//check if all bricks are destroyed
	public boolean isAllBricksDestroyed() {

		for (int i = 0; i < bricks.length; i++) {

			if (bricks[i].getX() != -1000) {

				return false;

			}

		}

		return true;

	}

	//All collisions are checked here
	public void checkForCollisions() {

		boolean hasBounced = false;

		//loop through the balls in the game
		for (Ball ball : balls) {

			//skips the rest of that loop if the ball is not alive. So it won't check for collisions with THAT ball.
			if (!ball.isAlive) {

				//
				continue;

			}
			//check for paddle colliding with ball
			if (paddle.contains(ball.getX(), ball.getY() + ball.getHeight()) ||
					paddle.contains(ball.getX() + ball.getHeight(), ball.getY() + ball.getHeight())
			) {

				double ballCenter = ball.getX() + ball.getWidth() / 2;
				double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
				//how many off to the side, this allows the players to aim the ball with their paddle
				// so the ball doesn't just go randomly everytime it collides with the ball
				double offSet = -(paddleCenter - ballCenter) / 15;
				ball.setVelocity_x(ball.getVelocity_x() + offSet);
				ball.setVelocity_y(-Math.abs(ball.getVelocity_y()));
				playHitSounds();

			}

			//checks if the ball is still within the canvas
			if (ball.getY() > CANVAS_HEIGHT) {

				ball.isAlive = false;
				remove(ball);

				if (ballsInPlay() == 0) {

					onLifeLost(1);

				}

			}

			//checks if the ball collides with the brick, so we can "destroy the bricks" (move the bricks to somewhere else)
			for (int i = 0; i < bricks.length; i++) {

				if (bricks[i].contains(ball.getX(), ball.getY() + ball.getHeight()) ||
						bricks[i].contains(ball.getX() + ball.getHeight(), ball.getY() + ball.getHeight()) ||
						bricks[i].contains(ball.getX(), ball.getY()) ||
						bricks[i].contains(ball.getX() + ball.getWidth(), ball.getY())) {

					if (!hasBounced) {

						//checks for when the ball collides vertically with the bricks
						if (isVerticalCollision(bricks[i], ball)) {

							ball.setVelocity_y(-ball.getVelocity_y());

						} else {

							ball.setVelocity_x(-ball.getVelocity_x());

						}
					}

					playHitSounds();
					//move the bricks to somewhere else when it is destroyed
					bricks[i].setX(-1000);
					hasBounced = true;
					addScore(getRowScore(bricks[i].getFillColor()));

					//see if all bricks are destroyed for win condition
					if (isAllBricksDestroyed()) {

						onWonRound();

					}


				}
			}
		}

	}

	//checks for if the ball vertically collides with the bricks so it doesn't destroy multiple bricks at the same time
	private boolean isVerticalCollision(GRect brick, GOval ball) {

		double brickCenterX = brick.getX() + brick.getWidth() / 2;
		double brickCenterY = brick.getY() + brick.getHeight() / 2;

		double ballCenterX = ball.getX() + ball.getWidth() / 2;
		double ballCenterY = ball.getY() + ball.getHeight() / 2;

		double differenceX = Math.abs(brickCenterX - ballCenterX);
		double differenceY = Math.abs(brickCenterY - ballCenterY);

		differenceY += Math.abs(brick.getHeight() - brick.getWidth()) / 2;
		//System.out.println(differenceX + ", " + differenceY);

		if (differenceY > differenceX) {

			return true;

		} else {

			return false;

		}

	}


	private void setUpBricks() {

		for (int j = 0; j < NBRICK_COLUMNS; j++) {

			for (int i = 0; i < NBRICK_ROWS; i++) {

				GRect brick = new GRect((BRICK_WIDTH + BRICK_SEP) * j + 6.2, i * (BRICK_HEIGHT + BRICK_SEP) + BRICK_Y_OFFSET, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFillColor(getRowColor(i));
				brick.setFilled(true);
				add(brick);
				bricks[i + j * NBRICK_COLUMNS] = brick;


			}

		}

	}

	private void setUpPaddle() {

		paddle = new GRect((CANVAS_WIDTH / 2) - PADDLE_WIDTH / 2, CANVAS_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle);
		//change

	}

	private void createBall() {


		for (int i = 0; i < balls.length; i++) {

			if (balls[i] == null) {

				balls[i] = new Ball(BALL_RADIUS, BALL_RADIUS);
				balls[i].setFillColor(Color.BLACK);
				balls[i].setFilled(true);


			}

			//sets the x velocity of the ball to a random one once it's launched
			balls[i].setVelocity_x(random.nextDouble(1.0, 3.0));
			balls[i].setVelocity_y(3.0);


			//adds the ball to the screen and alives it if you're less than the required ball count,
			// and removes and unalives it if you're over
			if (i < ballCount) {

				add(balls[i]);
				balls[i].isAlive = true;

			} else {

				remove(balls[i]);
				balls[i].isAlive = false;

			}

		}

	}

	//resets the ball into the middleaR
	private void resetBallPositions() {

		int count = 0;

		for (Ball ball : balls) {

			if (count < ballCount) {

				ball.isAlive = true;
				add(ball);
				ball.setX(CANVAS_WIDTH / 2 - ball.getWidth() / 2);
				ball.setY(CANVAS_HEIGHT / 2 - ball.getHeight() / 2);

			}

			count++;
		}

	}

	private void addScore(int amount) {

		//System.out.println(amount);
		totalScore += amount;
		scoreBoard.setLabel("" + totalScore);
		scoreBoard.setX(scorePositionX - scoreBoard.getWidth() / 2);
		scoreHeading.setX(scorePositionX - scoreHeading.getWidth() / 2);

	}

	public void setScore(int amount) {

		totalScore = amount;
		scoreBoard.setLabel("" + totalScore);
		scoreBoard.setX(scorePositionX - scoreBoard.getWidth() / 2);
		scoreHeading.setX(scorePositionX - scoreHeading.getWidth() / 2);

	}

	private int getRowScore(Color color) {

		if (color.equals(Color.red)) {
			return 50;
		} else if (color.equals(Color.orange)) {
			return 40;
		} else if (color.equals(Color.yellow)) {
			return 30;
		} else if (color.equals(Color.green)) {
			return 20;
		} else if (color.equals(Color.blue)) {
			return 10;
		} else if (color.equals(new Color(127, 0, 255))) {
			return 5;
		}
		return 0;

	}

	private Color getRowColor(int row) {

		//red, orange, yellow, green, blue, indigo, and violet
		switch (row) {
			case 0:
				return Color.red;
			case 1:
				return Color.red;
			case 2:
				return Color.orange;
			case 3:
				return Color.orange;
			case 4:
				return Color.yellow;
			case 5:
				return Color.green;
			case 6:
				return Color.green;
			case 7:
				return Color.blue;
			case 8:
				return Color.blue;
			case 9:
				return new Color(127, 0, 255);
			default:
				return Color.black;
		}

	}

	public void mouseMoved(MouseEvent e) {

		double mousePosition = e.getX();
		if (e.getX() > CANVAS_WIDTH - (paddle.getWidth() / 2)) {

			mousePosition = CANVAS_WIDTH - (paddle.getWidth() / 2);

		} else if (e.getX() < paddle.getWidth() / 2) {

			mousePosition = 0 + (paddle.getWidth() / 2);

		}

		paddle.setX(mousePosition - (paddle.getWidth() / 2));

	}

	public void keyPressed(KeyEvent e) {

		//System.out.println("Press Kcode");

		switch (e.getKeyCode()) {

			//debuging condition
			case KeyEvent.VK_P:

				if (!pause) {

					onWonRound();

				}

				break;

			case KeyEvent.VK_R:

				if (pause) {

					resetGame();
				}

				break;
		}

	}

	public static void main(String[] args) {
		new Breakout().start();
	}

}
