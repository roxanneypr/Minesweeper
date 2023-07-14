package minesweeper;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Element {
	private String type;
	protected Image img;
	protected ImageView imgView;
	protected GameStage gameStage;
	protected int row, col;
	protected int neighboringBombCount;
	
	public final static Image FLAG_IMAGE = new Image("images/flag.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image BOMB_IMAGE = new Image("images/bomb.gif",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image LAND_IMAGE = new Image("images/land.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image ONE_IMAGE = new Image("images/one.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image TWO_IMAGE = new Image("images/two.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image THREE_IMAGE = new Image("images/three.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image FOUR_IMAGE = new Image("images/four.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image FIVE_IMAGE = new Image("images/five.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image SIX_IMAGE = new Image("images/six.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image SEVEN_IMAGE = new Image("images/seven.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static Image EIGHT_IMAGE = new Image("images/eight.png",GameStage.CELL_WIDTH,GameStage.CELL_WIDTH,false,false);
	public final static int IMAGE_SIZE = 70;
	
	public final static String FLAG_TYPE = "flag";
	public final static String BOMB_TYPE = "bomb";
	public final static String LAND_TYPE = "land";
	public final static String LAND_FLAG_TYPE = "landToFlag";
	public final static String ONE_TYPE = "1";
	public final static String TWO_TYPE = "2";
	public final static String THREE_TYPE = "3";
	public final static String FOUR_TYPE = "4";
	public final static String FIVE_TYPE = "5";
	public final static String SIX_TYPE = "6";
	public final static String SEVEN_TYPE = "7";
	public final static String EIGHT_TYPE = "8";
	
	

	public Element(String type, GameStage gameStage) {
		this.type = type;	
		this.gameStage = gameStage;
		
		// load image depending on the type
		switch(this.type) {
			case Element.FLAG_TYPE: this.img = Element.FLAG_IMAGE; break;
			case Element.LAND_TYPE: this.img = Element.LAND_IMAGE; break;
			case Element.BOMB_TYPE: this.img = Element.BOMB_IMAGE; break;
		}
		
		this.setImageView();
		this.setMouseHandler();
		
	}
	
	protected void loadImage(String filename,int width, int height){
		try{
			this.img = new Image(filename,width,height,false,false);
		} catch(Exception e){}
	}
	
	//element type getter
	String getType(){
		return this.type;
	}
	
	//element row index getter
	int getRow() {
		return this.row;
	}
	
	//element column index getter
	int getCol() {
		return this.col;
	}
	
	protected ImageView getImageView(){
		return this.imgView;
	}
	
	void setType(String type){
		this.type = type;
	}
	
	void initRowCol(int i, int j) {
		this.row = i;
		this.col = j;
	}
	
	private void setImageView() {
		// initialize the image view of this element
		this.imgView = new ImageView();
		this.imgView.setImage(this.img);
		this.imgView.setLayoutX(0);
		this.imgView.setLayoutY(0);
		this.imgView.setPreserveRatio(true);
		
		if(this.type.equals(Element.FLAG_TYPE)) {
			this.imgView.setFitWidth(GameStage.FLAG_WIDTH);
			this.imgView.setFitHeight(GameStage.FLAG_HEIGHT);
		}else {
			this.imgView.setFitWidth(GameStage.CELL_WIDTH);
			this.imgView.setFitHeight(GameStage.CELL_HEIGHT);	
		}
	}
	
	private void setMouseHandler(){
		Element element = this;
		this.imgView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
                switch(element.getType()) {
	                case Element.FLAG_TYPE: 		// if flag, set flagClicked to true
	                								if (gameStage.getFlagCount() < GameStage.NUM_OF_BOMBS) {
	                									System.out.println("FLAG clicked!");
	                									gameStage.setFlagClicked(true);
	                								} else {
	                									System.out.println("All flags are placed!");
	                								}
								    	            break;
								    	           
	    			case Element.LAND_TYPE: 
			    									System.out.println("LAND clicked!");
								    				if(!gameStage.isFlagClicked()) {
								    					if(!gameStage.isBomb(element)) {	// if not a bomb, shows the number image or clears image
								    						int neighbourBomb = gameStage.neighbouringBombCount(element);
								    						gameStage.setOpenedLandCount();
								    						if(neighbourBomb == 1) {
								    							changeImage(element,Element.ONE_IMAGE);
								    						} else if(neighbourBomb == 2) {
								    							changeImage(element,Element.TWO_IMAGE);
								    						} else if(neighbourBomb == 3) {
								    							changeImage(element,Element.THREE_IMAGE);
								    						} else if(neighbourBomb == 4) {
								    							changeImage(element,Element.FOUR_IMAGE);
								    						} else if(neighbourBomb == 5) {
								    							changeImage(element,Element.FIVE_IMAGE);
								    						} else if(neighbourBomb == 6) {
								    							changeImage(element,Element.SIX_IMAGE);
								    						} else if(neighbourBomb == 7) {
								    							changeImage(element,Element.SEVEN_IMAGE);
								    						} else if(neighbourBomb == 8) {
								    							changeImage(element,Element.EIGHT_IMAGE);
								    						} else {
								    							clearImage(element);
								    						}    
								    						
								    						if (gameStage.getopenedLandCount() == (GameStage.MAP_NUM_COLS*GameStage.MAP_NUM_ROWS-GameStage.NUM_OF_BOMBS)) {
								    							gameStage.flashGameOver(1);
								    						}
								    					}
								    					else {
								    						changeImage(element,Element.BOMB_IMAGE); // if bomb, change image to bomb
								    						gameStage.flashGameOver(0);
								    					}
								    	            } else {
								    	            	changeImage(element,Element.FLAG_IMAGE);	// if flag was clicked before hand, change image to flag
								    	            	element.setType(LAND_FLAG_TYPE);			// change type to landToFlag
								    	            	gameStage.setFlagClicked(false);	    	// reset flagClicked to false
								    	            	gameStage.addFlagCount();
								    	            	
								    	            	//if all flags are placed correctly, shows the exit stage
								    	            	if(gameStage.getFlagCount() == GameStage.NUM_OF_BOMBS) {
								    	            		int result = gameStage.checkResults();
								    	            		if (result == 1) {
								    	            			gameStage.flashGameOver(result);
								    	            		}
								    	            	}
								    	         
								    	            }
								    				break;
	    			case Element.LAND_FLAG_TYPE: 
								    				changeImage(element,Element.LAND_IMAGE);		// if flag is clicked, change image back to land
							    	            	element.setType(LAND_TYPE);
							    	            	gameStage.deductFlagCount();
							    	            	break;
                }
			}	//end of handle()		
		});	
	}
	
	//method for clearing the the image
	private void clearImage(Element element) {
		imgView.setImage(null);
	}
	
	//method for changing the cell image
	private void changeImage(Element element, Image image) {
		this.imgView.setImage(image);
			
	}
}
