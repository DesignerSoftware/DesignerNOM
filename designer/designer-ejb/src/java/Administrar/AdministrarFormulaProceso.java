/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarFormulaProcesoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasProcesosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author PROYECTO01
 */
@Stateless
@Local
public class AdministrarFormulaProceso implements AdministrarFormulaProcesoInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulaProceso.class);

   @EJB
   PersistenciaFormulasProcesosInterface persistenciaFormulasProcesos;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<FormulasProcesos> listFormulasProcesosParaFormula(BigInteger secuencia) {
      try {
         return persistenciaFormulasProcesos.formulasProcesosParaFormulaSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listFormulasProcesosParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasProcesos(List<FormulasProcesos> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasProcesos.crear(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasProcesos Admi : " + e.toString());
      }
   }

   @Override
   public void editarFormulasProcesos(List<FormulasProcesos> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasProcesos.editar(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasProcesos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasProcesos(List<FormulasProcesos> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasProcesos.borrar(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasProcesos Admi : " + e.toString());
      }
   }

   @Override
   public List<Procesos> listProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error listProcesos Admi : " + e.toString());
         return null;
      }
   }

   public Formulas formulaActual(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error formulaActual Admi : " + e.toString());
         return null;
      }
   }
}
