package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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


	private List<Level> levels = new ArrayList<>();
	private int currentLevelIndex = 0;

	Level level;
	String levelAtual;
	String nomeJogador;
	TabelaPontuacoes tabelaPontuacoes = new TabelaPontuacoes();

	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	private GameEngine() {
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);
		gui.registerObserver(this);
		gui.go();
		loadLevels();
	}

	public void start() throws Exception {

		solicitarNomeJogador();

		level = levels.get(currentLevelIndex);

		level.addFloor();
		level.readFile(); // Leitura do ficheiro do nível e inicialização do mesmo

		empilhadora = new Empilhadora(level.getEmpelhadoraPos());
		gui.addImage(empilhadora);

		gui.setStatusMessage(
				"Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
		gui.update();

	}

	private void loadLevels() {
		// Assuming levels are named as "level0.txt", "level1.txt", ...
		String levelFileNamePattern = "levels/level%d.txt";
		int levelIndex = 0;

		while (true) {
			String currentLevelFileName = String.format(levelFileNamePattern, levelIndex);
			Level currentLevel = new Level(currentLevelFileName);

			if (currentLevel.fileExists()) {
				levels.add(currentLevel);
				levelIndex++;
			} else {
				break; // Exit the loop if the next level file doesn't exist
			}
		}

	}

	private void restartLevel() {

		level = levels.get(currentLevelIndex);

		level.clearLevel();

		// Clear the GUI and reset turns
		gui.clearImages();
		turns = 0;

		// Add floor and read the new level
		level.addFloor();
		level.readFile();

		// Reset the empilhadora position
		empilhadora.changePos(level.getEmpelhadoraPos());
		gui.addImage(empilhadora);
		empilhadora.resetBateria();

		// Update the GUI status
		gui.setStatusMessage(
				"Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());

		// Update the GUI
		gui.update();

		

	}

	private void displayBatteryOutMenu() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Out of battery! What do you want to do?");
		System.out.println("1. Restart Level");
		System.out.println("2. Quit");

		System.out.print("Choose an option: ");
		int choice = scanner.nextInt();

		switch (choice) {
		case 1:
			restartLevel();
			break;
		case 2:
			System.out.println("Quitting the game. Goodbye!");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice. Please enter 1 or 2.");
			displayBatteryOutMenu(); // Ask again if the choice is invalid
		}
	}

	private void loadNextLevel() {

		// Check if there is a next level
		if (currentLevelIndex < levels.size() - 1) {
			// Increment the index to load the next level

			currentLevelIndex++;

			// Load the next level
			level = levels.get(currentLevelIndex);

			// Clear the GUI and reset turns
			gui.clearImages();
			turns = 0;

			// Add floor and read the new level
			level.addFloor();
			level.readFile();

			// Reset the empilhadora position
			empilhadora.changePos(level.getEmpelhadoraPos());
			gui.addImage(empilhadora);
			empilhadora.resetBateria();

			// Update the GUI status
			gui.setStatusMessage(
					"Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());

			// Update the GUI
			gui.update();
		} else {
			// If there is no next level, you could handle it as a game completion
			System.out.println("Congratulations! You have completed all levels.");
		}
	}

	private void solicitarNomeJogador() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite o nome do jogador: ");
		nomeJogador = scanner.nextLine();
	}

	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();

		empilhadora.move(key, level.getParedesPos(), level.getCaixotesPos(), level.getAlvosPos(), level);

		if (empilhadora.moved == true) {// deteta se o heroi se mexeu (user jogou)
			turns++;
			empilhadora.drainBateria();
			if (empilhadora.getBateria() == 0) {
				int option = gui.setMessageWithOptions("Out of battery! What now...?");
				if(option==1) {
					restartLevel();
				}else {
					tabelaPontuacoes.writeScoresToFile("scores");
					System.exit(0);
				}
				//gui.setMessage("Out of battery! What now...?");
				//displayBatteryOutMenu();
			}

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

		if (level.checkForVictory()) {
			System.out.println("You won level "+ currentLevelIndex + "with a score of "+ turns + "!");
			// No final de cada nível, criar uma instância de Pontuacao
			Pontuacao pontuacao = new Pontuacao(nomeJogador, turns, currentLevelIndex); // Supondo que 'turns' seja a
																						// pontuação

			tabelaPontuacoes.adicionarPontuacao(pontuacao);
			loadNextLevel();
		}

		gui.update();

	}

	public ImageMatrixGUI getGUI() {
		return gui;
	}
}
