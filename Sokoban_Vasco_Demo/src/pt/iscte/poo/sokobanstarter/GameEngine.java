package pt.iscte.poo.sokobanstarter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Point2D;

/**
 * Classe que representa o motor do jogo Sokoban.
 * Esta classe implementa o padrão Singleton e a interface Observer.
 * E responsavel por gerir o estado do jogo, interações do jogador e logica do jogo.
 */

public class GameEngine implements Observer {

    public static final int GRID_HEIGHT = 10;
    public static final int GRID_WIDTH = 10;

    private int turns;
    private static GameEngine INSTANCE = null;
    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private Empilhadora empilhadora;

    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;

    private Level level;
    private String nomeJogador;
    private TabelaPontuacoes tabelaPontuacoes = new TabelaPontuacoes();

    // Singleton pattern, make the constructor private
    private GameEngine() {
        gui.setSize(GRID_HEIGHT, GRID_WIDTH);
        gui.registerObserver(this);
        gui.go();
        loadLevels();
    }
    
    //Obtem a referencia para a interface grafica do jogo.
    public ImageMatrixGUI getGUI() {
		return gui;
	}
    
    //Obtem a instancia da empilhadora do jogo.
    public Empilhadora getEmpilhadora() {
    	return empilhadora;
    }

    //Obtem a unica instancia do GameEngine (Singleton pattern).
     
    public static GameEngine getInstance() {
        if (INSTANCE == null)
            return INSTANCE = new GameEngine();
        return INSTANCE;
    }

    //Inicia o jogo.
    //Exceção lançada se ocorrer um problema durante o inicio do jogo.
    public void start() throws Exception {

        solicitarNomeJogador();

        level = levels.get(currentLevelIndex);

        level.addFloor();
        level.readFile();

        empilhadora = new Empilhadora(level.getEmpelhadoraPos());
        gui.addImage(empilhadora);

        gui.setStatusMessage("Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
        gui.update();
    }

    //carregar os niveis a partir do ficheiro levels
    private void loadLevels() {
        String levelFileNamePattern = "levels/level%d.txt";
        int levelIndex = 0;

        while (true) {
            String currentLevelFileName = String.format(levelFileNamePattern, levelIndex);
            Level currentLevel = new Level(currentLevelFileName);

            if (currentLevel.fileExists()) {
                levels.add(currentLevel);
                levelIndex++;
            } else {
                break;
            }
        }
    }

    // Reinicia o nivel atual
    private void restartLevel() {
        level = levels.get(currentLevelIndex);

        level.clearLevel();

        gui.clearImages();
        turns = 0;

        level.addFloor();
        level.readFile();

        empilhadora.changePos(level.getEmpelhadoraPos());
        gui.addImage(empilhadora);
        empilhadora.resetBateria();

        gui.setStatusMessage("Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
        gui.update();
    }

    //exibir o menu de reinicio ou saida
    private void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What do you want to do ?");
        System.out.println("1. Restart Level");
        System.out.println("2. Quit");

        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                restartLevel();
                break;
            case 2:
                System.out.println("Quitting the game and saving the Scores. Goodbye!");
                tabelaPontuacoes.writeScoresToFile(nomeJogador);
                tabelaPontuacoes.updateAndWriteHighScores();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
                displayMenu();
        }
    }

    // carrega o proximo nivel
    private void loadNextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            level = levels.get(currentLevelIndex);

            gui.clearImages();
            turns = 0;

            level.addFloor();
            level.readFile();

            empilhadora.changePos(level.getEmpelhadoraPos());
            gui.addImage(empilhadora);
            empilhadora.resetBateria();

            gui.setStatusMessage("Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
            gui.update();
        } else {
            System.out.println("Congratulations! You have completed all levels.");
            tabelaPontuacoes.writeScoresToFile(nomeJogador);
            tabelaPontuacoes.updateAndWriteHighScores();
            System.exit(0);
        }
    }


    private void solicitarNomeJogador() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do jogador: ");
        nomeJogador = scanner.nextLine();
    }

    // atualiza o jogo com base nas interações do jogador
    @Override
    public void update(Observed source) {
        int key = gui.keyPressed();

        empilhadora.move(key, level);

        if (empilhadora.moved) {
            turns++;
            empilhadora.drainBateria();
            level.fixBuracos();

            if (empilhadora.getBateria() == 0) {
                gui.setMessage("Out of battery! What now...?");
                displayMenu();
            }

            empilhadora.toggleMoved(false);

            handleElementInteractions();

            handleTeleportation();

            gui.setStatusMessage("Sokoban Starter - Turns:" + turns + "            " + "Bateria:" + empilhadora.getBateria());
            gui.update();

            handleGameOutcome();
        }
    }

    // Handle interactions with game elements (Bateria, Buraco, Martelo, ParedeRachada)
    private void handleElementInteractions() {
        if (Arrays.asList(level.getElementsPos()).contains(empilhadora.getPosition())) {
            for (int i = 0; i < level.getElements().size(); i++) {
                Object a = level.getElements().get(i);

                if (a instanceof Bateria && ((Bateria) a).getPosition().equals(empilhadora.getPosition())) {
                    empilhadora.useBateria();
                    ((Bateria) a).setUsed();
                    level.updElements();
                }

                if (a instanceof Buraco && ((Buraco) a).getPosition().equals(empilhadora.getPosition())) {
                    gui.setMessage("You fell! What now...?");
                    displayMenu();
                }

                if (a instanceof Martelo && ((Martelo) a).getPosition().equals(empilhadora.getPosition())) {
                    empilhadora.toggleMartelo();
                    ((Martelo) a).setUsed();
                    level.updElements();
                }

                if (a instanceof ParedeRachada
                        && ((ParedeRachada) a).getPosition().equals(empilhadora.getPosition())
                        && empilhadora.getMarteloState()) {
                    ((ParedeRachada) a).setUsed();
                    level.updElements();
                }
            }
        }
    }

    // Handle teleportation logic
    private void handleTeleportation() {
        if (Arrays.asList(level.getPortaisPos()).contains(empilhadora.getPosition())) {
            for (int i = 0; i < level.getPortais().size(); i++) {
                Object a = level.getPortais().get(i);
                Point2D posSaida = (((Teleporte) a).getTpSaida()).getPosition();

                if (a instanceof Teleporte && ((Teleporte) a).getPosition().equals(empilhadora.getPosition())) {
                    if (!((Teleporte) a).isSomethingOnTop(posSaida, level)) {
                        empilhadora.changePos(posSaida);
                        break;
                    }
                }
            }
        }
    }

    // Handle the outcome of the game (victory or not enough caixotes)
    private void handleGameOutcome() {
        if (level.allAlvosFilled()) {
            System.out.println("You won level " + currentLevelIndex + " with a score of " + turns + "!");
            Pontuacao pontuacao = new Pontuacao(nomeJogador, turns, currentLevelIndex);
            tabelaPontuacoes.adicionarPontuacao(pontuacao);
            loadNextLevel();
        }

        if (level.notEnoughCaixotes()) {
            gui.setMessage("Out of boxes! What now...?");
            displayMenu();
        }
    }
}
