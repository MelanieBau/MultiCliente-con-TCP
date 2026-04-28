package TCP;

import Cifrado.CifradoRSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.List;

public class GestorCliente extends Thread {

    private Socket socket;
    private List<GestorCliente> listaClientes;
    private String nombre;
    private DataOutputStream salida;
    private PublicKey clavePublicaCliente;

    public GestorCliente(Socket socket, List<GestorCliente> listaClientes) {
        this.socket = socket;
        this.listaClientes = listaClientes;
    }

    public String getNombre() {
        return nombre;
    }

    public void enviarMensajeCifrado(String mensaje, String remitente) {
        try {
            if (clavePublicaCliente != null && salida != null) {
                byte[] cifrado = CifradoRSA.cifrar("[" + remitente + "]: " + mensaje, clavePublicaCliente);
                salida.writeInt(cifrado.length);
                salida.write(cifrado);
            }
        } catch (Exception e) {
            System.out.println("Error de cifrado para el cliente: " + nombre + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());

            // Recibir la clave pública del cliente
            int longitud = entrada.readInt();
            byte[] claveBytes = new byte[longitud];
            entrada.readFully(claveBytes);
            clavePublicaCliente = CifradoRSA.bytesAClavePublica(claveBytes);
            System.out.println("Clave pública recibida de un cliente.");

            // Pedir nombre de usuario
            salida.writeUTF("Introduce tu nombre de usuario:");
            nombre = entrada.readUTF();
            System.out.println("Cliente conectado: " + nombre);

            // Añadir a la lista de clientes
            synchronized (listaClientes) {
                listaClientes.add(this);
            }

            // Notificar a todos
            difundirATodos(nombre + " se ha unido al chat.", "Servidor");

            // Bucle principal de mensajes
            String mensaje;
            while (!(mensaje = entrada.readUTF()).isEmpty()) {
                System.out.println("[" + nombre + "]: " + mensaje);
                difundirATodos(mensaje, nombre);
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado: " + nombre);
        } finally {
            synchronized (listaClientes) {
                listaClientes.remove(this);
            }
            if (nombre != null) {
                difundirATodos(nombre + " ha salido del chat.", "Servidor");
            }
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    public void difundirATodos(String mensaje, String remitente) {
        synchronized (listaClientes) {
            for (GestorCliente cliente : listaClientes) {
                if (!cliente.getNombre().equals(remitente)) {
                    cliente.enviarMensajeCifrado(mensaje, remitente);
                }
            }
        }
    }
}