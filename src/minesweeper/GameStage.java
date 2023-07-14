package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameStage {
	private Scene scene;
	private Stage stage;
	/*Group layout/container is a component which applies no special 
	layout to its children. All child components (nodes) are positioned at 0,0*/
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private Element flag;	
	private GridPane map;
	private int[][] gameBoard;
	private ArrayList<Element> landCells;
	private int flagCount;
	private int openedLandCount;
	
	private boolean flagClicked=false;
	
	public final static int MAX_CELLS = 81;
	public final static int MAP_NUM_ROWS = 9;
	public final static int MAP_NUM_COLS = 9;	
	public final static int MAP_WIDTH = 400;
	public final static int MAP_HEIGHT = 400;
	public final static int CELL_WIDTH = 45;
	public final static int CELL_HEIGHT = 45;
	public final static int FLAG_WIDTH = 70;
	public final static int FLAG_HEIGHT = 70;
	public final static boolean IS_GAME_DONE = false;
	public final static int WINDOW_WIDTH = 550;
	public final static int WINDOW_HEIGHT = 550;
	public final static int NUM_OF_BOMBS = 20;
	
	public final Image bg = new Image("images/background.jpg",550,550,false,false);
	
	public GameStage() {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);	
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
		this.gc = canvas.getGraphicsContext2D();
		this.flag = new Element(Element.FLAG_TYPE,this);
		this.map = new GridPane();
		this.landCells = new ArrayList<Element>();
		this.gameBoard = new int[GameStage.MAP_NUM_ROWS][GameStage.MAP_NUM_COLS];
		this.flagCount = 0;
		this.openedLandCount =0;
	}

	//method to add the stage elements
	public void setStage(Stage stage) {
		this.stage = stage;
		//draw the background to the canvas at location x=0, y=70
		this.gc.drawImage( this.bg, 0, 70);			
		
		this.initGameBoard();	//initialize game board with 1s and 0s
		this.createMap();
		
		//set stage elements here	
		this.root.getChildren().add(canvas);
		this.root.getChildren().add(map);		
		this.root.getChildren().add(this.flag.getImageView());
		this.stage.setTitle("Modified Minesweeper Game");
		this.stage.setScene(this.scene);
		this.stage.show();
	}
	
	//Initializes the game board
	private void initGameBoard(){
		Random r = new Random();
		int numOfBombs = 0;
		
		while(numOfBombs != GameStage.NUM_OF_BOMBS) {
			int i = r.nextInt(GameStage.MAP_NUM_COLS);
			int j = r.nextInt(GameStage.MAP_NUM_ROWS);
			if (this.gameBoard[i][j] == 0) {
				this.gameBoard[i][j] = 1;
				numOfBombs += 1;
			}
		}
		
		for(int i=0;i<GameStage.MAP_NUM_ROWS;i++){
			System.out.println(Arrays.toString(this.gameBoard[i]));//print final board content	
		}
	}
	
	private void createMap(){
		//create 9 land cells
		for(int i=0;i<GameStage.MAP_NUM_ROWS;i++){
			for(int j=0;j<GameStage.MAP_NUM_COLS;j++){
					
				// Instantiate land elements
				Element land = new Element(Element.LAND_TYPE,this);
				land.initRowCol(i,j);
				
				//add each land to the array list landCells
				this.landCells.add(land);
			}		
		}
		
		this.setGridPaneProperties();
		this.setGridPaneContents();
	}
	
	//method to set size and location of the grid pane 
	private void setGridPaneProperties(){
		this.map.setPrefSize(GameStage.MAP_WIDTH, GameStage.MAP_HEIGHT);
		//set the map to x and y location; add border color to see the size of the gridpane/map  
//	    this.map.setStyle("-fx-border-color: red ;");
		this.map.setLayoutX(GameStage.WINDOW_WIDTH*0.10);
	    this.map.setLayoutY(GameStage.WINDOW_WIDTH*0.15);
	    this.map.setVgap(5);
	    this.map.setHgap(5);
	}
	
	//method to add row and column constraints of the grid pane
	private void setGridPaneContents(){  
	          
		 //loop that will set the constraints of the elements in the grid pane
	     int counter=0;
	     for(int row=0;row<GameStage.MAP_NUM_ROWS;row++){
	    	 for(int col=0;col<GameStage.MAP_NUM_COLS;col++){
	    		 // map each land's constraints
	    		 GridPane.setConstraints(landCells.get(counter).getImageView(),col,row); 
	    		 counter++;
	    	 }
	     }   
	     
	   //loop to add each land element to the gridpane/map
	     for(Element landCell: landCells){
	    	 this.map.getChildren().add(landCell.getImageView());
	     }
	}
	
	//Method for counting the number of neighboring bombs
	int neighbouringBombCount(Element element) {
		int bombCount=0;
		int i= element.getRow();
		int j = element.getCol();
		
		for (int v = i-1; v<i+2; v++) {
			for (int s = j-1; s<j+2; s++) {
				if(v>= 0 && v<GameStage.MAP_NUM_ROWS && s>= 0 && s<GameStage.MAP_NUM_COLS) {
					if(v==i && j==s) {
						continue;
					} else {
						if(this.gameBoard[v][s] == 1) {
							bombCount+=1;
						}
					}
				}
			}
		}
		System.out.println("NUMBER OF NEIGHBORING BOMBS: "+ bombCount);
		return bombCount;
	}
	
	//Method for checking if the cell has a bomb
	boolean isBomb(Element element){
		int i = element.getRow();
		int j = element.getCol();
		
		//if the row col cell value is equal to 1, cell has bomb
		if(this.gameBoard[i][j] == 1){
			System.out.println(">>>>>>>>>Bomb!");
			return true;
		}
		
		System.out.println(">>>>>>>>>SAFE!");
		return false;
	}
	
	public boolean isFlagClicked() {
		return this.flagClicked;
	}
	
	public void setFlagClicked(boolean value) {
		this.flagClicked = value;
	}
	
	Stage getStage() {
		return this.stage;
	}
	
	//Method for displaying the game over stage
	void flashGameOver(int num){
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();
		
		transition.setOnFinished(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				GameOverStage gameover = new GameOverStage(num);
				stage.setScene(gameover.getScene());
			}
		});
	}
	
	//Adds 1 to the number of flags
	public void addFlagCount(){
		this.flagCount+=1;
	}
	
	//Deducts 1 from the number of flags
	public void deductFlagCount(){
		this.flagCount-=1;
	}
	
	//Flag count getter
	public int getFlagCount() {
		return this.flagCount;
	}
	
	//Increments the number of lands opened
	public void setOpenedLandCount(){
		this.openedLandCount+=1;
	}
	
	//Number of lands opened getter
	public int getopenedLandCount() {
		return this.openedLandCount;
	}

	//Method for checking if all flags are placed in the bombs
	int checkResults() {
		int correctFlag = 0;
		for (Element element : landCells) {
			int i = element.getRow();
			int j = element.getCol();
			
			if(this.gameBoard[i][j] == 1) {
				if(element.getType() == Element.LAND_FLAG_TYPE) {
					correctFlag +=1;
				}
			}
		}
		if(correctFlag == GameStage.NUM_OF_BOMBS) {
			return 1;
		}
		return 0;
	}
}

