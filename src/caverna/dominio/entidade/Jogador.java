package caverna.dominio.entidade;

import caverna.dominio.inventario.Inventario;
import caverna.dominio.item.Arma;
import caverna.dominio.mundo.Posicao;

public class Jogador extends Criatura{
    private Inventario inventario;
    private Arma armaEquipada;
    private int turnosBuffForca = 0; // para o cogumelo

    public Jogador(Posicao posicao){
        super(posicao, 15, 0, 0, null); // dano inicial 0
        this.inventario = new Inventario(10); // limite de 10 espaços
        // Começa com graveto equipado
        Arma graveto = new Arma("Graveto", 1, 3, "graveto.png", "graveto.png");
        equiparArma(graveto);
        inventario.adicionar(graveto);
    }

    @Override
    public int atacar() {
        int danoBase = armaEquipada.calcularDano();
        if (turnosBuffForca > 0){
            danoBase += 2;
            turnosBuffForca--;
        }
        return danoBase;
    }

    public void curar(int quantidade){
        int novaVida = Math.min(getVida() + quantidade, getVidaMaxima());
        tomaDano(getVida() - novaVida);
    }

    public void buffForca() {
        turnosBuffForca = 3;
    }

    public void equiparArma(Arma arma){
        this.armaEquipada = arma;
    }

    public Inventario getInventario(){
        return inventario;
    }
    public Arma getArmaEquipada(){
        return armaEquipada;
    }
}