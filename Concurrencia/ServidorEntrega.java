package Concurrencia;
public class ServidorEntrega extends Thread {
    private final String nombre;
    private final BuzonEntrega buzonEntrega;

    private static boolean finalizado = false;

    public ServidorEntrega(String nombre, BuzonEntrega entrega) {
        this.nombre = nombre;
        this.buzonEntrega = entrega;
    }

    @Override
    public void run() {
        try {
            while (!finalizado) {
                //espera activa
                while (buzonEntrega.estaVacio() && !finalizado) {

                }
                Mensaje m = buzonEntrega.extraer();

                if (m != null) {
                    if (m.tipo == TipoMensaje.FIN) {
                        System.out.println(nombre + " recibió mensaje FIN. Finaliza y avisa a los demás servidores.");
                        ServidorEntrega.finalizado = true;
                    }

                    else {
                        System.out.println(nombre + " procesando " + m.id);
                        Thread.sleep((int) (Math.random() * 1000 + 500)); 

                    }
                }
            }
            System.out.println(nombre + " finalizado.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
