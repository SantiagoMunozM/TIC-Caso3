package Concurrencia;
import java.util.Random;

public class ManejadorCuarentena extends Thread {
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    public ManejadorCuarentena(BuzonCuarentena cuarentena, BuzonEntrega entrega) {
        this.buzonCuarentena = cuarentena;
        this.buzonEntrega = entrega;
    }

    @Override
    public void run() {
        try {
            boolean finalizado = false;
            while (!finalizado) {

                while (buzonCuarentena.estaVacio()) {
                    Thread.yield();
                }
                Mensaje m = buzonCuarentena.extraer();

                if (m.tipo == TipoMensaje.FIN) {
                    //solo acabamos si hemos recibido fin y si el buzon de cuarentena esta vacio
                    if (buzonCuarentena.estaVacio()) {
                        System.out.println("Manejador de cuarentena finalizo");
                        finalizado = true;
                    }

                    else {
                        buzonCuarentena.depositar(m);
                    }
                    
                }

                else {
                        
                    m.tiempoCuarentena -= 1000;
                    int azar = 1 + new Random().nextInt(21);
                    boolean descartado = azar % 7 == 0;
                    //si es descartado imprimimos esto y el mensaje se pierde
                    if (descartado) {
                        System.out.println("Mensaje malicioso descartado: " + m.id);
                    }
                    
                    else if (!descartado) {
                        
                        if (m.tiempoCuarentena< 0) {
                            // si no esta descartado y su tiempo ya se cumplio se pone en el buzon de entrega
                            // espera semiactiva
                            while (buzonEntrega.estaLleno()) {
                                Thread.yield();
                            }
                            buzonEntrega.depositar(m);
                            System.out.println("Cuarentena libero: " + m.id);
                        }
                        else {
                            //no se ha cumplido el tiempo, entonces se devuelve a la cola
                            buzonCuarentena.depositar(m);
                        }
                    }
                    Thread.sleep(1000);
                }

                
            }


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
