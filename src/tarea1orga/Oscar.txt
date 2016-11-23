package def;
public class main {
    static int RAM[];
    static int Cache[];
    static int CacheBlock[];
    static boolean CacheValid[];
    static boolean CacheModified[];
    static int tiempos[]=new int[4]; //tiempios en microsegundos*100 
    static int r=8; //palabras por bloque
    static int c; //numero de bloques en cache
    public static void main(String[] args) {
        int n=12; //bits de dirección
        int RAMsize=(int) Math.pow(2,n); //tamaño de RAM
        RAM=new int[RAMsize];
        int m=RAMsize/r; //numero de bloques en memoria principal
        int q=8; //bits de cache
        int CacheSize=(int) Math.pow(2,q); //tamaño de cache
        Cache=new int[CacheSize];
        c=CacheSize/r; //numero de bloques en cache
        CacheBlock=new int[c];
        CacheValid=new boolean[c];
        CacheModified=new boolean[c];
        System.out.println("RAM: "+RAMsize+"B (n="+n+")");
        System.out.println("Cache: "+CacheSize+"B (q="+q+")");
        int temp;
        int num=4096;
        for(int tipo=0; tipo<=3; tipo++){
            RAM=new int[RAMsize];
            Cache=new int[CacheSize];
            CacheBlock=new int[c];
            CacheValid=new boolean[c];
            CacheModified=new boolean[c];
            //TODO leer de archivo
            escribir(100,tipo,10);    //En la memoria 100 escribe un 10
            escribir(101,tipo,13);
            escribir(102,tipo,21);
            escribir(103,tipo,11);
            escribir(104,tipo,67);
            escribir(105,tipo,43);
            escribir(106,tipo,9);
            escribir(107,tipo,11);
            escribir(108,tipo,19);
            escribir(109,tipo,23);
            escribir(110,tipo,32);
            escribir(111,tipo,54);
            escribir(112,tipo,98);
            escribir(113,tipo,7);
            escribir(114,tipo,13);
            escribir(115,tipo,1);
            int menor=leer(100,tipo);
            int mayor=menor;
            int a=0;
            for(int i=101;i<=115;i++){
               a++;
               escribir(615,tipo,a);
               if (leer(i,tipo)<menor){
                   menor=leer(i,tipo);
               }
               if (leer(i,tipo)>mayor){
                   mayor=leer(i,tipo);
               }
            }
            /*for(int i=0; i<num-1; i++){
                for(int j=i+1; j<num; j++){
                    if(leer(i,tipo)>leer(j,tipo)){
                        temp=leer(i,tipo);
                        escribir(i,tipo,leer(j,tipo));
                        escribir(j,tipo,temp);
                    }
                }
            }*/
        }
        System.out.println(" ----------------------------------------------");
        System.out.println("|           Tipo           | Tiempo de Corrida");
        System.out.println(" --------------------------+-------------------");
        System.out.println("|        Sin Cache         | "+tiempos[0]*1.0/100.0+" us");
        System.out.println("|         Directo          | "+tiempos[1]*1.0/200.0+" us");
        System.out.println("|        Asociativo        | "+tiempos[2]*1.0/400.0+" us");
        System.out.println("| Asociativo por Conjuntos | "+tiempos[3]*1.0/800.0+" us");
        System.out.println(" ----------------------------------------------");
    }
    
    public static int leer(int i,int tipo){
        int valor=0;
        switch(tipo){
            case 0:default:{ //Sin Cache
                
            };
            case 1:{ //Directo
                int j=Math.floorDiv(i,r);
                int ld=j % c;
                if(!CacheValid[ld]){
                    CacheValid[ld]=true;
                    CacheModified[ld]=false;
                    for(int k=0; k<r; k++){ //Mover de RAM a Cache
                        Cache[ld*r+k]=RAM[j*r+k];
                    }
                    CacheBlock[ld]=j;
                    tiempos[1]=tiempos[1]+11;
                }else{
                    if(CacheBlock[ld]!=j){
                        if(CacheModified[ld]){
                            for(int k=0; k<r; k++){ //Mover de Cache a RAM
                                RAM[j*r+k]=Cache[ld*r+k];
                            }
                            CacheModified[ld]=false;
                            tiempos[1]=tiempos[1]+22;
                        }else{
                            tiempos[1]=tiempos[1]+11;
                        }
                        for(int k=0; k<r; k++){ //Mover de RAM a Cache
                            Cache[ld*r+k]=RAM[j*r+k];
                        }
                        CacheBlock[ld]=j;
                    }else{
                        tiempos[1]=tiempos[1]+1;
                    }
                }
                valor=Cache[ld*r+(i%r)];
            };
            case 2:{ //Asociativo
                
            };
            case 3:{ //Asociativo por Conjuntos
                
            };
        }
        return valor;
    }
    
    public static void escribir(int i, int tipo, int valor){
        switch(tipo){
            case 0:default:{ //Sin Cache
                
            };
            case 1:{ //Directo
                int j=Math.floorDiv(i,r);
                int ld=j % c;
                if(!CacheValid[ld]){
                    CacheValid[ld]=true;
                    for(int k=0; k<r; k++){ //Mover de RAM a Cache
                        Cache[ld*r+k]=RAM[j*r+k];
                    }
                    CacheBlock[ld]=j;
                    tiempos[1]=tiempos[1]+11;
                }else{
                    if(CacheBlock[ld]!=j){
                        if(CacheModified[ld]){
                            for(int k=0; k<r; k++){ //Mover de Cache a RAM
                                RAM[j*r+k]=Cache[ld*r+k];
                            }
                            tiempos[1]=tiempos[1]+22;
                        }else{
                            tiempos[1]=tiempos[1]+11;
                        }
                        for(int k=0; k<r; k++){ //Mover de RAM a Cache
                            Cache[ld*r+k]=RAM[j*r+k];
                        }
                        CacheBlock[ld]=j;
                    }else{
                        tiempos[1]=tiempos[1]+1;
                    }
                }
                CacheModified[ld]=true;
                Cache[ld*r+(i%r)]=valor;
            };
            case 2:{ //Asociativo
                
            };
            case 3:{ //Asociativo por Conjuntos
                
            };
        }
    }
}
