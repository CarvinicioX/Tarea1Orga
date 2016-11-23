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
import java.util.Queue;
/**
 *
 * @author Vinicio.Guevara
 */
public class Tarea1Orga {

    /* Ram = 4096 B = 2^(10*2) Bytes
    Cache = 256 B = 32 Lineas
    Bloque = 8 B = 8 Palabras
    Conjunto = 4 Lineas
    Palabras = 1 B
    *************
    ASOCIATIVA
    tamaño Bloque = tamaño linea = 8 bytes = 2^3 palabras
    3 bits la palabra
    9 bits la etiqueta
    12 bits la direccion
    +++++++++++++
    */
    static int[] Ram;
    static int[][] CacheData;
    static int[] LFU;
    static boolean[] Validate;
    static double[] Time;
    static String[] Tag;
    static double cont = 0;
    static File Data = null;

    public static void main(String[] args) {
        //int x = Integer.rotateRight(2777, 3)&4095; //Obtiene solo los primeros 9 bits (etiqueta)
        //System.out.println(x);
        Time = new double[4];
        int menor, mayor, a;
        for (int tipo = 0; tipo < 4; tipo++) {
            init();
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
            Time[tipo] = cont;
            cont = 0;
        }
        System.out.println("SIN RAM:\t" + Time[0]);
        System.out.println("DIRECTA:\t" + Time[1]);
        System.out.println("ASOCIATIVA:\t" + Time[2]);
        System.out.println("POR CONJUNTOS:\t" + Time[3]);
        System.exit(0);
    }

    public static void init() {
        Ram = new int[4096];
        CacheData = new int[32][8];
        LFU = new int[32];
        Validate = new boolean[32];
        Tag =  new String[32];
        cont = 0;
        for (int i = 0; i < 32; i++) {
            Validate[i] = false;
            Tag[i] = "";
            LFU[i] = 0;
            for (int j = 0; j < 8; j++) {
                CacheData[i][j] = 0;
            }
        }
        if (Data != null) {
            return;
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
        Data = jfc.getSelectedFile();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(Data));
            String line;
            int i = 0;
            while ((line = fileIn.readLine()) != null) {
                Ram[i] = Integer.parseInt(line);
                i++;
            }
        } catch (FileNotFoundException x) {
            System.err.println(x.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
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
                int line = -1;//-1 no existe
                int lineNotExists = 0;//-1 no existe
                int menor = LFU[0];
                for (int i = 0; i < CacheData.length; i++) {
                    if(Validate[i]){
                        if(!Tag[i].isEmpty() && Tag[i].equals((Integer.rotateRight(d, 3)&4095) + "")){
                            line = i;
                        }
                    }
                    if(menor > LFU[i]){
                        menor = LFU[i];
                        lineNotExists = i;
                    }
                }
                if(line == -1){
                    line = lineNotExists;
                    moverRam_Cache(line,d);
                    Validate[line] = true;
                    Tag[line] = (Integer.rotateRight(d, 3)&4095) + "";
                    cont += 0.1;
                }
                cont += 0.01;
                LFU[line]++;
                return CacheData[line][d&7];//d&7 son solo los ultimos 3 bits la direccion
                //Integer.rotateRight(2777, 3)&4095 //dis gets the value of the tag
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
                int line = -1;//-1 no existe
                int lineNotExists = 0;//-1 no existe
                int menor = LFU[0];
                for (int i = 0; i < CacheData.length; i++) {
                    if(Validate[i]){
                        if(!Tag[i].isEmpty() && Tag[i].equals((Integer.rotateRight(d, 3)&4095) + "")){
                            line = i;
                        }
                    }
                    if(menor > LFU[i]){
                        menor = LFU[i];
                        lineNotExists = i;
                    }
                }
                if(line == -1){
                    line = lineNotExists;
                    Validate[line] = true;
                    Tag[line] = (Integer.rotateRight(d, 3)&4095) + "";
                    moverRam_Cache(line,d);
                    cont += 0.1;
                }
                cont += 0.01;
                CacheData[line][d&7] = data;//d&7 son solo los ultimos 3 bits la direccion
                //cont += 0.11;
                moverCache_Ram(line,d);
                LFU[line]++;
            }
            case 3: {//Correspondecia Asociativa por Conjuntos

                break;
            }
        }
    }
    //pero antes hay que pasarlo de la RAM a la cache 
    public static void moverRam_Cache(int line, int d){
        //System.out.println(line);
        for(int i = 0; i < CacheData[line].length; i++){
             CacheData[line][i] = Ram[d+i];
        }
    }
    //y luego de la caché a la RAM 
    public static void moverCache_Ram(int line, int d){
       Ram[d]=CacheData[line][d&7];//d&7 son solo los ultimos 3 bits la direccion
    }
}
