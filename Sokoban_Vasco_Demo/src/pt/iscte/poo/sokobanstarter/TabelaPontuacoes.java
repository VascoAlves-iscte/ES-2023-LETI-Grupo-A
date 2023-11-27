package pt.iscte.poo.sokobanstarter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabelaPontuacoes {

	private List<Pontuacao> pontuacoes;

	public TabelaPontuacoes() {
		this.pontuacoes = new ArrayList<>();
	}

	// Método para adicionar uma nova pontuação à tabela
	public void adicionarPontuacao(Pontuacao pontuacao) {
		pontuacoes.add(pontuacao);
		ordenarPontuacoes(); // Ordena as pontuações após adicionar uma nova
	}

	// Método para obter o Top-3 de pontuações
	public List<Pontuacao> obterTopPontuacoes(int quantidade) {
		int tamanho = Math.min(quantidade, pontuacoes.size());
		return pontuacoes.subList(0, tamanho);
	}

	// Método para ordenar as pontuações em ordem decrescente
	private void ordenarPontuacoes() {
		Collections.sort(pontuacoes, Collections.reverseOrder());
	}
	
	public void writeScoresToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("scores/" + fileName))) {
            for (Pontuacao pontuacao : pontuacoes) {
                writer.println(pontuacao.getNomeJogador() + "," + pontuacao.getlevelIndex() + "," + pontuacao.getPontos());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException (e.g., log or display an error message)
        }
    }
}
