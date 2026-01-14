package caverna.dominio.entidade;

import java.io.Serializable;

import caverna.dominio.mundo.Posicao;

public abstract class Criatura implements Serializable{
    private Posicao posicao;
    private int vida;
    private int vidaMaxima;
    private int danoMinimo;
    private int danoMaximo;
    private String sprite; // null para o jogador, nome do arquivo para inimigos
    private boolean vivo = true;
    private static final long serialVersionUID = 1L;

    public Criatura(Posicao pos, int vidaMaxima, int danoMinimo, int danoMaximo, String imagemSprite){
        this.posicao = pos;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.danoMinimo = danoMinimo;
        this.danoMaximo = danoMaximo;
        this.sprite = imagemSprite;
    }

    public int atacar(){
        return danoMinimo + (int)(Math.random() * (danoMaximo-danoMinimo+1));
    }

    public void tomaDano(int dano){
        vida -= dano;
        if(vida <= 0){
            vida = 0;
            vivo = false;
        }
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public String getSprite() {
        return sprite;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public boolean taVivo(){
        return vivo;
    }
}
