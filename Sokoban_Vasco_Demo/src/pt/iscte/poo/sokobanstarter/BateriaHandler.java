package pt.iscte.poo.sokobanstarter;

public class BateriaHandler {
	
	public static final int MAX_BATERIA = 100;

    private int bateria;

    public BateriaHandler() {
        this.bateria = MAX_BATERIA;
    }

    public int getBateria() {
        return bateria;
    }

    public void drainBateria() {
        bateria--;
    }

    public void resetBateria() {
        bateria = MAX_BATERIA;
    }

    public void useBateria() {
        bateria = Math.min(bateria + 50, MAX_BATERIA);
    }
}
