package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class ParedeRachada implements GameElement {

	private Point2D Point2D;
	private boolean used = false;

	public ParedeRachada(Point2D Point2D) {
		this.Point2D = Point2D;
	}

	@Override
	public String getName() {
		return "ParedeRachada";
	}

	@Override
	public Point2D getPosition() {
		return Point2D;
	}

	@Override
	public int getLayer() {
		return 0;
	}

	// Getter and setter for 'used' field
	public boolean isUsed() {
		return used;
	}

	public void setUsed() {
		this.used = true;
	}
}
