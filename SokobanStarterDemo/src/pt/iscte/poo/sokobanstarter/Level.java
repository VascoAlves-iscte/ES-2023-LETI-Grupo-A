package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Level {

	private List<Caixote> caixotes = new ArrayList<>();// lista de caixotes
	private List<GameElement> elements = new ArrayList<>();// lista de elementos
	private List<GameElement> portais = new ArrayList<>();// lista de portas
	private Point2D paredesPos[] = new Point2D[100];// array com posição das paredes
	private Point2D caixotesPos[] = new Point2D[100];// array posição caixotes
	private Point2D elementsPos[] = new Point2D[100];// array posição elementos
	private Point2D portaisPos[] = new Point2D[50];// array com posição das portas
	private Point2D empelhadoraPos;

	int caixotesCount = 0;
	int paredesCount = 0;
	int elementsCount = 0;

	private String level;

	public Level(String level) {
		this.level = level;

	}

	public String getName() {
		String[] s = this.level.split("[.]");
		return s[0];
	}

	public List<Caixote> getCaixotes() {
		return caixotes;
	}

	public List<GameElement> getElements() {
		return elements;
	}

	public List<GameElement> getDoors() {
		return portais;
	}

	public Point2D getEmpelhadoraPos() {
		return empelhadoraPos;
	}

	public Point2D[] getParedesPos() {
		return paredesPos;
	}

	public Point2D[] getCaixotesPos() {
		return caixotesPos;
	}

	public Point2D[] getElementsPos() {
		return elementsPos;
	}

	public Point2D[] getPortaisPos() {
		return portaisPos;
	}

	public void addFloor() { // prencher o fundo do mapa com "chão"
		List<ImageTile> tileList = new ArrayList<>();
		for (int x = 0; x != GameEngine.GRID_WIDTH; x++) {
			for (int y = 0; y != GameEngine.GRID_HEIGHT; y++)
				tileList.add(new Chao(new Point2D(x, y)));
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public void readFile() {// adiciona as paredes, inimigos, items atc ao mapa
							// de acordo com o ficheiro lido

		List<ImageTile> tileList = new ArrayList<>();
		File file = new File(level);

		try {
			Scanner sc = new Scanner(file);

			for (int y = 0; y < GameEngine.GRID_HEIGHT; y++) {
				String line = sc.nextLine();
				for (int x = 0; x < GameEngine.GRID_WIDTH; x++) {
					char symbol = line.charAt(x);
					Point2D position = new Point2D(x, y);

					switch (symbol) {
					case '#':
						tileList.add(new Parede(position));
						paredesPos[paredesCount] = position;
						paredesCount++;
						break;

					case '=':
						tileList.add(new Vazio(position));
						break;
					case 'C':
						Caixote caixote = new Caixote(position);
						tileList.add(caixote);
						caixotes.add(caixote);
						caixotesPos[caixotesCount] = position;
						caixotesCount++;
						break;
					case 'X':
						tileList.add(new Alvo(position));
						break;
					case 'E':
						empelhadoraPos = (position);
						break;
					case 'B':
						Bateria bateria = new Bateria(position);
						tileList.add(bateria);
						elements.add(bateria);
						elementsPos[elementsCount] = position;
						elementsCount++;
						break;
					case 'T':
							//informações adicionais dum objeto Teleporte
						break;

					}
				}
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("Ficheiro não encontrado");
		}

		for (int j = 0; j < elements.size(); j++) {
			tileList.add(elements.get(j));
		}

		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public void fillPortaisPos() {
		for (int i = 0; i < portais.size(); i++) {
			Object a = portais.get(i);

			if (a instanceof Teleporte)
				portaisPos[i] = ((Teleporte) a).getPosition();
		}
	}

	public void fillElementsPos() {
		for (int i = 0; i < elements.size(); i++) {
			Object a = elements.get(i);

			if (a instanceof Bateria)
				elementsPos[i] = ((Bateria) a).getPosition();
			if (a instanceof Martelo)
				elementsPos[i] = ((Martelo) a).getPosition();

		}

	}

	public void updPortais() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int j = 0; j < portais.size(); j++) {
			tileList.add(portais.get(j));
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	Point2D oldPos;

	public void updCaixotes() {
		List<ImageTile> tileList = new ArrayList<>();
		// Remove previous caixote images
		for (int i = 0; i < caixotes.size(); i++) {
			Caixote caixote = caixotes.get(i);
			tileList.add(new Chao(caixote.getPosition()));
		}
		tileList.add(new Chao(oldPos));
		// Add the updated caixote images
		for (Caixote caixote : caixotes) {
			tileList.add(caixote);
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}
	
	public void updElements() {
	    List<ImageTile> tileList = new ArrayList<>();

	    // Remove previous elements images and update arrays
	    Iterator<GameElement> iterator = elements.iterator();
	    while (iterator.hasNext()) {
	        GameElement element = iterator.next();

	        if (element instanceof Bateria && ((Bateria) element).isUsed()) {
	            iterator.remove(); // Remove the used Bateria from the list
	            updateElementArray(element.getPosition()); // Update the array of positions
	        } else {
	            tileList.add(new Chao(element.getPosition()));
	        }
	    }

	    // Add the updated elements images
	    for (GameElement element : elements) {
	        tileList.add(element);
	        if (element instanceof Bateria) {
	        	
	        	tileList.add(new Chao(element.getPosition()));
	        
	        }
	    }

	    GameEngine.getInstance().getGUI().addImages(tileList);
	}

	private void updateElementArray(Point2D position) {
	    for (int i = 0; i < elementsPos.length; i++) {
	        if (position.equals(elementsPos[i])) {
	            elementsPos[i] = null; // Set the corresponding position to null
	            break;
	        }
	    }
	}

	public void moveBox(Point2D currentPosition, Point2D newPosition) {
		// Update the position of the Caixote objects
		for (int i = 0; i < caixotes.size(); i++) {
			if (currentPosition.equals(caixotes.get(i).getPosition())) {
				oldPos = currentPosition;
				caixotes.get(i).changePos(newPosition);
				caixotesPos[i] = newPosition;
				break;
			}
		}
		updCaixotes();

	}

}
