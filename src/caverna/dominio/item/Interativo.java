package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Mundo;
import caverna.dominio.mundo.Posicao;

public interface Interativo {
    boolean usarEmPorta(Mundo mundo, Posicao posPorta, Jogador jogador);
}
