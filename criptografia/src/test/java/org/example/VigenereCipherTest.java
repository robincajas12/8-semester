package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VigenereCipherTest {

    private static final String ALFABETO_37 = "ABCDEFGHIJKLMN\u00D1OPQRSTUVWXYZ0123456789";

    @Test
    void roundTripSobreAlfabetoExtendido() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        assertEquals(37, c.size());
        String texto = "ELMENSAJEDPRUEBAESCORRECTOYDEBEVOLVERSINTILDE2026";
        String cifrado = c.encrypt(texto, "ECUADOR2026");
        assertNotEquals(texto, cifrado);
        assertEquals(texto, c.decrypt(cifrado, "ECUADOR2026"));
    }

    @Test
    void desbordeModularConFloorMod() {
        VigenereCipher c = new VigenereCipher("ABC");
        assertEquals("C", c.encrypt("A", "C"));
        assertEquals("A", c.decrypt("C", "C"));
        assertEquals("AB", c.encrypt("BC", "C"));
        assertEquals("BC", c.decrypt("AB", "C"));
    }

    @Test
    void rechazaAlfabetoConCaracterDuplicado() {
        assertThrows(IllegalArgumentException.class, () -> new VigenereCipher("AABC"));
        assertThrows(IllegalArgumentException.class, () -> new VigenereCipher(""));
        assertThrows(IllegalArgumentException.class, () -> new VigenereCipher(null));
    }

    @Test
    void rechazaClaveFueraDelAlfabeto() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        assertThrows(IllegalArgumentException.class, () -> c.encrypt("HOLA", "*#"));
        assertThrows(IllegalArgumentException.class, () -> c.decrypt("HOLA", "   "));
    }

    @Test
    void elRuidoNoAvanzaLaClaveYSePreserva() {
        VigenereCipher c = new VigenereCipher(ALFABETO_37);
        String texto = "HOLA.MUNDO\nSEGUNDALINEA,CONPUNTOS";
        String cifrado = c.encrypt(texto, "XYZ");
        assertTrue(cifrado.contains("."));
        assertTrue(cifrado.contains("\n"));
        assertTrue(cifrado.contains(","));
        assertEquals(texto, c.decrypt(cifrado, "XYZ"));
    }

    @Test
    void sanitizaMayusculasYVocalesAcentuadas() {
        VigenereCipher minusculas = new VigenereCipher("abcdefghijklmnopqrstuvwxyz");
        assertEquals("cafeyte", minusculas.sanitize("Caf\u00E9 y t\u00E9!"));
        VigenereCipher mixto = new VigenereCipher("aAbBcC");
        assertEquals("aA", mixto.sanitize("aA"));
        assertEquals("ab", minusculas.sanitize("\u00C1b"));
    }

    @Test
    void mapeoEstrictoCuandoElAlfabetoTieneAmbosCasos() {
        VigenereCipher c = new VigenereCipher("aAbB");
        assertEquals(0, c.indiceDe('a'));
        assertEquals(1, c.indiceDe('A'));
        assertEquals(2, c.indiceDe('b'));
        assertEquals(3, c.indiceDe('B'));
        assertEquals(Integer.valueOf(1), c.indiceDe('A'));
        assertEquals(0, c.clavesIndices("ab")[0]);
        assertEquals(2, c.clavesIndices("ab")[1]);
    }

    @Test
    void alfabetoConNumerosYSimbolos() {
        VigenereCipher c = new VigenereCipher("0123456789\u00A1!");
        assertEquals(12, c.size());
        String texto = "10\u00A1!9";
        assertEquals(texto, c.decrypt(c.encrypt(texto, "\u00A1!0"), "\u00A1!0"));
    }
}
