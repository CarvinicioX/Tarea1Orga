package tarea1orga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Vinicio.Guevara
 */
public class Tarea1Orga {

    static int[] Ram;
    static Object[][] Cache;
    static double cont = 0;

    public static void main(String[] args) {
        init();
        int tipo, menor, mayor, a;
        for (int k = 0; k < 4; k++) {
            tipo = k;
            escribir(100, tipo, 10);    //En la memoria 100 escribe un 10
            escribir(101, tipo, 13);
            escribir(102, tipo, 21);
            escribir(103, tipo, 11);
            escribir(104, tipo, 67);
            escribir(105, tipo, 43);
            escribir(106, tipo, 9);
            escribir(107, tipo, 11);
            escribir(108, tipo, 19);
            escribir(109, tipo, 23);
            escribir(110, tipo, 32);
            escribir(111, tipo, 54);
            escribir(112, tipo, 98);
            escribir(113, tipo, 7);
            escribir(114, tipo, 13);
            escribir(115, tipo, 1);
            menor = leer(100, tipo);
            mayor = menor;
            a = 0;
            for (int i = 101; i <= 115; i++) {
                a++;
                escribir(615, tipo, a);
                if (leer(i, tipo) < menor) {
                    menor = leer(i, tipo);
                }
                if (leer(i, tipo) > mayor) {
                    mayor = leer(i, tipo);
                }
            }
            System.out.println(cont);
        }
        System.exit(0);
    }

    public static void init() {
        Ram = new int[4096];
        Cache = new Object[32][11];
        cont = 0;
        for (int i = 0; i < 32; i++) {
            Cache[i][0] = false;
            Cache[i][1] = false;
            Cache[i][2] = "";
            for (int j = 3; j < 11; j++) {
                Cache[i][j] = 0;
            }
        }
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int opt = 1;
        while (opt != JFileChooser.OPEN_DIALOG) {
            if (jfc.showOpenDialog(new JFrame()) == JFileChooser.CANCEL_OPTION) {
                System.exit(0);
            }
            if (jfc.getSelectedFile() != null && jfc.getSelectedFile().getName().equals("datos.txt")) {
                break;
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Error\nArchivo Invalido", "Error de Capa 8", JOptionPane.ERROR_MESSAGE);
            }
        }
        File Data = jfc.getSelectedFile();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(Data));
            String line;
            int i = 0;
            while ((line = fileIn.readLine()) != null) {
                Ram[i] = Integer.parseInt(line);
                i++;
            }
        } catch (FileNotFoundException x) {
        } catch (IOException ex) {
        }
    }

    public static int leer(int d, int type) {
        switch (type) {
            case 0: {//Sin Ram
                cont += 0.1;
                return Ram[d];
            }
            case 1: {//Correspondecia Directa

                break;
            }
            case 2: {//Correspondecia Asociativa

                break;
            }
            case 3: {//Correspondecia Asociativa por Conjuntos

                break;
            }
        }
        return 0;
    }

    public static void escribir(int d, int type, int data) {
        switch (type) {
            case 0: {//Sin Ram
                Ram[d] = data;
                cont += 0.1;
                break;
            }
            case 1: {//Correspondecia Directa

                break;
            }
            case 2: {//Correspondecia Asociativa

                break;
            }
            case 3: {//Correspondecia Asociativa por Conjuntos

                break;
            }
        }
    }
}
