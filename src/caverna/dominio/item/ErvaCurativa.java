package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Mundo;

public class ErvaCurativa extends Item implements Consumivel{
    public ErvaCurativa(){
        super("Erva Curativa", "erva.png");
    }

    @Override
    public void consumir(Jogador jogador){
        jogador.curar(3);
    }

    @Override
    public void usar(Jogador jogador, Mundo mundo){
        consumir(jogador);
        jogador.getInventario().remover(this);
        mundo.getRegistro().adicionar("Usou a erva curativa");
    }
}
