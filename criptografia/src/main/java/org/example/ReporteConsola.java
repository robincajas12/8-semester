package org.example;

import java.util.List;
import java.util.Locale;

public final class ReporteConsola {

    private ReporteConsola() {
    }

    public static void imprimirResultado(AtaqueVigenere.Resultado r, boolean full) {
        imprimirAlfabeto(r.cipher());
        int enAlfabeto = r.limpio().length();
        int total = enAlfabeto + r.ruido();
        System.out.printf(Locale.US, "Criptograma: %d caracteres | en alfabeto: %d | ruido descartado: %d%n",
                total, enAlfabeto, r.ruido());
        imprimirTablaIC(r.ic(), r.cipher().size(), r.longitud());
        imprimirColumnas(r.candidatos(), r.longitud());
        System.out.println();
        System.out.println(">>> PALABRA CLAVE RECUPERADA: " + r.clave());
        System.out.printf(Locale.US,
                ">>> IC del texto descifrado: %.4f (espanol ~0.0750 | aleatorio 1/%d = %.4f)%n",
                r.icPlano(), r.cipher().size(), 1.0 / r.cipher().size());
        imprimirTexto(r.plano(), full);
    }

    public static void imprimirAlfabeto(VigenereCipher cipher) {
        System.out.println();
        System.out.println("ALFABETO ASIGNADO  (anillo Zn, N = " + cipher.size() + ")");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cipher.size(); i++) {
            sb.append(String.format(Locale.US, "[%02d]='%s' ", i, String.valueOf(cipher.charAt(i))));
            if ((i + 1) % 8 == 0) {
                sb.append('\n');
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '\n') {
            sb.append('\n');
        }
        System.out.print(sb);
        System.out.printf(Locale.US, "1/N (IC del texto aleatorio) = %.4f%n", 1.0 / cipher.size());
    }

    public static void imprimirTablaIC(double[] ic, int N, int L) {
        int maxL = ic.length - 1;
        double mejor = 0.0;
        for (int l = 1; l <= maxL; l++) {
            mejor = Math.max(mejor, ic[l]);
        }
        System.out.println();
        System.out.println("TABLA / GRAFICA DEL INDICE DE COINCIDENCIA  IC(L)");
        System.out.println("(IC promedio de las L subcriptogramas; ancho de barra proporcional al maximo)");
        System.out.println("------------------------------------------------------------------------");
        System.out.printf(Locale.US, "%4s %9s   %-58s%n", "L", "IC", "grafica");
        System.out.println("------------------------------------------------------------------------");
        List<Integer> picos = picosDe(ic);
        for (int l = 1; l <= maxL; l++) {
            int barras = mejor > 0 ? (int) Math.round(ic[l] / mejor * 58.0) : 0;
            StringBuilder bar = new StringBuilder();
            for (int b = 0; b < barras; b++) {
                bar.append('#');
            }
            String marca = "";
            if (l == L) {
                marca = "  <== PICO PRINCIPAL";
            } else if (picos.contains(l) && ic[l] >= 0.6 * mejor) {
                marca = "  <- armonico / pico";
            }
            System.out.printf(Locale.US, "%4d %9.4f   %-58s%s%n", l, ic[l], bar.toString(), marca);
        }
        System.out.println("------------------------------------------------------------------------");
        int p = L;
        System.out.println("Pico principal detectado: L = " + p + String.format(Locale.US, "  (IC = %.4f)", ic[p]));
        StringBuilder arm = new StringBuilder("Armonicos esperados del periodo:");
        boolean alguno = false;
        for (int m = 2; p * m <= maxL; m++) {
            arm.append(String.format(Locale.US, " %dL=%d (IC=%.4f)", m, p * m, ic[p * m]));
            alguno = true;
        }
        if (!alguno) {
            arm.append(" ninguno dentro del rango analizado (2L = ").append(2 * p).append(" > ").append(maxL)
                    .append(")");
        }
        System.out.println(arm);
        StringBuilder loc = new StringBuilder("Maximos locales de la curva:");
        for (int pk : picos) {
            String tipo = pk == L ? "principal" : (ic[pk] >= 0.6 * mejor ? "armonico" : "ruido");
            loc.append(String.format(Locale.US, " L=%d (IC=%.4f, %s)", pk, ic[pk], tipo));
        }
        System.out.println(loc);
        System.out.println("------------------------------------------------------------------------");
        System.out.printf(Locale.US, "Referencias: IC espanol ~0.0750 | IC aleatorio 1/%d = %.4f%n", N, 1.0 / N);
        System.out.println("================================================================");
    }

    public static void imprimirColumnas(List<List<VigenereAnalyzer.Candidato>> cands, int L) {
        System.out.println();
        System.out.println("ANALISIS DE COLUMNAS (subcriptogramas) para L = " + L);
        System.out.println("Distancia chi-cuadrado contra la distribucion de frecuencias del espanol");
        System.out.println("------------------------------------------------------------------------");
        System.out.printf(Locale.US, "%4s %6s %9s   %-40s%n", "col", "k", "chi2", "top-3 candidatos");
        System.out.println("------------------------------------------------------------------------");
        for (int j = 0; j < L; j++) {
            List<VigenereAnalyzer.Candidato> top = cands.get(j);
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < top.size(); i++) {
                VigenereAnalyzer.Candidato c = top.get(i);
                if (i > 0) {
                    s.append(", ");
                }
                s.append(String.format(Locale.US, "'%s'=%.1f", c.caracter(), c.chi2()));
            }
            VigenereAnalyzer.Candidato mejor = top.get(0);
            System.out.printf(Locale.US, "%4d %6d %9.1f   %-40s%n", j, mejor.desplazamiento(), mejor.chi2(),
                    s.toString());
        }
        System.out.println("------------------------------------------------------------------------");
    }

    public static void imprimirTexto(String texto, boolean full) {
        String[] lineas = texto.split("\n");
        int mostrar = full ? lineas.length : Math.min(15, lineas.length);
        System.out.println();
        System.out.println("TEXTO DESCIFRADO (primeras lineas)");
        System.out.println("------------------------------------------------------------------------");
        for (int i = 0; i < mostrar; i++) {
            System.out.println(lineas[i]);
        }
        if (mostrar < lineas.length) {
            System.out.println("... (" + (lineas.length - mostrar) + " lineas mas; use --full para verlo todo)");
        }
        System.out.println("------------------------------------------------------------------------");
    }

    public static void imprimirTiempo(long inicio) {
        double seg = (System.nanoTime() - inicio) / 1e9;
        int min = (int) (seg / 60);
        double resto = seg - min * 60;
        System.out.printf(Locale.US, "TIEMPO TOTAL DE RESOLUCION: %d min %.1f s  (%.1f s)%n", min, resto, seg);
    }

    private static List<Integer> picosDe(double[] ic) {
        int maxL = ic.length - 1;
        List<Integer> picos = new java.util.ArrayList<>();
        for (int L = 1; L <= maxL; L++) {
            double izq = L == 1 ? -1.0 : ic[L - 1];
            double der = L == maxL ? -1.0 : ic[L + 1];
            if (ic[L] >= izq && ic[L] >= der && (ic[L] > izq || ic[L] > der)) {
                picos.add(L);
            }
        }
        return picos;
    }
}
