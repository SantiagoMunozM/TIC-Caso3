import java.util.LinkedList;
import java.util.Queue;

public class Buzon {
    private final Queue<Mensaje> cola = new LinkedList<>();
    private final int capacidad; 

    public Buzon(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Mensaje m) throws InterruptedException {
        while (capacidad > 0 && cola.size() >= capacidad) {
            wait(); // Espera pasiva
        }
        cola.add(m);
        notifyAll();
    }

    public synchronized Mensaje extraer() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // Espera pasiva
        }
        Mensaje m = cola.poll();
        notifyAll();
        return m;
    }

    public synchronized boolean estaVacio() {
        return cola.isEmpty();
    }

    public synchronized int tamanio() {
        return cola.size();
    }

    public synchronized void esperarHastaVacio() throws InterruptedException {
        while (!cola.isEmpty()) {
            wait();
        }
    }

}
