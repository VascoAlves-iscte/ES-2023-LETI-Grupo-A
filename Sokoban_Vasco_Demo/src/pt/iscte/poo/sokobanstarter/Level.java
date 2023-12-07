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

//classe que representa um nivel no jogo
public class Level {

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

	// contadores para garantir o correcto posicinamento dos objetos nos arrays das
	// posicoes
	int caixotesCount = 0;
	int paredesCount = 0;
	int elementsCount = 0;
	int alvosCount = 0;
	int paletesCount = 0;
	int portaisCount = 0;

	// posicao usada nos updates de posicao para guardar a posicao antiga do objeto
	// a ser movido
	private Point2D oldPos;

	// ficheiro com o nivel a ser usado
	private File levelFile;

	// constructor para a classe Level, recebe o nome do ficheiro a ser carregado
	public Level(String levelFileName) {
		this.levelFile = new File(levelFileName);
	}

	// verifica se o ficheiro do nivel existe
	public boolean fileExists() {
		return levelFile.exists();
	}

	// getters e setters para os varios elementos da classe (listas, arrays de
	// posicoes etc)

	public File getFileName() {
		return levelFile;
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

	// "limpa" o nivel, esvaziando todas as listas, arrays, contadores de forma a
	// reniciar o nivel
	public void clearLevel() {
		clearArrays();
		clearLists();
		resetCounts();
		empelhadoraPos = null;
	}

	// reset dos arrays de posicao
	private void clearArrays() {
		Arrays.fill(paredesPos, null);
		Arrays.fill(caixotesPos, null);
		Arrays.fill(elementsPos, null);
		Arrays.fill(alvosPos, null);
		Arrays.fill(portaisPos, null);
		Arrays.fill(paletesPos, null);
	}

	// reset das listas de objetos
	private void clearLists() {
		caixotes.clear();
		elements.clear();
		portais.clear();
		alvos.clear();
		paletes.clear();
	}

	// reset dos contadores
	private void resetCounts() {
		caixotesCount = 0;
		paredesCount = 0;
		elementsCount = 0;
		alvosCount = 0;
		paletesCount = 0;
		portaisCount = 0;
	}

	// prencher o fundo do mapa com "chao"
	public void addFloor() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int x = 0; x != GameEngine.GRID_WIDTH; x++) {
			for (int y = 0; y != GameEngine.GRID_HEIGHT; y++)
				tileList.add(new Chao(new Point2D(x, y)));
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	// adiciona as paredes, inimigos, items atc ao mapa de acordo com o ficheiro
	// lido
	public void readFile() {

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

	// conecta os pares de portais dando a posicao de cada um como a saida do outro
	public void setSaidasPortais() {
		if (portais.size() == 2) {
			Teleporte portalEntrada = portais.get(0);
			Teleporte portalSaida = portais.get(1);

			portalEntrada.setTpSaida(portalSaida);
			portalSaida.setTpSaida(portalEntrada);
		}
	}

	// verifica se todos os alvos estao preenchidos com caixotes
	public boolean allAlvosFilled() {
		for (Alvo alvo : alvos) {
			if (!alvo.hasBox(caixotesPos)) {
				return false;
			}
		}
		return true;
	}

	// Verifica se algum caixote esta na posicao de um boraco e remove o mesmo se
	// verificar
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
		caixotes.removeAll(caixotesToRemove);
		updCaixotesPaletes();
	}

	// verifica se alguma palete esta na posicao de um buraco.Se se verificar este
	// buraco passa a estar "fixed" por um palete que o torna agora transponivel
	public void fixBuracos() {
		List<ImageTile> tileList = new ArrayList<>();

		Iterator<GameElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			GameElement element = iterator.next();
			if (element instanceof Buraco) {
				Buraco buraco = (Buraco) element;
				if (buraco.isFilled(paletesPos)) {
					iterator.remove(); // remove o Buraco da lista de elementos
					tileList.add(new Palete(buraco.getPosition()));
					// muda o campo "placed" da palete que ocupou o buraco
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

	// verifica se existem caixotes suficientes para todos os alvos
	public boolean notEnoughCaixotes() {
		return caixotes.size() < alvos.size();
	}

	// metodos que atualizam as imagens dos objetos de acordo com o estado atual do
	// jogo
	public void updPortais() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int j = 0; j < portais.size(); j++) {
			tileList.add(portais.get(j));
		}
		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public void updCaixotesPaletes() {
		List<ImageTile> tileList = new ArrayList<>();
		// remove imagens de caixotes anteriormente colocados
		for (int i = 0; i < caixotes.size(); i++) {
			Caixote caixote = caixotes.get(i);
			tileList.add(new Chao(caixote.getPosition()));
		}

		// remove imagens de paletes anteriormente colocadas
		for (int i = 0; i < paletes.size(); i++) {
			Palete palete = paletes.get(i);
			if (palete.isPlaced()) {
				tileList.add(new Buraco(palete.getPosition())); // por buraco com palete em cima ("pacthed")
				tileList.add(new Palete(palete.getPosition()));
			} else {
				tileList.add(new Chao(palete.getPosition()));
			}
		}

		tileList.add(new Chao(oldPos));

		// adiciona as novas imagens dos caixotes, alvos, paletes e outros elementos
		for (Alvo alvo : alvos) {
			tileList.add(alvo);
		}

		for (Palete palete : paletes) {
			tileList.add(palete);
		}

		for (Caixote caixote : caixotes) {
			tileList.add(caixote);
		}

		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	public void updElements() {
		List<ImageTile> tileList = new ArrayList<>();

		// remove imagens antigas dos elementos que foram "usados" e atualiaza o array
		// das posicoes
		Iterator<GameElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			GameElement element = iterator.next();

			if (element instanceof Bateria && ((Bateria) element).isUsed()) {
				iterator.remove(); // Remove baterias usadas da lista
				updateElementArray(element.getPosition());
				tileList.add(new Chao(element.getPosition()));
			}

			if (element instanceof Martelo && ((Martelo) element).isUsed()) {
				iterator.remove(); // Remove martelo se tiver sido apanhado
				updateElementArray(element.getPosition());
				tileList.add(new Chao(element.getPosition()));
			}

			if (element instanceof ParedeRachada && ((ParedeRachada) element).isUsed()) {
				iterator.remove(); // Remove paredes rachadas que tenham sido partidas pela empilhadora
				updateElementArray(element.getPosition());
				tileList.add(new Chao(element.getPosition()));
			}
		}

		// adiciona todos os elemntos do jogo que se manteem
		for (GameElement element : elements) {
			tileList.add(element);

		}

		GameEngine.getInstance().getGUI().addImages(tileList);
	}

	// tira uma posicao dada do array de posicoes dos elementos
	private void updateElementArray(Point2D position) {
		for (int i = 0; i < elementsPos.length; i++) {
			if (position.equals(elementsPos[i])) {
				elementsPos[i] = null;
				break;
			}
		}
	}

	// atualiza o array de posicoes dos caixotes com as novas posicoes
	public void moveCaixote(Point2D currentPosition, Point2D newPosition) {
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

	// atualiza o array de posicoes das paletes com as novas posicoes
	public void movePalete(Point2D currentPosition, Point2D newPosition) {
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

	// retorna o objecto palete numa dada posicao
	public Palete getPaleteAtPosition(Point2D position) {
		for (Palete palete : paletes) {
			if (palete.getPosition().equals(position)) {
				return palete;
			}
		}
		return null;
	}

	// retorna o objecto caixote numa dada posicao
	public Caixote getCaixoteAtPosition(Point2D position) {
		for (Caixote caixote : caixotes) {
			if (caixote.getPosition().equals(position)) {
				return caixote;
			}
		}
		return null;
	}

	// retorna o objecto Teleporte numa dada posicao
	public Teleporte getPortalAtPosition(Point2D position) {
		for (Teleporte portal : portais) {
			if (portal.getPosition().equals(position)) {
				return portal;
			}
		}
		return null;
	}

	// retorna o objecto martelo numa dada posicao
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

	// Muda a posicao de um objeto "teleportavel" (caixotes e paletes) de acordo com
	// a direcao com que entra no portal de entrada e as coordenadas do portal de saida
	public void teleportObject(Positionable teleportableObject, Point2D portalEntrada, int key) {
		if (Arrays.asList(portaisPos).contains(portalEntrada)) {
			for (int i = 0; i < portais.size(); i++) {
				Object a = portais.get(i);
				Point2D posSaida = ((Teleporte) a).getTpSaida().getPosition();
				Direction d = Direction.directionFor(key);
				Point2D posObjAtSaida = posSaida.plus(d.asVector());

				if (a instanceof Teleporte && ((Teleporte) a).getPosition().equals(portalEntrada)) {
					if (!((Teleporte) a).isSomethingOnTop(posSaida, this)) {
						
						if (teleportableObject instanceof Caixote) {
							moveCaixote(teleportableObject.getPosition(), posObjAtSaida);
						}
						if (teleportableObject instanceof Palete) {
							movePalete(teleportableObject.getPosition(), posObjAtSaida);
						}
						break;
					}
				}
			}
		}
	}
}
