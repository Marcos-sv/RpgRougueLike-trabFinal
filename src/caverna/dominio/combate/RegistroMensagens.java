package caverna.dominio.combate;

import java.util.LinkedList;
import java.util.List;

public class RegistroMensagens {
    private final LinkedList<String> mensagens = new LinkedList<>();
    private static final int maxMensagens = 10;

    public void adicionar(String mensagem) {
        mensagens.addLast(mensagem);
        if (mensagens.size() > maxMensagens) {
            mensagens.removeFirst();
        }
    }

    public List<String> getMensagensRecentes() {
        return new LinkedList<>(mensagens);
    }

    public void limpar() {
        mensagens.clear();
    }

    public boolean estaVazio() {
        return mensagens.isEmpty();
    }
}
