package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Buraco implements GameElement, Positionable {

	private Point2D position;

	public Buraco(Point2D Point2D) {
		this.position = Point2D;
	}

	@Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 0;
	}

	public boolean isFilled(Point2D paletesPos[]) {
		if (isObjectAtPosition(paletesPos, position) == true) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void changePos(Point2D newPosition) {	
	}
}
