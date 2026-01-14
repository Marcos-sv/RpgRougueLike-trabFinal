package caverna.dominio.entidade;

import caverna.dominio.mundo.Posicao;

public class Tigre extends Criatura{
    public Tigre(Posicao posicao){
        super(posicao, 12, 2, 6, "tigre.png");
    }
}