/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarSoCondicionesAmbientalesPInterface;
import Entidades.SoCondicionesAmbientalesP;
import InterfacePersistencia.PersistenciaSoCondicionesAmbientalesPInterface;
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
public class AdministrarSoCondicionesAmbientalesP implements AdministrarSoCondicionesAmbientalesPInterface {

   private static Logger log = Logger.getLogger(AdministrarSoCondicionesAmbientalesP.class);

   @EJB
   PersistenciaSoCondicionesAmbientalesPInterface persistenciaSoCondicionesAmbientalesP;
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
   public void modificarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
      try {
         for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoCondicionesAmbientalesP.editar(getEm(), listSoCondicionesAmbientalesP.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
      try {
         for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoCondicionesAmbientalesP.borrar(getEm(), listSoCondicionesAmbientalesP.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
      try {
         for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoCondicionesAmbientalesP.crear(getEm(), listSoCondicionesAmbientalesP.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SoCondicionesAmbientalesP> consultarSoCondicionesAmbientalesP() {
      try {
         return persistenciaSoCondicionesAmbientalesP.buscarSoCondicionesAmbientalesP(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public SoCondicionesAmbientalesP consultarSoCondicionAmbientalP(BigInteger secSoCondicionesAmbientalesP) {
      try {
         return persistenciaSoCondicionesAmbientalesP.buscarSoCondicionAmbientalP(getEm(), secSoCondicionesAmbientalesP);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarSoAccidentesMedicos(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoCondicionesAmbientalesP.contadorSoAccidentesMedicos(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARSOCONDICIONSEAMBIENTALESP verificarSoAccidtenesMedicos ERROR :" + e);
         return null;
      }
   }
}
