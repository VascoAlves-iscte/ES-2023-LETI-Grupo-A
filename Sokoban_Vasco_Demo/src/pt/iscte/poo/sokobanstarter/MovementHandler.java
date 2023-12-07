package pt.iscte.poo.sokobanstarter;

import java.util.Arrays;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

//Classe responsavel por lidar com a movimentacao da empilhadora no jogo
public class MovementHandler implements Positionable {

	// Metodo que move a empilhadora no jogo para a proxima posicao com base na
	// tecla pressionada.
	// Verifica se a empilhadora pode mover-se para a proxima posicao e trata
	// interacoes com caixotes e paletes.

	public void empilhadoraMove(int key, Level level, Point2D nextPos, Empilhadora empilhadora) {

		if (canMove(nextPos, level)) {
			if (isCaixoteAtPosition(level, nextPos)) {
				moveCaixoteAtPosition(level, key, nextPos, empilhadora);
			} else {
				if (isPaleteAtPosition(level, nextPos)) {
					movePaleteAtPosition(level, key, nextPos, empilhadora);
				} else {
					empilhadora.changePos(nextPos);
					empilhadora.toggleMoved(true);
				}
			}
		}
	}

	// Verifica se ha um caixote na posicao
	public boolean isCaixoteAtPosition(Level level, Point2D nextPos) {
		return isObjectAtPosition(level.getCaixotesPos(), nextPos);
	}

	// Verifica se ha uma palete na posicao
	public boolean isPaleteAtPosition(Level level, Point2D nextPos) {
		return isObjectAtPosition(level.getPaletesPos(), nextPos);
	}

	// Move o caixote para a proxima posicao se possivel.
	public void moveCaixoteAtPosition(Level level, int key, Point2D position, Empilhadora empilhadora) {
		// calculo da nova posicao do caixote
		Point2D caixoteNextPos = nextPos(key, position);

		// Verifica se tanto a empilhadora como o caixote podem ser movidos para as
		// novas posicoes
		if (canMove(caixoteNextPos, level) && canMoveBoxPaletes(position, caixoteNextPos, level)) {

			if (isObjectAtPosition(level.getPortaisPos(), caixoteNextPos)) {
				Caixote caixote = level.getCaixoteAtPosition(position);
				level.teleportObject(caixote, caixoteNextPos, key);
			}

			// move a empilhadora e o caixote e verifica se algum caixote esta na posicao de
			// um buraco
			empilhadora.changePos(position);
			level.moveCaixote(position, caixoteNextPos);
			level.checkBuraco();
			empilhadora.toggleMoved(true);
		}
	}

	// Move a palete para a proxima posicao se possivel.
	public void movePaleteAtPosition(Level level, int key, Point2D position, Empilhadora empilhadora) {
		Palete paleteAtNextPos = level.getPaleteAtPosition(position);
		if (paleteAtNextPos != null && paleteAtNextPos.isPlaced()) {
			// se for uma palete que teja "Placed", a empilhadora move para a posicao mas a
			// palete n se muda
			empilhadora.changePos(position);
			empilhadora.toggleMoved(true);
		} else {
			// calculo da nova posicao da palete
			Point2D paleteNextPos = nextPos(key, position);

			// Verifica se tanto a empilhadora como a palete podem ser movidas para as novas
			// posicoes
			if (canMove(paleteNextPos, level) && canMoveBoxPaletes(position, paleteNextPos, level)) {

				if (isObjectAtPosition(level.getPortaisPos(), paleteNextPos)) {
					Palete palete = level.getPaleteAtPosition(position);
					level.teleportObject(palete, paleteNextPos, key);
				}
				// move a empilhadora e a palete
				empilhadora.changePos(position);
				level.movePalete(position, paleteNextPos);
				empilhadora.toggleMoved(true);
			}
		}
	}

	public boolean canMove(Point2D nextPos, Level level) {// verifica se entidade se pode mover para a próxima
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

	
	// calcula proxima posição da empelhadora de acordo com a tecla usada
	public Point2D nextPos(int key, Point2D empelhadoraPos) {
		
		if (Direction.isDirection(key)) {

			Direction d = Direction.directionFor(key);
			Point2D nextPosition = empelhadoraPos.plus(d.asVector());

			return nextPosition;
		}
		return null;
	}

	
	//verifica se e possivel mover palete e caixote de acordo com as regras do jogo
	public boolean canMoveBoxPaletes(Point2D currentPos, Point2D nextPos, Level level) {
		// verifica se a proxima posicao esta dentro dos limites do jogo
		boolean withinBounds = (nextPos.getX() >= 0 && nextPos.getY() >= 0 && nextPos.getX() < GameEngine.GRID_WIDTH
				&& nextPos.getY() < GameEngine.GRID_HEIGHT);

		if (!withinBounds) {
			return false;
		}

		//verifica se tem paredes na proxima posicao
		for (Point2D wallPos : level.getParedesPos()) {
			if (nextPos.equals(wallPos)) {
				return false; // If there's a wall, the box cannot move
			}
		}

		//verifica se tem um caixote na proxima posicao
		for (Point2D boxPos : level.getCaixotesPos()) {
			if (nextPos.equals(boxPos)) {
				return false; // If there's another box, the box cannot move
			}
		}

		//verifica se tem uma palete na proxima posicao
		for (Point2D palePos : level.getPaletesPos()) {
			if (nextPos.equals(palePos)) {
				Palete paleteAtNextPos = level.getPaleteAtPosition(nextPos);
				if (paleteAtNextPos != null && paleteAtNextPos.isPlaced()) {
					//se tiver um palete "placed" retorna true de forma a deixar a empilhadora passar
					return true;
				}
				return false; 
			}
		}

		//verifica se tem uma parede rachada na proxima posicao
		for (GameElement element : level.getElements()) {
			if (element instanceof ParedeRachada) {
				ParedeRachada paredeRachada = (ParedeRachada) element;
				if (nextPos.equals(paredeRachada.getPosition())) {
					return false;
				}
			}
		}

		return true; //se nenhuma da condicoes acima for verificada o objeto pode-se mexer
	}

	@Override
	public Point2D getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePos(Point2D newPosition) {
		// TODO Auto-generated method stub
	}
}
