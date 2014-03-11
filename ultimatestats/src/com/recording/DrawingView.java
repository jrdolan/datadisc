package com.recording;

import sqlite.helper.Action;
import sqlite.model.DatabaseHandler;

import com.example.ultimatestats.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class DrawingView extends View {
	
	//Possession possession;
	Activity activity = (Activity) this.getContext();
	
	final DatabaseHandler db = new DatabaseHandler(activity);
	int absoluteX;
	int absoluteOldX;
	
	int drawFlag = 0;
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF29ABDD; //FF0000; //Main Team paint  
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap, mutableBitmap;
	
	private Path oppoPath;
	private Paint oppoPaint;
	private int oppoColor = 0xFFAA330F;		//Opponent paint
	
	private Path undoPath;
	private Paint undoPaint;
	private int undoColor = 0xFF80EB55;		//00FF00;
	
	private Path linesPath;
	private Paint linesPaint;
	private int linesColor = 0xFF000000;
	
	int oldX = 0;
	int oldY = 0;
	int oldestX = 0;
	int oldestY = 0;
	int arrowSegment = 40;
	
	public DrawingView (Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}
	
	private void setupDrawing() {
		linesPath = new Path();
		linesPaint = new Paint();
		linesPaint.setColor(undoColor);
		linesPaint.setAntiAlias(true);
		linesPaint.setStrokeWidth(3);
		linesPaint.setStyle(Paint.Style.STROKE);
		linesPaint.setStrokeJoin(Paint.Join.ROUND);
		linesPaint.setStrokeCap(Paint.Cap.ROUND);
		
		undoPath = new Path();
		undoPaint = new Paint();
		undoPaint.setColor(undoColor);
		undoPaint.setAntiAlias(true);
		undoPaint.setStrokeWidth(20);
		undoPaint.setStyle(Paint.Style.STROKE);
		undoPaint.setStrokeJoin(Paint.Join.ROUND);
		undoPaint.setStrokeCap(Paint.Cap.ROUND);
		
		oppoPath = new Path();
		oppoPaint = new Paint();
		oppoPaint.setColor(oppoColor);
		oppoPaint.setAntiAlias(true);
		oppoPaint.setStrokeWidth(10);
		oppoPaint.setStyle(Paint.Style.STROKE);
		oppoPaint.setStrokeJoin(Paint.Join.ROUND);
		oppoPaint.setStrokeCap(Paint.Cap.ROUND);
		
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(10);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);	
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		canvasBitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.field); //Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888); //Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mutableBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		//canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(mutableBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawBitmap(mutableBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int touchX = (int) event.getX();
		int touchY = (int) event.getY();
				
		//HANDLE DRAWING AND LINEUP POPUP
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:		
				
				if ((db.getActionsInPossession(db.getLastPossessionInPoint(db.getLastPoint())).length == 1) || oldX == 0) { //MAKING THE ASSUMPTION THAT THE LAST POINT BELONGS TO THE CURRENT GAME.  SIMPLY, MULTIPLE GAMES WON'T BE IN USE AT THE SAME TIME. 
					//If there is one action for the latest possession, it's a PickUp and needs an X
					
					if (db.getPossession(db.getLastPossession()).getPossessionType().compareToIgnoreCase("Offense") == 0) {
						drawCanvas.drawPath(drawPath, drawPaint);
						drawX(touchX, touchY, paintColor);
					}
					else {
						drawCanvas.drawPath(oppoPath, oppoPaint);
						drawX(touchX, touchY, oppoColor);
					}
					//RECORD 'PICKUP' ACTION AND POSITION
					
					drawFlag = 1;
					
					Action action = db.getAction(db.getLastAction()); 
					action.setActionType("PickUp"); 
					db.updateAction(action);
					
					if (Math.abs(db.getPoint(db.getLastPoint()).getStartingSide().compareTo("Right")) == 0) { 
						absoluteX =  (int) Math.abs(touchX-9.61*120);
					}
					else {
						absoluteX = touchX;
					}
					
					Action actionOne = db.getAction(db.getLastAction());
					actionOne.setXPosition((int) Math.ceil(absoluteX/9.61));
					actionOne.setYPosition((int) Math.ceil(touchY/9.61));
					db.updateAction(actionOne);
					//CREATE NEW EMPTY ACTION AND ADD TO DB
					Action actionTwo = new Action(db.getLastPossession(), 0, "Throw", 0, 0);
					db.addAction(actionTwo);
					
					if (oldX != 0) { 
						
						drawCanvas.drawPath(undoPath, undoPaint);
						undoArrow(oldX, oldY, oldestX, oldestY, undoColor);
					
					}
					
				}
				else {
					//Not a new Possession, so draw Arrow
					if (db.getPossession(db.getLastPossession()).getPossessionType().compareToIgnoreCase("Offense") == 0) {
						drawCanvas.drawPath(drawPath, drawPaint);
						drawArrow(touchX, touchY, oldX, oldY, paintColor);
						
					}
					else {
						drawCanvas.drawPath(oppoPath, oppoPaint);
						drawArrow(touchX, touchY, oldX, oldY, oppoColor);
					}
//					drawCanvas.drawPath(drawPath, drawPaint);
//					drawArrow(touchX, touchY, oldX, oldY, paintColor);
					
					//RECORD THROW ACTION WITH CURRENT PLAYER
					Action actionOne = db.getAction(db.getLastAction());
					actionOne.setActionType("Throw");
					
					if (Math.abs(db.getPoint(db.getLastPoint()).getStartingSide().compareTo("Right")) == 0) {    //if (db.getPoint(db.getLastPoint()).getStartingSide() != "Left") {
						absoluteX =  (int) Math.abs(touchX-9.61*120);
						absoluteOldX =  (int) Math.abs(oldX-9.61*120);
					}
					else {
						absoluteX = touchX;
						absoluteOldX = oldX;
					}
					
					actionOne.setXPosition((int) Math.ceil(absoluteOldX/9.61));
					actionOne.setYPosition((int) Math.ceil(oldY/9.61)); 
					db.updateAction(actionOne);
					double deltaX = Math.ceil(absoluteX/9.61) - Math.ceil(absoluteOldX/9.61);  //catch - throw
					double deltaY = -(Math.ceil(touchY/9.61) - Math.ceil(oldY/9.61)); // opposite because starting at the top left, not bottom left
					double theta = 0;
//					Log.d("Check", "deltaX = " + Double.toString(deltaX));
	//				Log.d("Check", "deltaY = " + Double.toString(deltaY));
					if (deltaX >= 0) {
						if (deltaY >= 0) {
							//Q1
							theta = Math.toDegrees(Math.atan((deltaY/deltaX)));
						}
						else {
							//Q4
							theta = 360 + Math.toDegrees(Math.atan((deltaY/deltaX)));// + 270;
						}
					}
					else {
						if (deltaY >= 0) {
							//Q2
							theta = 180 + Math.toDegrees(Math.atan((deltaY/deltaX)));// + 90;
						}
						else {
							//Q3
							theta = Math.toDegrees(Math.atan((deltaY/deltaX))) + 180;
						}
					}
//					Log.d("Check", "Theta = " + Double.toString(theta));
					double distance = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
	//				Log.d("Check", "Distance = " + Double.toString(distance));
					//CREATE NEW CATCH ACTION WITH UNKNOWN PLAYER (THIS WILL CHANGE WITH THE POPUP)
					Action actionCatch = new Action(db.getLastPossession(), 0, "Catch", (int) Math.ceil(absoluteX/9.61), (int) Math.ceil(touchY/9.61), distance, theta);
					db.addAction(actionCatch);
					//CREATE NEW ACTION
					Action actionTwo = new Action(db.getLastPossession(), 0, "", 0, 0);
					db.addAction(actionTwo);
					//TURNOVERS AND GOALS ARE HANDLED BY BUTTONS
					
					if (oldX != 0) { 
						if (drawFlag == 1) { 		//db.getActionsInPossession(db.getLastPossession()).length > 1 && db.getActionsInPossession(db.getLastPossession()).length < 4) {		//db.getAction(db.getLastAction()-1).getActionType().compareToIgnoreCase("PickUp") == 0) {
							//If last action was PickUp, undoX
							undoX(oldX, oldY, undoColor);
							
							drawFlag = 0;
						}
						else{
							drawCanvas.drawPath(undoPath, undoPaint);
							undoArrow(oldX, oldY, oldestX, oldestY, undoColor);
							
						}
					}
				}
				
				//Redraw field and adjust variables
				drawFieldLines();
				//Used to clear the previous arrow
				oldestX = oldX;
				oldestY = oldY;
				
				//Used to draw the new one
				oldX = touchX;
				oldY = touchY;
				
				showPopup(activity, touchX, touchY);
			    break;
			case MotionEvent.ACTION_MOVE:
				//Do nothing
				break;
			case MotionEvent.ACTION_UP:
			    drawCanvas.drawPath(undoPath, undoPaint);
			    undoPath.reset();
			    
			    drawCanvas.drawPath(drawPath, drawPaint);
			    drawPath.reset();
			    
			    drawCanvas.drawPath(oppoPath, oppoPaint);
			    oppoPath.reset();
			    
			    drawCanvas.drawPath(linesPath, linesPaint);
			    linesPath.reset();
			    
			    break;
			default:
			    return false;
		}
		invalidate();
		return true;
	}
	
	public void setColor(String newColor){
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void drawArrow(int touchX, int touchY, int oldX, int oldY, int paintColor) {
		
		//drawPaint.setColor(paintColor);
		if (db.getPossession(db.getLastPossession()).getPossessionType().compareToIgnoreCase("Offense") == 0) {
			drawPath.moveTo(touchX, touchY);
			drawPath.lineTo(oldX, oldY);
			//draw one arrow segment
			drawPath.moveTo(touchX, touchY);
			
			int distanceY = touchY-oldY;
			int distanceX = touchX-oldX;
			double lineAngle = -Math.atan2(distanceY, distanceX);
			
			int xFirstArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(45)+lineAngle));
			int yFirstArrow = (int) (arrowSegment*Math.sin(Math.toRadians(45)+lineAngle));

			drawPath.lineTo(touchX+xFirstArrow, touchY+yFirstArrow);
			
			//draw the other arrow segment
			drawPath.moveTo(touchX, touchY);
			int xSecondArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(-45)+lineAngle));
			int ySecondArrow = (int) (arrowSegment*Math.sin(Math.toRadians(-45)+lineAngle));
			
			drawPath.lineTo(touchX+xSecondArrow, touchY+ySecondArrow);
		}
		else {
			oppoPath.moveTo(touchX, touchY);
			oppoPath.lineTo(oldX, oldY);
			//draw one arrow segment
			oppoPath.moveTo(touchX, touchY);
			
			int distanceY = touchY-oldY;
			int distanceX = touchX-oldX;
			double lineAngle = -Math.atan2(distanceY, distanceX);
			
			int xFirstArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(45)+lineAngle));
			int yFirstArrow = (int) (arrowSegment*Math.sin(Math.toRadians(45)+lineAngle));

			oppoPath.lineTo(touchX+xFirstArrow, touchY+yFirstArrow);
			
			//draw the other arrow segment
			oppoPath.moveTo(touchX, touchY);
			int xSecondArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(-45)+lineAngle));
			int ySecondArrow = (int) (arrowSegment*Math.sin(Math.toRadians(-45)+lineAngle));
			
			oppoPath.lineTo(touchX+xSecondArrow, touchY+ySecondArrow);
		}
		
	}
	
	public void undoArrow(int oldX, int oldY, int oldestX, int oldestY, int paintColor) {
	
		undoPaint.setColor(paintColor);

		undoPath.moveTo(oldX, oldY);
		undoPath.lineTo(oldestX, oldestY);
		//draw one arrow segment
		undoPath.moveTo(oldX, oldY);
		
		int distanceY = oldY-oldestY;
		int distanceX = oldX-oldestX;
		double lineAngle = -Math.atan2(distanceY, distanceX);
				
		int xFirstArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(45)+lineAngle));
		int yFirstArrow = (int) (arrowSegment*Math.sin(Math.toRadians(45)+lineAngle));

		undoPath.lineTo(oldX+xFirstArrow, oldY+yFirstArrow);
		
		//draw the other arrow segment
		undoPath.moveTo(oldX, oldY);
		int xSecondArrow = (int) -(arrowSegment*Math.cos(Math.toRadians(-45)+lineAngle));
		int ySecondArrow = (int) (arrowSegment*Math.sin(Math.toRadians(-45)+lineAngle));
		
		undoPath.lineTo(oldX+xSecondArrow, oldY+ySecondArrow);

	}

	public void drawX(int touchX, int touchY, int paintColor) {
		if (db.getPossession(db.getLastPossession()).getPossessionType().compareToIgnoreCase("Offense") == 0) {
			drawPaint.setColor(paintColor);
			drawPath.moveTo(touchX-20, touchY-20);
			drawPath.lineTo(touchX+20, touchY+20);
			
			drawPath.moveTo(touchX+20, touchY-20);
			drawPath.lineTo(touchX-20, touchY+20);
		}
		else {
			oppoPaint.setColor(paintColor);
			oppoPath.moveTo(touchX-20, touchY-20);
			oppoPath.lineTo(touchX+20, touchY+20);
			
			oppoPath.moveTo(touchX+20, touchY-20);
			oppoPath.lineTo(touchX-20, touchY+20);
		}
		
	}

	public void undoX(int touchX, int touchY, int paintColor) {
		
		undoPaint.setColor(paintColor);
		undoPath.moveTo(touchX-20, touchY-20);
		undoPath.lineTo(touchX+20, touchY+20);
		
		undoPath.moveTo(touchX+20, touchY-20);
		undoPath.lineTo(touchX-20, touchY+20);
		
	}
	
	public void drawFieldLines() {
		int endZoneLength = 25;
		int fieldLength = 70;
		int fieldWidth = 40;
		double scaleFactor = 9.61;
		
		linesPaint.setColor(linesColor);
		linesPath.moveTo((int)Math.ceil(endZoneLength*scaleFactor), 0);
		linesPath.lineTo((int)Math.ceil(endZoneLength*scaleFactor), (int)Math.ceil(fieldWidth*scaleFactor));
		
		linesPath.moveTo((int)Math.ceil((endZoneLength+fieldLength)*scaleFactor), 0);
		linesPath.lineTo((int)Math.ceil((endZoneLength+fieldLength)*scaleFactor), (int)Math.ceil(fieldWidth*scaleFactor));
		
	}
	
