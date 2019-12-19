/*
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
“Space Alien” Grid Game Program, called "Alien"
Alex Gan
6/03/2017
Java, Version: Neon.2 Release (4.6.2)
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
Problem Definition:
			This assignment requires a mathematical game to help young math students learn about the Cartesian plane and 
		locate items on a grid. Students will learn about mathematical concepts of using grid coordinates to target 
		specific location on a 2D grid in a fun and challenging way.
Solution:
			Instead of having the users to enter coordinates of aliens, a mouse clicking based game is made (though another 
		reason is that since the grid required looks different from a standard Cartesian plane, it is better not to let the 
		users mix them up). The goal is for the user to click on as many aliens as possible within a given time limit, while
		showing the coordinates of the previous alien that appeared.
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
List of Identifiers:

===Global:===

---Type int:---
	scale 			- length of every square in the grid
	numOfGrid 		- number of squares in the grid
	startCoord 		- x/y length from the top left corner of the applet to the top left corner of the grid
	userIX 			- x location for all buttons and other features on the right side of the grid
	count 			- number of rounds (how many aliens appeared)
	userScore 		- the user's score
	userX 			- x location of where the mouse clicked by the user
	userY 			- y location of where the mouse clicked by the user
	imageXCoord 	- x location of appearing alien
	imageYCoord 	- y location of appearing alien
	userLives		- maximum number of user's lives
	userLivesInit 	- for resetting userLives's initial value
	SIZE			- size of arrays that sort and store user scores & names
	
---Type double:---
	capturedCount	- number of aliens user captured
	userAccuracy	- user's accuracy
	startTime		- the time when an alien appears
	endTime			- the time when the user clicks on the alien
	userTime		- the maximum time allowed for each game
	userTimeInit	- for resetting userTime's initial value
	
---Type boolean:---
	switch1			- signals when user first click on an alien (tells when the real starts)
	alienClicked	- signals whether the user captured an alien or not
	gameResetSwitch - signals when the game stops
	showLocSwitch	- signals when user chooses to see all past aliens
	startGame		- signals when the game initially starts
	inputName		- signals when user enter a valid name to begin game
	intro			- only used once in the entire program, used to display introductory message
	version2Global  - signals when user chooses to play the Blitz Version of the game

---Type String:---
	username		- the player's name
	fontName		- the name of the majority fonts 

---Arrays:---
	noRepeat[][] (type boolean) - for storing past alien locations so they will not appear in the same spot
	showLocation[][] (type int)	- store all alien locations so user may choose to see them when game ends
	userHighScore[][](type int) - store the top 10 highest user's scores
	usernames[] (type String)	- store the player's names that corresponds to the scores in userHighScore[][]
_________________________________________________________________________________________________________________________
---DecimalFormats:---
	df				- only decimal format used, always to two decimal places
	
---Timers:---
	timer 			- timer that counts down (should be 20 seconds) the time user has left for one game
	
---Buttons:---
	quitGameButton	- button to exit and close the game should the user choose to
	showLocButton	- (does not show in-game) shows at end of every game, and displays all alien (appeared) coordinates
	startButton		- (does not show in-game) starts the game by first making the user enter their name
	helpButton		- (does not show in-game) gives the user all the information they need to know how to play
	settingButton	- (does not show in-game) change the size of window&grid and playing style of game, etc.

---Images:---
	alienImage		- image of alien
	
---Labels:---
	scoreLabel		- shows the user's scores
	livesLabel		- title "Lives:" which appears before user lives (in heart shaped)
	accuracyLabel	- shows user's accuracy for clicking aliens
	timerLabel		- shows the time user has left (and during countdown)
	leaderBoardLabel- title of the leaderboard
	titleLabel		- title of the game "Alien"
	
---Fonts:---
	standardFont	- most commonly used font in the program
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
*NOTE:
	- 'scale' should be 42 if 'numOfGrid' is 21, and 65 if 'numOfGrid' is 11
	- Local Variables are stated & described in code
	- scale, numOfGrid, startCoord. Together these make up the locations of all the graphic interfaces, and allows the 
	  mobility to move all the graphics just by changing these variables
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
*/
import java.io.*;				//allow access to the coding within the io library 
import java.applet.*;			//import applet
import java.awt.*;				//import Abstract Window Toolkit
import java.awt.event.*;		//import AWT event
import java.text.DecimalFormat;	//import DecimalFormat
import javax.swing.*;			//import swing

import java.net.URL;
import javax.sound.sampled.*;

public class finalProjectAlien extends Applet implements ActionListener, MouseListener {//start of finalProjectAlien class

