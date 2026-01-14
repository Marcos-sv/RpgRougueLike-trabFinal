package caverna.dominio.item;

import java.io.Serializable;

import caverna.dominio.entidade.Jogador;
import caverna.dominio.mundo.Mundo;

public abstract class Item implements Serializable{
    protected String nome;
    protected String imagemChao;
    private static final long serialVersionUID = 1L;

    public Item(String nome, String imagemChao){
        this.nome = nome;
        this.imagemChao = imagemChao;
    }

    public abstract void usar(Jogador jogador, Mundo mundo);

    public String getNome() {
        return nome;
    }

    public String getImagemChao() {
        return imagemChao;
    }
}
