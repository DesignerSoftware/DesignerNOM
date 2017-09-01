/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.IbcsAutoliquidaciones;
import Entidades.Procesos;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarIbcAutoliquidInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIbcsAutoliquidacionesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import java.math.BigInteger;
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
public class AdministrarIbcAutoliquid implements AdministrarIbcAutoliquidInterface {

   private static Logger log = Logger.getLogger(AdministrarIbcAutoliquid.class);

   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaIbcsAutoliquidacionesInterface persistenciaIbcsAutoliquidaciones;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<TiposEntidades> listTiposEntidadesIBCS() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidadesIBCS(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposEntidadesIBCS Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<IbcsAutoliquidaciones> listIBCSAutoliquidaciones(BigInteger secuenciaTE, BigInteger secuenciaEmpl) {
      try {
         return persistenciaIbcsAutoliquidaciones.buscarIbcsAutoliquidacionesTipoEntidadEmpleado(getEm(), secuenciaTE, secuenciaEmpl);
      } catch (Exception e) {
         log.warn("Error listIBCSAutoliquidaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> crearIbcsAutoliquidaciones) {
      try {
         for (int i = 0; i < crearIbcsAutoliquidaciones.size(); i++) {
            if (crearIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
               crearIbcsAutoliquidaciones.get(i).setProceso(null);
            }
            persistenciaIbcsAutoliquidaciones.crear(getEm(), crearIbcsAutoliquidaciones.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en crearCargo Admi : " + e.toString());
      }
   }

   @Override
   public void modificarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> modificarIbcsAutoliquidaciones) {
      try {
         for (int i = 0; i < modificarIbcsAutoliquidaciones.size(); i++) {
            if (modificarIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
               modificarIbcsAutoliquidaciones.get(i).setProceso(null);
            }
            persistenciaIbcsAutoliquidaciones.editar(getEm(), modificarIbcsAutoliquidaciones.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en modificarCargo Admi : " + e.toString());
      }
   }

   @Override
   public void borrarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> borrarIbcsAutoliquidaciones) {
      try {
         for (int i = 0; i < borrarIbcsAutoliquidaciones.size(); i++) {
            if (borrarIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
               borrarIbcsAutoliquidaciones.get(i).setProceso(null);
            }
            persistenciaIbcsAutoliquidaciones.borrar(getEm(), borrarIbcsAutoliquidaciones.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en borrarCargo Admi : " + e.toString());
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

   @Override
   public Empleados empleadoActual(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }
}
