package caverna.dominio.mundo;

import caverna.dominio.entidade.*;
import caverna.dominio.item.*;
import caverna.dominio.combate.GerenciadorCombate;
import caverna.dominio.combate.RegistroMensagens;

import java.io.Serializable;
import java.util.*;
//import java.util.concurrent.StructuredTaskScope.Joiner;

public class Mundo implements Serializable{
    public static final int largura = 50;
    public static final int altura = 30;
    private Celula[][] celulas;
    private List<Criatura> criaturas = new ArrayList<>();
    private Jogador jogador;
    private Mamute mamute;
    private RegistroMensagens registro = new RegistroMensagens();
    private Random rand = new Random();
    private GerenciadorCombate gerenciadorCombate;
    private static final long serialVersionUID = 1L;

    public Mundo(){
        celulas = new Celula[altura][largura];
        gerarMapa();
        posicionarSeres();
        gerenciadorCombate = new GerenciadorCombate(this, registro);
    }

    private void posicionarSeres(){
        // Jogador
        Posicao posJogador = encontrarChaoAleatorio();
        jogador = new Jogador(posJogador);
        celulaEm(posJogador).setCriatura(jogador);
        criaturas.add(jogador);
        
        // Picareta
        Posicao posPicareta = encontrarChaoAleatorio();
        celulaEm(posPicareta).setItem(new AbrePassagem());

        // Itens
        for (int i = 0; i < 12; i++){ // 13 itens no total
            Posicao pos = encontrarChaoAleatorio();
            if (celulaEm(pos).getItem() == null && celulaEm(pos).getCriatura() == null) {
                Item item = switch(rand.nextInt(4)) {
                    case 0 -> new Arma("Clava", 4, 6, "clava.png", "clava.png");
                    case 1 -> new Arma("Lança", 8, 11, "lanca.png", "lanca.png");
                    case 2 -> new ErvaCurativa();
                    default -> new CogumeloForca();
                };
                celulaEm(pos).setItem(item);
            }
        }
        
        // Inimigos (MAIS inimigos)
        // Morcegos
        for (int i = 0; i < 8; i++) { // 8 morcegos
            Posicao pos = encontrarChaoAleatorio();
            if (celulaEm(pos).getCriatura() == null) {
                Morcego morcego = new Morcego(pos);
                celulaEm(pos).setCriatura(morcego);
                criaturas.add(morcego);
            }
        }
        
        // Javalis
        for (int i = 0; i < 6; i++) { // 6 javalis
            Posicao pos = encontrarChaoAleatorio();
            if (celulaEm(pos).getCriatura() == null) {
                Javali javali = new Javali(pos);
                celulaEm(pos).setCriatura(javali);
                criaturas.add(javali);
            }
        }
        
        // Tigres
        for (int i = 0; i < 4; i++) { // 4 tigres
            Posicao pos = encontrarChaoAleatorio();
            if (celulaEm(pos).getCriatura() == null) {
                Tigre tigre = new Tigre(pos);
                celulaEm(pos).setCriatura(tigre);
                criaturas.add(tigre);
            }
        }
        
        // Mamute só aparece depois
        mamute = null;
    }

    public Mamute getMamute(){
        return mamute;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public RegistroMensagens getRegistro() {
        return registro;
    }

    public List<Criatura> geCriaturas(){
        return criaturas;
    }

    public Celula celulaEm(Posicao p){
        if(p.x()<0 || p.x() >= largura || p.y()<0 || p.y() >= altura){
            return new Celula("parede.png", false);
        }
        return celulas[p.y()][p.x()];
    }

    public void moverCriatura(Criatura c, Posicao pos){
        celulaEm(c.getPosicao()).setCriatura(null);
        c.setPosicao(pos);
        celulaEm(pos).setCriatura(c);
    }

    private void gerarMapa() {
        System.out.println("Gerando mapa labirinto...");
        celulas = new Celula[altura][largura];
        
        // 1. Preenche tudo com paredes
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                celulas[y][x] = new Celula("parede.png", false);
            }
        }
        
        // 2. Cria um caminho simples (não é um labirinto complexo, mas funciona)
        criarCaminhoBasico();
        
        // 3. Garante bordas sólidas
        for (int x = 0; x < largura; x++) {
            celulas[0][x] = new Celula("parede.png", false);
            celulas[altura-1][x] = new Celula("parede.png", false);
        }
        for (int y = 0; y < altura; y++) {
            celulas[y][0] = new Celula("parede.png", false);
            celulas[y][largura-1] = new Celula("parede.png", false);
        }
        
