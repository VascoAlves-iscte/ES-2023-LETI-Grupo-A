package pt.iscte.poo.sokobanstarter;

import java.util.Arrays;
import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface Positionable {
	
	Point2D getPosition();
	
	void changePos(Point2D newPosition);

	public default Point2D nextPos(int key, Point2D empelhadoraPos) {// calcula proxima posição da empelhadora de acordo
																		// com a key usada

		if (Direction.isDirection(key)) {

			Direction d = Direction.directionFor(key);
			Point2D nextPosition = empelhadoraPos.plus(d.asVector());

			return nextPosition;
		}
		return null;
	}

	public default boolean canMove(Point2D nextPos, Level level) {// verifica se entidade se pode mover para a próxima
																	// posição
		
		Empilhadora empilhadora = GameEngine.getInstance().getEmpilhadora();
		boolean withinBounds = (nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < 10
				&& nextPos.getY() < 10);// verifica se esta dentro do mapa

		if (!(empilhadora.getMarteloState())) {
			for (GameElement element : level.getElements()) {
				if (element instanceof ParedeRachada) {
					ParedeRachada paredeRachada = (ParedeRachada) element;
					if (nextPos.equals(paredeRachada.getPosition())) {

						return false;
					}
				}
			}
		}

		if (!Arrays.asList(level.getParedesPos()).contains(nextPos) && withinBounds) {

			return true;
		}
		return false;
	}

	public default boolean isObjectAtPosition(Point2D[] objectosPos, Point2D pos) {
		for (Point2D objPos : objectosPos) {
			if (pos.equals(objPos)) {
				return true;
			}
		}
		return false;
	}

	public default boolean isMarteloAtPosition(List<GameElement> elements, Point2D pos) {
		for (GameElement element : elements) {
			if (element instanceof Martelo) {
				Martelo martelo = (Martelo) element;
				if (pos.equals(martelo.getPosition())) {
					return true;
				}
			}
		}
		return false;
	}

	public default boolean canMoveBoxPaletes(Point2D currentPos, Point2D nextPos, Level level) {
		// Check if the next position is within the boundaries of the grid
		boolean withinBounds = (nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < GameEngine.GRID_WIDTH
				&& nextPos.getY() < GameEngine.GRID_HEIGHT);

		if (!withinBounds) {
			return false; // If the next position is outside the grid, the box cannot move
		}

		// Check if there's a wall at the next position
		for (Point2D wallPos : level.getParedesPos()) {
			if (nextPos.equals(wallPos)) {
				return false; // If there's a wall, the box cannot move
			}
		}

		// Check if there's another box at the next position
		for (Point2D boxPos : level.getCaixotesPos()) {
			if (nextPos.equals(boxPos)) {
				return false; // If there's another box, the box cannot move
			}
		}

		// Check if there's another palete at the next position
		for (Point2D palePos : level.getPaletesPos()) {
			if (nextPos.equals(palePos)) {
				Palete paleteAtNextPos = level.getPaleteAtPosition(nextPos);
				if (paleteAtNextPos != null && paleteAtNextPos.isPlaced()) {
					// If there's a placed Palete, return true
					return true;
				}
				return false; // If there's another palete and it's not placed, the box cannot move
			}
		}

		// Check if there's another parede rachada at the next position
		for (GameElement element : level.getElements()) {
			if (element instanceof ParedeRachada) {
				ParedeRachada paredeRachada = (ParedeRachada) element;
				if (nextPos.equals(paredeRachada.getPosition())) {

					return false;
				}
			}
		}

		return true; // If none of the above conditions are met, the box can move
	}

}
