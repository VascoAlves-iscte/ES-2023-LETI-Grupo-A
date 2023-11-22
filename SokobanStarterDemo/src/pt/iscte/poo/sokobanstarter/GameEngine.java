package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {

	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	private int turns;
	private static GameEngine INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();;
	private Empilhadora empilhadora;
	// Usa o caminho relativo à pasta "levels"
	String levelFileName = "levels/level5.txt";
	Level level = new Level(levelFileName);

	private String levelAtual;
	private List<Level> levels = new ArrayList<>();

	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	private GameEngine() {
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);
		gui.registerObserver(this);
		gui.go();
	}

	public void start() throws Exception {

		levelAtual = level.getName();
		levels.add(level);

		level.addFloor();
		level.readFile(); // Leitura do ficheiro do nível e inicialização do mesmo

		empilhadora = new Empilhadora(level.getEmpelhadoraPos());
		gui.addImage(empilhadora);

		gui.setStatusMessage(
				"Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
		gui.update();
	}

	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();

		empilhadora.move(key, level.getParedesPos(), level.getCaixotesPos(), level);

		if (empilhadora.moved == true) {// deteta se o heroi se mexeu (user jogou)
			turns++;
			empilhadora.drainBateria();
			empilhadora.toggleMoved();

			if (Arrays.asList(level.getElementsPos()).contains(empilhadora.getPosition())) {

				for (int i = 0; i < level.getElements().size(); i++) {
					Object a = level.getElements().get(i);

					if (a instanceof Bateria) {
						if (((Bateria) a).getPosition().equals(empilhadora.getPosition())) {
							
							empilhadora.useBateria();
							((Bateria) a).setUsed();
							level.updElements();
						}
					}
				}
			}
		}

		gui.setStatusMessage(
				"Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());

		gui.update();

	}

	public ImageMatrixGUI getGUI() {
		return gui;
	}
}
