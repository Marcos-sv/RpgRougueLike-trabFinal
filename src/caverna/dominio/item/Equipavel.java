package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;

public interface Equipavel {
    void equipar(Jogador jogador);
    void desequipar(Jogador jogador);
}
