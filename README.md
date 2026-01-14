Trabalho Final – Linguagem de Programação II
📚 Disciplina
Linguagem de Programação II

👨‍🏫 Professor
Thiago Bonini Borchartt



👥 Integrantes do Grupo
Marcos Vitor Silva Vasconcelos
Iago Calazans Silva Ramos
Luis Felipe Lopes Rosa



🅱️ Opção de Trabalho
Opção B – Explorador de Mundos (Roguelike)



🎮 Descrição do Projeto
Este projeto consiste no desenvolvimento de um jogo do tipo Roguelike, onde o jogador explora um mundo gerado proceduralmente, coleta itens, gerencia inventário e enfrenta inimigos em combates por turnos.
O sistema foi desenvolvido aplicando os principais conceitos de Programação Orientada a Objetos, como:
Herança
Polimorfismo
Encapsulamento
Interfaces
Separação clara entre domínio, interface gráfica e persistência
O jogo possui sistema de combate com registro de mensagens, inventário limitado, itens consumíveis, equipáveis e interativos, além de salvamento e carregamento do estado do jogo.



🛠️ Tecnologias Utilizadas
Java
Java Swing (interface gráfica)
Programação Orientada a Objetos



📁 Estrutura do Projeto
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



⚙️ Compilação
No diretório raiz do projeto (com o JanelaJogo.java aberto se estiver no VScode), execute:
javac -d bin -cp "src" src/caverna/ui/JanelaJogo.java

▶️ Execução
Após a compilação, execute:
java -cp "bin" caverna.ui.JanelaJogo



✅ Requisitos Atendidos
Separação entre interface gráfica, domínio e persistência
Sistema de inventário limitado
Itens consumíveis, equipáveis e interativos
Sistema de combate com log de mensagens
Persistência de dados (save/load)
Tratamento de erros e exceções
Uso consistente de conceitos de Orientação a Objetos
