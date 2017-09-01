/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasTallasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposTallasInterface;
import InterfacePersistencia.PersistenciaVigenciasTallasInterface;
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
public class AdministrarVigenciasTallas implements AdministrarVigenciasTallasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasTallas.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasTallasInterface persistenciaVigenciasTallas;
   @EJB
   PersistenciaTiposTallasInterface persistenciaTiposTallas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
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

   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      try {
         for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaVigenciasTallas.editar(getEm(), listaVigenciasTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      try {
         for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaVigenciasTallas.borrar(getEm(), listaVigenciasTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      try {
         for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaVigenciasTallas.crear(getEm(), listaVigenciasTallas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<VigenciasTallas> consultarVigenciasTallasPorEmpleado(BigInteger secPersona) {
      try {
         return persistenciaVigenciasTallas.consultarVigenciasTallasPorPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposTallas> consultarLOVTiposTallas() {
      try {
         return persistenciaTiposTallas.buscarTiposTallas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Empleados consultarEmpleadoSecuenciaPersona(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuenciaPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("ERROR AdministrarHvReferencias  consultarEmpleadoSecuenciaPersona ERROR =====" + e);
         return null;
      }
   }

}
