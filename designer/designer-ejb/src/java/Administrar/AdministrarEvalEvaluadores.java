/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalEvaluadores;
import InterfaceAdministrar.AdministrarEvalEvaluadoresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalEvaluadoresInterface;
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
public class AdministrarEvalEvaluadores implements AdministrarEvalEvaluadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalEvaluadores.class);

   @EJB
   PersistenciaEvalEvaluadoresInterface persistenciaEvalEvaluadores;
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

   private EvalEvaluadores valEvaluadoresSeleccionado;
   private EvalEvaluadores valEvaluadores;
   private List<EvalEvaluadores> listEvalEvaluadores;

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
      try {
         for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEvalEvaluadores.editar(getEm(), listEvalEvaluadores.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
      try {
         for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Borrar...");
            persistenciaEvalEvaluadores.borrar(getEm(), listEvalEvaluadores.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEvalEvaluadores(List<EvalEvaluadores> listEvalEvaluadores) {
      try {
         for (int i = 0; i < listEvalEvaluadores.size(); i++) {
            log.warn("Administrar Crear...");
            persistenciaEvalEvaluadores.crear(getEm(), listEvalEvaluadores.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<EvalEvaluadores> consultarEvalEvaluadores() {
      try {
         return persistenciaEvalEvaluadores.buscarEvalEvaluadores(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public EvalEvaluadores consultarEvalEvaluador(BigInteger secEvalEvaluadores) {
      try {
         return persistenciaEvalEvaluadores.buscarEvalEvaluador(getEm(), secEvalEvaluadores);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarEvalPruebas(BigInteger secuenciaMovitoCambioCargo) {
      try {
         return persistenciaEvalEvaluadores.verificarBorradoEvalPruebas(getEm(), secuenciaMovitoCambioCargo);
      } catch (Exception e) {
         log.error("ERROR AdministrarEvalEvaluadores verificarBorradoVC ERROR :" + e);
         return new BigInteger("-1");
      }
   }
}
