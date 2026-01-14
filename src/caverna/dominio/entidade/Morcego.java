package caverna.dominio.entidade;

import caverna.dominio.mundo.Posicao;

public class Morcego extends Criatura {
    public Morcego(Posicao posicao){
        super(posicao, 5, 1, 2, "morcego.png");
    }
}