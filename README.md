<h1>Trabalho Final – Linguagem de Programação II</h1>
<h3>📚 Disciplina</h3>
Linguagem de Programação II

<h3>👨‍🏫 Professor</h3>
Thiago Bonini Borchartt



<h3>👥 Integrantes do Grupo</h3>
Marcos Vitor Silva Vasconcelos
Iago Calazans Silva Ramos
Luis Felipe Lopes Rosa



<h3>🅱️ Opção de Trabalho</h3>
Opção B – Explorador de Mundos (Roguelike)



<h3>🎮 Descrição do Projeto</h3>
Este projeto consiste no desenvolvimento de um jogo do tipo Roguelike, onde o jogador explora um mundo gerado proceduralmente, coleta itens, gerencia inventário e enfrenta inimigos em combates por turnos.
O sistema foi desenvolvido aplicando os principais conceitos de Programação Orientada a Objetos, como:
Herança
Polimorfismo
Encapsulamento
Interfaces
Separação clara entre domínio, interface gráfica e persistência
O jogo possui sistema de combate com registro de mensagens, inventário limitado, itens consumíveis, equipáveis e interativos, além de salvamento e carregamento do estado do jogo.



<h3>🛠️ Tecnologias Utilizadas</h3>
Java
Java Swing (interface gráfica)
Programação Orientada a Objetos



<h3>📁 Estrutura do Projeto</h3><br>
trabFinaLp2/ <br>
│ <br>
├── src/ <br>
│   └── caverna/ <br>
│       ├── ui/ <br>
│       │   └── JanelaJogo.java <br>
│       │ <br>
│       ├── persistencia/ <br>
│       │   └── SaveJogo.java <br>
│       │ <br>
│       ├── dominio/ <br>
│       │   ├── mundo/ <br>
│       │   │   ├── Mundo.java <br>
│       │   │   ├── Celula.java <br>
│       │   │   └── Posicao.java <br>
│       │   │ <br>
│       │   ├── entidade/ <br>
│       │   │   ├── Criatura.java <br>
│       │   │   ├── Jogador.java <br>
│       │   │   ├── Mamute.java <br>
│       │   │   ├── Tigre.java <br>
│       │   │   ├── Javali.java <br>
│       │   │   └── Morcego.java <br>
│       │   │ <br>
│       │   ├── item/ <br>
│       │   │   ├── Item.java <br>
│       │   │   ├── Arma.java <br>
│       │   │   ├── ErvaCurativa.java <br>
│       │   │   ├── CogumeloForca.java <br>
│       │   │   ├── AbrePassagem.java <br>
│       │   │   ├── Consumivel.java <br>
│       │   │   ├── Equipavel.java <br>
│       │   │   └── Interativo.java <br>
│       │   │ <br>
│       │   ├── inventario/ <br>
│       │   │   └── Inventario.java <br>
│       │   │ <br>
│       │   └── combate/ <br>
│       │       ├── GerenciadorCombate.java <br>
│       │       └── RegistroMensagens.java <br>
│ <br>
├── imagens/ <br>
│   ├── jogador.png <br>
│   ├── mamute.png <br>
│   ├── tigre.png <br>
│   ├── javali.png <br>
│   ├── morcego.png <br>
│   ├── graveto.png <br>
│   ├── clava.png <br>
│   ├── lanca.png <br>
│   ├── erva.png <br>
│   ├── cogumelo.png <br>
│   ├── picareta.png <br>
│   ├── parede.png <br>
│   ├── chao.png <br>
│   ├── pedra.png <br>
│   ├── vitoria.png <br>
│   └── derrota.png <br>



<h3>⚙️ Compilação </h3><br>
No diretório raiz do projeto (com o JanelaJogo.java aberto se estiver no VScode), execute: <br>
javac -d bin -cp "src" src/caverna/ui/JanelaJogo.java <br>
<br>
<h3>▶️ Execução </h3><br>
Após a compilação, execute: <br>
java -cp "bin" caverna.ui.JanelaJogo <br>



<h3>✅ Requisitos Atendidos</h3>
Separação entre interface gráfica, domínio e persistência<br>
Sistema de inventário limitado<br>
Itens consumíveis, equipáveis e interativos<br>
Sistema de combate com log de mensagens<br>
Persistência de dados (save/load)<br>
Tratamento de erros e exceções<br>
Uso consistente de conceitos de Orientação a Objetos<br>
