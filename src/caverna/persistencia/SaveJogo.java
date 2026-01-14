package caverna.persistencia;

import caverna.dominio.mundo.Mundo;
import java.io.*;

public class SaveJogo {
    private static final String arquivo = "caverna.save";

    public static void salvar(Mundo mundo) throws IOException {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
                oos.writeObject(mundo);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static Mundo carregar() throws IOException, ClassNotFoundException {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                return (Mundo) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
            return null;
        }
    }
}
