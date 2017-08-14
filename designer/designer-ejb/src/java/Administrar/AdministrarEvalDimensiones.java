/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalDimensiones;
import InterfaceAdministrar.AdministrarEvalDimensionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalDimensionesInterface;
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
public class AdministrarEvalDimensiones implements AdministrarEvalDimensionesInterface {

   private static Logger log = Logger.getLogger(AdministrarEvalDimensiones.class);

   @EJB
   PersistenciaEvalDimensionesInterface persistenciaTiposCentrosCostos;
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
   public void modificarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
      try {
         for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCentrosCostos.editar(getEm(), listaEvalDimensiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
      try {
         for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCentrosCostos.borrar(getEm(), listaEvalDimensiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
      try {
         for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCentrosCostos.crear(getEm(), listaEvalDimensiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public EvalDimensiones consultarEvalDimension(BigInteger secTipoCentrosCostos) {
      try {
         return persistenciaTiposCentrosCostos.buscarEvalDimension(getEm(), secTipoCentrosCostos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<EvalDimensiones> consultarEvalDimensiones() {
      try {
         return persistenciaTiposCentrosCostos.buscarEvalDimensiones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarEvalPlanillas(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaTiposCentrosCostos.contradorEvalPlanillas(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRAREVALPLANILLAS verificarEvalPlanillas ERROR :" + e);
         return null;
      }
   }
}
