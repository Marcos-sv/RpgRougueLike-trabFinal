package caverna.ui;

import caverna.dominio.mundo.*;
import caverna.dominio.entidade.*;
import caverna.dominio.item.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

public class JanelaJogo extends JFrame {
    private static final int TILE_SIZE = 48;
    private Mundo mundo;
    private JLabel[][] gridLabels;
    private JLabel labelArmaHUD;
    private JProgressBar barraVida;
    private JTextArea areaLog;
    private JPanel painelAcoes;
    
    // Imagens - carregadas de arquivos PNG
    private ImageIcon imgJogador;
    private ImageIcon imgMamute;
    private ImageIcon imgTigre;
    private ImageIcon imgJavali;
    private ImageIcon imgMorcego;
    private ImageIcon imgGraveto;
    private ImageIcon imgClava;
    private ImageIcon imgLanca;
    private ImageIcon imgErva;
    private ImageIcon imgCogumelo;
    private ImageIcon imgPicareta;
    private ImageIcon imgParede;
    private ImageIcon imgChao;
    private ImageIcon imgPedra;
    
    // Telas de fim de jogo
    private ImageIcon imgVitoria;
    private ImageIcon imgDerrota;
    
    private enum ModoJogo { EXPLORAR, COMBATE, MENU_INVENTARIO, MENU_ITENS, FIM_JOGO }
    private ModoJogo modoAtual = ModoJogo.EXPLORAR;
    
