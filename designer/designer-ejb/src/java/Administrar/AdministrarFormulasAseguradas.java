/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.FormulasAseguradas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarFormulasAseguradasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasAseguradasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarFormulasAseguradas implements AdministrarFormulasAseguradasInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulasAseguradas.class);

   //-------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasAseguradas'.
    */
   @EJB
   PersistenciaFormulasAseguradasInterface persistenciaFormulasAseguradas;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;

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
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
      try {
         for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            log.warn("Administrar Modificando...");
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
               listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.editar(getEm(), listaFormulasAseguradas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
      try {
         for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            log.warn("Administrar Borrando...");
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
               listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.borrar(getEm(), listaFormulasAseguradas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas) {
      try {
         for (int i = 0; i < listaFormulasAseguradas.size(); i++) {
            if (listaFormulasAseguradas.get(i).getPeriodicidad().getSecuencia() == null) {
               listaFormulasAseguradas.get(i).setPeriodicidad(null);
            }
            persistenciaFormulasAseguradas.crear(getEm(), listaFormulasAseguradas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<FormulasAseguradas> consultarFormulasAseguradas() {
      try {
         return persistenciaFormulasAseguradas.consultarFormulasAseguradas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Formulas> consultarLOVFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Procesos> consultarLOVProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Periodicidades> consultarLOVPPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
