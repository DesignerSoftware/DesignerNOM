/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Indices;
import Entidades.TiposIndices;
import InterfaceAdministrar.AdministrarIndicesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaIndicesInterface;
import InterfacePersistencia.PersistenciaTiposIndicesInterface;
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
public class AdministrarIndices implements AdministrarIndicesInterface {

   private static Logger log = Logger.getLogger(AdministrarIndices.class);

   @EJB
   PersistenciaIndicesInterface persistenciaIndices;
   @EJB
   PersistenciaTiposIndicesInterface PersistenciaTiposIndices;
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

   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarIndices(List<Indices> listaIndices) {
      try {
         for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaIndices.editar(getEm(), listaIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarIndices(List<Indices> listaIndices) {
      try {
         for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaIndices.borrar(getEm(), listaIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearIndices(List<Indices> listaIndices) {
      try {
         for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar crear...");
            persistenciaIndices.crear(getEm(), listaIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Indices> mostrarIndices() {
      try {
         return persistenciaIndices.consultarIndices(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<TiposIndices> consultarLOVTiposIndices() {
      try {
         return PersistenciaTiposIndices.consultarTiposIndices(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarParametrosIndicesIndice(BigInteger secuenciaIndices) {
      try {
         return persistenciaIndices.contarParametrosIndicesIndice(getEm(), secuenciaIndices);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarParametrosIndicesIndice ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarResultadosIndicesDetallesIndice(BigInteger secuenciaIndices) {
      try {
         return persistenciaIndices.contarResultadosIndicesDetallesIndice(getEm(), secuenciaIndices);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarResultadosIndicesDetallesIndice ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarResultadosIndicesIndice(BigInteger secuenciaIndices) {
      try {
         return persistenciaIndices.contarResultadosIndicesIndice(getEm(), secuenciaIndices);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarResultadosIndicesIndice ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarResultadosIndicesSoportesIndice(BigInteger secuenciaIndices) {
      try {
         return persistenciaIndices.contarResultadosIndicesSoportesIndice(getEm(), secuenciaIndices);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarResultadosIndicesSoportesIndice ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarUsuariosIndicesIndice(BigInteger secuenciaIndices) {
      try {
         return persistenciaIndices.contarUsuariosIndicesIndice(getEm(), secuenciaIndices);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarUsuariosIndicesIndice ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarCodigosRepetidosIndices(Short codigo) {
      try {
         return persistenciaIndices.contarCodigosRepetidosIndices(getEm(), codigo);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIndices contarCodigosRepetidosIndices ERROR :" + e);
         return null;
      }
   }
}