    public JanelaJogo() {
        try {
            System.out.println("=== INICIANDO JOGO ===");
            
            // Criar pasta de imagens se não existir
            criarPastaImagens();
            
            // Carregar imagens PNG
            carregarImagensPNG();
            
            mundo = new Mundo();
            configurarJanela();
            criarPainelMapa();
            criarHUD();
            criarPainelAcoes();
            configurarControles();
            finalizarJanela();
            atualizarTela();
            atualizarHUD();
            
            areaLog.append("\n=== JOGO INICIADO ===\n");
            areaLog.append("Objetivo: Derrote o Mamute!\n");
            areaLog.append("Cuidado: Se você morrer, é GAME OVER!\n");
            
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao iniciar jogo.\nCertifique-se que a pasta 'imagens' existe com os PNGs necessários.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarPastaImagens() {
        File pastaImagens = new File("imagens");
        if (!pastaImagens.exists()) {
            if (pastaImagens.mkdir()) {
                System.out.println("Pasta 'imagens' criada.");
                JOptionPane.showMessageDialog(this,
                    "Pasta 'imagens' criada.\nColoque os arquivos PNG dentro dela:\n" +
                    "• Criaturas: jogador.png, mamute.png, tigre.png, javali.png, morcego.png\n" +
                    "• Armas: graveto.png, clava.png, lanca.png\n" +
                    "• Itens: erva.png, cogumelo.png, picareta.png\n" +
                    "• Terreno: parede.png, chao.png, pedra.png\n" +
                    "• Telas: vitoria.png, derrota.png",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void carregarImagensPNG() {
        System.out.println("Carregando imagens PNG...");
        
        // Criaturas
        imgJogador = carregarImagem("jogador.png", "Jogador");
        imgMamute = carregarImagem("mamute.png", "Mamute");
        imgTigre = carregarImagem("tigre.png", "Tigre");
        imgJavali = carregarImagem("javali.png", "Javali");
        imgMorcego = carregarImagem("morcego.png", "Morcego");
        
        // Armas
        imgGraveto = carregarImagem("graveto.png", "Graveto");
        imgClava = carregarImagem("clava.png", "Clava");
        imgLanca = carregarImagem("lanca.png", "Lança");
        
        // Itens
        imgErva = carregarImagem("erva.png", "Erva");
        imgCogumelo = carregarImagem("cogumelo.png", "Cogumelo");
        imgPicareta = carregarImagem("picareta.png", "Picareta");
        
        // Terreno
        imgParede = carregarImagem("parede.png", "Parede");
        imgChao = carregarImagem("chao.png", "Chão");
        imgPedra = carregarImagem("pedra.png", "Pedra");
        
        // Telas de fim de jogo
        imgVitoria = carregarImagem("vitoria.png", "Vitória");
        imgDerrota = carregarImagem("derrota.png", "Derrota");
        
        System.out.println("Imagens carregadas com sucesso!");
    }
    
    private ImageIcon carregarImagem(String nomeArquivo, String descricao) {
        try {
            File arquivo = new File("imagens/" + nomeArquivo);
            
            if (arquivo.exists()) {
                System.out.println("Carregando: " + nomeArquivo);
                ImageIcon icon = new ImageIcon(arquivo.getAbsolutePath());
                
                // Para telas de vitória/derrota, mantém tamanho original
                if (descricao.equals("Vitória") || descricao.equals("Derrota")) {
                    return icon;
                }
                
                // Para outras imagens, redimensiona para TILE_SIZE
                Image imagem = icon.getImage();
                Image redimensionada = imagem.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
                return new ImageIcon(redimensionada);
            } else {
                System.err.println("AVISO: Arquivo não encontrado: " + nomeArquivo);
                return criarPlaceholder(descricao);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar " + nomeArquivo + ": " + e.getMessage());
            return criarPlaceholder(descricao);
        }
    }
    
    private ImageIcon criarPlaceholder(String descricao) {
        Color corFundo = Color.GRAY;
        Color corTexto = Color.WHITE;
        String texto = "?";
        
        switch(descricao) {
            case "Jogador": corFundo = Color.GREEN; texto = "P"; break;
            case "Mamute": corFundo = Color.RED; texto = "M"; break;
            case "Tigre": corFundo = Color.ORANGE; texto = "T"; break;
            case "Javali": corFundo = Color.PINK; texto = "J"; break;
            case "Morcego": corFundo = Color.BLACK; texto = "B"; break;
            case "Graveto": corFundo = new Color(139, 90, 43); texto = "G"; break;
            case "Clava": corFundo = new Color(101, 67, 33); texto = "C"; break;
            case "Lança": corFundo = new Color(184, 134, 11); texto = "L"; break;
            case "Erva": corFundo = Color.GREEN.darker(); texto = "V"; break;
            case "Cogumelo": corFundo = new Color(139, 69, 19); texto = "C"; break;
            case "Picareta": corFundo = Color.GRAY; texto = "P"; break;
            case "Parede": corFundo = new Color(70, 70, 70); texto = "#"; break;
            case "Chão": corFundo = new Color(101, 67, 33); texto = "."; break;
            case "Pedra": corFundo = new Color(120, 120, 120); texto = "■"; break;
            case "Vitória": 
                corFundo = new Color(0, 100, 0); 
                texto = "VITÓRIA!\nDerrotou o Mamute!"; 
                break;
            case "Derrota": 
                corFundo = new Color(100, 0, 0); 
                texto = "DERROTA!\nVocê morreu!"; 
                break;
        }
        
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
            descricao.equals("Vitória") || descricao.equals("Derrota") ? 600 : TILE_SIZE,
            descricao.equals("Vitória") || descricao.equals("Derrota") ? 400 : TILE_SIZE,
            java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(corFundo);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        
        g2d.setColor(corTexto);
        
        if (descricao.equals("Vitória") || descricao.equals("Derrota")) {
            g2d.setFont(new Font("Arial", Font.BOLD, 32));
            String[] linhas = texto.split("\n");
            
            for (int i = 0; i < linhas.length; i++) {
                java.awt.FontMetrics fm = g2d.getFontMetrics();
                int x = (img.getWidth() - fm.stringWidth(linhas[i])) / 2;
                int y = (img.getHeight() / 2) - (fm.getHeight() * (linhas.length - 1 - i * 2)) / 2 + fm.getAscent();
                g2d.drawString(linhas[i], x, y);
            }
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            String mensagemExtra = descricao.equals("Vitória") ? 
                "Pressione R para jogar novamente" : "Pressione R para tentar de novo";
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int x = (img.getWidth() - fm.stringWidth(mensagemExtra)) / 2;
            int y = img.getHeight() - 50;
            g2d.drawString(mensagemExtra, x, y);
        } else if (!texto.equals(".")) {
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int x = (TILE_SIZE - fm.stringWidth(texto)) / 2;
            int y = (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(texto, x, y);
        }
        
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    private void configurarJanela() {
        setTitle("Caverna Game - Sistema de Ações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setFocusable(true);
    }
    
    private void criarPainelMapa() {
        JPanel painelMapa = new JPanel(new GridLayout(Mundo.altura, Mundo.largura, 0, 0));
        painelMapa.setBackground(Color.BLACK);
        
        gridLabels = new JLabel[Mundo.altura][Mundo.largura];
        
        for (int y = 0; y < Mundo.altura; y++) {
            for (int x = 0; x < Mundo.largura; x++) {
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                painelMapa.add(label);
                gridLabels[y][x] = label;
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(painelMapa);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void criarHUD() {
        JPanel hud = new JPanel(new BorderLayout());
        hud.setBackground(Color.DARK_GRAY);
        hud.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel painelStatus = new JPanel(new GridLayout(2, 1, 5, 5));
        painelStatus.setBackground(new Color(40, 40, 60));
        painelStatus.setBorder(BorderFactory.createTitledBorder("Status"));
        
        JPanel painelVida = new JPanel(new BorderLayout());
        JLabel labelVida = new JLabel("VIDA:");
        labelVida.setForeground(Color.WHITE);
        
        barraVida = new JProgressBar(0, 100);
        barraVida.setValue(100);
        barraVida.setStringPainted(true);
        barraVida.setForeground(Color.GREEN);
        barraVida.setBackground(new Color(50, 20, 20));
        
        painelVida.add(labelVida, BorderLayout.WEST);
        painelVida.add(barraVida, BorderLayout.CENTER);
        
        JPanel painelArma = new JPanel(new BorderLayout());
        labelArmaHUD = new JLabel(" Arma: Graveto (1-3) ");
        labelArmaHUD.setForeground(Color.YELLOW);
        painelArma.add(labelArmaHUD, BorderLayout.WEST);
        
        painelStatus.add(painelVida);
        painelStatus.add(painelArma);
        
        areaLog = new JTextArea(8, 50);
        areaLog.setEditable(false);
        areaLog.setBackground(Color.BLACK);
        areaLog.setForeground(Color.LIGHT_GRAY);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.append("=== CAVERNA GAME ===\n");
        areaLog.append("OBJETIVO: Derrote o Mamute!\n");
        areaLog.append("CUIDADO: Se morrer, é GAME OVER!\n");
        
        JScrollPane scrollLog = new JScrollPane(areaLog);
        
        hud.add(painelStatus, BorderLayout.WEST);
        hud.add(scrollLog, BorderLayout.CENTER);
        
        add(hud, BorderLayout.SOUTH);
    }
    
    private void criarPainelAcoes() {
        painelAcoes = new JPanel(new GridLayout(4, 2, 5, 5));
        painelAcoes.setBackground(new Color(60, 40, 40));
        painelAcoes.setBorder(BorderFactory.createTitledBorder("Ações"));
        painelAcoes.setPreferredSize(new Dimension(200, 0));
        painelAcoes.setVisible(false);
        
        String[] acoes = {"A - Atacar", "F - Fugir", "U - Usar Item", "T - Trocar Arma",
                        "C - Coletar", "D - Dropar", "M - Mover", "V - Ver Status"};
        
        for (String acao : acoes) {
            JButton btn = new JButton(acao);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setBackground(new Color(80, 60, 60));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            
            String primeiraLetra = acao.substring(0, 1);
            btn.addActionListener(e -> processarAcao(primeiraLetra.charAt(0)));
            
            painelAcoes.add(btn);
        }
        
        add(painelAcoes, BorderLayout.EAST);
    }
    
    private void configurarControles() {
        // Movimento
        bindTecla("W", () -> executarMovimento(0, -1));
        bindTecla("S", () -> executarMovimento(0, 1));
        bindTecla("A", () -> executarMovimento(-1, 0));
        bindTecla("D", () -> executarMovimento(1, 0));
        
        // Ações
        bindTecla("E", this::abrirMenuAcoes);
        bindTecla("I", this::abrirInventario);
        bindTecla("SPACE", this::atacarAdjacente);
        bindTecla("U", this::usarItemSelecionado);
        bindTecla("T", this::trocarArma);
        bindTecla("C", this::coletarItem);
        bindTecla("M", () -> { 
            modoAtual = ModoJogo.EXPLORAR; 
            painelAcoes.setVisible(false);
            areaLog.append("Modo exploração ativado\n");
        });
        bindTecla("V", this::verStatus);
        bindTecla("R", this::reiniciarJogo);
        bindTecla("ESCAPE", () -> System.exit(0));
        
        // Números para itens
        for (int i = 1; i <= 9; i++) {
            final int num = i;
            bindTecla(String.valueOf(i), () -> selecionarItem(num));
        }
        
        requestFocusInWindow();
    }
    
    private void bindTecla(String tecla, Runnable acao) {
        JPanel contentPane = (JPanel) getContentPane();
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();
        
        String actionName = "acao_" + tecla;
        inputMap.put(KeyStroke.getKeyStroke(tecla), actionName);
        
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modoAtual == ModoJogo.FIM_JOGO && !tecla.equals("R") && !tecla.equals("ESCAPE")) {
                    return; // Ignora outras teclas no modo fim de jogo
                }
                acao.run();
            }
        });
    }
    
    private void executarMovimento(int dx, int dy) {
        if (modoAtual != ModoJogo.EXPLORAR) {
            areaLog.append("Você não pode se mover neste modo!\n");
            return;
        }
        
        Jogador jogador = mundo.getJogador();
        if (!jogador.taVivo()) {
            mostrarTelaDerrota();
            return;
        }
        
        boolean sucesso = mundo.executarTurno(dx, dy);
        
        if (sucesso) {
            atualizarTela();
            atualizarHUD();
            verificarCombateAdjacente();
            
            // Verifica se ganhou (derrotou o mamute)
            if (mundo.getMamute() != null && !mundo.getMamute().taVivo()) {
                mostrarTelaVitoria();
                return;
            }
            
            // Verifica se perdeu
            if (!jogador.taVivo()) {
                mostrarTelaDerrota();
                return;
            }
        }
        
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
    
    private void atacarAdjacente() {
        if (modoAtual != ModoJogo.EXPLORAR) {
            areaLog.append("Use M para voltar ao modo exploração primeiro.\n");
            return;
        }
        
        Jogador jogador = mundo.getJogador();
        if (!jogador.taVivo()) {
            mostrarTelaDerrota();
            return;
        }
        
        Posicao posJogador = jogador.getPosicao();
        
        int[][] direcoes = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        
        for (int[] dir : direcoes) {
            Posicao posAlvo = new Posicao(posJogador.x() + dir[0], posJogador.y() + dir[1]);
            Celula celula = mundo.celulaEm(posAlvo);
            Criatura inimigo = celula.getCriatura();
            
            if (inimigo != null && inimigo.taVivo() && inimigo != jogador) {
                areaLog.append("Atacando " + getNomeCriatura(inimigo) + "!\n");
                
                int dano = jogador.atacar();
                inimigo.tomaDano(dano);
                areaLog.append("Causou " + dano + " de dano!\n");
                
                if (!inimigo.taVivo()) {
                    areaLog.append("Você derrotou " + getNomeCriatura(inimigo) + "!\n");
                    celula.setCriatura(null);
                    mundo.geCriaturas().remove(inimigo);
                    
                    // VERIFICA SE PRECISA SPAWNAR MAMUTE!
                    verificarMamute();
                    
                    // VERIFICA SE DERROTOU O MAMUTE
                    if (inimigo instanceof Mamute) {
                        areaLog.append("\n=== VOCÊ DERROTOU O MAMUTE! ===\n");
                        mostrarTelaVitoria();
                        return;
                    }
                } else {
                    int danoInimigo = inimigo.atacar();
                    jogador.tomaDano(danoInimigo);
                    areaLog.append(getNomeCriatura(inimigo) + " contra-atacou: " + danoInimigo + " de dano!\n");
                    
                    if (!jogador.taVivo()) {
                        areaLog.append("Você foi derrotado!\n");
                        mostrarTelaDerrota();
                        return;
                    }
                }
                
                atualizarHUD();
                atualizarTela();
                return;
            }
        }
        
        areaLog.append("Nenhum inimigo adjacente para atacar!\n");
    }
    
    private void mostrarTelaVitoria() {
        modoAtual = ModoJogo.FIM_JOGO;
        areaLog.append("\n=== PARABÉNS! VOCÊ VENCEU! ===\n");
        areaLog.append("Você derrotou o temível Mamute!\n");
        areaLog.append("A caverna agora está segura!\n");
        areaLog.append("Pressione R para jogar novamente\n");
        
        // Mostra tela de vitória no centro
        mostrarTelaFimJogo(imgVitoria, "VITÓRIA!");
    }
    
    private void mostrarTelaDerrota() {
        modoAtual = ModoJogo.FIM_JOGO;
        areaLog.append("\n=== GAME OVER ===\n");
        areaLog.append("Você foi derrotado!\n");
        areaLog.append("Pressione R para tentar novamente\n");
        
        // Mostra tela de derrota no centro
        mostrarTelaFimJogo(imgDerrota, "DERROTA!");
    }
    
    private void mostrarTelaFimJogo(ImageIcon imagem, String titulo) {
        // Remove componentes antigos
        getContentPane().removeAll();
        
        // Cria painel para a tela de fim de jogo
        JPanel painelFim = new JPanel(new BorderLayout());
        painelFim.setBackground(Color.BLACK);
        
        // Label com a imagem
        JLabel labelImagem = new JLabel(imagem);
        labelImagem.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBackground(Color.BLACK);
        
        JButton btnReiniciar = new JButton("Jogar Novamente (R)");
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 16));
        btnReiniciar.setBackground(new Color(0, 100, 0));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.addActionListener(e -> reiniciarJogo());
        
        JButton btnSair = new JButton("Sair (ESC)");
        btnSair.setFont(new Font("Arial", Font.BOLD, 16));
        btnSair.setBackground(new Color(100, 0, 0));
        btnSair.setForeground(Color.WHITE);
        btnSair.addActionListener(e -> System.exit(0));
        
        painelBotoes.add(btnReiniciar);
        painelBotoes.add(Box.createHorizontalStrut(20));
        painelBotoes.add(btnSair);
        
        painelFim.add(labelImagem, BorderLayout.CENTER);
        painelFim.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelFim);
        revalidate();
        repaint();
    }
    
    private void verificarMamute() {
        boolean temInimigos = false;
        
        for (Criatura c : mundo.geCriaturas()) {
            if (c != mundo.getJogador() && c.taVivo()) {
                temInimigos = true;
                break;
            }
        }
        
        if (!temInimigos && mundo.getMamute() == null) {
            Posicao posMamute = encontrarPosicaoSegura();
            Mamute mamute = new Mamute(posMamute);
            
            mundo.celulaEm(posMamute).setCriatura(mamute);
            
            try {
                java.lang.reflect.Field criaturasField = Mundo.class.getDeclaredField("criaturas");
                criaturasField.setAccessible(true);
                java.util.List<Criatura> criaturas = (java.util.List<Criatura>) criaturasField.get(mundo);
                criaturas.add(mamute);
                
                java.lang.reflect.Field mamuteField = Mundo.class.getDeclaredField("mamute");
                mamuteField.setAccessible(true);
                mamuteField.set(mundo, mamute);
                
                areaLog.append("\n=== UM RUGIDO ECOA NA CAVERNA! ===\n");
                areaLog.append("O MAMUTE APARECEU!\n");
                areaLog.append("Vida: 18 | Dano: 3-8 | CUIDADO!\n");
                
                atualizarTela();
                
            } catch (Exception e) {
                System.err.println("Erro ao spawnar mamute: " + e.getMessage());
            }
        }
    }
    
    private Posicao encontrarPosicaoSegura() {
        Jogador jogador = mundo.getJogador();
        Posicao posJogador = jogador.getPosicao();
        
        for (int y = 1; y < Mundo.altura - 1; y++) {
            for (int x = 1; x < Mundo.largura - 1; x++) {
                Posicao pos = new Posicao(x, y);
                Celula celula = mundo.celulaEm(pos);
                
                int distancia = Math.abs(x - posJogador.x()) + Math.abs(y - posJogador.y());
                
                if (distancia > 10 && celula.ETransitavel() && 
                    celula.getCriatura() == null && celula.getItem() == null) {
                    return pos;
                }
            }
        }
        
        return new Posicao(5, 5);
    }
    
    // ... (métodos abrirMenuAcoes, abrirInventario permanecem iguais) ...
    
    private void abrirMenuAcoes() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        modoAtual = ModoJogo.MENU_ITENS;
        painelAcoes.setVisible(true);
        areaLog.append("\n=== MENU DE AÇÕES ===\n");
        areaLog.append("1-9: Selecionar item\n");
        areaLog.append("U: Usar item selecionado\n");
        areaLog.append("T: Trocar arma\n");
        areaLog.append("C: Coletar item do chão\n");
        areaLog.append("M: Voltar a mover\n");
    }
    
    private void abrirInventario() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        modoAtual = ModoJogo.MENU_INVENTARIO;
        mostrarInventarioCompleto();
    }
    
    private void usarItemSelecionado() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        Jogador jogador = mundo.getJogador();
        List<Item> itens = jogador.getInventario().getItens();
        
        if (itens.isEmpty()) {
            areaLog.append("Inventário vazio!\n");
            return;
        }
        
        areaLog.append("\n=== USAR ITEM ===\n");
        for (int i = 0; i < itens.size(); i++) {
            areaLog.append((i+1) + ": " + itens.get(i).getNome() + "\n");
        }
        areaLog.append("Pressione 1-" + itens.size() + " para usar um item\n");
        
        modoAtual = ModoJogo.MENU_ITENS;
    }
    
    private void selecionarItem(int numero) {
        if (modoAtual != ModoJogo.MENU_ITENS) return;
        
        Jogador jogador = mundo.getJogador();
        List<Item> itens = jogador.getInventario().getItens();
        
        if (numero < 1 || numero > itens.size()) {
            areaLog.append("Item inválido!\n");
            return;
        }
        
        Item itemSelecionado = itens.get(numero - 1);
        areaLog.append("Selecionado: " + itemSelecionado.getNome() + "\n");
        
        usarItem(itemSelecionado);
        modoAtual = ModoJogo.EXPLORAR;
        painelAcoes.setVisible(false);
    }
    
    private void usarItem(Item item) {
        Jogador jogador = mundo.getJogador();
        
        if (item instanceof Arma) {
            jogador.equiparArma((Arma) item);
            areaLog.append("Você equipou: " + item.getNome() + "\n");
            
        } else if (item instanceof Consumivel) {
            if (item instanceof ErvaCurativa) {
                jogador.curar(3);
                areaLog.append("Usou Erva Curativa! +3 de vida\n");
            } else if (item instanceof CogumeloForca) {
                jogador.buffForca();
                areaLog.append("Usou Cogumelo da Força! +2 de dano por 3 turnos\n");
            }
            
            jogador.getInventario().remover(item);
            
        } else if (item instanceof AbrePassagem) {
            Posicao posJogador = jogador.getPosicao();
            int[][] direcoes = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
            
            for (int[] dir : direcoes) {
                Posicao posAlvo = new Posicao(posJogador.x() + dir[0], posJogador.y() + dir[1]);
                Celula celula = mundo.celulaEm(posAlvo);
                
                if (celula.EBarrado()) {
                    celula.destrancar();
                    jogador.getInventario().remover(item);
                    areaLog.append("Usou picareta para quebrar pedras!\n");
                    atualizarTela();
                    return;
                }
            }
            
            areaLog.append("Não há pedras adjacentes para quebrar!\n");
            return;
        }
        
        atualizarHUD();
    }
    
    private void trocarArma() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        Jogador jogador = mundo.getJogador();
        List<Item> itens = jogador.getInventario().getItens();
        
        List<Item> armas = itens.stream()
            .filter(item -> item instanceof Arma)
            .toList();
        
        if (armas.isEmpty()) {
            areaLog.append("Você não tem outras armas!\n");
            return;
        }
        
        areaLog.append("\n=== TROCAR ARMA ===\n");
        for (int i = 0; i < armas.size(); i++) {
            Arma arma = (Arma) armas.get(i);
            String equipado = (arma == jogador.getArmaEquipada()) ? " [EQUIPADA]" : "";
            areaLog.append((i+1) + ": " + arma.getNome() + " (Dano: " + 
                          arma.calcularDano() + ")" + equipado + "\n");
        }
        areaLog.append("Pressione 1-" + armas.size() + " para equipar\n");
        
        modoAtual = ModoJogo.MENU_ITENS;
    }
    
    private void coletarItem() {
        if (modoAtual != ModoJogo.EXPLORAR) {
            areaLog.append("Você precisa estar no modo exploração!\n");
            return;
        }
        
        Jogador jogador = mundo.getJogador();
        Posicao posJogador = jogador.getPosicao();
        
        Celula celulaAtual = mundo.celulaEm(posJogador);
        Item item = celulaAtual.getItem();
        
        if (item != null) {
            if (jogador.getInventario().adicionar(item)) {
                celulaAtual.setItem(null);
                areaLog.append("Coletou: " + item.getNome() + "\n");
                atualizarTela();
            } else {
                areaLog.append("Inventário cheio! Não pode coletar " + item.getNome() + "\n");
            }
        } else {
            areaLog.append("Não há itens aqui para coletar!\n");
        }
    }
    
    private void verStatus() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        Jogador jogador = mundo.getJogador();
        
        areaLog.append("\n=== STATUS DETALHADO ===\n");
        areaLog.append("Vida: " + jogador.getVida() + "/" + jogador.getVidaMaxima() + "\n");
        
        Arma arma = jogador.getArmaEquipada();
        if (arma != null) {
            String danoInfo = "";
            if (arma.getNome().equals("Graveto")) danoInfo = " (1-3)";
            else if (arma.getNome().equals("Clava")) danoInfo = " (4-6)";
            else if (arma.getNome().equals("Lança")) danoInfo = " (8-11)";
            
            areaLog.append("Arma: " + arma.getNome() + danoInfo + "\n");
        }
        
        areaLog.append("Inventário: " + jogador.getInventario().getQuantItens() + "/" + 
                      jogador.getInventario().getEspacoMax() + " slots\n");
        
        int inimigosVivos = 0;
        for (Criatura c : mundo.geCriaturas()) {
            if (c.taVivo() && c != jogador) inimigosVivos++;
        }
        areaLog.append("Inimigos restantes: " + inimigosVivos + "\n");
        
        if (mundo.getMamute() != null) {
            areaLog.append("Mamute: " + (mundo.getMamute().taVivo() ? "VIVO (18 HP)" : "DERROTADO") + "\n");
        }
    }
    
    private void verificarCombateAdjacente() {
        Jogador jogador = mundo.getJogador();
        Posicao pos = jogador.getPosicao();
        
        int[][] direcoes = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
        
        for (int[] dir : direcoes) {
            Posicao posAlvo = new Posicao(pos.x() + dir[0], pos.y() + dir[1]);
            Celula celula = mundo.celulaEm(posAlvo);
            Criatura inimigo = celula.getCriatura();
            
            if (inimigo != null && inimigo.taVivo() && inimigo != jogador) {
                areaLog.append("CUIDADO! " + getNomeCriatura(inimigo) + " está próximo!\n");
                areaLog.append("Use SPACE para atacar ou afaste-se!\n");
                break;
            }
        }
    }
    
    private void mostrarInventarioCompleto() {
        Jogador jogador = mundo.getJogador();
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== INVENTÁRIO COMPLETO ===\n\n");
        sb.append("Vida: ").append(jogador.getVida()).append("/").append(jogador.getVidaMaxima()).append("\n");
        sb.append("Espaço: ").append(jogador.getInventario().getQuantItens()).append("/")
          .append(jogador.getInventario().getEspacoMax()).append("\n\n");
        
        Arma armaEquipada = jogador.getArmaEquipada();
        if (armaEquipada != null) {
            sb.append("ARMA EQUIPADA:\n");
            sb.append("• ").append(armaEquipada.getNome());
            
            if (armaEquipada.getNome().equals("Graveto")) sb.append(" (Dano: 1-3)");
            else if (armaEquipada.getNome().equals("Clava")) sb.append(" (Dano: 4-6)");
            else if (armaEquipada.getNome().equals("Lança")) sb.append(" (Dano: 8-11)");
            
            sb.append("\n\n");
        }
        
        sb.append("ITENS (").append(jogador.getInventario().getQuantItens()).append("):\n");
        
        List<Item> itens = jogador.getInventario().getItens();
        if (itens.isEmpty()) {
            sb.append("  (inventário vazio)\n");
        } else {
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.get(i);
                sb.append(i+1).append(". ").append(item.getNome());
                
                if (item instanceof Arma) {
                    Arma arma = (Arma) item;
                    sb.append(" [ARMA");
                    if (arma.getNome().equals("Graveto")) sb.append(" - Dano: 1-3");
                    else if (arma.getNome().equals("Clava")) sb.append(" - Dano: 4-6");
                    else if (arma.getNome().equals("Lança")) sb.append(" - Dano: 8-11");
                    sb.append("]");
                } else if (item instanceof ErvaCurativa) {
                    sb.append(" [CURA +3]");
                } else if (item instanceof CogumeloForca) {
                    sb.append(" [BUFF +2 dano por 3 ataques]");
                } else if (item instanceof AbrePassagem) {
                    sb.append(" [QUEBRA PEDRAS]");
                }
                
                if (item == armaEquipada) {
                    sb.append(" [EQUIPADA]");
                }
                sb.append("\n");
            }
        }
        
        sb.append("\n=== COMANDOS ===\n");
        sb.append("• Pressione o NÚMERO do item para selecionar\n");
        sb.append("• U: Usar item selecionado\n");
        sb.append("• T: Trocar arma\n");
        sb.append("• C: Coletar item do chão\n");
        sb.append("• M: Voltar a mover\n");
        
        areaLog.append("\n" + sb.toString());
        modoAtual = ModoJogo.MENU_INVENTARIO;
    }
    
    private void processarAcao(char acao) {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        switch(Character.toUpperCase(acao)) {
            case 'A': atacarAdjacente(); break;
            case 'F': areaLog.append("Fugindo...\n"); break;
            case 'U': usarItemSelecionado(); break;
            case 'T': trocarArma(); break;
            case 'C': coletarItem(); break;
            case 'D': areaLog.append("Dropando item...\n"); break;
            case 'M': modoAtual = ModoJogo.EXPLORAR; painelAcoes.setVisible(false); break;
            case 'V': verStatus(); break;
        }
        
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
        requestFocusInWindow();
    }
    
    private String getNomeCriatura(Criatura c) {
        if (c instanceof Mamute) return "Mamute";
        if (c instanceof Tigre) return "Tigre";
        if (c instanceof Javali) return "Javali";
        if (c instanceof Morcego) return "Morcego";
        return "Criatura";
    }
    
    private void atualizarTela() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        for (int y = 0; y < Mundo.altura; y++) {
            for (int x = 0; x < Mundo.largura; x++) {
                JLabel label = gridLabels[y][x];
                Posicao pos = new Posicao(x, y);
                Celula cel = mundo.celulaEm(pos);
                
                ImageIcon icon = null;
                
                Criatura cri = cel.getCriatura();
                if (cri != null && cri.taVivo()) {
                    if (cri == mundo.getJogador()) {
                        icon = imgJogador;
                    } else if (cri instanceof Mamute) {
                        icon = imgMamute;
                    } else if (cri instanceof Tigre) {
                        icon = imgTigre;
                    } else if (cri instanceof Javali) {
                        icon = imgJavali;
                    } else if (cri instanceof Morcego) {
                        icon = imgMorcego;
                    }
                }
                
                if (icon == null) {
                    Item item = cel.getItem();
                    if (item != null) {
                        if (item instanceof Arma) {
                            String nomeArma = item.getNome();
                            if (nomeArma.equals("Graveto")) {
                                icon = imgGraveto;
                            } else if (nomeArma.equals("Clava")) {
                                icon = imgClava;
                            } else if (nomeArma.equals("Lança")) {
                                icon = imgLanca;
                            } else {
                                icon = imgGraveto;
                            }
                        } else if (item instanceof ErvaCurativa) {
                            icon = imgErva;
                        } else if (item instanceof CogumeloForca) {
                            icon = imgCogumelo;
                        } else if (item instanceof AbrePassagem) {
                            icon = imgPicareta;
                        }
                    }
                }
                
                if (icon == null) {
                    if (x == 0 || x == Mundo.largura-1 || y == 0 || y == Mundo.altura-1) {
                        icon = imgParede;
                    } else if (cel.EBarrado()) {
                        icon = imgPedra;
                    } else if (!cel.ETransitavel()) {
                        icon = imgParede;
                    } else {
                        icon = imgChao;
                    }
                }
                
                label.setIcon(icon);
                label.setText("");
                
                if (cri instanceof Mamute) {
                    label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                } else {
                    label.setBorder(null);
                }
            }
        }
        
        repaint();
    }
    
