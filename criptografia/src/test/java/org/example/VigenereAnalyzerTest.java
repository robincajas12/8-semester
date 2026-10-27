package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VigenereAnalyzerTest {

    private static final String ALFABETO_37 = "ABCDEFGHIJKLMN\u00D1OPQRSTUVWXYZ0123456789";
    private static String muestra;

    @BeforeAll
    static void cargarMuestra() throws IOException {
        try (InputStream in = VigenereAnalyzerTest.class.getResourceAsStream("/muestra_es.txt")) {
            assertNotNull(in, "Falta el recurso /muestra_es.txt");
            muestra = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void icDelEspanolSuperaAlTextoAleatorio() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double icEspanol = a.ic(muestra);
        Random r = new Random(42);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4000; i++) {
            sb.append(c.charAt(r.nextInt(c.size())));
        }
        double icAleatorio = a.ic(sb.toString());
        assertTrue(icEspanol > 0.05, "IC de espanol demasiado bajo: " + icEspanol);
        assertTrue(icAleatorio < 0.045, "IC aleatorio demasiado alto: " + icAleatorio);
        assertTrue(icEspanol > 1.5 * icAleatorio);
    }

    @Test
    void detectaLongitudDeClaveDeCinco() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        String cifrado = c.encrypt(muestra, "CLAVE");
        double[] ic = a.icPorLongitud(cifrado, 15);
        assertEquals(16, ic.length);
        assertEquals(5, a.detectarLongitud(ic));
        assertTrue(ic[5] > 0.05, "IC(5) = " + ic[5]);
        assertTrue(ic[5] > ic[3]);
        assertTrue(ic[10] > 0.05, "armonico 2L = " + ic[10]);
    }

    @Test
    void recuperaClaveDeOnceSobreAlfabetoExtendido() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        String clave = "ECUADOR2026";
        String cifrado = c.encrypt(muestra, clave);
        double[] ic = a.icPorLongitud(cifrado, 15);
        assertEquals(11, a.detectarLongitud(ic));
        assertEquals(clave, a.recuperarClave(cifrado, 11));
    }

    @Test
    void elRuidoEstructuralNoContaminaElAnalisis() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        String cifrado = c.encrypt(muestra, "SEGURO");
        StringBuilder conRuido = new StringBuilder();
        for (int i = 0; i < cifrado.length(); i++) {
            conRuido.append(cifrado.charAt(i));
            if (i > 0 && i % 37 == 0) {
                conRuido.append("\n\n");
            }
        }
        conRuido.append("####################\n");
        double[] ic = a.icPorLongitud(conRuido.toString(), 15);
        assertEquals(6, a.detectarLongitud(ic));
        assertEquals("SEGURO", a.recuperarClave(conRuido.toString(), 6));
    }

    @Test
    void candidatosPorColumnaEstanOrdenadosPorChi2() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        String cifrado = c.encrypt(muestra, "CLAVE");
        List<List<VigenereAnalyzer.Candidato>> cands = a.candidatosPorColumna(cifrado, 5, 3);
        assertEquals(5, cands.size());
        for (List<VigenereAnalyzer.Candidato> col : cands) {
            assertEquals(3, col.size());
            for (int i = 1; i < col.size(); i++) {
                assertTrue(col.get(i - 1).chi2() <= col.get(i).chi2());
            }
        }
        assertEquals("CLAVE", a.recuperarClave(cifrado, 5));
    }
}
