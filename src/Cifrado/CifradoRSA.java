package Cifrado;

import javax.crypto.Cipher;
import javax.crypto.ExemptionMechanismException;
import java.security.*;
import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;


public class CifradoRSA {

    public static KeyPair generarClaves() throws Exception {
        KeyPairGenerator generadorClaves = KeyPairGenerator.getInstance("RSA");

        //Tamaño de la clave
        generadorClaves.initialize(2048);

        //Devuelve claves publica y privada
        return generadorClaves.generateKeyPair();
    }

    public static String cifrar(String mensaje, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");

        //Cifrar
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);

        //Conversión de mensaje a bytes y cifrado
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes("UTF-8"));

        //Convertir a texto
        return Base64.getEncoder().encodeToString(mensajeCifrado);
    }

    public static String descrifrarClaves (String mensajeCifrado, PrivateKey clavePrivada) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");

        //Descifrar clave
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);

        //Recuperar los bytes cifrados
        byte[] mensajeBytes = cipher.doFinal(Base64.getDecoder().decode(mensajeCifrado));

        //Devolución de bytes a texto legible
        return new String (mensajeBytes, "UTF-8");
    }

    //Enviar la clave publica por la red

    public static String clavePublicaAString(PublicKey clavePublica) {
        return Base64.getEncoder().encodeToString(clavePublica.getEncoded());
    }

    public static PublicKey stringAClavePublica(String claveStr) throws Exception {
        byte[] claveBytes = Base64.getDecoder().decode(claveStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(claveBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}
