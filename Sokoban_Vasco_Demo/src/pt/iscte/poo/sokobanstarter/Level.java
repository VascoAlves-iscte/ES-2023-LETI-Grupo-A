package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Level{

	private List<Caixote> caixotes = new ArrayList<>();// lista de caixotes
	private List<GameElement> elements = new ArrayList<>();// lista de elementos
	private List<Teleporte> portais = new ArrayList<>();// lista de portas
	private List<Alvo> alvos = new ArrayList<>();// lista de alvos
	private List<Palete> paletes = new ArrayList<>();// lista de paletes
	private Point2D paredesPos[] = new Point2D[100];// array com posição das paredes
	private Point2D caixotesPos[] = new Point2D[100];// array posição caixotes
	private Point2D elementsPos[] = new Point2D[100];// array posição elementos
	private Point2D alvosPos[] = new Point2D[10];// array posição alvos
	private Point2D paletesPos[] = new Point2D[10]; // array com posicao das paletes
	private Point2D portaisPos[] = new Point2D[2];// array com posição das portas
	private Point2D empelhadoraPos;

	int caixotesCount = 0;
	int paredesCount = 0;
	int elementsCount = 0;
	int alvosCount = 0;
	int paletesCount = 0;
	int portaisCount = 0;
	Point2D oldPos;

	private String level;
	private File levelFile;

	public Level(String levelFileName) {
		this.levelFile = new File(levelFileName);
		// Other existing initialization...
	}

	public boolean fileExists() {
		return levelFile.exists();
	}

	public File getFileName() {
		return levelFile;
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

	public List<Teleporte> getPortais() {
		return portais;
	}

	public List<Alvo> getAlvos() {
		return alvos;
	}

	public List<Palete> getPaletes() {
		return paletes;
	}

	public Point2D getEmpelhadoraPos() {
		return empelhadoraPos;
	}

	public Point2D[] getAlvosPos() {
		return alvosPos;
	}

	public Point2D[] getParedesPos() {
		return paredesPos;
	}

	public Point2D[] getCaixotesPos() {
		return caixotesPos;
	}

	public Point2D[] getPaletesPos() {
		return paletesPos;
	}

	public Point2D[] getElementsPos() {
		return elementsPos;
	}

	public Point2D[] getPortaisPos() {
		return portaisPos;
	}

	public void clearLevel() {
		// Clear arrays and reset counts
		clearArrays();
		clearLists();
		resetCounts();

		empelhadoraPos = null; // Reset empilhadora position
	}

	private void clearArrays() {
		Arrays.fill(paredesPos, null);
		Arrays.fill(caixotesPos, null);
		Arrays.fill(elementsPos, null);
		Arrays.fill(alvosPos, null);
		Arrays.fill(portaisPos, null);
		Arrays.fill(paletesPos, null);
	}

	private void clearLists() {
		caixotes.clear();
		elements.clear();
		portais.clear();
		alvos.clear();
		paletes.clear();
	}

	private void resetCounts() {
		caixotesCount = 0;
		paredesCount = 0;
		elementsCount = 0;
		alvosCount = 0;
		paletesCount = 0;
		portaisCount = 0;
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

		File file = levelFile;

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
						Alvo alvo = new Alvo(position);
						tileList.add(alvo);
						alvos.add(alvo);
						alvosPos[alvosCount] = position;
						alvosCount++;
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
					case 'O':
						Buraco buraco = new Buraco(position);
						tileList.add(buraco);
						elements.add(buraco);
						elementsPos[elementsCount] = position;
						elementsCount++;
						break;
					case 'P':
						Palete palete = new Palete(position);
						tileList.add(palete);
						paletes.add(palete);
						paletesPos[paletesCount] = position;
						paletesCount++;
						break;
					case 'M':
						Martelo martelo = new Martelo(position);
						tileList.add(martelo);
						elements.add(martelo);
						elementsPos[elementsCount] = position;
						elementsCount++;
						break;
					case '%':
						ParedeRachada paredeRachada = new ParedeRachada(position);
						tileList.add(paredeRachada);
						elements.add(paredeRachada);
						elementsPos[elementsCount] = position;
						elementsCount++;
						break;
					case 'T':
						Teleporte teleporte = new Teleporte(position);
						tileList.add(teleporte);
						portais.add(teleporte);
						portaisPos[portaisCount] = position;
						portaisCount++;
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
		setSaidasPortais();

		GameEngine.getInstance().getGUI().addImages(tileList);
	}
	
	public void setSaidasPortais() {
		if (portais.size() == 2) {
            Teleporte portalEntrada = portais.get(0);
            Teleporte portalSaida = portais.get(1);

            // Set the exit teleporter for the entrance teleporter
            portalEntrada.setTpSaida(portalSaida); 
            // Set the entrance teleporter for the exit teleporter
            portalSaida.setTpSaida(portalEntrada);
        }
	}

	public boolean allAlvosFilled() {
		for (Alvo alvo : alvos) {
			if (!alvo.hasBox(caixotesPos)) {
				return false; // If any Alvo doesn't have a box, return false
			}
		}
		return true; // If all Alvos have a box, return true
	}

	public void checkBuraco() {
		List<Caixote> caixotesToRemove = new ArrayList<>();

		for (Caixote caixote : caixotes) {
			for (GameElement b : elements) {
				if (b instanceof Buraco && ((Buraco) b).getPosition().equals(caixote.getPosition())) {
					caixotesToRemove.add(caixote);
					oldPos = caixote.getPosition();
				}
			}
		}

		// Remove caixotes outside the loop to avoid concurrent modification
		caixotes.removeAll(caixotesToRemove);
		updCaixotesPaletes();
	}

	public void fixBuracos() {
		List<ImageTile> tileList = new ArrayList<>();

		Iterator<GameElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			GameElement element = iterator.next();
			if (element instanceof Buraco) {
				Buraco buraco = (Buraco) element;
				if (buraco.isFilled(paletesPos)) {
					iterator.remove(); // Remove the Buraco from the list of elements
					tileList.add(new Palete(buraco.getPosition()));
					// Set the placed property of the Palete to true
					Point2D paletePosition = buraco.getPosition();
					Palete palete = getPaleteAtPosition(paletePosition);
					if (palete != null) {
						palete.togglePlaced();
					}
				}

			} else {
				tileList.add(element);
			}
		}

		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public boolean checkForVictory() {
		if (allAlvosFilled()) {

			return true;

		} else {
			return false;
		}

	}

	public boolean notEnoughCaixotes() {
		return caixotes.size() < alvos.size();
	}

	public void updPortais() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int j = 0; j < portais.size(); j++) {
			tileList.add(portais.get(j));
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public void updCaixotesPaletes() {
		List<ImageTile> tileList = new ArrayList<>();
		// Remove previous caixote images
		for (int i = 0; i < caixotes.size(); i++) {
			Caixote caixote = caixotes.get(i);
			tileList.add(new Chao(caixote.getPosition()));
		}

		// Remove previous paletes images
		for (int i = 0; i < paletes.size(); i++) {
			Palete palete = paletes.get(i);
			if (palete.isPlaced()) {
				tileList.add(new Buraco(palete.getPosition())); //por buraco com palete em cima ("pacthed")
				tileList.add(new Palete(palete.getPosition()));
			} else {
				tileList.add(new Chao(palete.getPosition()));
			}
		}

		tileList.add(new Chao(oldPos));

		// Add the updated caixote, alvo and palete images
		for (Alvo alvo : alvos) {
			tileList.add(alvo);
		}

		for (Palete palete : paletes) {
			tileList.add(palete);
		}

		for (GameElement element : elements) {
			tileList.add(element);
		}

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
				tileList.add(new Chao(element.getPosition()));
			}

			if (element instanceof Martelo && ((Martelo) element).isUsed()) {
				iterator.remove(); // Remove the used Martelo from the list
				updateElementArray(element.getPosition()); // Update the array of positions
				tileList.add(new Chao(element.getPosition()));
			}

			if (element instanceof ParedeRachada && ((ParedeRachada) element).isUsed()) {
				iterator.remove(); // Remove the used ParedeRachada from the list
				updateElementArray(element.getPosition()); // Update the array of positions
				tileList.add(new Chao(element.getPosition()));
			}
		}

		// Add the updated elements images
		for (GameElement element : elements) {
			tileList.add(element);

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

	public void moveCaixote(Point2D currentPosition, Point2D newPosition) {
		// Update the position of the Caixote objects
		for (int i = 0; i < caixotes.size(); i++) {
			if (currentPosition.equals(caixotes.get(i).getPosition())) {
				oldPos = currentPosition;
				caixotes.get(i).changePos(newPosition);
				caixotesPos[i] = newPosition;
				break;
			}

		}
		updCaixotesPaletes();

	}

	public void movePalete(Point2D currentPosition, Point2D newPosition) {
		// Update the position of the Palete objects
		for (int i = 0; i < paletes.size(); i++) {
			if (currentPosition.equals(paletes.get(i).getPosition())) {
				oldPos = currentPosition;
				paletes.get(i).changePos(newPosition);
				paletesPos[i] = newPosition;
				break;
			}

		}
		updCaixotesPaletes();

	}

	public Palete getPaleteAtPosition(Point2D position) {
		for (Palete palete : paletes) {
			if (palete.getPosition().equals(position)) {
				return palete;
			}
		}
		return null; // No Palete found at the specified position
	}
	
	
	
	public Caixote getCaixoteAtPosition(Point2D position) {
		for (Caixote caixote : caixotes) {
			if (caixote.getPosition().equals(position)) {
				return caixote;
			}
		}
		return null; // No Palete found at the specified position
	}
	
	public Teleporte getPortalAtPosition(Point2D position) {
		for (Teleporte portal : portais) {
			if (portal.getPosition().equals(position)) {
				return portal;
			}
		}
		return null; // No Palete found at the specified position
	}

	public Martelo findMarteloAtPosition(List<GameElement> elements, Point2D pos) {
		for (GameElement element : elements) {
			if (element instanceof Martelo) {
				Martelo martelo = (Martelo) element;
				if (pos.equals(martelo.getPosition())) {
					return martelo;
				}
			}
		}
		return null;
	}

	

	public void teleportObject(Positionable teleportableObject, Point2D portalEntrada, int key) {
	    // Check if the object is on a teleporter
		
		
	    if (Arrays.asList(portaisPos).contains(portalEntrada)) {
	        for (int i = 0; i < portais.size(); i++) {
	            Object a = portais.get(i);
	            Point2D posSaida = ((Teleporte) a).getTpSaida().getPosition();
	            Direction d = Direction.directionFor(key);
				Point2D posObjAtSaida = posSaida.plus(d.asVector());
	            

	            if (a instanceof Teleporte && ((Teleporte) a).getPosition().equals(portalEntrada)) {
	                if (!((Teleporte) a).isSomethingOnTop(posSaida, this)) {
	                    // Teleport the object to the exit teleporter position
	                	if (teleportableObject instanceof Caixote) {
	                        moveCaixote(teleportableObject.getPosition(),posObjAtSaida);
	                    }
	                    if (teleportableObject instanceof Palete) {
	                        movePalete(teleportableObject.getPosition(),posObjAtSaida);
	                    }
	                    break;
	                }
	            }
	        }
	    }
	}
}
