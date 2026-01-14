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
trabFinaLp2/
-src/
--caverna/
---ui/
----JanelaJogo.java
|
---persistencia/
----SaveJogo.java
|
---dominio/
----mundo/
----- Mundo.java
-----Celula.java
-----Posicao.java
|
----entidade/
-----Criatura.java
-----Jogador.java
-----Mamute.java
-----Tigre.java
-----Javali.java
-----Morcego.java
|
----item/
-----Item.java
-----Arma.java
-----ErvaCurativa.java
-----CogumeloForca.java
-----AbrePassagem.java
-----Consumivel.java
-----Equipavel.java
-----Interativo.java
|
----inventario/
-----Inventario.java
|
----combate/
-----GerenciadorCombate.java
-----RegistroMensagens.java
|
-imagens/
--jogador.png
--mamute.png
--tigre.png
--javali.png
--morcego.png
--graveto.png
--clava.png
--lanca.png
--erva.png
--cogumelo.png
--picareta.png
--parede.png
--chao.png
--pedra.png
--vitoria.png
--derrota.png



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
