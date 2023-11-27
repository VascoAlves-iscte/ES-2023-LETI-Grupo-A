package pt.iscte.poo.sokobanstarter;

public class Pontuacao implements Comparable<Pontuacao> {
	private String nomeJogador;
	private int pontos;
	private int levelIndex;

	public Pontuacao(String nomeJogador, int pontos, int levelIndex) {
		this.nomeJogador = nomeJogador;
		this.pontos = pontos;
		this.levelIndex = levelIndex;
	}

	public String getNomeJogador() {
		return nomeJogador;
	}

	public int getPontos() {
		return pontos;
	}

	public int getlevelIndex() {
		return levelIndex;
	}

	// Método compareTo para ordenar as pontuações
	@Override
	public int compareTo(Pontuacao outraPontuacao) {
		return Integer.compare(this.pontos, outraPontuacao.pontos);
	}
}