@SuppressWarnings("deprecation")
// The method that displays the popup.
	void showPopup(final Activity context, int xPos, int yPos) { //Point p) { //
		//Log.d("Check", Integer.toString(db.getLastAction()));
	   int popupWidth = 400;
	   int popupHeight = 280;
		
	   // Inflate the popup_layout.xml
	   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);
	 
	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(context);
	   popup.setContentView(layout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);
	 
	   popup.setBackgroundDrawable(new BitmapDrawable());
	   
	   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
	   int OFFSET_X = -popupWidth/2; //30;
	   int OFFSET_Y = 75; //popupHeight/2; //30;
	 
	   // Clear the default translucent background
	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(layout, Gravity.NO_GRAVITY, xPos + OFFSET_X, yPos + OFFSET_Y);
	   
	   
	   Button player1Button = (Button) layout.findViewById(R.id.player1_btn);
	   player1Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player2Button = (Button) layout.findViewById(R.id.player2_btn);
	   player2Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player3Button = (Button) layout.findViewById(R.id.player3_btn);
	   player3Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player4Button = (Button) layout.findViewById(R.id.player4_btn);
	   player4Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player5Button = (Button) layout.findViewById(R.id.player5_btn);
	   player5Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player6Button = (Button) layout.findViewById(R.id.player6_btn);
	   player6Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button player7Button = (Button) layout.findViewById(R.id.player7_btn);
	   player7Button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
	   Button playerUnknownButton = (Button) layout.findViewById(R.id.playerUnknown_btn);
	   playerUnknownButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));

	   if (db.getPossession(db.getLastPossession()).getPlayer1ID() == 0) {
		 player1Button.setText("Unkn");  
	   }
	   else {
		   player1Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer1ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer2ID() == 0) {
		 player2Button.setText("Unkn");  
	   }
	   else {
		   player2Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer2ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer3ID() == 0) {
		 player3Button.setText("Unkn");  
	   }
	   else {
		   player3Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer3ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer4ID() == 0) {
		 player4Button.setText("Unkn");  
	   }
	   else {
		   player4Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer4ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer5ID() == 0) {
		 player5Button.setText("Unkn");  
	   }
	   else {
		   player5Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer5ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer6ID() == 0) {
		 player6Button.setText("Unkn");  
	   }
	   else {
		   player6Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer6ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   if (db.getPossession(db.getLastPossession()).getPlayer7ID() == 0) {
		 player7Button.setText("Unkn");  
	   }
	   else {
		   player7Button.setText(db.getPlayer(db.getPossession(db.getLastPossession()).getPlayer7ID()).getPlayerNickName()); //get NickName of Player(get Player1ID of Poss(getLastPossID))
	   }
	   
	   

	   player1Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   //If action is not pick up, set last TWO actions as the player to get both the catch and throw.
			   //Otherwise just set last
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer1ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer1ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer1ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   
			   popup.dismiss();
		   }	
	   });
	   player2Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer2ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer2ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer2ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   player3Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer3ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer3ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer3ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   player4Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer4ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer4ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer4ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   player5Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer5ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer5ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer5ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   player6Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer6ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer6ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer6ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   player7Button.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer7ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer7ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayer7ID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	   
	   playerUnknownButton.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   Action action = db.getAction(db.getLastAction()); //Gets the most recent action
			   if (action.getActionType().compareToIgnoreCase("PickUp") != 0) {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayerUnknownID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
				   
				   Action actionPrevious = db.getAction(db.getLastAction()-1);
				   actionPrevious.setPlayerID(db.getPossession(db.getLastPossession()).getPlayerUnknownID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(actionPrevious); //updates the action table
			   }
			   else {
				   action.setPlayerID(db.getPossession(db.getLastPossession()).getPlayerUnknownID()); //Sets the PlayerID of the action to Player1
				   db.updateAction(action); //updates the action table
			   }
			   popup.dismiss();
		   }	
	   });
	}
	
}
