/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
public class AsistenteReporte implements AsynchronousFilllListener {

   private static Logger log = Logger.getLogger(AsistenteReporte.class);

   @Override
   public void reportFinished(JasperPrint jp) {
      log.warn("El reporte finalizó correctamente.");
   }

   @Override
   public void reportCancelled() {
      log.warn("El reporte fue cancelado.");
   }

   @Override
   public void reportFillError(Throwable thrwbl) {
      log.warn("El reporte generó errores.");
   }

}
