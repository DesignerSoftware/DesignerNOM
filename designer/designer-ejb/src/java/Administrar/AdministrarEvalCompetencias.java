/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalCompetencias;
import InterfaceAdministrar.AdministrarEvalCompetenciasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalCompetenciasInterface;
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
public class AdministrarEvalCompetencias implements AdministrarEvalCompetenciasInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalCompetencias.class);

   @EJB
   PersistenciaEvalCompetenciasInterface persistenciaEvalCompetencias;

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
   public void modificarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
      try {
         for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEvalCompetencias.editar(getEm(), listEvalCompetencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
      try {
         for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEvalCompetencias.borrar(getEm(), listEvalCompetencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
      try {
         for (int i = 0; i < listEvalCompetencias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaEvalCompetencias.crear(getEm(), listEvalCompetencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<EvalCompetencias> consultarEvalCompetencias() {
      try {
         return persistenciaEvalCompetencias.buscarEvalCompetencias(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public EvalCompetencias consultarEvalCompetencia(BigInteger secTipoEmpresa) {
      try {
         return persistenciaEvalCompetencias.buscarEvalCompetencia(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarCompetenciasCargos(BigInteger secuenciaCompetenciasCargos) {
      try {
         log.error("Secuencia Borrado Competencias Cargos" + secuenciaCompetenciasCargos);
         return persistenciaEvalCompetencias.contadorCompetenciasCargos(getEm(), secuenciaCompetenciasCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarEvalCompetencias verificarBorradoCompetenciasCargos ERROR :" + e);
         return null;
      }
   }

}
