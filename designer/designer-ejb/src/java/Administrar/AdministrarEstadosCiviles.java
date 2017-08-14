/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EstadosCiviles;
import InterfaceAdministrar.AdministrarEstadosCivilesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEstadosCiviles implements AdministrarEstadosCivilesInterface {

   private static Logger log = Logger.getLogger(AdministrarEstadosCiviles.class);

   @EJB
   PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
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
   public void modificarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
      try {
         for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEstadosCiviles.editar(getEm(), listaEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
      try {
         for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEstadosCiviles.borrar(getEm(), listaEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
      try {
         for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEstadosCiviles.crear(getEm(), listaEstadosCiviles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<EstadosCiviles> consultarEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarVigenciasEstadosCiviles(BigInteger secuenciaEstadosCiviles) {
      try {
         log.warn("Secuencia verificarBorradoVigenciasEstadoCiviles  " + secuenciaEstadosCiviles);
         return persistenciaEstadosCiviles.contadorVigenciasEstadosCiviles(getEm(), secuenciaEstadosCiviles);
      } catch (Exception e) {
         log.error("ERROR AdministrarEstadosCiviles verificarBorradoVigenciasEstadoCiviles ERROR :" + e);
         return null;
      }
   }
}
