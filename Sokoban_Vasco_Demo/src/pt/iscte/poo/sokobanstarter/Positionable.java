package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

//interafce com metodos de uso para objetos com posicao no jogo Sokoban.

public interface Positionable {
	
	//Obtém a posição do objeto.	
	Point2D getPosition();
	
	//Altera a posição do objeto para a nova posição especificada
	void changePos(Point2D newPosition);

	//Verifica se há um objeto com posicao numa posição específica.	 
	public default boolean isObjectAtPosition(Point2D[] objectosPos, Point2D pos) {
		for (Point2D objPos : objectosPos) {
			if (pos.equals(objPos)) {
				return true;
			}
		}
		return false;
	}

}
