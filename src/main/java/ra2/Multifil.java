package ra2;

public class Multifil {

    public static void main(String[] args) {

        System.out.println("=== RA2 - Ejecución Multifil ==="); // Título descriptivo

        // Aqui creamos varios hilos definiendo el retraso que tendrá cada uno (delay):
        Thread t1 = new Worker("Thread-Alpha", 300);
        Thread t2 = new Worker("Thread-Beta", 500);
        Thread t3 = new Worker("Thread-Gamma", 700);


        //Arrancamos los hilos:
        t1.start();
        t2.start();
        t3.start();

        // Mensaje para indicar que los hilos han sido iniciados:
        System.out.println("Hilos iniciados! (Observa la concurrencia)");
    }
}

// Definición de la clase Worker que extiende Thread:
class Worker extends Thread {
// Atributos para el nombre del hilo y el retraso entre pasos:
    private String nombre;
    private int delay;

// Constructor para inicializar los atributos:
    public Worker(String nombre, int delay) {
        this.nombre = nombre;
        this.delay = delay;
    }

    //aqui definimos el comportamiento del hilo:
    @Override
    public void run() {
        try {
            for(int i = 1; i <= 5; i++) {
                System.out.println(nombre + " -> Paso " + i);//estamos imprimiendo el nombre del hilo y el paso actual
                Thread.sleep(delay);//usamos sleep para simular que el hilo tarda un tiempo en completar cada paso
            }
            System.out.println(nombre + " ha terminado.");//mensaje indicando que el hilo ha completado su tarea
        } catch (InterruptedException e) {
            System.err.println("Error en el hilo " + nombre);//mensaje de error en caso de interrupción
        }
    }
}
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Multifil {

    public static void main(String[] args) {
        System.out.println("== SecureChat: Motor Multifil RA2 ==\n");

        // Buffer compartido entre productores y consumidor
        MessageBuffer buffer = new MessageBuffer();

        // Pool de 5 hilos (4 clientes + 1 procesador)
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Iniciamos el procesador (consume mensajes)
        executor.submit(() -> {
            try {
                while (true) {
                    String msg = buffer.take();
                    System.out.println(timeNow() + " [PROCESSADOR] Processant -> " + msg);
                    Thread.sleep(300);
                }
            } catch (Exception e) { e.printStackTrace(); }
        });

        // Iniciamos clientes simulados (producen mensajes)
        for (int i = 1; i <= 4; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
                        String msg = "Missatge " + j + " de Client-" + id;
                        System.out.println(timeNow() + " [CLIENT] Enviant -> " + msg);
                        buffer.put(msg);
                        Thread.sleep(500);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            });
        }

        // Cerramos el executor cuando terminen las tareas
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS); // Espera a terminar
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n== Fi del motor RA2 ==");
    }

    // Mostrar hora del sistema (requisit de les captures)
    private static String timeNow() {
        return java.time.LocalTime.now().toString();
    }
}
