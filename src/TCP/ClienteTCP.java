package TCP;

import Cifrado.CifradoRSA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;

public class ClienteTCP {

    public static void main(String[] args) {
        try {
            // Generar claves RSA
            System.out.println("Generando las claves.. tic..tac");
            KeyPair miParDeClaves = CifradoRSA.generarClaves();
            PrivateKey miClavePrivada = miParDeClaves.getPrivate();
            byte[] miClavePublicaBytes = CifradoRSA.clavePublicaABytes(miParDeClaves.getPublic());
            System.out.println("Las claves fueron generadas con exito.\n");

            // Conectar al servidor
            String host = "localhost";
            int puerto = 5050;
            Socket socket = new Socket(host, puerto);
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            // Enviar clave pública al servidor
            salida.writeInt(miClavePublicaBytes.length);
            salida.write(miClavePublicaBytes);
            System.out.println("La clave pública fue enviada al servidor");

            // Introducir nombre de usuario
            String prompt = entrada.readUTF();
            System.out.println(prompt);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            String nombre = teclado.readLine();
            salida.writeUTF(nombre);

            // Hilo receptor
            Thread hiloLectura = new Thread(() -> {
                try {
                    while (true) {
                        int longitud = entrada.readInt();
                        byte[] cifrado = new byte[longitud];
                        entrada.readFully(cifrado);
                        String mensajeEnClaro = CifradoRSA.descifrar(cifrado, miClavePrivada);
                        System.out.println(mensajeEnClaro);
                    }
                } catch (Exception e) {
                    System.out.println("Conexión cerrada por el servidor.");
                }
            });
            hiloLectura.setDaemon(true);
            hiloLectura.start();

            // Bucle de envío
            System.out.println("=== Bienvenido al chat de Nicole. Escribe tus mensajes :) ===\n");
            String mensajeUsuario;
            while ((mensajeUsuario = teclado.readLine()) != null) {
                if (mensajeUsuario.equalsIgnoreCase("salir")) {
                    System.out.println("Saliendo del chat...tic..tac");
                    break;
                }
                salida.writeUTF(mensajeUsuario);
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}