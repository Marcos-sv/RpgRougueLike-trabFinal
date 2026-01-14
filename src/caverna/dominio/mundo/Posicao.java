package caverna.dominio.mundo;

public record Posicao(int x, int y){
    public Posicao adicionar(int dx, int dy){
        return new Posicao(dx+x, dy+y);
    }
}