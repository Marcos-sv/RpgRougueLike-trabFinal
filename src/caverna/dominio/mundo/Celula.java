package caverna.dominio.mundo;

import caverna.dominio.item.Item;
import caverna.dominio.entidade.Criatura;

public class Celula {
    private String imagemFundo;
    private boolean transitavel;
    private Item item;
    private Criatura criatura;
    private boolean passagemBarrada = false;

    public Celula(String imagemFundo, boolean transitavel){
        this.imagemFundo = imagemFundo;
        this.transitavel = transitavel;
    }

    public String getImagemFundo() {
        return imagemFundo;
    }

    public boolean ETransitavel(){
        return transitavel && criatura == null && !passagemBarrada;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Criatura getCriatura() {
        return criatura;
    }

    public void setCriatura(Criatura criatura) {
        this.criatura = criatura;
    }

    public void setPassagemBarrada(boolean barrado){
        this.passagemBarrada = barrado;
        if(barrado){
            this.imagemFundo = "pedra.png";
            transitavel = false;
        }
    }

    public boolean passagemBarrada(){
        return passagemBarrada;
    }

    public void destrancar(){
        passagemBarrada = false;
        transitavel = true;
        this.imagemFundo = "chao.png";
    }

    public boolean EBarrado(){
        return passagemBarrada;
    }

    public void setImagemFundo(String imagemFundo) {
        this.imagemFundo = imagemFundo;
    }
}
