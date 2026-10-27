package org.example;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class VigenereAnalyzer {

    public record Candidato(char caracter, int desplazamiento, double chi2)
            implements Comparable<Candidato> {

        @Override
        public int compareTo(Candidato otro) {
            return Double.compare(chi2, otro.chi2);
        }
    }

    private static final double[] LETRAS = {
            11.52, 2.21, 4.02, 5.01, 12.18, 0.69, 1.76, 1.12, 6.25, 0.44,
            0.01, 4.97, 3.15, 6.71, 8.68, 2.51, 0.88, 6.87, 7.98, 4.63,
            2.88, 1.09, 0.06, 0.54, 1.06, 0.47
    };

    private static final Map<Character, Double> FREC_ES = new LinkedHashMap<>();

    static {
        for (int i = 0; i < LETRAS.length; i++) {
            FREC_ES.put((char) ('a' + i), LETRAS[i]);
        }
        FREC_ES.put('ñ', 0.31);
        FREC_ES.put('á', 0.44);
        FREC_ES.put('é', 0.36);
        FREC_ES.put('í', 0.44);
        FREC_ES.put('ó', 0.47);
        FREC_ES.put('ú', 0.15);
        FREC_ES.put('ü', 0.01);
    }

    private final VigenereCipher cipher;
    private final double[] esperado;

    public VigenereAnalyzer(VigenereCipher cipher) {
        if (cipher == null) {
            throw new IllegalArgumentException("Cifrador nulo");
        }
        this.cipher = cipher;
        this.esperado = construirEsperado();
    }

    public double ic(String texto) {
        int[] conteos = contar(texto);
        long n = 0;
        for (int c : conteos) {
            n += c;
        }
        if (n < 2) {
            return 0.0;
        }
        long pares = 0;
        for (int c : conteos) {
            pares += (long) c * (c - 1);
        }
        return pares / (double) (n * (n - 1));
    }

    public double[] icPorLongitud(String texto, int maxL) {
        if (maxL < 1) {
            throw new IllegalArgumentException("maxL debe ser >= 1");
        }
        int[] idx = indices(texto);
        double[] res = new double[maxL + 1];
        for (int L = 1; L <= maxL; L++) {
            double suma = 0.0;
            int columnas = 0;
            for (int j = 0; j < L; j++) {
                int[] conteos = new int[cipher.size()];
                int n = 0;
                for (int p = j; p < idx.length; p += L) {
                    conteos[idx[p]]++;
                    n++;
                }
                if (n >= 2) {
                    suma += icDesdeConteos(conteos, n);
                    columnas++;
                }
            }
            res[L] = columnas == 0 ? 0.0 : suma / columnas;
        }
        return res;
    }

    public List<Integer> picosLocales(double[] ic) {
        int maxL = ic.length - 1;
        List<Integer> picos = new ArrayList<>();
        for (int L = 1; L <= maxL; L++) {
            double izq = L == 1 ? -1.0 : ic[L - 1];
            double der = L == maxL ? -1.0 : ic[L + 1];
            if (ic[L] >= izq && ic[L] >= der && (ic[L] > izq || ic[L] > der)) {
                picos.add(L);
            }
        }
        return picos;
    }

    public int detectarLongitud(double[] ic) {
        int maxL = ic.length - 1;
        double mejor = 0.0;
        for (int L = 1; L <= maxL; L++) {
            mejor = Math.max(mejor, ic[L]);
        }
        List<Integer> picos = picosLocales(ic);
        int argmax = 1;
        for (int L = 1; L <= maxL; L++) {
            if (ic[L] > ic[argmax]) {
                argmax = L;
            }
        }
        int elegido = -1;
        for (int L = 1; L <= maxL; L++) {
            if (picos.contains(L) && ic[L] >= 0.75 * mejor) {
                elegido = L;
                break;
            }
        }
        return elegido == -1 ? argmax : elegido;
    }

    public List<List<Candidato>> candidatosPorColumna(String texto, int L, int topK) {
        if (L < 1) {
            throw new IllegalArgumentException("L debe ser >= 1");
        }
        int[] idx = indices(texto);
        int n = cipher.size();
        List<List<Candidato>> res = new ArrayList<>();
        for (int j = 0; j < L; j++) {
            int[] obs = new int[n];
            for (int p = j; p < idx.length; p += L) {
                obs[idx[p]]++;
            }
            List<Candidato> cands = new ArrayList<>(n);
            for (int k = 0; k < n; k++) {
                cands.add(new Candidato(cipher.charAt(k), k, chi2(obs, k)));
            }
            cands.sort(null);
            res.add(new ArrayList<>(cands.subList(0, Math.min(topK, cands.size()))));
        }
        return res;
    }

    public String recuperarClave(String texto, int L) {
        List<List<Candidato>> cands = candidatosPorColumna(texto, L, 1);
        StringBuilder sb = new StringBuilder(cands.size());
        for (List<Candidato> columna : cands) {
            sb.append(columna.get(0).caracter());
        }
        return sb.toString();
    }

    public double[] esperadas() {
        return esperado.clone();
    }

    private double chi2(int[] obs, int desplazamiento) {
        int n = cipher.size();
        double total = 0.0;
        for (int p = 0; p < n; p++) {
            if (esperado[p] > 0.0) {
                total += obs[Math.floorMod(p + desplazamiento, n)];
            }
        }
        if (total <= 0.0) {
            return Double.MAX_VALUE;
        }
        double estadistico = 0.0;
        for (int p = 0; p < n; p++) {
            double e = esperado[p] * total;
            if (e <= 0.0) {
                continue;
            }
            double d = obs[Math.floorMod(p + desplazamiento, n)] - e;
            estadistico += d * d / e;
        }
        return estadistico;
    }

    private double icDesdeConteos(int[] conteos, long n) {
        long pares = 0;
        for (int c : conteos) {
            pares += (long) c * (c - 1);
        }
        return pares / (double) (n * (n - 1));
    }

    private int[] indices(String texto) {
        String limpio = cipher.sanitize(texto);
        int[] res = new int[limpio.length()];
        for (int i = 0; i < limpio.length(); i++) {
            res[i] = cipher.indiceDe(limpio.charAt(i));
        }
        return res;
    }

    private int[] contar(String texto) {
        int[] conteos = new int[cipher.size()];
        String limpio = cipher.sanitize(texto);
        for (int i = 0; i < limpio.length(); i++) {
            conteos[cipher.indiceDe(limpio.charAt(i))]++;
        }
        return conteos;
    }

    private double[] construirEsperado() {
        int n = cipher.size();
        double[] f = new double[n];
        double total = 0.0;
        for (int i = 0; i < n; i++) {
            Double v = frecuencia(cipher.charAt(i));
            if (v != null) {
                f[i] = v;
                total += v;
            }
        }
        if (total <= 0.0) {
            throw new IllegalArgumentException(
                    "El alfabeto no contiene letras con frecuencias modelables en espanol: " + cipher.alphabet());
        }
        for (int i = 0; i < n; i++) {
            f[i] = f[i] / total;
        }
        return f;
    }

    private static Double frecuencia(char c) {
        Character key = Character.toLowerCase(c);
        Double v = FREC_ES.get(key);
        if (v != null) {
            return v;
        }
        String descompuesto = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
        if (descompuesto.length() >= 2) {
            v = FREC_ES.get(Character.toLowerCase(descompuesto.charAt(0)));
            if (v != null) {
                return v;
            }
        }
        return null;
    }
}
