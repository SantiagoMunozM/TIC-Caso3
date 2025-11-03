package Concurrencia;
import java.util.ArrayList;
import java.util.List;

public class MainSimulador {
    public static void main(String[] args) throws Exception {
        String rutaConfig = "config.txt";
        if (args.length > 0) {
            rutaConfig = args[0];
        }

        Configuracion cfg = Configuracion.cargar(rutaConfig);
        System.out.println("Configuracion cargada: " + cfg);

        BuzonEntrada entrada = new BuzonEntrada(cfg.capacidadBuzonEntrada);
        BuzonCuarentena cuarentena = new BuzonCuarentena();
        BuzonEntrega entrega = new BuzonEntrega(cfg.capacidadBuzonEntrega);

        List<Thread> hilos = new ArrayList<>();

        // Clientes
        for (int i = 1; i <= cfg.numeroClientes; i++) {
            hilos.add(new ClienteEmisor("Cliente" + i, entrada, cfg.numeroMensajesPorCliente));
        }

        // Filtros
        for (int i = 1; i <= cfg.numeroFiltros; i++) {
            hilos.add(new FiltroSpam(entrada, entrega, cuarentena, i));
        }

        // Manejador de cuarentena
        hilos.add(new ManejadorCuarentena(cuarentena, entrega));

        // Servidores
        for (int i = 1; i <= cfg.numeroServidores; i++) {
            hilos.add(new ServidorEntrega("Servidor" + i, entrega));
        }

        // Iniciar y esperar a todos
        for (Thread t : hilos) t.start();
        for (Thread t : hilos) t.join();

        System.out.println("=== Sistema Finalizado: todos los buzones vacios ===");
    }
}