        // 4. Adiciona algumas pedras aleatórias
        Random rand = new Random();
        int numPedras = 15;
        for (int i = 0; i < numPedras; i++) {
            int x = rand.nextInt(largura - 2) + 1;
            int y = rand.nextInt(altura - 2) + 1;
            
            if (celulas[y][x].ETransitavel()) {
                celulas[y][x].setPassagemBarrada(true);
                System.out.println("Pedra em (" + x + "," + y + ")");
            }
        }
        
        System.out.println("Mapa gerado com sucesso!");
    }

    private void criarCaminhoBasico() {
        // Cria algumas salas conectadas
        criarSala(5, 5, 7, 7); // Sala inicial
        criarSala(20, 5, 7, 7); // Sala direita
        criarSala(5, 15, 7, 7); // Sala abaixo
        criarSala(20, 15, 10, 10); // Sala grande
        
        // Conecta as salas com corredores
        criarCorredorHorizontal(12, 8, 8); // Conecta sala 1 e 2
        criarCorredorVertical(8, 12, 7);   // Conecta sala 1 e 3
        criarCorredorVertical(25, 12, 7);  // Conecta sala 2 e 4
        criarCorredorHorizontal(12, 20, 13); // Conecta sala 3 e 4
        
        // Garante que não tenha paredes solitárias
        suavizarParedes();
    }

    private void criarSala(int x, int y, int larguraSala, int alturaSala) {
        System.out.println("Criando sala em (" + x + "," + y + ") tamanho " + larguraSala + "x" + alturaSala);
        
        for (int dy = 0; dy < alturaSala; dy++) {
            for (int dx = 0; dx < larguraSala; dx++) {
                int px = x + dx;
                int py = y + dy;
                
                if (px > 0 && px < largura-1 && py > 0 && py < altura-1) {
                    celulas[py][px] = new Celula("chao.png", true);
                }
            }
        }
    }

    private void criarCorredorHorizontal(int x, int y, int comprimento) {
        for (int i = 0; i < comprimento; i++) {
            int px = x + i;
            if (px > 0 && px < largura-1 && y > 0 && y < altura-1) {
                // Corredor de 3 células de altura
                for (int dy = -1; dy <= 1; dy++) {
                    int py = y + dy;
                    if (py > 0 && py < altura-1) {
                        celulas[py][px] = new Celula("chao.png", true);
                    }
                }
            }
        }
    }

    private void criarCorredorVertical(int x, int y, int comprimento) {
        for (int i = 0; i < comprimento; i++) {
            int py = y + i;
            if (py > 0 && py < altura-1 && x > 0 && x < largura-1) {
                // Corredor de 3 células de largura
                for (int dx = -1; dx <= 1; dx++) {
                    int px = x + dx;
                    if (px > 0 && px < largura-1) {
                        celulas[py][px] = new Celula("chao.png", true);
                    }
                }
            }
        }
    }

    private void suavizarParedes() {
        // Remove paredes solitárias no meio do caminho
        for (int y = 1; y < altura-1; y++) {
            for (int x = 1; x < largura-1; x++) {
                if (!celulas[y][x].ETransitavel()) {
                    int vizinhosTransitaveis = 0;
                    
                    // Conta vizinhos transitáveis
                    if (celulas[y-1][x].ETransitavel()) vizinhosTransitaveis++;
                    if (celulas[y+1][x].ETransitavel()) vizinhosTransitaveis++;
                    if (celulas[y][x-1].ETransitavel()) vizinhosTransitaveis++;
                    if (celulas[y][x+1].ETransitavel()) vizinhosTransitaveis++;
                    
                    // Se tiver 3 ou mais vizinhos transitáveis, vira chão
                    if (vizinhosTransitaveis >= 3) {
                        celulas[y][x] = new Celula("chao.png", true);
                    }
                }
            }
        }
    }

    private Posicao encontrarChaoAleatorio() {
        Random rand = new Random();
        int tentativas = 0;
        
        while (tentativas < 100) { // Limite de tentativas para evitar loop infinito
            int x = rand.nextInt(largura - 2) + 1; // Não nas bordas
            int y = rand.nextInt(altura - 2) + 1;
            Posicao pos = new Posicao(x, y);
            
            Celula celula = celulaEm(pos);
            if (celula.ETransitavel() && celula.getCriatura() == null && celula.getItem() == null) {
                return pos;
            }
            tentativas++;
        }
        
        // Fallback: qualquer posição livre
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                Posicao pos = new Posicao(x, y);
                Celula celula = celulaEm(pos);
                if (celula.ETransitavel() && celula.getCriatura() == null && celula.getItem() == null) {
                    return pos;
                }
            }
        }
        
        return new Posicao(1, 1); // Último recurso
    }

    public GerenciadorCombate getGerenciadorCombate() {
        return gerenciadorCombate;
    }

    public boolean executarTurno(int dx, int dy) {
        System.out.println("=== EXECUTANDO TURNO ===");
        System.out.println("Movimento: dx=" + dx + ", dy=" + dy);
        
        Jogador jogador = getJogador();
        if (!jogador.taVivo()) {
            registro.adicionar("Você está morto! Pressione R para recomeçar.");
            return false;
        }
        
        // Calcula nova posição
        Posicao posAtual = jogador.getPosicao();
        Posicao novaPos = new Posicao(posAtual.x() + dx, posAtual.y() + dy);
        
        System.out.println("Pos atual: " + posAtual + " -> Nova pos: " + novaPos);
        
        // Verifica se a nova posição é válida
        Celula celulaAlvo = celulaEm(novaPos);
        
        if (!celulaAlvo.ETransitavel()) {
            System.out.println("Célula NÃO transitável!");
            if (celulaAlvo.EBarrado()) {
                registro.adicionar("Caminho bloqueado por pedras!");
            } else {
                registro.adicionar("Você bateu em uma parede!");
            }
            return true; // Turno executado (mesmo sem mover)
        }
        
        // Verifica se tem inimigo na célula alvo
        Criatura criatura = celulaAlvo.getCriatura();
        if (criatura != null && criatura.taVivo() && criatura != jogador) {
            System.out.println("ENCONTROU INIMIGO! Tipo: " + criatura.getClass().getSimpleName());
            
            // ATAQUE DO JOGADOR
            int danoJogador = jogador.atacar();
            criatura.tomaDano(danoJogador);
            registro.adicionar("Você atacou " + getNomeCriatura(criatura) + " causando " + danoJogador + " de dano!");
            
            if (!criatura.taVivo()) {
                // Inimigo morreu
                registro.adicionar("Você derrotou " + getNomeCriatura(criatura) + "!");
                celulaAlvo.setCriatura(null);
                geCriaturas().remove(criatura);
                
                // Verifica se precisa spawnar mamute
                verificaSpawnMamute();
            } else {
                // Inimigo contra-ataca
                int danoInimigo = criatura.atacar();
                jogador.tomaDano(danoInimigo);
                registro.adicionar(getNomeCriatura(criatura) + " contra-atacou causando " + danoInimigo + " de dano!");
                
                if (!jogador.taVivo()) {
                    registro.adicionar("Você foi derrotado!");
                    return false;
                }
            }
            
            return true;
        }
        
        // MOVE O JOGADOR (se não tem inimigo)
        System.out.println("Movendo jogador para nova posição...");
        moverCriatura(jogador, novaPos);
        
        // Coleta item se houver
        Item item = celulaAlvo.getItem();
        if (item != null) {
            if (jogador.getInventario().adicionar(item)) {
                registro.adicionar("Você coletou: " + item.getNome());
                celulaAlvo.setItem(null);
            } else {
                registro.adicionar("Inventário cheio! Não pode coletar " + item.getNome());
            }
        }
        
        // NÃO CHAMA turnoInimigo() - INIMIGOS NÃO SE MOVEM
        System.out.println("Turno concluído. Inimigos permanecem estáticos.");
        
        return true;
    }

    private String getNomeCriatura(Criatura c) {
        if (c instanceof Mamute) return "Mamute";
        if (c instanceof Tigre) return "Tigre";
        if (c instanceof Javali) return "Javali";
        if (c instanceof Morcego) return "Morcego";
        return "Criatura";
    }

    public void verificaSpawnMamute(){
        System.out.println("=== VERIFICANDO SPAWN MAMUTE ===");
        
        // Verifica se já tem mamute
        if (mamute != null) {
            System.out.println("Mamute já existe!");
            return;
        }
        
        // Conta inimigos comuns vivos (exclui jogador)
        int inimigosVivos = 0;
        for (Criatura c : criaturas) {
            if (c != jogador && c.taVivo()) {
                inimigosVivos++;
                System.out.println("Inimigo vivo: " + c.getClass().getSimpleName());
            }
        }
        
        System.out.println("Inimigos vivos: " + inimigosVivos);
        
        // Se NÃO tem mais inimigos comuns, spawna o mamute
        if (inimigosVivos == 0 && criaturas.size() > 1) {
            System.out.println("SPAWNANDO MAMUTE!");
            
            Posicao posMamute = encontrarChaoAleatorio();
            // Garante que não spawna em cima do jogador
            while (posMamute.equals(jogador.getPosicao())) {
                posMamute = encontrarChaoAleatorio();
            }
            
            mamute = new Mamute(posMamute);
            celulaEm(posMamute).setCriatura(mamute);
            criaturas.add(mamute);
            registro.adicionar("UM RUGIDO ECOA NA CAVERNA... O MAMUTE APARECEU!");
            registro.adicionar("CUIDADO! O MAMUTE É EXTREMAMENTE PERIGOSO!");
            
            System.out.println("Mamute spawnado em: " + posMamute);
        }
    }
}
