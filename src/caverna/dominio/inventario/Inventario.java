package caverna.dominio.inventario;

import caverna.dominio.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Item> itens;
    private int espacoMax;

    public Inventario(int espacos){
        this.espacoMax = espacos;
        this.itens = new ArrayList<>();
    }

    public boolean adicionar(Item item){
        if(itens.size() < espacoMax){
            itens.add(item);
            return true;
        }
        return false;
    }

    public void remover(Item item){
        itens.remove(item);
    }

    public Item removerEspecifico(int indice){
        if(indice >= 0 && indice < itens.size()){
            return itens.remove(indice);
        }
        return null;
    }

    public List<Item> getItens(){
        return itens;
    }

    public boolean cheio(){
        return itens.size() >= espacoMax;
    }

    public int getQuantItens(){
        return itens.size();
    }

    public int getEspacoMax(){
        return espacoMax;
    }
}
