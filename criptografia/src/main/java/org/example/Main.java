package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class Main {

    private static final int MAX_L = 15;

    public static void main(String[] args) {
        long inicio = System.nanoTime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            System.out.println("=========================================================");
            System.out.println(" CRIPTOANALISIS VIGENERE POLIALFABETICO SOBRE Zn");
            System.out.println("=========================================================");
            System.out.print("Alfabeto Zn (o 'demo' para autochequeo): ");
            String alfabeto = br.readLine();
            if (alfabeto == null || alfabeto.isBlank()) {
                throw new IllegalArgumentException("No se recibio el alfabeto");
            }
            if (alfabeto.trim().equalsIgnoreCase("demo")) {
                Demo.ejecutar(inicio, MAX_L);
                return;
            }

            System.out.println("Criptograma (termine con una linea vacia):");
            StringBuilder criptograma = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null && !linea.isBlank()) {
                criptograma.append(linea).append('\n');
            }
            if (criptograma.toString().trim().isEmpty()) {
                throw new IllegalArgumentException("No se recibio el criptograma");
            }

            AtaqueVigenere ataque = new AtaqueVigenere(alfabeto.trim());
            ReporteConsola.imprimirResultado(ataque.resolver(criptograma.toString(), MAX_L), false);
            ReporteConsola.imprimirTiempo(inicio);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }
}
