import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configuracion {
    public int numeroClientes;
    public int numeroMensajesPorCliente;
    public int numeroFiltros;
    public int numeroServidores;
    public int capacidadBuzonEntrada;
    public int capacidadBuzonEntrega;

    public static Configuracion cargar(String rutaArchivo) throws FileNotFoundException {
        
        Configuracion c = new Configuracion();
        File archivo = new File(rutaArchivo);
        Scanner sc = new Scanner(archivo);

        while (sc.hasNextLine()) {
            String linea = sc.nextLine().trim();

            if (linea.isEmpty()) {
                continue;
            }

            String[] partes = linea.split("=");
            if (partes.length != 2) {
                continue;
            }

            String clave = partes[0].trim();
            String valor = partes[1].trim();

            switch (clave) {
                case "numeroClientes":
                    c.numeroClientes = Integer.parseInt(valor);
                    break;
                case "numeroMensajesPorCliente":
                    c.numeroMensajesPorCliente = Integer.parseInt(valor);
                    break;
                case "numeroFiltros":
                    c.numeroFiltros = Integer.parseInt(valor);
                    break;
                case "numeroServidores":
                    c.numeroServidores = Integer.parseInt(valor);
                    break;
                case "capacidadBuzonEntrada":
                    c.capacidadBuzonEntrada = Integer.parseInt(valor);
                    break;
                case "capacidadBuzonEntrega":
                    c.capacidadBuzonEntrega = Integer.parseInt(valor);
                    break;
                default:
                    System.out.println("Entrada Invalida");
                    break;
            }
        }

        sc.close();
        return c;
    }

    @Override
    public String toString() {
        return "Clientes=" + numeroClientes +
                ", Mensajes/Cliente=" + numeroMensajesPorCliente +
                ", Filtros=" + numeroFiltros +
                ", Servidores=" + numeroServidores +
                ", CapacidadEntrada=" + capacidadBuzonEntrada +
                ", CapacidadEntrega=" + capacidadBuzonEntrega;
    }
}
