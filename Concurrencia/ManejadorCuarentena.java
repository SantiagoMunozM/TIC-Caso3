import java.util.Random;

public class ManejadorCuarentena extends Thread {
    private final Buzon buzonCuarentena;
    private final Buzon buzonEntrega;

    public ManejadorCuarentena(Buzon cuarentena, Buzon entrega) {
        this.buzonCuarentena = cuarentena;
        this.buzonEntrega = entrega;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje m = buzonCuarentena.extraer();

                if (m.tipo == TipoMensaje.FIN) {
                    System.out.println("Manejador de cuarentena finalizo");
                    break;
                }

                // Simular tiempo en cuarentena (espera semiactiva)
                while (m.tiempoCuarentena > 0) {
                    Thread.sleep(1000);
                    m.tiempoCuarentena -= 1000;
                }

                int azar = 1 + new Random().nextInt(21);
                if (azar % 7 == 0) {
                    System.out.println("Mensaje malicioso descartado: " + m.id);
                } else {
                    buzonEntrega.depositar(m);
                    System.out.println("Cuarentena libero: " + m.id);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
