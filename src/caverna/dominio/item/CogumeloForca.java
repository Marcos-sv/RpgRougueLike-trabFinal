package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Mundo;

public class CogumeloForca extends Item implements Consumivel{
    public CogumeloForca(){
        super("Cogumelo da caverna", "cogumelo.png");
    }

    @Override
    public void consumir(Jogador jogador){
        jogador.buffForca();
    }

    @Override
    public void usar(Jogador jogador, Mundo mundo){
        consumir(jogador);
        jogador.getInventario().remover(this);
        mundo.getRegistro().adicionar("Usou cogumelo da caverna, voce se sente mais forte. +2 de dano por 3 ataques");
    }
}
