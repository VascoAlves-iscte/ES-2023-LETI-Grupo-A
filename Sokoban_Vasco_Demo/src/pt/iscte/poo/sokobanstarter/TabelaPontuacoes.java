package pt.iscte.poo.sokobanstarter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelaPontuacoes {

	private List<Pontuacao> pontuacoes;
	private List<Pontuacao> currentHighScores;

	public TabelaPontuacoes() {
		this.pontuacoes = new ArrayList<>();
	}

	// Método para adicionar uma nova pontuação à tabela
	public void adicionarPontuacao(Pontuacao pontuacao) {
		pontuacoes.add(pontuacao);
		ordenarPontuacoes(); // Ordena as pontuações após adicionar uma nova
	}

	// Método para ordenar as pontuações em ordem decrescente
	private void ordenarPontuacoes() {
		Collections.sort(pontuacoes, Collections.reverseOrder());
	}

	public void updateAndWriteHighScores() {
		readHighScoresFromFile();
		updateAllHighScores();
		writeHighScoresToFile();
	}

	public void writeScoresToFile(String fileName, String nomeJogador) {
		try (PrintWriter writer = new PrintWriter(new FileWriter("scores/" + fileName + ".txt", false))) {
			writer.println("Here are your scores from this session " + nomeJogador + "!");
			for (Pontuacao pontuacao : pontuacoes) {

				writer.println("Level " + pontuacao.getlevelIndex() + ": " + pontuacao.getPontos());
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Handle IOException (e.g., log or display an error message)
		}
	}

	public void readHighScoresFromFile() {
		int maxLevels = 7; // Change this to the maximum number of levels
		List<Pontuacao> scoresList = new ArrayList<>();

		for (int levelIndex = 0; levelIndex < maxLevels; levelIndex++) {
			String levelHeader = "Level " + levelIndex + ":";

			try (BufferedReader reader = new BufferedReader(new FileReader("scores/HighScores.txt"))) {
				String line;
				boolean levelFound = false;

				while ((line = reader.readLine()) != null) {
					if (line.equals(levelHeader)) {
						levelFound = true;
					} else if (levelFound && line.startsWith(":")) {
						// Parse the line and extract player and score
						String[] parts = line.split("->");
						if (parts.length == 2) {
							String playerName = parts[0].substring(1);
							int score = Integer.parseInt(parts[1].trim());

							// Create a new ScoreEntry and add it to the list
							Pontuacao scoreEntry = new Pontuacao(playerName, score, levelIndex);
							scoresList.add(scoreEntry);
						}
					} else if (levelFound && line.isEmpty()) {
						// End of the current level
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				// Handle IOException (e.g., log or display an error message)
			}
		}
		currentHighScores = scoresList;
	}

	public void updateHighScoresFromPontuacoes(int levelIndex) {

		// Get the current top scores for the specified level,
		List<Pontuacao> topScores = currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3);

		// Get the new score from pontuacoes list for the specified level
		Pontuacao newScore = getScoreForLevel(levelIndex);

		// Check if the new score is better than any of the current top scores
		for (int i = 0; i < 3; i++) {
			if (newScore.compareTo(topScores.get(i)) < 0) {
				// Replace the existing top score with the new score
				topScores.set(i, newScore);
				break; // Assuming you only want to replace the first matching score
			}
		}

		// Sort the updated top scores
		Collections.sort(currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3));
	}

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

	public void updateAllHighScores() {
		for (int levelIndex = 0; levelIndex < pontuacoes.size(); levelIndex++) {
			updateHighScoresFromPontuacoes(levelIndex);
		}
	}

	public void writeHighScoresToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("scores/HighScores.txt", false))) {
			writer.println("HighScores:");
			writer.println(); // Add a blank line between

			for (int levelIndex = 0; levelIndex < (currentHighScores.size()) / 3; levelIndex++) {
				writer.println("Level " + levelIndex + ":");
				List<Pontuacao> topScores = currentHighScores.subList(levelIndex * 3, (levelIndex + 1) * 3);

				// Write each score to the file
				for (Pontuacao score : topScores) {
					writer.println(":" + score.getNomeJogador() + "->" + score.getPontos());
				}
				writer.println(); // Add a blank line between levels
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Handle IOException (e.g., log or display an error message)
		}
	}

}
