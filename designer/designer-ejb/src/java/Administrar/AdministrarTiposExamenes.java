/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposExamenesInterface;
import Entidades.TiposExamenes;
import InterfacePersistencia.PersistenciaTiposExamenesInterface;
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
public class AdministrarTiposExamenes implements AdministrarTiposExamenesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposExamenes.class);

   @EJB
   PersistenciaTiposExamenesInterface persistenciaTiposExamenes;
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
   public void modificarTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
      try {
         for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposExamenes.editar(getEm(), listaTiposExamenes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
      try {
         for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposExamenes.borrar(getEm(), listaTiposExamenes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearTiposExamenes(List<TiposExamenes> listaTiposExamenes) {
      try {
         for (int i = 0; i < listaTiposExamenes.size(); i++) {
            log.warn("Administrar crear...");
            persistenciaTiposExamenes.crear(getEm(), listaTiposExamenes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<TiposExamenes> consultarTiposExamenes() {
      try {
         return persistenciaTiposExamenes.buscarTiposExamenes(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public TiposExamenes consultarTipoExamen(BigInteger secTipoEmpresa) {
      try {
         return persistenciaTiposExamenes.buscarTipoExamen(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarTiposExamenesCargosTipoExamen(BigInteger secuenciaTiposExamenesCargos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaTiposExamenesCargos);
         return persistenciaTiposExamenes.contadorTiposExamenesCargos(getEm(), secuenciaTiposExamenesCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposExamenes verificarBorradoTiposExamenesCargos ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarVigenciasExamenesMedicosTipoExamen(BigInteger secuenciaVigenciasExamenesMedicos) {
      try {
         log.error("Secuencia Borrado Vigencias Tallas" + secuenciaVigenciasExamenesMedicos);
         return persistenciaTiposExamenes.contadorVigenciasExamenesMedicos(getEm(), secuenciaVigenciasExamenesMedicos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposExamenes verificarBorradoVigenciasExamenesMedicos ERROR :" + e);
         return null;
      }
   }
}
