package pt.iscte.poo.sokobanstarter;

import java.util.List;
import java.util.Random;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Empilhadora implements ImageTile, Moovable {

	private Point2D position;
	private String imageName;
	public boolean moved;
	private int bateria = 100;// bateria inicial da empilhadora

	public Empilhadora(Point2D position) {
		this.position = position;
		imageName = "Empilhadora_D";
	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public int getBateria() {
		return bateria;
	}
	
	public void toggleMoved() {// mudar o moved para false apos cada jogada
		moved = false;
	}

	public void drainBateria() {
		bateria--;
	}
	
	public void useBateria() {
		if (bateria + 50 <= 100) {
			bateria = bateria + 50;
		} else {
			bateria = bateria + (100 - bateria);
		}
	}
	public void move(int key, Point2D paredesPos[], Point2D caixotesPos[], Level level) {
		Point2D nextPos = nextPos(key, position);
		Direction d = Direction.directionFor(key);

		switch (d) {
		case UP:
			imageName = "Empilhadora_U";
			break;
		case DOWN:
			imageName = "Empilhadora_D";
			break;
		case LEFT:
			imageName = "Empilhadora_L";
			break;
		case RIGHT:
			imageName = "Empilhadora_R";
			break;
		}

		if (canMove(paredesPos, nextPos)) {
		    // Check if there's a box at the next position
		    if (isBoxAtPosition(caixotesPos, nextPos)) {
		        // Calculate the new position for the box
		        Point2D boxNextPos = nextPos(key, nextPos);

		        // Check if both empilhadora and the box can be moved to their new positions
		        if (canMove(paredesPos, boxNextPos) && canMoveBox(paredesPos, caixotesPos, nextPos, boxNextPos)) {
		            // Move the empilhadora to the next position
		            this.position = nextPos;

		            // Move the box to the new position
		            level.moveBox(nextPos, boxNextPos);
		            moved = true;
		        }
		    } else {
		        // If there's no box, move the empilhadora to the next position
		        this.position = nextPos;
		        moved = true;
		    }
		}
	}
	

	public void changePos(Point2D pos) {
		position = pos;
	}
}