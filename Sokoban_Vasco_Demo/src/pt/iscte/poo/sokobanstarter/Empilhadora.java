package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

/**
 * Classe que representa a empilhadora no jogo.
 * A empilhadora e controlada pelo jogador e possui metodos para movimentacao e manipulcao da bateria.
 */
public class Empilhadora implements ImageTile, Positionable {

    private Point2D position; // Posição atual da empilhadora no jogo.
    private String imageName; // Nome da imagem que representa o estado atual da empilhadora.
    public boolean moved; // Indica se a empilhadora foi movida.
    private boolean hasMartelo = false; // Indica se a empilhadora possui um martelo.

    private MovementHandler movementHandler; // Objeto para lidar com a movimentacao da empilhadora.
    private BateriaHandler bateriaHandler; // Objeto para lidar com a gestao da bateria da empilhadora.

   
    public Empilhadora(Point2D position) {
        this.position = position;
        this.imageName = "Empilhadora_D";
        this.movementHandler = new MovementHandler();
        this.bateriaHandler = new BateriaHandler();
    }

   
    @Override
    public String getName() {
        return imageName;
    }
    
    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public int getLayer() {
        return 2;
    }

    //Obtem o estado atual do martelo da empilhadora.
    
    public boolean getMarteloState() {
        return hasMartelo;
    }

    //Define o estado do martelo da empilhadora.
     
    public void setMarteloState(boolean hasMartelo) {
        this.hasMartelo = hasMartelo;
    }

    //Alterna o estado de movimento da empilhadora.
     
    public void toggleMoved(boolean option) {
        moved = option;
    }

    //Ativa o estado do martelo quando este é apanhado pela empilhadora.
    
    public void toggleMartelo() {
        setMarteloState(true);
    }

    //Obtem o nivel atual de bateria empilhadora.

    public int getBateria() {
        return bateriaHandler.getBateria();
    }

    //retira bateria da empelhadora
    public void drainBateria() {
        bateriaHandler.drainBateria();
    }

    //repoem o valor da bateria de volta ao maximo
     
    public void resetBateria() {
        bateriaHandler.resetBateria();
    }

    //altera o estado da bateria para "used" de forma a retirar baterias usadas do jogo
    public void useBateria() {
        bateriaHandler.useBateria();
    }

    //Move a empilhadora com base na tecla pressionada e muda a imagem da empilhadora de acordo com a direcao do movimento
   
    public void move(int key, Level level) {
        Point2D nextPos = movementHandler.nextPos(key, position);
        Direction d = Direction.directionFor(key);

        switch (d) {
            case UP:
                imageName = "Empilhadora_U";
                break;
            case DOWN:
                imageName = "Empilhadora_D";
                break;
            case LEFT:
                imageName = "Empilhadora_L";
                break;
            case RIGHT:
                imageName = "Empilhadora_R";
                break;
        }

        movementHandler.empilhadoraMove(key, level, nextPos, this);
    }

 
    @Override
    public void changePos(Point2D pos) {
        position = pos;
    }

}
