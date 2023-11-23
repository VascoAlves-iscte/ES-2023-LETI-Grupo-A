package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Parede implements ImageTile {

	private Point2D position;

	public Parede(Point2D position) {
		this.position = position;
	}

	@Override
	public String getName() {
		return "Parede";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
