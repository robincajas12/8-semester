package org.example;

import java.util.List;

public final class AtaqueVigenere {

    public record Resultado(
            VigenereCipher cipher,
            String limpio,
            int ruido,
            double[] ic,
            int longitud,
            List<List<VigenereAnalyzer.Candidato>> candidatos,
            String clave,
            String plano,
            double icPlano) {
    }

    private final VigenereCipher cipher;
    private final VigenereAnalyzer analyzer;

    public AtaqueVigenere(String alfabeto) {
        this.cipher = new VigenereCipher(alfabeto);
        this.analyzer = new VigenereAnalyzer(cipher);
    }

    public VigenereCipher cipher() {
        return cipher;
    }

    public Resultado resolver(String criptograma, int maxL) {
        String limpio = cipher.sanitize(criptograma);
        if (limpio.isEmpty()) {
            throw new IllegalArgumentException("El criptograma no contiene simbolos del alfabeto");
        }
        int ruido = 0;
        for (int i = 0; i < criptograma.length(); i++) {
            if (cipher.indiceDe(criptograma.charAt(i)) == null) {
                ruido++;
            }
        }
        double[] ic = analyzer.icPorLongitud(limpio, maxL);
        int L = analyzer.detectarLongitud(ic);
        List<List<VigenereAnalyzer.Candidato>> cands = analyzer.candidatosPorColumna(limpio, L, 3);
        StringBuilder kb = new StringBuilder();
        for (List<VigenereAnalyzer.Candidato> col : cands) {
            kb.append(col.get(0).caracter());
        }
        String clave = kb.toString();
        String plano = cipher.decrypt(criptograma, clave);
        return new Resultado(cipher, limpio, ruido, ic, L, cands, clave, plano, analyzer.ic(plano));
    }

    public Resultado descifrarConClave(String criptograma, String clave) {
        String plano = cipher.decrypt(criptograma, clave);
        return new Resultado(cipher, cipher.sanitize(criptograma), 0,
                new double[0], 0, List.of(), clave, plano, analyzer.ic(plano));
    }
}
