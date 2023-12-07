package pt.iscte.poo.sokobanstarter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// classe que guarda uma lista dos scores dos niveis completados na sessao atual bem como uma lista de highscores
public class TabelaPontuacoes {

	private List<Pontuacao> pontuacoes;
	private List<Pontuacao> currentHighScores;
	private String highScoresFilePath = "scores/HighScores.txt";
	private String scoresFilePath = "scores/scores.txt";

	public TabelaPontuacoes() {
		this.pontuacoes = new ArrayList<>();
	}

	// Metodo para adicionar uma nova pontuação a tabela
	public void adicionarPontuacao(Pontuacao pontuacao) {
		pontuacoes.add(pontuacao);
		ordenarPontuacoes(); // Ordena as pontuações apos adicionar uma nova ser adicionada
	}

	// Metodo para ordenar as pontuacoes em ordem decrescente
	private void ordenarPontuacoes() {
		Collections.sort(pontuacoes, Collections.reverseOrder());
	}

	// Atualiza e escreve as pontuações mais altas no ficheiro .txt dos HighScores.
	public void updateAndWriteHighScores() {
		readHighScoresFromFile();
		updateAllHighScores();
		writeHighScoresToFile();
	}

	// Escreve as pontuacoes da sessao atual para um ficheiro txt
	public void writeScoresToFile(String nomeJogador) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(scoresFilePath, false))) {
			writer.println("Here are your scores from this session " + nomeJogador + "!");
			for (Pontuacao pontuacao : pontuacoes) {

				writer.println("Level " + pontuacao.getlevelIndex() + ": " + pontuacao.getPontos());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Scores file does not exist.");
		}
	}

	// le as pontuacoes do ficheiro highscores, com uma estrutura pre defenida, por nivel e guarda-as numa lista de
	// pontuacoes "currentHighScores"
	public void readHighScoresFromFile() {
		int maxLevels = 7;
		List<Pontuacao> scoresList = new ArrayList<>();

		for (int levelIndex = 0; levelIndex < maxLevels; levelIndex++) {
			String levelHeader = "Level " + levelIndex + ":";

			try (BufferedReader reader = new BufferedReader(new FileReader(highScoresFilePath))) {
				String line;
				boolean levelFound = false;

				while ((line = reader.readLine()) != null) {
					if (line.equals(levelHeader)) {
						levelFound = true;
					} else if (levelFound && line.startsWith(":")) {

						String[] parts = line.split("->");
						if (parts.length == 2) {
							String playerName = parts[0].substring(1);
							int score = Integer.parseInt(parts[1].trim());

							Pontuacao scoreEntry = new Pontuacao(playerName, score, levelIndex);
							scoresList.add(scoreEntry);
						}
					} else if (levelFound && line.isEmpty()) {

						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("HighScores file path does not exist.");
			}
		}
		currentHighScores = scoresList;
	}

	// compara a pontuacoe da sessao atual com as pontuacoes da lista
	// anteriormente gravada para um dado nível e verifica se deve ser posicionada na lista de highscores
	public void updateHighScoresFromPontuacoes(int levelIndex) {

		// obtem os melhores resultados para cada nivel
		List<Pontuacao> topScores = currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3);

		// obtem a pontuacao da sessao atual para o nivel em questao
		Pontuacao newScore = getScoreForLevel(levelIndex);

		//verifica se é melhor que as pontuacoes atuais da lista de highscores
		for (int i = 0; i < 3; i++) {
			if (newScore.compareTo(topScores.get(i)) < 0) {
				//troca a pontuacao nova pontuacao pela a antiga
				topScores.set(i, newScore);
				break; 
			}
		}

		//organiza os as novas pontuacoes 
		Collections.sort(currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3));
	}

	
	//obtem a pontuacao da sessao atual para um nivel em especifico
	private Pontuacao getScoreForLevel(int levelIndex) {
		// Get all scores for the specified level
		Pontuacao levelScore = null;
		for (Pontuacao pontuacao : pontuacoes) {
			if (pontuacao.getlevelIndex() == levelIndex) {
				levelScore = pontuacao;
			}
		}
		return levelScore;
	}

	//atualiza a lista de highscores para todos os niveis que foram jogados
	public void updateAllHighScores() {
		for (int levelIndex = 0; levelIndex < pontuacoes.size(); levelIndex++) {
			updateHighScoresFromPontuacoes(levelIndex);
		}
	}

	
	//escreve todos os highscores para um ficheiro txt de acordo com uma estrutura pre defenida
	public void writeHighScoresToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(highScoresFilePath, false))) {
			writer.println("HighScores:");
			writer.println();

			//adiciona as 3 melhores pontuacoes por nivel
			for (int levelIndex = 0; levelIndex < (currentHighScores.size()) / 3; levelIndex++) {
				writer.println("Level " + levelIndex + ":");
				List<Pontuacao> topScores = currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3);

				for (Pontuacao score : topScores) {
					writer.println(":" + score.getNomeJogador() + "->" + score.getPontos());
				}
				writer.println(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("HighScores file path does not exist.");
		}
	}

}
