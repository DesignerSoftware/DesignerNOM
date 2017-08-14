/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoActosInseguros;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoActosInsegurosInterface;
import InterfacePersistencia.PersistenciaSoActosInsegurosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author John Pineda
 */
@Stateful
public class AdministrarSoActosInseguros implements AdministrarSoActosInsegurosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoActosInseguros.class);

   @EJB
   PersistenciaSoActosInsegurosInterface persistenciaSoActosInseguros;
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
   public void modificarSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
      try {
         for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoActosInseguros.editar(getEm(), listSoActosInseguros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
      try {
         for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoActosInseguros.borrar(getEm(), listSoActosInseguros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
      try {
         for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoActosInseguros.crear(getEm(), listSoActosInseguros.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SoActosInseguros> consultarSoActosInseguros() {
      try {
         return persistenciaSoActosInseguros.buscarSoActosInseguros(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public SoActosInseguros consultarSoActoInseguro(BigInteger secSoCondicionesAmbientalesP) {
      try {
         return persistenciaSoActosInseguros.buscarSoActoInseguro(getEm(), secSoCondicionesAmbientalesP);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarSoAccidentesMedicos(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoActosInseguros.contadorSoAccidentesMedicos(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARSOACTOSINSEGUROS verificarSoAccidtenesMedicos ERROR :" + e);
         return null;
      }
   }
}
