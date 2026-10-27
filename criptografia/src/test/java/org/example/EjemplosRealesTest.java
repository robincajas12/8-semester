package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EjemplosRealesTest {

    private static int argmax(double[] ic) {
        int m = 1;
        for (int L = 1; L < ic.length; L++) {
            if (ic[L] > ic[m]) {
                m = L;
            }
        }
        return m;
    }

    

    private static final String ALFABETO_KRYPTOS = "KRYPTOSABCDEFGHIJLMNQUVWXZ";
    private static final String K1_CIFRADO =
            "EMUFPHZLRFAXYUSDJKZLDKRNSHGNFIVJYQTQUXQBQVYUVLLTREVJYQTMKYRDMFD";
    private static final String K1_CLAVE = "PALIMPSEST";
    private static final String K1_PLANO =
            "BETWEENSUBTLESHADINGANDTHEABSENCEOFLIGHTLIESTHENUANCEOFIQLUSION";

    @Test
    void kryptosK1DescifraConLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_KRYPTOS);
        assertEquals(K1_PLANO, c.decrypt(K1_CIFRADO, K1_CLAVE),
                "K1 debe descifrar con PALIMPSEST sobre el alfabeto KRYPTOS...");
    }

    
    

    private static final String K2_CIFRADO =
            "VFPJUDEEHZWETZYVGWHKKQETGFQJNCEGGWHKK?DQMCPFQZDQMMIAGPFXHQRLG"
            + "TIMVMZJANQLVKQEDAGDVFRPJUNGEUNAQZGZLECGYUXUEENJTBJLBQCRTBJDFHRR"
            + "YIZETKZEMVDUFKSJHKFWHKUWQLSZFTIHHDDDUVH?DWKBFUFPWNTDFIYCUQZERE"
            + "EVLDKFEZMOQQJLTTUGSYQPFEUNLAVIDXFLGGTEZ?FKZBSFDQVGOGIPUFXHHDRKF"
            + "FHQNTGPUAECNUVPDJMQCLQUMUNEDFQELZZVRRGKFFVOEEXBDMVPNFQXEZLGRE"
            + "DNQFMPNZGLFLPMRJQYALMGNUVPDXVKPDQUMEBEDMHDAFMJGZNUPLGEWJLLAETG";
    private static final String K2_CLAVE = "ABSCISSA";

    @Test
    void kryptosK2IcApuntaAlPeriodoOcho() {
        VigenereCipher c = new VigenereCipher(ALFABETO_KRYPTOS);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(K2_CIFRADO, 20);
        assertEquals(8, argmax(ic),
                "ABSCISSA tiene 8 letras; el maximo de IC(K2) debe ser 8");
        assertTrue(ic[8] > 0.06, "IC(8) deberia acercarse al de texto natural: " + ic[8]);
    }

    @Test
    void kryptosK2DescifraConLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_KRYPTOS);
        String plano = c.decrypt(K2_CIFRADO, K2_CLAVE);
        assertTrue(plano.startsWith("ITWASTOTALLYINVISIBLE"),
                "K2 debe empezar con la frase publicada, obtuvo: " + plano.substring(0, 40));
        assertTrue(plano.contains("UNDERGRUUND"),
                "K2 decodifica el error tipografico UNDERGRUUND documentado por Sanborn");
        assertTrue(plano.endsWith("WESTIDBYROWS"),
                "La escultura termina en IDBYROWS (correccion posterior: XLAYERTWO)");
    }

    
    

    private static final String ALFABETO_Z26 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MTU1_CIFRADO =
            "DAZFI SFSPA VQLSN PXYSZ WXALC DAFGQ UISMT PHZGA MKTTF TCCFX "
            + "KFCRG GLPFE TZMMM ZOZDE ADWVZ WMWKV GQSOH QSVHP WFKLS LEASE "
            + "PWHMJ EGKPU RVSXJ XVBWV POSDE TEQTX OBZIK WCXLW NUOVJ MJCLL "
            + "OEOFA ZENVM JILOW ZEKAZ EJAQD ILSWW ESGUG KTZGQ ZVRMN WTQSE "
            + "OTKTK PBSTA MQVER MJEGL JQRTL GFJYG SPTZP GTACM OECBX SESCI "
            + "YGUFP KVILL TWDKS ZODFW FWEAA PQTFS TQIRG MPMEL RYELH QSVWB "
            + "AWMOS DELHM UZGPG YEKZU KWTAM ZJMLS EVJQT GLAWV OVVXH KWQIL "
            + "IEUYS ZWXAH HUSZO GMUZQ CIMVZ UVWIF JJHPW VXFSE TZEDF";
    private static final String MTU1_CLAVE = "AMBROISETHOMAS";

    @Test
    void mtuEjemplo1IcApuntaAlPeriodoCatorce() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(MTU1_CIFRADO, 20);
        assertEquals(14, argmax(ic),
                "El maximo de IC debe ser 14 (el tutorial MTU reporta 0.064 en L=14)");
    }

    @Test
    void mtuEjemplo1DescifraConLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        String plano = c.decrypt(MTU1_CIFRADO, MTU1_CLAVE).replace(" ", "");
        assertTrue(plano.startsWith("DOYOUKNOWTHELANDWHERETHEORANGETREEBLOSSOMS"),
                "Debe salir el aria de Mignon, obtuvo: " + plano.substring(0, 40));
        assertTrue(plano.endsWith("ITISTHEREYESTHERE"),
                "El aria debe terminar con YES, THERE segun el tutorial MTU");
    }

    
    

    private static final String MTU2_CIFRADO =
            "QRBAI UWYOK ILBRZ XTUWL EGXSN VDXWR XMHXY FCGMW WWSME LSXUZ "
            + "MKMFS BNZIF YEIEG RFZRX WKUFA XQEDX DTTHY NTBRJ LHTAI KOCZX "
            + "QHBND ZIGZG PXARJ EDYSJ NUMKI FLBTN HWISW NVLFM EGXAI AAWSL "
            + "FMHXR SGRIG HEQTU MLGLV BRSIL AEZSG XCMHT OWHFM LWMRK HPRFB "
            + "ELWGF RUGPB HNBEM KBNVW HHUEA KILBN BMLHK XUGML YQKHP RFBEL "
            + "EJYNV WSIJB GAXGO TPMXR TXFKI WUALB RGWIE GHWHG AMEWW LTAEL "
            + "NUMRE UWTBL SDPRL YVRET LEEDF ROBEQ UXTHX ZYOZB XLKAC KSOHN "
            + "VWXKS MAEPH IYQMM FSECH RFYPB BSQTX TPIWH GPXQD FWTAI KNNBX "
            + "SIYKE TXTLV BTMQA LAGHG OTPMX RTXTH XSFYG WMVKH LOIVU ALMLD "
            + "LTSYV WYNVW MQVXP XRVYA BLXDL XSMLW SUIOI IMELI SOYEB HPHNR "
            + "WTVUI AKEYG WIETG WWBVM VDUMA EPAUA KXWHK MAUPA MUKHQ PWKCX "
            + "EFXGW WSDDE OMLWL NKMWD FWTAM FAFEA MFZBN WIHYA LXRWK MAMIK "
            + "GNGHJ UAZHM HGUAL YSULA ELYHJ BZMSI LAILH WWYIK EWAHN PMLBN "
            + "NBVPJ XLBEF WRWGX KWIRH XWWGQ HRRXW IOMFY CZHZL VXNVI OYZCM "
            + "YDDEY IPWXT MMSHS VHHXZ YEWNV OAOEL SMLSW KXXFX STRVI HZLEF "
            + "JXDAS FIE";
    private static final String MTU2_CLAVE = "UNITEDSTATES";

    @Test
    void mtuEjemplo2IcApuntaAlPeriodoDoce() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(MTU2_CIFRADO, 20);
        assertEquals(12, argmax(ic),
                "El maximo de IC debe ser 12 (el tutorial MTU reporta 0.072 en L=12)");
    }

    @Test
    void mtuEjemplo2DescifraConLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        String plano = c.decrypt(MTU2_CIFRADO, MTU2_CLAVE).replace(" ", "");
        assertTrue(plano.startsWith("WETHEREFORETHEREPRESENTATIVESOFTHEUNITEDSTATES"),
                "Debe salir la Declaracion de Independencia, obtuvo: " + plano.substring(0, 50));
        assertTrue(plano.endsWith("OURLIVESOURFORTUNESANDOURSACREDHONOR"),
                "Debe terminar con el final publicado");
    }

    @Test
    void mtuEjemplo2RecuperaCasiTodaLaClaveConChi2Español() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        String rec = a.recuperarClave(MTU2_CIFRADO, 12);
        int aciertos = 0;
        for (int i = 0; i < 12; i++) {
            if (rec.charAt(i) == MTU2_CLAVE.charAt(i)) {
                aciertos++;
            }
        }
        assertTrue(aciertos >= 11,
                "Con frecuencias españolas sobre texto ingles se esperan >= 11/12, "
                        + "obtuvo " + aciertos + "/12 (" + rec + ")");
    }

    
    

    private static final String MTU3_CIFRADO =
            "LPROZ OOGRJ ZGFLV TUKMC WFDQM PZXIJ LVRWQ XEOSZ ZHTEK UYSCR "
            + "PTFCZ UHXIJ LPPTD CPRBY OSMGY TLEVD UAQMF IFMZV LVYTO QDLHX "
            + "LBPLL KYCQY ODRKS ACTEU XZEVO UAQMF OSDSU BKMBJ QEORF WFQCK "
            + "HKSOD INGJZ SHGVV LMSZD WWHFJ AVQGF NUWMW AOIXT CSRYC YPPTP "
            + "LFUCR AVQHR RVRES QCKHM LGARF YHXZP CSNWS NCRQV GHRLR ZEDHF "
            + "JVUPC XZJQC ATISQ SCGXN BALWY MAQCY OSD";
    private static final String MTU3_CLAVE = "THEILIADOFHOMER";

    @Test
    void mtuEjemplo3IcApuntaAlPeriodoQuince() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(MTU3_CIFRADO, 20);
        assertEquals(15, argmax(ic),
                "El maximo de IC debe ser 15 (el tutorial MTU reporta 0.061 en L=15)");
    }

    @Test
    void mtuEjemplo3DescifraConLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z26);
        String plano = c.decrypt(MTU3_CIFRADO, MTU3_CLAVE);
        assertTrue(plano.replace(" ", "").startsWith("SINGOGODDESSTHEANGEROFACHILLES"),
                "La Ilíada debe empezar con la frase publicada, obtuvo: "
                        + plano.replace(" ", "").substring(0, 40));
    }

    
    

    private static final String ALFABETO_Z27_ES = "ABCDEFGHIJKLMN\u00D1OPQRSTUVWXYZ";
    private static final String ES_GOLONDRINAS_CIFRADO =
            "BDVKQURUXALUHNJEDK\u00D1BLHSRCWZDKMZTNHOVQBPKCFNAJDDOORC\u00D1NRQUICOIHQK"
            + "BNWQOVONVMAORAYILZQVACSAFJDVZNORZNNIKGZODXVSXALWJOSXYMMXOKKTCSZ"
            + "DSIZTNNSCABVMZNYE\u00D1RSQTDRSOOFZSWEXDJIDUWQZLHDXVICRWSRSSEREUHELZGZHZ"
            + "RDJEELKHLHZRNWXVWXOX";
    private static final String ES_GOLONDRINAS_CLAVE = "GOLONDRINAS";

    @Test
    void espanolGolondrinasIcDetectaElPeriodoOnce() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z27_ES);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(ES_GOLONDRINAS_CIFRADO, 15);
        assertEquals(11, a.detectarLongitud(ic),
                "El analizador debe detectar el periodo real 11 de GOLONDRINAS");
        assertTrue(ic[11] > ic[5] && ic[11] > ic[7], "IC(11) debe dominar");
    }

    @Test
    void espanolGolondrinasRecuperaLaClavePublicada() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z27_ES);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        assertEquals(ES_GOLONDRINAS_CLAVE, a.recuperarClave(ES_GOLONDRINAS_CIFRADO, 11),
                "La clave recuperada debe coincidir con GOLONDRINAS");
    }

    @Test
    void espanolGolondrinasDescifraElPoemaDeGerardoDiego() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z27_ES);
        String plano = c.decrypt(ES_GOLONDRINAS_CIFRADO, ES_GOLONDRINAS_CLAVE);
        assertEquals(212, plano.length(), "El descifrado debe conservar la longitud");
        assertTrue(plano.startsWith("VOLVERANLASOSCURASGOLONDRINAS"),
                "Debe salir el poema publicado, obtuvo: " + plano.substring(0, 40));
        assertTrue(plano.endsWith("ESASNOVOLVERAN"),
                "El poema debe terminar con ESAS NO VOLVERAN");
        double ic = new VigenereAnalyzer(c).ic(plano);
        assertTrue(ic > 0.06,
                "El texto descifrado debe tener IC de espanol (~0.075), obtuvo: " + ic);
    }

    
    

    private static final String ES_KASISKI_CIFRADO =
            "XUFMZUFPVM\u00D1LHCPUYAKU\u00D1IZOUHFLQSIUFAPENNDWZCAESBBTNRFPZATATLQTKATLQ"
            + "LSBBZEAVEMCZIUOCIEQNEWPYOLACPQRMODIZTSMFCZAUA\u00D1KUOFAMWUDHYUMCOF"
            + "GPCZCNBBBNCHNVUNCHNEPOIHNRCQMWDFQQSSBJMETHEMJNLUO\u00D1LQTNSPQBSVEHI"
            + "GADODWCOKCPUBCWRMWFSWCSMGOLDFBHDHRNPGOKIPMFAFODOQCSNUMNLIIBU"
            + "BDWLBTNNWCFZGOVONPEEIESBBRAOMWFCDIFUGELDFS\u00D1AKU\u00D1WNUFOTMRUWRPUY"
            + "AKCIIZDHTVANLASUMNCWRSIEYHMFLUJWCVPPAVODONVSLUMQSMATMZAEOSIZDHLV"
            + "MSOMOEWCALOEMEEIE\u00D1BQTNDFLBEFMJMFPSLEIPITUKWHNUOSIMOFYNPYAFOMMO"
            + "OKRFACOFDJWPETAKWPEMUGIXDSCBTUNATPIXHHSUIXNHSCMFAEOTMZCSDBNNRHLB"
            + "MEANNQCQBDODWZMSRZWDUWRJIPOKMJZOOFTJ\u00D1BYMU\u00D1WDUWRJIFDHRNPESHLB";

    @Test
    void espanolKasiskiPeriodo6Detectado() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z27_ES);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        double[] ic = a.icPorLongitud(ES_KASISKI_CIFRADO, 20);
        assertEquals(6, a.detectarLongitud(ic),
                "criptored.es fija el periodo en 6; detectarLongitud debe elegirlo "
                        + "como el primer pico fuerte (12 y 18 son armónicos)");
        List<Integer> picos = a.picosLocales(ic);
        assertTrue(picos.contains(6), "L=6 debe ser un maximo local de la curva IC");
        assertTrue(picos.contains(12) || picos.contains(18),
                "Los armónicos 2L/3L del periodo deben aparecer como picos");
    }

    @Test
    void espanolKasiskiRecuperaLaClaveSABINA() {
        VigenereCipher c = new VigenereCipher(ALFABETO_Z27_ES);
        VigenereAnalyzer a = new VigenereAnalyzer(c);
        assertEquals("SABINA", a.recuperarClave(ES_KASISKI_CIFRADO, 6),
                "La clave recuperada en el periodo 6 debe ser SABINA");
        assertEquals("SABINASABINASABINA", a.recuperarClave(ES_KASISKI_CIFRADO, 18),
                "En el armónico 3L=18 la clave se repite tres veces");
    }
}
