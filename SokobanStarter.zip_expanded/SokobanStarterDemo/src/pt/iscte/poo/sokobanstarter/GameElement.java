package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public interface GameElement extends ImageTile  {

	public default String getName() {
		return null;	
	}

	
	public default Point2D getPosition() {
		return null;
		
	}

	
	public default int getLayer() {
		return 1;
		
	}
	
	
}
