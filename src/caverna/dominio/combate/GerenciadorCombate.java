// Em GerenciadorCombate.java - Modifique completamente:
package caverna.dominio.combate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import caverna.dominio.entidade.*;
import caverna.dominio.item.*;
import caverna.dominio.mundo.*;

public class GerenciadorCombate {
    private final Mundo mundo;
    private final RegistroMensagens registro;
    private boolean emCombate = false;
    private Criatura alvoCombate;
    private Random random = new Random();
    
    public GerenciadorCombate(Mundo mundo, RegistroMensagens registro) {
        this.mundo = mundo;
        this.registro = registro;
    }
    
    // Método para iniciar combate
    public void iniciarCombate(Criatura inimigo) {
        this.emCombate = true;
        this.alvoCombate = inimigo;
        registro.adicionar("COMBATE INICIADO! Você enfrenta um " + nomeCriatura(inimigo));
        registro.adicionar("Escolha: Atacar ou Fugir");
    }
    
    // Método para processar ação no combate
    public boolean processarAcaoCombate(char acao) {
        if (!emCombate || alvoCombate == null) return false;
        
        Jogador jogador = mundo.getJogador();
        
        switch(Character.toUpperCase(acao)) {
            case 'A': // Atacar
                int danoJogador = jogador.atacar();
                alvoCombate.tomaDano(danoJogador);
                registro.adicionar("Você atacou causando " + danoJogador + " de dano!");
                
                if (!alvoCombate.taVivo()) {
                    registro.adicionar("Você derrotou o " + nomeCriatura(alvoCombate) + "!");
                    mundo.geCriaturas().remove(alvoCombate);
                    mundo.celulaEm(alvoCombate.getPosicao()).setCriatura(null);
                    fimCombate();
                    return true;
                }
                
                // Inimigo contra-ataca
                int danoInimigo = alvoCombate.atacar();
                jogador.tomaDano(danoInimigo);
                registro.adicionar("O " + nomeCriatura(alvoCombate) + " contra-atacou causando " + danoInimigo + " de dano!");
                
                if (!jogador.taVivo()) {
                    registro.adicionar("Você foi derrotado!");
                }
                break;
                
            case 'F': // Fugir
                registro.adicionar("Você tentou fugir do combate!");
                if (Math.random() < 0.5) { // 50% chance de fuga
                    registro.adicionar("Você conseguiu fugir!");
                    fimCombate();
                } else {
                    registro.adicionar("Falha na fuga! O inimigo ataca!");
                    int dano = alvoCombate.atacar();
                    jogador.tomaDano(dano);
                    registro.adicionar("Você tomou " + dano + " de dano!");
                }
                break;
        }
        
        return true;
    }
    
    public boolean estaEmCombate() {
        return emCombate;
    }
    
    public Criatura getAlvoCombate() {
        return alvoCombate;
    }
    
    private void fimCombate() {
        emCombate = false;
        alvoCombate = null;
    }
    
    // Método para movimento normal (fora de combate)
    public boolean moverJogador(int dx, int dy) {
        if (emCombate) {
            registro.adicionar("Você está em combate! Use A para atacar ou F para fugir");
            return false;
        }
        
        Jogador jogador = mundo.getJogador();
        Posicao novaPos = jogador.getPosicao().adicionar(dx, dy);
        Celula celulaAlvo = mundo.celulaEm(novaPos);
        
        if (!celulaAlvo.ETransitavel()) {
            if (celulaAlvo.EBarrado()) {
                registro.adicionar("Caminho bloqueado por pedras");
            } else {
                registro.adicionar("Você bateu em uma parede");
            }
            return false;
        }
        
        // Verifica se tem inimigo
        Criatura criatura = celulaAlvo.getCriatura();
        if (criatura != null && criatura.taVivo() && criatura != jogador) {
            iniciarCombate(criatura);
            return false;
        }
        
        // Move jogador
        mundo.moverCriatura(jogador, novaPos);
        
        // Coleta item
        Item item = celulaAlvo.getItem();
        if (item != null) {
            if (jogador.getInventario().adicionar(item)) {
                registro.adicionar("Você coletou: " + item.getNome());
                celulaAlvo.setItem(null);
            } else {
                registro.adicionar("Inventário cheio! Não pode coletar " + item.getNome());
            }
        }
        
        // Turno dos inimigos (fora de combate)
        turnoInimigos();
        
        return true;
    }
    
    private void turnoInimigos() {
        // Criar uma cópia da lista para evitar problemas de concorrência
        List<Criatura> inimigosParaMover = new ArrayList<>();
        for (Criatura c : mundo.geCriaturas()) {
            if (c != mundo.getJogador() && c.taVivo()) {
                inimigosParaMover.add(c);
            }
        }
        
        for (Criatura inimigo : inimigosParaMover) {
            Posicao posInimigo = inimigo.getPosicao();
            Posicao posJogador = mundo.getJogador().getPosicao();
            
            // Distância Manhattan até o jogador
            int distX = Math.abs(posJogador.x() - posInimigo.x());
            int distY = Math.abs(posJogador.y() - posInimigo.y());
            int distancia = distX + distY;
            
            // Se está adjacente ao jogador, não se move (já está na posição ideal)
            if (distancia <= 1) {
                continue;
            }
            
            // DECISÃO: 80% chance de ir na direção do jogador, 20% aleatório
            int dx = 0;
            int dy = 0;
            
            if (random.nextInt(100) < 80) {
                // Move na direção do jogador
                if (distX > distY) {
                    // Prioriza movimento horizontal
                    dx = Integer.compare(posJogador.x(), posInimigo.x());
                    dy = 0;
                } else {
                    // Prioriza movimento vertical
                    dx = 0;
                    dy = Integer.compare(posJogador.y(), posInimigo.y());
                }
            } else {
                // Movimento aleatório
                int[][] direcoes = {
                    {0, -1},  // Cima
                    {1, 0},   // Direita
                    {0, 1},   // Baixo
                    {-1, 0}   // Esquerda
                };
                
                // Escolhe uma direção aleatória
                int[] dir = direcoes[random.nextInt(4)];
                dx = dir[0];
                dy = dir[1];
            }
            
            Posicao novaPos = new Posicao(posInimigo.x() + dx, posInimigo.y() + dy);
            Celula celulaAlvo = mundo.celulaEm(novaPos);
            
            // Verifica se a célula está livre para mover
            if (celulaAlvo.ETransitavel() && celulaAlvo.getCriatura() == null) {
                // Move o inimigo
                mundo.moverCriatura(inimigo, novaPos);
                
                // Verifica se ficou adjacente ao jogador após mover
                Posicao novaPosInimigo = inimigo.getPosicao();
                int novaDistX = Math.abs(posJogador.x() - novaPosInimigo.x());
                int novaDistY = Math.abs(posJogador.y() - novaPosInimigo.y());
                
                if (novaDistX <= 1 && novaDistY <= 1) {
                    // Não loga toda hora para não poluir o log
                    if (random.nextInt(100) < 30) { // 30% chance de logar
                        registro.adicionar(nomeCriatura(inimigo) + " está próximo!");
                    }
                }
            }
        }
    }
    
    private String nomeCriatura(Criatura c) {
        if (c instanceof Mamute) return "Mamute";
        if (c instanceof Tigre) return "Tigre-dente-de-sabre";
        if (c instanceof Javali) return "Javali";
        if (c instanceof Morcego) return "Morcego";
        return "Criatura";
    }
}