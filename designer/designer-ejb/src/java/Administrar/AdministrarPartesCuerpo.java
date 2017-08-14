/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarPartesCuerpoInterface;
import Entidades.PartesCuerpo;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPartesCuerpoInterface;
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
public class AdministrarPartesCuerpo implements AdministrarPartesCuerpoInterface {

   private static Logger log = Logger.getLogger(AdministrarPartesCuerpo.class);

   @EJB
   PersistenciaPartesCuerpoInterface persistenciaPartesCuerpo;
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

   public void modificarPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
      try {
         for (int i = 0; i < listPartesCuerpo.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPartesCuerpo.editar(getEm(), listPartesCuerpo.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
      try {
         for (int i = 0; i < listPartesCuerpo.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPartesCuerpo.borrar(getEm(), listPartesCuerpo.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
      try {
         for (int i = 0; i < listPartesCuerpo.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPartesCuerpo.crear(getEm(), listPartesCuerpo.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<PartesCuerpo> consultarPartesCuerpo() {
      try {
         return persistenciaPartesCuerpo.buscarPartesCuerpo(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public PartesCuerpo consultarParteCuerpo(BigInteger secElementosCausasAccidentes) {
      try {
         return persistenciaPartesCuerpo.buscarParteCuerpo(getEm(), secElementosCausasAccidentes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarSoAccidentesMedicosParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
      try {
         return persistenciaPartesCuerpo.contadorSoAccidentesMedicos(getEm(), secuenciaElementosCausasAccidentes);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPARTESCUERPO verificarBorradoDetallesLicensias ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarDetallesExamenesParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
      try {
         return persistenciaPartesCuerpo.contadorDetallesExamenes(getEm(), secuenciaElementosCausasAccidentes);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPARTESCUERPO verificarBorradoSoAccidentesDomesticos ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarSoDetallesRevisionesParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
      try {
         return persistenciaPartesCuerpo.contadorSoDetallesRevisiones(getEm(), secuenciaElementosCausasAccidentes);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPARTESCUERPO verificarBorradoSoAccidentesDomesticos ERROR :" + e);
         return null;
      }
   }
}
