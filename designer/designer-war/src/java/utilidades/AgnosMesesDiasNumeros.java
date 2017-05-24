/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.util.HashMap;

/**
 *
 * @author Edwin Hastamorir
 * @version 1.0
 */
public class AgnosMesesDiasNumeros {

    private static HashMap agnos;
    private static HashMap meses;
    private static HashMap dias;

    public static HashMap getMeses() {
        meses = null;
        if (meses == null) {
            inicializarmeses();
        }
        return meses;
    }

    private static void inicializarmeses() {
        meses = new HashMap();

        meses.put("TODOS LOS MESES", Short.valueOf("0"));
        meses.put("ENERO", Short.valueOf("1"));
        meses.put("FEBRERO", Short.valueOf("2"));
        meses.put("MARZO", Short.valueOf("3"));
        meses.put("ABRIL", Short.valueOf("4"));
        meses.put("MAYO", Short.valueOf("5"));
        meses.put("JUNIO", Short.valueOf("6"));
        meses.put("JULIO", Short.valueOf("7"));
        meses.put("AGOSTO", Short.valueOf("8"));
        meses.put("SEPTIEMBRE", Short.valueOf("9"));
        meses.put("OCTUBRE", Short.valueOf("10"));
        meses.put("NOVIEMBRE", Short.valueOf("11"));
        meses.put("DICIEMBRE", Short.valueOf("12"));
    }

    public static HashMap getDias() {
        if (dias == null) {
            inicializardias();
        }
        return dias;
    }

    private static void inicializardias() {
        dias = new HashMap();

        dias.put("TODOS LOS DIAS", Short.valueOf("0"));
        dias.put("01", Short.valueOf("1"));
        dias.put("02", Short.valueOf("2"));
        dias.put("03", Short.valueOf("3"));
        dias.put("04", Short.valueOf("4"));
        dias.put("05", Short.valueOf("5"));
        dias.put("06", Short.valueOf("6"));
        dias.put("07", Short.valueOf("7"));
        dias.put("08", Short.valueOf("8"));
        dias.put("09", Short.valueOf("9"));
        dias.put("10", Short.valueOf("10"));
        dias.put("11", Short.valueOf("11"));
        dias.put("12", Short.valueOf("12"));
        dias.put("13", Short.valueOf("13"));
        dias.put("14", Short.valueOf("14"));
        dias.put("15", Short.valueOf("15"));
        dias.put("16", Short.valueOf("16"));
        dias.put("17", Short.valueOf("17"));
        dias.put("18", Short.valueOf("18"));
        dias.put("19", Short.valueOf("19"));
        dias.put("20", Short.valueOf("20"));
        dias.put("21", Short.valueOf("21"));
        dias.put("22", Short.valueOf("22"));
        dias.put("23", Short.valueOf("23"));
        dias.put("24", Short.valueOf("24"));
        dias.put("25", Short.valueOf("25"));
        dias.put("26", Short.valueOf("26"));
        dias.put("27", Short.valueOf("27"));
        dias.put("28", Short.valueOf("28"));
        dias.put("29", Short.valueOf("29"));
        dias.put("30", Short.valueOf("30"));
        dias.put("31", Short.valueOf("31"));
    }

    public static HashMap getAgnos(int agno) {
        if (agnos == null) {
            inicializaragnos(agno);
        }
        return agnos;
    }

    private static void inicializaragnos(int agno) {
        agnos = new HashMap();
        for (int i = (agno - 10); i < (agno + 10); i++) {
            agnos.put(String.valueOf(i), i);
        }
    }

}