	DecimalFormat df = new DecimalFormat("0.00");									//declaration & instantiation of DecimalFormat df
	Timer timer;																	//main timer
	Button quitGameButton, showLocButton, startButton, helpButton, settingButton;	//buttons
	Image alienImage;																//picture of the alien
	Label scoreLabel, livesLabel, accuracyLabel, timerLabel = new Label(""), leaderboardLabel, titleLabel;//labels
	Font standardFont = new Font("Arial", Font.PLAIN, 20);							//most used font

	int scale = 42, numOfGrid = 21, startCoord = 70, userIX = startCoord + numOfGrid * scale + 30, count = 0,
			userScore = 0, userX, userY, imageXCoord, imageYCoord, userLives = 3;
	int userLivesInit = userLives;// for reset purposes
	int SIZE = 12;// for sorting scores

	double capturedCount = 0, userAccuracy = 0, startTime, endTime, userTime = 20;
	double userTimeInit = userTime;// for reset purposes
	boolean switch1 = false, alienClicked = false, gameResetSwitch = false, showLocSwitch = false, startGame = false,
			inputName = false, version2Global = false, intro = false;
	String username, fontName = "Monospaced";

	boolean noRepeat[][] = new boolean[numOfGrid + 2][numOfGrid + 2];
	int showLocation[][] = new int[numOfGrid * numOfGrid][2];
	int userHighScore[] = new int[SIZE];// for scoring user score
	String usernames[] = new String[SIZE];
	
	public static Clip inGameClip, backgroundClip;

