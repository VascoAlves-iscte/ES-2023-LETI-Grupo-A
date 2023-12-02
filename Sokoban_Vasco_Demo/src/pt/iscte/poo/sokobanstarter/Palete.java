package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Palete implements GameElement, Positionable {

	private Point2D Point2D;
	private boolean placed = false;
	
	public Palete(Point2D Point2D){
		this.Point2D = Point2D;
	}
	
	@Override
	public String getName() {
		return "Palete";
	}

	@Override
	public Point2D getPosition() {
		return Point2D;
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public boolean isPlaced() {
        return placed;
    }
	
	@Override
	public void changePos(Point2D pos) {
		Point2D = pos;
	}
	
	public void togglePlaced() {
		placed = true;
	}
}
