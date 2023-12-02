package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Alvo implements GameElement, Positionable {

	private Point2D Point2D;
	
	public Alvo(Point2D Point2D){
		this.Point2D = Point2D;
	}
	
	@Override
	public String getName() {
		return "Alvo";
	}

	@Override
	public Point2D getPosition() {
		return Point2D;
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public boolean hasBox(Point2D[] caixotesPos) {
        return isObjectAtPosition(caixotesPos, getPosition());
    }

	@Override
	public void changePos(Point2D newPosition) {		
	}

}
