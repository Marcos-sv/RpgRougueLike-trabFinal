package caverna.dominio.item;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Mundo;

public class Arma extends Item implements Equipavel{
    private int danoMin;
    private int danoMax;
    private String imagemMao;

    public Arma(String nome, int danoMin, int danoMax, String imagemChao, String imagemMao){
        super(nome, imagemChao);
        this.danoMin = danoMin;
        this.danoMax = danoMax;
        this.imagemMao = imagemMao;
    }

    public int calcularDano(){
        return danoMin + (int)(Math.random() * (danoMax - danoMin + 1));
    }

    @Override
    public void equipar(Jogador jogador){
        jogador.equiparArma(this);
    }

    @Override
    public void desequipar(Jogador jogador){}

    @Override
    public void usar(Jogador jogador, Mundo mundo){
        equipar(jogador);
        mundo.getRegistro().adicionar("Equipou "+nome);
    }

    public String getImagemMao() {
        return imagemMao;
    }
}