    private void atualizarHUD() {
        if (modoAtual == ModoJogo.FIM_JOGO) return;
        
        try {
            Jogador jogador = mundo.getJogador();
            
            int vida = jogador.getVida();
            int vidaMax = jogador.getVidaMaxima();
            barraVida.setValue(vida);
            barraVida.setMaximum(vidaMax);
            barraVida.setString("Vida: " + vida + "/" + vidaMax);
            
            if (vida < vidaMax * 0.3) barraVida.setForeground(Color.RED);
            else if (vida < vidaMax * 0.6) barraVida.setForeground(Color.YELLOW);
            else barraVida.setForeground(Color.GREEN);
            
            Arma arma = jogador.getArmaEquipada();
            if (arma != null) {
                String danoInfo = "";
                if (arma.getNome().equals("Graveto")) danoInfo = " (1-3)";
                else if (arma.getNome().equals("Clava")) danoInfo = " (4-6)";
                else if (arma.getNome().equals("Lança")) danoInfo = " (8-11)";
                
                labelArmaHUD.setText(" Arma: " + arma.getNome() + danoInfo + " ");
            }
            
        } catch (Exception e) {
            System.err.println("Erro HUD: " + e.getMessage());
        }
    }
    
    private void reiniciarJogo() {
        int confirm = JOptionPane.YES_OPTION;
        
        if (modoAtual != ModoJogo.FIM_JOGO) {
            confirm = JOptionPane.showConfirmDialog(this,
                "Deseja reiniciar o jogo?\nTodo o progresso será perdido.",
                "Reiniciar", JOptionPane.YES_NO_OPTION);
        }
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Remove tela de fim de jogo se estiver visível
            getContentPane().removeAll();
            
            // Recria tudo
            criarPainelMapa();
            criarHUD();
            criarPainelAcoes();
            
            mundo = new Mundo();
            areaLog.setText("");
            areaLog.append("=== NOVO JOGO ===\n");
            areaLog.append("Objetivo: Derrote o Mamute!\n");
            areaLog.append("Você começa com Graveto (1-3 dano)\n");
            
            modoAtual = ModoJogo.EXPLORAR;
            painelAcoes.setVisible(false);
            
            atualizarTela();
            atualizarHUD();
            revalidate();
            repaint();
            requestFocusInWindow();
        }
    }
    
    private void finalizarJanela() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        Timer timer = new Timer(1000, e -> {
            if (!hasFocus()) {
                requestFocusInWindow();
            }
        });
        timer.start();
        
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });
    }
    
    public static void main(String[] args) {
        System.out.println("=== CAVERNA GAME - SISTEMA DE AÇÕES ===");
        
        SwingUtilities.invokeLater(() -> {
            new JanelaJogo();
        });
    }
}