	/**init method
	 * This method readies the program by:
	 * - initialize all buttons and labels
	 * - set colours and applet size
	 * - receives the scores file data to game
	 * - change mouse appearance
	 * - calls the changeSize method
	 * - preparing the applet
	 * 
	 * @return void
	 */
	public void init() {
		setSize(1300, 1000);								//set size of applet
		setBackground(Color.black);							//set background color
		try {
			outputFile();									//get saved user high score file
		} catch (IOException e1) {
		}

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("cursorImage.png").getImage(),
				new Point(0, 0), ""));						//set cursor image

		Font bigFont = new Font(fontName, Font.PLAIN, 30);	//initialize fonts
		Font titleFont = new Font(fontName, Font.PLAIN, 60);
		Font myFont2 = new Font(fontName, Font.PLAIN, 16);
		Font myFont = new Font(fontName, Font.BOLD, 25);
		Font myFont3 = new Font(fontName, Font.PLAIN, 20);

		scoreLabel = new Label("Score: 0");					//initialize labels 
		livesLabel = new Label("Lives:");
		accuracyLabel = new Label("Accuracy: 0%");
		leaderboardLabel = new Label("LeaderBoard");
		titleLabel = new Label("ALIEN");
		timerLabel.setText("Time left: " + String.valueOf(userTime) + "z");
		
		scoreLabel.setFont(bigFont);						//give labels custom fonts
		livesLabel.setFont(myFont);
		accuracyLabel.setFont(myFont3);
		timerLabel.setFont(bigFont);
		leaderboardLabel.setFont(bigFont);
		titleLabel.setFont(titleFont);
		setForeground(Color.green);							//set labels to color green

		quitGameButton = new Button("Quit Game");			//initialize buttons
		showLocButton = new Button("Show all\n Alien Location");
		startButton = new Button("Start Game");
		helpButton = new Button("Help");
		settingButton = new Button("Settings");

		startButton.setBackground(Color.red);				//give buttons different text/background colors
		showLocButton.setForeground(Color.black);
		startButton.setForeground(Color.black);
		helpButton.setForeground(Color.black);
		settingButton.setForeground(Color.black);
		quitGameButton.setForeground(Color.black);

		quitGameButton.addActionListener(this);				//add action listener to buttons
		showLocButton.addActionListener(this);
		startButton.addActionListener(this);
		helpButton.addActionListener(this);
		settingButton.addActionListener(this);

		quitGameButton.setFont(myFont2);					//give buttons different fonts
		startButton.setFont(myFont3);
		helpButton.setFont(myFont2);
		settingButton.setFont(myFont2);

		boolean b = false;									//'b'(boolean) tells applet first set Default Version
		changeSize(b);										//call the change size method 

		add(scoreLabel);									//all all labels and buttons
		add(livesLabel);
		add(accuracyLabel);
		add(timerLabel);
		add(leaderboardLabel);
		add(titleLabel);

		add(quitGameButton);
		add(showLocButton);
		add(startButton);
		add(helpButton);
		add(settingButton);

		showLocButton.setVisible(false);					//temporarily hides showLocButton

		addMouseListener(this);								//add mouse listener
		setLayout(null);									//set labels/buttons' layouts
		
		try {
			backgroundMusic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end init method

	/**paint method
	 * This procedural method which is called automatically calls 4 methods:
	 * 		1.drawUserLives 2.drawBackGround 3.captureAlien 4.generateImage
	 * and serves as the main method of the program
	 * 
	 * @param g-graphics object used to call methods that draws the entire program
	 * 
	 * @return void
	 */
	public void paint(Graphics g) {			
		drawUserLives(g);				//call drawUserLives method
		drawBackGround(g);				//call drawBackGround method
		if (gameResetSwitch == false) {	//if game started
			captureAlien(g);			//call captureAlien method
			generateImage(g);			//call generateImage method
		} 								//end if statement
		if (intro==false)
			intro();					//call intro method
	}// end paint(main) method
	
	/**intro method
	 * This method introduces the game with a story that hints the user
	 * what they will be doing
	 * 
	 * @return void
	 */
	public void intro(){
		intro = true;
		JOptionPane.showMessageDialog(null,
		"<html><span style='font-size:5em'><font color='red'>!!!WARNING!!!ALIEN!!!\n\n"
		+ "<html><span style='font-size:1.4em'>....*...*.... Distress signal ... BEEP... BEEP... calling Foehn Revolt HQ: Distress\n"
		+ "<html><span style='font-size:1.4em'>signal: flag ship under ambush attack by aliens ... they are blue colored, eye\n"
		+ "<html><span style='font-size:1.4em'>shaped monster that appear without warning ... BEEP... user fighter 'mouse' to\n"
		+ "<html><span style='font-size:1.4em'>click on them and terminate them all ... you have 20 seconds ... signal lost ..*...\n"
		,"*...DISTRESS SIGNAL...*", JOptionPane.INFORMATION_MESSAGE);//shows JOptionPane that tells user the background story of game
	}//end intro method
	
	/**changeSize method
	 * This method sets the locations of all labels and buttons, while
	 * having the function to change the locations, if the user choose so
	 * 
	 * @param  version2	- signals when user chooses to play the Blitz Version of the game
	 * @return void
	 */
	public void changeSize(boolean version2) {
		if (version2 == true) {												//if Blitz Version of game is chose
			numOfGrid = 11;													//set Blitz Version's perspective sizes
			scale = 65;
			setSize(1120, 830);
		} else {															//if Default Version of game is chose
			numOfGrid = 21;													//set Default Version's perspective sizes
			scale = 42;
			setSize(1300, 1000);
		}
		userIX = startCoord + numOfGrid * scale + 30;						//resets userIX 

		scoreLabel.setBounds(userIX, scale * numOfGrid / 2 + 140, 200, 25);	//set labels' sizes/locations
		livesLabel.setBounds(50, 23, 100, 30);
		accuracyLabel.setBounds(userIX, scale * numOfGrid / 2 + 100, 200, 20);
		timerLabel.setBounds(userIX - scale * numOfGrid / 4, 15, 330, 50);
		leaderboardLabel.setBounds(userIX + 30, 80, 200, 30);
		titleLabel.setBounds(numOfGrid * scale / 2, 10, 300, 50);

		quitGameButton.setBounds(userIX, userIX - 60, 150, 30);			   	//set buttons' sizes/locations
		showLocButton.setBounds(userIX, userIX - 190, 150, 40);
		startButton.setBounds(userIX, userIX - 260, 150, 50);
		helpButton.setBounds(userIX, userIX - 140, 150, 30);
		settingButton.setBounds(userIX, userIX - 100, 150, 30);
	}//end changeSize method

	/**constructor
	 * This constructor is a timer which count down the total time allowed for each game
	 */
	public finalProjectAlien() {
		timer = new Timer(10, new ActionListener() {//declare & instantiate timer
			public void actionPerformed(ActionEvent e) {
				userTime -= 0.01;					//decrease time by 1/100th of a second
				if (userTime >= 0) {				//if there's still time remaining, display time
					timerLabel.setText("Time left: " + String.valueOf(df.format(userTime)) + "z");
				} else {
					timer.stop();					// stops timer
					timerLabel.setText("Time left: 0.00z");
					JOptionPane.showMessageDialog(null, "Game Over", "Time's up!", JOptionPane.INFORMATION_MESSAGE);
					gameReset1();					//call gameReset1 method
				}
			}//end actionPerformed method
		});//end timer
	}//end constructor
	
	/**saveFile method
	 * This method saves the user scores & names into a text file
	 * 
	 * @throws IOException
	 * @return void
	 */
	public void saveFile() throws IOException {
		PrintWriter output = new PrintWriter(new FileWriter("scores.txt"));//declare & instantiate text file
		int count = 0;									//'count'(int) to change index value of arrays
		for (int i = 0; i < 20; i++) {					
			if (i % 2 == 0) {
				output.println(usernames[count]);		//save user's name
			} else {
				output.println(userHighScore[count]);   //save user's score
				count++;								//increment count
			}
		}//end for loop for saving names/scores
		output.close();//close PrintWriter
	}//end saveFile method

	/**outputFile method
	 * This method reads the saved user scores & names from the text file
	 * and copy them to the program's arrays
	 * 
	 * @throws IOException
	 * @return void
	 */
	public void outputFile() throws IOException {
		FileReader fr = new FileReader("scores.txt");	//declare & instantiate FileReader
		BufferedReader br = new BufferedReader(fr);	 	//declare & instantiate BufferedReader

		String line = null, userHighScoreStr;			//'line'(String) as lines read from text file
			//'userHighScoreStr'(String) as temp converter from line to userHighScore array
		int index = 0, count = 0;						//'count'(int) to change index of arrays
			//'index'(int) to change lines read from text file
		boolean inputSwitch = true;						//'inputSwitch'(boolean) change lines between name/score 

		while ((line = br.readLine()) != null && index < userHighScore.length * 2) {
			if (inputSwitch == true) {					
				usernames[count] = line;//set usernames[count] to what's on the line of text file
				inputSwitch = false;	//set inputSwitch to false
			} else {
				userHighScoreStr = line;//set userHighScoreStr to line
				try{					//in case someone dares to change the scores in the text file to a string
					userHighScore[count] = Integer.parseInt(userHighScoreStr);//parse to integer
				}catch(NumberFormatException e){
					line = "0";			
				}
				count++;				//increment count
				inputSwitch = true;		//set inputSwitch to true
			}
			index++;					//increment index
		}//end while loop for output text file
		br.close();
	}//end outputFile method

	/**inputUsername method
	 * This method creates JOptionPanes that makes the user to enter their names
	 * before the game starts
	 * 
	 * @return void
	 */
	public void inputUsername() {
		String[] options = { "Start Game!!" };					//custom make JOptionPanel
		String[] options2 = { "Cancel" };
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel("Enter Your name: (no spaces) ");
		JTextField txt = new JTextField(10);
		panel.add(lbl);
		panel.add(txt);

		String U;												//'U'(String) is a temp name entered by user
		String tempString = "";									//'tempString'(String) is compressed (no space) user's name
		boolean inputName=false;		//'inputName'(boolean) signals when user enter a valid name to begin game
		do {
			int selectedOption = JOptionPane.showOptionDialog(null, panel, "Lets begin by...", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options2);
			if (selectedOption == 0) {							
				U = txt.getText();								
				for (int i = 0; i < U.length(); i++) {
					char temp = U.charAt(i);					
					if (temp != ' ') {							
						tempString += temp;
					}
				} // end for loop for removing all spaces
				if (0 >= tempString.length() || 14 < tempString.length()) {
					JOptionPane.showMessageDialog(null, "Your name must have more than 0 and less than 15 characters",
							"Oops!", JOptionPane.WARNING_MESSAGE);
					tempString = "";							//reset tempString
				} // make user enter the right quantity of characters
				else {
					inputName = true;
					username = tempString;						//set username equal to tempString
				}
			}
		} while (inputName != true);							//forcing user to enter the right name 
		System.out.println(username);							//display on console for checking purpose (not for users)
	}//end inputUsername method

	/**gameReset method
	 * This method resets the game just before the game starts, by 
	 * hiding certain buttons & labels and resets certain variables
	 * 
	 */
	public void gameReset() {			//2nd reset marking the begin of a new game
		switch1 = false;				//reset selected buttons / variables / labels
		gameResetSwitch = false;
		alienClicked = false;
		startButton.setVisible(false);
		helpButton.setVisible(false);
		settingButton.setVisible(false);
		startGame = true;

		userScore = 0;
		userLives = userLivesInit;
		count = 0;
		userTime = userTimeInit;
		capturedCount = 0;
		userAccuracy = 0;

		scoreLabel.setText("Score: 0");

		showLocButton.setVisible(false);
		startButton.setVisible(false);
		timer.start();					 // starts timer
		System.out.println("Game reset");//display on console for checking purpose (not for users)

		try {
			inGameMusic();
		} catch (Exception e) {
			e.printStackTrace();
		}
		backgroundClip.stop();
		repaint();
	}//end gameReset method

	/**gameReset1 method
	 * This method partially resets the game when a game is over by:
	 * - making selected buttons available to the user 
	 * - sort the player's score into the leader board if needed 
	 * - save the sorted scores into the text file
	 * 
	 * @return void
	 */
	public void gameReset1() {			//1st reset when a game is over
		gameResetSwitch = true;			//reset selected variables and show certain buttons
		sortScore();					//sort user's score/name
		userScore = 0;
		inputName = false;
		showLocButton.setVisible(true);
		startButton.setVisible(true);
		helpButton.setVisible(true);
		settingButton.setVisible(true);
		try {
			saveFile();					//save leaderboard
		} catch (IOException ioe) {
		}
		
		try {
			backgroundMusic();
		} catch (Exception e) {
			e.printStackTrace();
		}
		inGameClip.stop();
		repaint();
	}//end gameReset1 method

	/**sortScore method
	 * Method that will sort users' scores along with their names
	 * This sorting eliminates the original lowest score & name, if the
	 * current user's score is higher than that lowest score
	 * 
	 * @return void
	 */
	public void sortScore() {
		for (int i = 0; i < userHighScore.length; i++)
			if (userScore > userHighScore[i]) {
				for (int c = userHighScore.length - 1; c > i; c--) {
					userHighScore[c] = userHighScore[c - 1];//the player to be replaced is placed lower in the leaderboard
					usernames[c] = usernames[c - 1];//same with the name, moves down one spot
				}
				userHighScore[i] = userScore;//store userScore to the array 'userHighScore'
				usernames[i] = username;	 //also store username to 'usernames' array
				i = userHighScore.length;	 
			}//end if statement if the user's score is higher than a past user's score on leaderboard
	}//end sortScore method

	/**captureAlien method
	 * The method calculates and updates about user's current
	 * status of the game 
	 * 
	 * @param g
	 * @return void
	 */
	public void captureAlien(Graphics g) {
		g.setColor(Color.blue);
		if (alienClicked == true) {
			endTime = System.nanoTime();					// end nanoTime
			g.drawString("You captured an Alien!", userIX, scale * numOfGrid / 2 + 209);
			capturedCount++;								// increment capturedCount
			scoreCalculation(g);							// call scoreCalculation method
		} else if (switch1 == true) 
			g.drawString("Fail to capture.", userIX, scale * numOfGrid / 2 + 209);
		
		if (switch1 == true) 
			userAccuracy = (capturedCount / count) * 100;	//calculate user accuracy
		accuracyLabel.setText("Accuracy: " + String.valueOf((int) userAccuracy) + "%");

		if (userLives == 0) {
			timer.stop();									// stops timer
			JOptionPane.showMessageDialog(null, "Game Over", "You have 0 lives left!", JOptionPane.INFORMATION_MESSAGE);
			gameReset1();									//call gameReset1 method
		}//end for loop used for when user has 0 lives
	}//end captureAlien method

	/**mousePressed method
	 * Invoked when a mouse button has been pressed on a component.
	 * When the user clicks on something during a game,
	 * it determines whether the user has clicked on an alien
	 * or failed to do so
	 * 
	 * @return void
	 */
	public void mousePressed(MouseEvent e) {
		if (gameResetSwitch == false && startGame == true) {
			switch1 = true;
			userX = e.getX();						//set userX to x coordinate of where the mouse clicked
			userY = e.getY();						//set userY to y coordinate of where the mouse clicked
			if (userX > imageXCoord && userX < imageXCoord + scale && userY > imageYCoord
					&& userY < imageYCoord + scale){ //if statement when user clicks on an alien
				alienClicked = true;
				try {
					clickAlienSound();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else {
				alienClicked = false;
				userLives--;
			}
			repaint();
		}//end if statement that is used only if the game has started & is not ending
	}//end mousePressed method

	/**scoreCalculation method
	 * This method calculates and displays user's scores 
	 * 
	 * @param g
	 * @return void
	 */
	public void scoreCalculation(Graphics g) {
		int bonusScore = 0;		//'bonusScore'(int) for temp scoring user's bonus score
		double timeDifference;	//'timeDifference'(double) calculating the time took for user to click on alien
		if (version2Global == false)
			userScore += 20;	//add regular points for an alien captured
		else					//else means user plays the Blitz Version, therefor less point per alien captured
			userScore += 13;										//less points if the Blitz Version is chose
		timeDifference = (endTime - startTime) / 1000000000;
		if (timeDifference <= 1.5) {
			if (version2Global == false)									//if user plays the Default version
				bonusScore = (int) ((1.5 - timeDifference) * 20);
			else
				bonusScore = (int) ((1.5 - timeDifference) * 15);	//less points if the Blitz Version is chose
			g.drawString("Captured within " + df.format(timeDifference) + "z: +" + bonusScore + "!!!", userIX,
					scale * numOfGrid / 2 + 269);
			userScore += bonusScore;								//add bonusScore to userScore
		}//end if statement (for the user clicks on an alien that is eligible for bonus points)

		if (capturedCount % 5 == 0) {
			userScore += 50;										//add combo scores 
			g.drawString("capturing " + (int) capturedCount + " aliens: +50!!!", userIX, scale * numOfGrid / 2 + 239);
		}//end combo score if statement
		scoreLabel.setText("Score: " + String.valueOf(userScore));
	}//end scoreCalculation method

	/**drawBackGround method
	 * This method draws most of the applet's visual effects, and calls
	 * several drawing methods that have different functions that include:
	 * 
	 * - custom applet title & icon image
	 * - drawing the grid 
	 * - drawing the score board
	 * - displays average time taken to click on an alien
	 * - counts the number of aliens appeared
	 * etc.
	 * 
	 * @param g
	 * @return void
	 */
	public void drawBackGround(Graphics g) {
		g.setColor(Color.green);

		Frame title;										//the followings are forcustomize icon image & applet title
		title = (Frame) this.getParent().getParent();
		title.setTitle("Alien");
		title.setIconImage(getImage(getDocumentBase(), "AlienPicture.png"));

		g.drawString("Alex Gan TM + " + "\u00a9 All Rights Reserved", userIX - 230, userIX - 10);
		g.setFont(standardFont);

		g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);//decorate edge of applet
		g.drawRect(userIX - 10, startCoord, 280, 330);			//surrounds the leaderboard

		if (switch1 == true && gameResetSwitch == false) {
			count++;											//increment count when game starts & before stops 
			System.out.println(count);							//display on console for checking purpose (not for users)
			drawUserCoord(g);									//call drawUserCoord method
		} else if (gameResetSwitch == true && capturedCount > 0)
			g.drawString("Average Time Taken: " + df.format((userTimeInit - userTime) / capturedCount) + "z", userIX,
					scale * numOfGrid / 2 + 80);				//display average time taken at the end of game

		if (showLocSwitch == true)
			showAlienLocation(g);								//call showAlienLocation method if user choose to
		
		drawScoreBoard(g);										//call drawScoreBoard method
		drawGrid(g);											//call drawGrid method
	}//end drawBackGround method

	/**drawScoreBoard method
	 * This method draws the leader board contents:
	 * - players' name, rank, and score
	 * 
	 * @param g
	 * @return void
	 */
	public void drawScoreBoard(Graphics g) {
		g.setColor(Color.white);
		g.setFont(standardFont);
		for (int i = 0; i < SIZE - 2; i++) {
			g.drawString(String.valueOf(i + 1) + ".", userIX, 150 + i * 26);
			g.drawString(usernames[i], userIX + 110, 150 + i * 26);
			g.drawString(String.valueOf(userHighScore[i]), userIX + 50, 150 + i * 26);
		}//end loop for drawing the leaderboard contents
	}//end drawScoreBoard method

	/**drawGrid method
	 * The method draws the grid (Cartesian plane), and 
	 * the numbers on the x and y lines
	 * 
	 * @param g
	 * @return void
	 */
	public void drawGrid(Graphics g) {
		g.setColor(Color.red);
		int count1 = 0;										//'count1'(int) used to help draw the grid
		int z = (numOfGrid - 1) / 2, y = (-numOfGrid) / 2;  //'z'/'y'(int) draws coordinates on x/y axis

		for (int i = startCoord; i <= startCoord + numOfGrid * scale; i += scale) {
			count1++;										//increment count1;
			g.drawLine(startCoord, i, startCoord + numOfGrid * scale, i);
			g.drawLine(i, startCoord, i, startCoord + numOfGrid * scale);

			if (count1 == (numOfGrid + 1) / 2) {
				g.drawRect(startCoord, i - 1, numOfGrid * scale, 2);
				g.drawRect(startCoord, i + scale - 1, numOfGrid * scale, 2);

				g.drawRect(i + scale + 1, startCoord, 1, numOfGrid * scale);
				g.drawRect(i + 1, startCoord, 1, numOfGrid * scale);
			}//end if statement for drawing thicker lines at the x/y axis
		} // end for loop for drawing grid & scale

		for (int i = 0; i < numOfGrid; i++) {
			g.drawString(String.valueOf(z), startCoord + scale * (numOfGrid + 1) / 2 + 5,
					i * scale + startCoord + scale / 2);
			g.drawString(String.valueOf(y), i * scale + startCoord + 15, startCoord + scale * (numOfGrid + 1) / 2 - 3);
			z--;
			y++;
		} // end for loop for drawing coordinates
	}//end drawGrid method

	/**drawUserCoord method
	 * This method draws the coordinates of the point where the user 
	 * just clicked on on the grid
	 * 
	 * @param g
	 * @return void
	 */
	public void drawUserCoord(Graphics g) {
		int xCoord, yCoord;
		//'xCoord'&'yCoord'(int) are the coordinates (not applet coordinates) of where the user clicked on						
		xCoord = (int) ((userX - startCoord) / scale - (numOfGrid - 1) / 2);//formulas for converting coordinates
		yCoord = (int) ((numOfGrid - 1) / 2 - (userY - startCoord) / scale);
		g.setColor(Color.white);
		g.setFont(standardFont);
		if (userX >= startCoord && userX <= startCoord + scale * numOfGrid && userY >= startCoord
				&& userY <= startCoord + scale * numOfGrid) {
			g.drawString("(" + xCoord + "," + yCoord + ")", userX, userY);
		}//end if statement (that only runs when user clicked inside the grid)
		// g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
	}//end drawUserCoord method

	/**generateImage method
	 * This method makes randomly appearing aliens that will
	 * not appear at the same location in the same game
	 * It also stores all aliens, giving the player a choice to 
	 * look at their locations when the game is over
	 * 
	 * @param g
	 * @return void
	 */
	public void generateImage(Graphics g) {
		int randNum1, randNum2;
		//'randNum1'&'randNum2'(int) helps generate aliens in random locations
		if (count == numOfGrid * numOfGrid)
		//if somehow aliens have spawned in all the possible locations before time's up etc.
			gameReset1();//call gameReset1 method
		else if (switch1 == false) {
			for (int i = 0; i < numOfGrid + 1; i++) {
				for (int c = 0; c < numOfGrid + 1; c++) {
					noRepeat[i][c] = true;
				}
			}
		} // set noRepeat array to true once before game starts
		do {
			randNum1 = (int) (Math.random() * (numOfGrid) + 1);
			randNum2 = (int) (Math.random() * (numOfGrid) + 1);
		} while (noRepeat[randNum1][randNum2] == false);	//end do while loop to not generate
															//the same locations
		noRepeat[randNum1][randNum2] = false;

		alienImage = getImage(getDocumentBase(), "alienPicture.png");
		imageXCoord = startCoord + randNum1 * scale - scale + 1;
		imageYCoord = startCoord + randNum2 * scale - scale + 1;

		g.drawImage(alienImage, imageXCoord, imageYCoord, scale - 1, scale - 1, this);

		startTime = System.nanoTime();						//start nanoTime

		showLocation[count][0] = imageXCoord;				//store alien locations		
		showLocation[count][1] = imageYCoord;
	}//end generateImage method

	/**showAlienLocation method
	 * The method shows the location of all aliens appeared when the 
	 * user chooses to
	 * 
	 * @param g
	 * @return void
	 */
	public void showAlienLocation(Graphics g) {
		for (int i = 0; i < count; i++) //for loop for drawing alien that have appeared
			g.drawImage(alienImage, showLocation[i][0], showLocation[i][1], scale - 1, scale - 1, this);
		showLocSwitch = false;
	}//end showAlienLocation method

	/**actionPerformed method
	 * triggers when user clicks on one of the buttons
	 * This method gives each button a function, the buttons are:
	 * 
	 * 1. settingButton: 	sets different the gaming style 
	 * 2. quitGameButton: 	confirming that the user wants to quit game
	 * 3. showLocButton: 	show all alien locations
	 * 4. startButton: 		user enter their names and the game starts
	 * 5. help button:	 	gives a brief list of what the game is about and
	 * 				   		how to play the game
	 * 
	 * @return void
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == settingButton) {
			Object[] options = { "<html><span style='font-size:1.2em'>(1)", "(2)", "(3)" };
			int n = JOptionPane.showOptionDialog(null,
				"<html><span style='font-size:1.8em'>Game Styles:\n"
					+ "<html><span style='font-size:1.2em'>- (1) Default Version: 21 x 21 grid, regular size.\n"
					+ "<html><span style='font-size:1.2em'>- (2) Blitz Version: 11 x 11 grid, smaller screen size\n\n"
					+ "<html><span style='font-size:1.8em'>Distress Signal Message:\n"
					+ "<html><span style='font-size:1.2em'>- (3) See Intro *Distress Signal* Message \n\n",
				"Settings", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
			boolean version2;							//'version2'(boolean) signals when user chooses to play the Blitz Version of the game
			if (n == 0) {
				version2 = false;
				version2Global = false;
				changeSize(version2);					//call changeSize method
				repaint();
			} else if (n == 1) {
				version2 = true;
				version2Global = true;
				changeSize(version2);					//call changeSize method
				repaint();
			} else if (n == 2)
				intro();
		} else if (e.getSource() == quitGameButton) {
			int quitGame = JOptionPane.showConfirmDialog(null, "Quit Game?", "Are you sure?",
					JOptionPane.YES_NO_OPTION);
			if (quitGame == JOptionPane.YES_OPTION) {
				System.out.println("Game TERMINATED");	//display on console for checking purpose (not for users)
				System.exit(0);							//terminates applet
			}
		} else if (e.getSource() == showLocButton) {
			showLocSwitch = true;
			repaint();
		} else if (e.getSource() == startButton) {
			inputUsername();							//call inputUsername method
			gameReset();								//call gameReset method after user input their name
		} else if (e.getSource() == helpButton) {
			timer.stop();								//stops timer
			JOptionPane.showMessageDialog(null,
				"<html><span style='font-size:1.8em'>How To Play:\n"
					+ "<html><span style='font-size:1.2em'>1. Click on the 'Start Game' button to play (there is no going back if you click it!!)\n"
					+ "<html><span style='font-size:1.2em'>2. You will be asked you enter your name\n"
					+ "<html><span style='font-size:1.2em'>3. Click the 'Start Game!!' button to begin game (and the timer starts)!\n"
					+ "<html><span style='font-size:1.2em'>4. You goal is to click on as much alien as possible within a given time limit\n\n"
					+ "<html><span style='font-size:1.8em'>How the scoring system works:\n"
					+ "<html><span style='font-size:1.2em'>- Click on an alien grants you 20 points (13 points for Blitz Version)\n\n"
					+ "<html><span style='font-size:1.8em'>Bonus Points:\n"
					+ "<html><span style='font-size:1.2em'>- The faster you capture, the higher the points (less points for Blitz Version)\n"
					+ "<html><span style='font-size:1.2em'>- If the amount captured is a multiple of 5, you will gain 50 points\n\n"
					+ "<html><span style='font-size:1.8em'>Lives:\n"
					+ "<html><span style='font-size:1.2em'>- You start with 3 lives\n"
					+ "<html><span style='font-size:1.2em'>- You lose 1 life when you fail to click on an alien\n"
					+ "<html><span style='font-size:1.2em'>- If you have 0 lives left, it's game over",
				"Help", JOptionPane.INFORMATION_MESSAGE);
		}
	}//end actionPerformed method

	/**drawUserLives method
	 * This method draws the number of lives that the user have left
	 * 
	 * @param g
	 * @return void
	 */
	public void drawUserLives(Graphics g) {
		Image userLivesImage = getImage(getDocumentBase(), "alienUserLives.png");
		for (int i = 0; i < userLives; i++)//for loop for drawing user lives
			g.drawImage(userLivesImage, 35 * i + 150, 25, 35, 30, this);
	}//end drawUserLives method

	public void inGameMusic() throws Exception {
		int numArray[] = {20, 40};
		int randNum = (int) (Math.random() * numArray.length + 0);
		URL url = finalProjectAlien.class.getResource("foehnMusic.wav");
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		inGameClip = AudioSystem.getClip();
		inGameClip.open(ais);
		inGameClip.setMicrosecondPosition((numArray[randNum]) * 1_000_000);

		FloatControl gainControl = (FloatControl) inGameClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-7.0f); // Change volume by __ decibels.
		Runnable r = new Runnable() {
			public void run() {
				inGameClip.start();
			}
		};
		SwingUtilities.invokeLater(r);
	}// end playMusic method

	public void backgroundMusic() throws Exception {
		URL url = finalProjectAlien.class.getResource("foehnMusic.wav");
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		backgroundClip = AudioSystem.getClip();
		backgroundClip.open(ais);
		backgroundClip.setMicrosecondPosition(0_000_000);

		FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-7.0f); // Change volume by __ decibels.
		Runnable r = new Runnable() {
			public void run() {
				backgroundClip.start();
				backgroundClip.loop(50);
			}
		};
		SwingUtilities.invokeLater(r);
		/*
		URL url = finalProjectAlien.class.getResource("soundFile1.wav");
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		backgroundClip = AudioSystem.getClip();
		backgroundClip.open(ais);
		backgroundClip.setMicrosecondPosition((long) (Math.random() * 580 + 13) * 1_000_000);

		FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-8.0f); // Change volume by __ decibels.
		Runnable r = new Runnable() {
			public void run() {
				backgroundClip.start();
			}
		};
		SwingUtilities.invokeLater(r);
		*/
	}
	
	public void clickAlienSound() throws Exception {
		URL url = finalProjectAlien.class.getResource("laserSound1.wav");
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		final Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.setMicrosecondPosition(700_000);
		
		Runnable r = new Runnable() {
			public void run() {
				clip.start();
			}
		};
		SwingUtilities.invokeLater(r);
	}//end playMusic method
	
	/**mouseClicked/mouseEntered etc. methods
	 * These methods are necessary since MouseListener is implemented
	 * *Note: These methods are not being used in the program 
	 */
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}// end finalProjectAlien class
