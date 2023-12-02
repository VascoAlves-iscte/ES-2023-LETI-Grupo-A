package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Teleporte implements GameElement, Positionable  {

	private Point2D Point2D;
	private Teleporte tpSaida;
	
	public Teleporte(Point2D Point2D){
		this.Point2D = Point2D;
	}
	
	@Override
	public String getName() {
		return "Teleporte";
	}

	@Override
	public Point2D getPosition() {
		return Point2D;
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public Teleporte getTpSaida() {
		return tpSaida;
	}
	
	public void setTpSaida(Teleporte tpSaida) {
        this.tpSaida = tpSaida;
    }


    
    public boolean isSomethingOnTop(Point2D position, Level level) {
        for (GameElement element : level.getElements()) {
            if (!element.equals(this) && element.getPosition().equals(position)) {
                return true; // Something is on top
            }
        }
        return false; // Nothing on top
    }

    public void changePos(Point2D pos) {
		Point2D = pos;
	}
}
