package ra2;

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
