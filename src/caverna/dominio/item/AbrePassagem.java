package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Celula;
import caverna.dominio.mundo.Mundo;
import caverna.dominio.mundo.Posicao;

public class AbrePassagem extends Item implements Interativo{
    public AbrePassagem(){
        super("Picareta Improvisada", "picareta.png");
    }

    @Override
    public boolean usarEmPorta(Mundo mundo, Posicao posPorta, Jogador jogador){
        Celula celula = mundo.celulaEm(posPorta);
        if(celula.passagemBarrada()){
            celula.destrancar();
            jogador.getInventario().remover(this);
            mundo.getRegistro().adicionar("Usou a picareta improvisada pra abrir passagem");
            return true;
        }
        return false;
    }

    @Override
    public void usar(Jogador jogador, Mundo mundo){
        mundo.getRegistro().adicionar("Use a picareta improvisada pra abrir passagem nas pedras soltas");
    }
}
