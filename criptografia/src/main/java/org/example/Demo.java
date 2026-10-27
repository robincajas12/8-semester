package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

final class Demo {

    private static final String ALFABETO = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
    private static final String CLAVE = "ECUADOR2026";

    static void ejecutar(long inicio, int maxL) throws IOException {
        System.out.println("================================================================");
        System.out.println(" MODO DEMO - AUTOTESTEO CON CRIPTOGRAMA DE MUESTRA");
        System.out.println("================================================================");
        AtaqueVigenere ataque = new AtaqueVigenere(ALFABETO);
        String cifrado = ataque.cipher().encrypt(cargarMuestra(), CLAVE);
        System.out.println("Clave usada para cifrar (solo verificacion): " + CLAVE);
        System.out.println();
        AtaqueVigenere.Resultado r = ataque.resolver(cifrado, maxL);
        ReporteConsola.imprimirResultado(r, false);
        ReporteConsola.imprimirTiempo(inicio);
        boolean claveOk = r.clave().equals(CLAVE);
        boolean textoOk = r.plano().equals(ataque.cipher().decrypt(cifrado, CLAVE));
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println("VERIFICACION AUTOMATICA");
        System.out.println("  clave recuperada == clave usada : " + (claveOk ? "OK" : "FALLO (" + r.clave() + ")"));
        System.out.println("  texto descifrado correcto      : " + (textoOk ? "OK" : "FALLO"));
        if (!claveOk || !textoOk) {
            System.exit(2);
        }
    }

    private static String cargarMuestra() throws IOException {
        try (InputStream in = Demo.class.getResourceAsStream("/muestra_es.txt")) {
            if (in == null) {
                throw new IllegalArgumentException("No se encontro /muestra_es.txt en los recursos");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
