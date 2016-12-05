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

    /* Ram = 4096 B = 2^(10*2) Bytes
    Cache = 256 B = 32 Lineas
    Bloque = 8 B = 8 Palabras
    Conjunto = 4 Lineas
    Palabras = 1 B
    512 Bloques
    *************
    ASOCIATIVA POR CONJUNTOS
    tamaño Bloque = tamaño linea = 8 bytes = 2^3 palabras
    3 bits la palabra
    9 bits la bloque
    Bloque
        2 bits Conjunto
        7 bits Etiqueta
    12 bits la direccion
    +++++++++++++
    Asociativa por Conjuntos
    Tamaño Bloque = 2^3 palabras
    Direccion = 12 bits
    Palabra = 3 bits
    Conjunto = 2 bits
    Etiqueta = 7 bits
     */
    static int[] Ram;
    static int[][] CacheData;
    //0 -
    static int[] LFU;
    static boolean[] Validate;
    static boolean[] Modified;
    static int[] Block;
    static double[] Time;
    static int[] Tag;
    static int[] Set;
    static double cont = 0;
    static File Data = null;

    public static void main(String[] args) {
        //int x = Integer.rotateRight(2777, 3)&4095; //Obtiene solo los primeros 9 bits (etiqueta)
        //System.out.println(x);
        Time = new double[4];
        int menor, mayor, a, n, temp;
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
            init();
            n = 1000;
            for (int i = 0; i <= n - 2; i++) {
                for (int j = i + 1; j <= n - 1; j++) {
                    if (leer(i, tipo) > leer(j, tipo)) {
                        temp = leer(i, tipo);
                        escribir(i, tipo, leer(j, tipo));
                        escribir(j, tipo, temp);
                    }
                }
            }
            System.out.println(cont);
        }
        for (int i = 0; i < 4; i++) {
            Time[i] = Math.round(Time[i] * 100.0) / 100.0;
        }
        System.out.println(" ----------------------------------------------");
        System.out.println("|           Tipo           | Tiempo de Corrida");
        System.out.println(" --------------------------+-------------------");
        System.out.println("|        Sin Cache         | " + Time[0] + " μs");
        System.out.println("|         Directo          | " + Time[1] + " μs");
        System.out.println("|        Asociativo        | " + Time[2] + " μs");
        System.out.println("| Asociativo por Conjuntos | " + Time[3] + " μs");
        System.out.println(" ----------------------------------------------");
        System.exit(0);
    }

    public static void init() {
        Ram = new int[4096];
        CacheData = new int[32][8];
        LFU = new int[32];
        Validate = new boolean[32];
        Modified = new boolean[32];
        Block = new int[32];
        Tag = new int[32];
        Set = new int[32];
        cont = 0;
        for (int i = 0; i < 32; i++) {
            Validate[i] = false;
            Modified[i] = false;
            Block[i] = 0;
            Tag[i] = -1;
            Set[i] = -1;
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
        int valor = -1;
        switch (type) {
            case 0: {//Sin Ram
                cont += 0.1;
                return Ram[d];
            }
            case 1: {//Correspondecia Directa
                int j = Math.floorDiv(d, 8);
                int ld = j % 32;
                if (!Validate[ld]) {
                    Validate[ld] = true;
                    Modified[ld] = false;
                    for (int k = 0; k < 8; k++) { //Mover de RAM a Cache
                        CacheData[ld][k] = Ram[j * 8 + k];
                    }
                    Block[ld] = j; //wtf? adonde
                    cont += 0.11;
                } else if (Block[ld] != j) { //Otra vez. ADONDE???
                    if (Modified[ld]) {
                        for (int k = 0; k < 8; k++) { //Mover de Cache a RAM
                            Ram[j * 8 + k] = CacheData[ld][k];
                        }
                        Modified[ld] = false;
                        cont += 0.22;
                    } else {
                        cont += 0.11;
                    }
                    for (int k = 0; k < 8; k++) { //Mover de RAM a Cache
                        CacheData[ld][k] = Ram[j * 8 + k];
                    }
                    Block[ld] = j; //Im done.
                } else {
                    cont += 0.01;
                }
                valor = CacheData[ld][d % 8];
                break;
            }
            case 2: {//Correspondecia Asociativa
                int line = -1;//-1 no existe
                int lineNotExists = 0;//-1 no existe
                int menor = LFU[0];
                for (int i = 0; i < CacheData.length; i++) {
                    if (Validate[i]) {
                        if (Tag[i] == (Integer.rotateRight(d, 3) & 4095)) {
                            line = i;
                        }
                    }
                    if (menor > LFU[i]) {
                        menor = LFU[i];
                        lineNotExists = i;
                    }
                }
                if (line == -1) {
                    line = lineNotExists;
                    moverRam_Cache(line, d);
                    Validate[line] = true;
                    Tag[line] = (Integer.rotateRight(d, 3) & 4095);
                    cont += 0.1;
                }
                cont += 0.01;
                LFU[line]++;
                return CacheData[line][d & 7];//d&7 son solo los ultimos 3 bits la direccion
                //Integer.rotateRight(2777, 3)&4095 //dis gets the value of the tag
            }
            case 3: {//Correspondecia Asociativa por Conjuntos
                int InnerBlock, InnerTag, InnerSet, InnerWord;
                InnerBlock = Integer.rotateRight(d, 3) & 511;//9 bits para el bloque, el bloque es la etiqueta y el conjunto merged
                InnerTag = Integer.rotateRight(d, 5) & 127;//7 bits para la etiqueta
                InnerSet = Integer.rotateRight(d, 3) & 3;//3 bits para el conjunto
                InnerWord = d & 7;//2 bits para la palabra
                if (Validate[InnerTag / 4]) {
                    if (Tag[InnerTag / 4] != InnerTag) {
                        if (Modified[InnerTag / 4]) {//Para saber donde empieza el bloque se divide la direccion entre 8 y se multiplica por ocho en un int de java para que solo agarre la parte entera de la division
                            for (int i = 0; i < 8; i++) {
                                
                                cont += 0.11;
                            }
                            Modified[InnerTag] = false;
                        }
                        for (int i = 0; i < 8; i++) {
                            
                            cont += 0.11;
                        }
                    }
                } else {
                    for (int i = 0; i < 8; i++) {
                        
                        cont += 0.11;
                    }
                }
                cont += 0.01;
                return CacheData[InnerTag / 4][InnerWord];
            }
        }
        return valor;
    }

    public static void escribir(int d, int type, int data) {
        switch (type) {
            case 0: {//Sin Ram
                Ram[d] = data;
                cont += 0.1;
                break;
            }
            case 1: {//Correspondecia Directa
                int j = Math.floorDiv(d, 8);
                int ld = j % 32;
                if (!Validate[ld]) {
                    Validate[ld] = true;
                    for (int k = 0; k < 8; k++) { //Mover de RAM a Cache
                        CacheData[ld][k] = Ram[j * 8 + k];
                    }
                    Block[ld] = j;
                    cont += 0.11;
                } else if (Block[ld] != j) {
                    if (Modified[ld]) {
                        for (int k = 0; k < 8; k++) { //Mover de Cache a RAM
                            Ram[j * 8 + k] = CacheData[ld][k];
                        }
                        cont += 0.22;
                    } else {
                        cont += 0.11;
                    }
                    for (int k = 0; k < 8; k++) { //Mover de RAM a Cache
                        CacheData[ld][k] = Ram[j * 8 + k];
                    }
                    Block[ld] = j;
                } else {
                    cont += 0.01;
                }
                Modified[ld] = true;
                CacheData[ld][d % 8] = data;
                break;
            }
            case 2: {//Correspondecia Asociativa
                int line = -1;//-1 no existe
                int lineNotExists = 0;//-1 no existe
                int menor = LFU[0];
                for (int i = 0; i < CacheData.length; i++) {
                    if (Validate[i]) {
                        if (Tag[i] == (Integer.rotateRight(d, 3) & 4095)) {
                            line = i;
                        }
                    }
                    if (menor > LFU[i]) {
                        menor = LFU[i];
                        lineNotExists = i;
                    }
                }
                if (line == -1) {
                    line = lineNotExists;
                    Validate[line] = true;
                    Tag[line] = (Integer.rotateRight(d, 3) & 4095);
                    moverRam_Cache(line, d);
                    cont += 0.1;
                }
                cont += 0.01;
                CacheData[line][d & 7] = data;//d&7 son solo los ultimos 3 bits la direccion
                cont += 0.11;
                moverCache_Ram(line, d);
                LFU[line]++;
            }
            case 3: {//Correspondecia Asociativa por Conjuntos

                break;
            }
        }
    }

    //pero antes hay que pasarlo de la RAM a la cache
    public static void moverRam_Cache(int line, int d) {
        //System.out.println(line);
        for (int i = 0; i < CacheData[line].length; i++) {
            CacheData[line][i] = Ram[d + i];
        }
    }

    //y luego de la caché a la RAM
    public static void moverCache_Ram(int line, int d) {
        Ram[d] = CacheData[line][d & 7];//d&7 son solo los ultimos 3 bits la direccion
    }
}
