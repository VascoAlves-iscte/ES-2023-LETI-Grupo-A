package pt.iscte.poo.sokobanstarter;



import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;


public class Empilhadora implements ImageTile, Positionable {

	private Point2D position;
	private String imageName;
	public boolean moved;
	private int bateria = 100;
	private boolean hasMartelo = false;

	public Empilhadora(Point2D position) {
		this.position = position;
		imageName = "Empilhadora_D";
	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public int getBateria() {
		return bateria;
	}

	public boolean getMarteloState() {
		return hasMartelo;
	}

	public void setMarteloState(boolean hasMartelo) {
		this.hasMartelo = hasMartelo;
	}

	public void toggleMoved() {// mudar o moved para false apos cada jogada
		moved = false;
	}

	public void toggleMartelo() {
		setMarteloState(true);
	}

	public void drainBateria() {
		bateria--;
	}

	public void resetBateria() {
		bateria = 100;
	}

	public void useBateria() {
		if (bateria + 50 <= 100) {
			bateria = bateria + 50;
		} else {
			bateria = bateria + (100 - bateria);
		}
	}

	public void move(int key, Level level) {
		Point2D nextPos = nextPos(key, position);
		Direction d = Direction.directionFor(key);

		switch (d) {
		case UP:
			imageName = "Empilhadora_U";
			break;
		case DOWN:
			imageName = "Empilhadora_D";
			break;
		case LEFT:
			imageName = "Empilhadora_L";
			break;
		case RIGHT:
			imageName = "Empilhadora_R";
			break;
		}

		if (canMove(nextPos, level)) {
			// Check if there's a box at the next position
			if (isObjectAtPosition(level.getCaixotesPos(), nextPos)) {
				// Calculate the new position for the box
				Point2D caixoteNextPos = nextPos(key, nextPos);
				

				// Check if both empilhadora and the box can be moved to their new positions
				if (canMove(caixoteNextPos, level) && canMoveBoxPaletes(nextPos, caixoteNextPos, level)) {

					if (isObjectAtPosition(level.getPortaisPos(), caixoteNextPos)) {
						Caixote caixote = level.getCaixoteAtPosition(nextPos);
						level.teleportObject(caixote,caixoteNextPos,key);
						Point2D caixotePortalSaidaPos = nextPos(key, nextPos);
					}
					// Move the empilhadora to the next position
					this.position = nextPos;

					// Move the box to the new position
					level.moveCaixote(nextPos, caixoteNextPos);
					level.checkBuraco();
					moved = true;
				}
			} else {
				if (isObjectAtPosition(level.getPaletesPos(), nextPos)) {
					Palete paleteAtNextPos = level.getPaleteAtPosition(nextPos);
					if (paleteAtNextPos != null && paleteAtNextPos.isPlaced()) {
						// If there's a placed Palete, move through it
						this.position = nextPos;
						moved = true;
					} else {
						// Calculate the new position for the palete
						Point2D paleteNextPos = nextPos(key, nextPos);
						

						// Check if both empilhadora and the palete can be moved to their new positions
						if (canMove(paleteNextPos, level) && canMoveBoxPaletes(nextPos, paleteNextPos, level)) {

							if (isObjectAtPosition(level.getPortaisPos(), paleteNextPos)) {
								Palete palete = level.getPaleteAtPosition(nextPos);
								level.teleportObject(palete,paleteNextPos,key);
							}
							// Move the empilhadora to the next position
							this.position = nextPos;

							// Move the box to the new position
							level.movePalete(nextPos, paleteNextPos);
							level.checkBuraco();
							moved = true;
						}
					}
				} else {
					// If there's no box or palete, move the empilhadora to the next position
					this.position = nextPos;
					moved = true;
				}
			}
		}

	}

	@Override
	public void changePos(Point2D pos) {
		position = pos;
	}

}