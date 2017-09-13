/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Tiposviajeros;
import Entidades.VigenciasViajeros;
import InterfaceAdministrar.AdministrarVigenciasViajerosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposViajerosInterface;
import InterfacePersistencia.PersistenciaVigenciasViajerosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciasViajeros implements AdministrarVigenciasViajerosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasViajeros.class);

   @EJB
   PersistenciaVigenciasViajerosInterface persistenciaVigenciasViajeros;
   @EJB
   PersistenciaTiposViajerosInterface persistenciaTiposViajeros;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
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
   public Empleados consultarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public void modificarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
      try {
         for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaVigenciasViajeros.editar(getEm(), listaVigenciasViajeros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarVigenciasViajeros() ERROR: " + e);
      }
   }

   public void borrarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
      try {
         for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaVigenciasViajeros.borrar(getEm(), listaVigenciasViajeros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarVigenciasViajeros() ERROR: " + e);
      }
   }

   public void crearVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
      try {
         for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaVigenciasViajeros.crear(getEm(), listaVigenciasViajeros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearVigenciasViajeros() ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasViajeros> consultarVigenciasViajeros() {
      try {
         return persistenciaVigenciasViajeros.consultarVigenciasViajeros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarVigenciasViajeros() ERROR: " + e);
         return null;
      }
   }

   public VigenciasViajeros consultarTipoViajero(BigInteger secVigenciasViajeros) {
      try {
         return persistenciaVigenciasViajeros.consultarTipoExamen(getEm(), secVigenciasViajeros);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoViajero() ERROR: " + e);
         return null;
      }
   }

   public List<VigenciasViajeros> consultarVigenciasViajerosPorEmpleado(BigInteger secVigenciasViajeros) {
      try {
         return persistenciaVigenciasViajeros.consultarVigenciasViajerosPorEmpleado(getEm(), secVigenciasViajeros);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarVigenciasViajerosPorEmpleado() ERROR: " + e);
         return null;
      }
   }

   public List<Tiposviajeros> consultarLOVTiposViajeros() {
      try {
         return persistenciaTiposViajeros.consultarTiposViajeros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVTiposViajeros() ERROR: " + e);
         return null;
      }
   }

}
