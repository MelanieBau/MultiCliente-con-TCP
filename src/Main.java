import TCP.ClienteTCP;
import TCP.ServidorTCP;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Leer las entradas
        Scanner sc = new Scanner(System.in);

        //Seleccionables
        System.out.println("=== Chat de Nicole con Cifrado RSA ===");
        System.out.println("1. Iniciar Servidor");
        System.out.println("2. Iniciar Cliente");

        System.out.print("Elige: ");
        int opcion = sc.nextInt();

        if (opcion == 1) {
            ServidorTCP.main(new String[]{});
        } else {
            ClienteTCP.main(new String[]{});
        }
    }
}