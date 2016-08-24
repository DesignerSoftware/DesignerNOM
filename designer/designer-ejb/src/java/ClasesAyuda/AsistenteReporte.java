/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ClasesAyuda;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 *
 * @author Administrador
 */
public class AsistenteReporte implements AsynchronousFilllListener{

    @Override
    public void reportFinished(JasperPrint jp) {
        System.out.println("El reporte finalizó correctamente.");
    }

    @Override
    public void reportCancelled() {
        System.out.println("El reporte fue cancelado.");
    }

    @Override
    public void reportFillError(Throwable thrwbl) {
        System.out.println("El reporte generó errores.");
    }
    
}
