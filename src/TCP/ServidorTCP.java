package TCP;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServidorTCP {

    public static void main(String[] args) {
        int puerto = 5050;

        List<GestorCliente> listaClientes = Collections.synchronizedList(new ArrayList<>());

        try {
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Nuevo cliente conectado!");

                GestorCliente gestor = new GestorCliente(socket, listaClientes);
                gestor.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}