package org.example;

import java.text.Normalizer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class VigenereCipher {

    private final String alphabet;
    private final Map<Character, Integer> index;
    private final boolean foldCase;

    public VigenereCipher(String alphabet) {
        if (alphabet == null || alphabet.isEmpty()) {
            throw new IllegalArgumentException("El alfabeto no puede estar vacio");
        }
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            Integer prev = map.putIfAbsent(c, i);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Caracter duplicado en el alfabeto: '" + c + "' en posiciones " + prev + " y " + i);
            }
        }
        if (map.size() < 2) {
            throw new IllegalArgumentException("El alfabeto debe tener al menos 2 simbolos");
        }
        this.alphabet = alphabet;
        this.index = Collections.unmodifiableMap(map);
        boolean mayus = false;
        boolean minus = false;
        for (char c : map.keySet()) {
            if (Character.isUpperCase(c)) mayus = true;
            if (Character.isLowerCase(c)) minus = true;
        }
        this.foldCase = !(mayus && minus);
    }

    public String alphabet() {
        return alphabet;
    }

    public int size() {
        return alphabet.length();
    }

    public boolean contains(char c) {
        return index.containsKey(c);
    }

    public char charAt(int i) {
        return alphabet.charAt(Math.floorMod(i, alphabet.length()));
    }

    public Integer indiceDe(char c) {
        Integer i = index.get(c);
        if (i != null) {
            return i;
        }
        if (foldCase) {
            char alt = Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
            i = index.get(alt);
            if (i != null) {
                return i;
            }
        }
        Character base = sinAcento(c);
        if (base != null) {
            i = index.get(base);
            if (i != null) {
                return i;
            }
            if (foldCase) {
                char alt = Character.isUpperCase(base) ? Character.toLowerCase(base) : Character.toUpperCase(base);
                i = index.get(alt);
                if (i != null) {
                    return i;
                }
            }
        }
        return null;
    }

    public String sanitize(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(texto.length());
        for (int i = 0; i < texto.length(); i++) {
            Integer idx = indiceDe(texto.charAt(i));
            if (idx != null) {
                sb.append(alphabet.charAt(idx));
            }
        }
        return sb.toString();
    }

    public int[] clavesIndices(String clave) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser nula");
        }
        String k = clave.trim();
        if (k.isEmpty()) {
            throw new IllegalArgumentException("La clave no puede estar vacia");
        }
        int[] res = new int[k.length()];
        for (int i = 0; i < k.length(); i++) {
            char c = k.charAt(i);
            Integer idx = index.get(c);
            if (idx == null) {
                throw new IllegalArgumentException("Caracter de la clave fuera del alfabeto: '" + c + "'");
            }
            res[i] = idx;
        }
        return res;
    }

    public String encrypt(String texto, String clave) {
        return desplazar(texto, clave, +1);
    }

    public String decrypt(String texto, String clave) {
        return desplazar(texto, clave, -1);
    }

    private String desplazar(String texto, String clave, int dir) {
        if (texto == null) {
            return "";
        }
        int[] k = clavesIndices(clave);
        int n = alphabet.length();
        StringBuilder sb = new StringBuilder(texto.length());
        int pos = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            Integer pi = indiceDe(c);
            if (pi == null) {
                sb.append(c);
                continue;
            }
            int ki = k[pos % k.length];
            pos++;
            sb.append(alphabet.charAt(Math.floorMod(pi + dir * ki, n)));
        }
        return sb.toString();
    }

    private static Character sinAcento(char c) {
        String descompuesto = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
        if (descompuesto.length() < 2) {
            return null;
        }
        char base = descompuesto.charAt(0);
        char marca = descompuesto.charAt(1);
        if (base == 'n' && marca == '\u0303') {
            return null;
        }
        return base;
    }
}
