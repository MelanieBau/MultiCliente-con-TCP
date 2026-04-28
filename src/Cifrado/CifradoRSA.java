package Cifrado;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class CifradoRSA {

    public static KeyPair generarClaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        return generador.generateKeyPair();
    }

    public static byte[] cifrar(String mensaje, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cipher.doFinal(mensaje.getBytes("UTF-8"));
    }

    public static String descifrar(byte[] mensajeCifrado, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        return new String(cipher.doFinal(mensajeCifrado), "UTF-8");
    }

    public static byte[] clavePublicaABytes(PublicKey clavePublica) {
        return clavePublica.getEncoded();
    }

    public static PublicKey bytesAClavePublica(byte[] claveBytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(claveBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}