public class ServidorEntrega extends Thread {
    private final String nombre;
    private final Buzon buzonEntrega;

    public ServidorEntrega(String nombre, Buzon entrega) {
        this.nombre = nombre;
        this.buzonEntrega = entrega;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje m = buzonEntrega.extraer();

                if (m.tipo == TipoMensaje.FIN) {
                    System.out.println(nombre + " recibio FIN. Finaliza.");
                    break;
                }

                System.out.println(nombre + " procesando " + m.id);
                Thread.sleep((int) (Math.random() * 1000 + 500)); // espera activa
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
