/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.VigenciasEstadosCiviles;
import Entidades.EstadosCiviles;
import Entidades.Empleados;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
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
public class AdministrarVigenciasEstadosCiviles implements AdministrarVigenciasEstadosCivilesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasEstadosCiviles.class);

   /**
    * CREACION DE LOS EJB
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
   @EJB
   PersistenciaVigenciasEstadosCivilesInterface persistenciaVigenciasEstadosCiviles;
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

   /**
    * Creacion de metodos
    */
   public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCivilesPorPersona(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
         return null;
      }
   }

   public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado() {
      try {
         return persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCiviles(getEm());
      } catch (Exception e) {
         log.warn("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasEstadosCiviles.editar(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            log.warn("borrar...");
            persistenciaVigenciasEstadosCiviles.borrar(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
      try {
         for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            log.warn("crear...");
            persistenciaVigenciasEstadosCiviles.crear(getEm(), listaVigenciasEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public Personas obtenerPersonaPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleado.buscarPersonaPorEmpleado(em, secEmpleado);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + " ERROR en obtenerPersonaPorEmpleado() : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<EstadosCiviles> lovEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
      } catch (Exception e) {
         log.error("ERROR EN AdministrarVigencianormaLaboral en NormasLabolares ERROR " + e);
         return null;
      }
   }
}
