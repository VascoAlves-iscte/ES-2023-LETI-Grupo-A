package pt.iscte.poo.sokobanstarter;

import java.util.Arrays;
import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface Moovable {

	public default Point2D nextPos(int key, Point2D empelhadoraPos) {// calcula proxima posição da empelhadora de acordo
																		// com a key usada
																					
		if (Direction.isDirection(key)) {

			Direction d = Direction.directionFor(key);
			Point2D nextPosition = empelhadoraPos.plus(d.asVector());

			return nextPosition;
		}
		return null;
	}

	public default boolean canMove(Point2D wallPos[], Point2D nextPos) {// verifica se entidade se
																							// pode mover para a
																							// próxima posição
		boolean withinBounds = (nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < 10
				&& nextPos.getY() < 10);// verifica se esta dentro do mapa
												
		if (!Arrays.asList(wallPos).contains(nextPos) && withinBounds ) {

			return true;
		}
		return false;
	}
	
	public default boolean isBoxAtPosition(Point2D[] caixotesPos, Point2D nextPos) {
	    for (Point2D boxPos : caixotesPos) {
	        if (nextPos.equals(boxPos)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public default boolean canMoveBox(Point2D[] paredesPos, Point2D[] caixotesPos, Point2D currentPos, Point2D nextPos) {
	    // Check if the next position is within the boundaries of the grid
	    boolean withinBounds = (nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < GameEngine.GRID_WIDTH
	            && nextPos.getY() < GameEngine.GRID_HEIGHT);

	    if (!withinBounds) {
	        return false;  // If the next position is outside the grid, the box cannot move
	    }

	    // Check if there's a wall at the next position
	    for (Point2D wallPos : paredesPos) {
	        if (nextPos.equals(wallPos)) {
	            return false;  // If there's a wall, the box cannot move
	        }
	    }

	    // Check if there's another box at the next position
	    for (Point2D boxPos : caixotesPos) {
	        if (nextPos.equals(boxPos)) {
	            return false;  // If there's another box, the box cannot move
	        }
	    }

	    return true;  // If none of the above conditions are met, the box can move
	}
}
