/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposDescansos;
import InterfaceAdministrar.AdministrarTiposDescansosInterface;
import InterfacePersistencia.PersistenciaTiposDescansosInterface;
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
public class AdministrarTiposDescansos implements AdministrarTiposDescansosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposDescansos.class);

   @EJB
   PersistenciaTiposDescansosInterface persistenciaTiposDescansos;
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
   public void modificarTiposDescansos(List<TiposDescansos> listaTiposDescansos) {
      try {
         for (int i = 0; i < listaTiposDescansos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposDescansos.editar(getEm(), listaTiposDescansos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposDescansos(List<TiposDescansos> listaTiposDescansos) {
      try {
         for (int i = 0; i < listaTiposDescansos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposDescansos.borrar(getEm(), listaTiposDescansos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTiposDescansos(List<TiposDescansos> listaTiposDescansos) {
      try {
         for (int i = 0; i < listaTiposDescansos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposDescansos.crear(getEm(), listaTiposDescansos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposDescansos> consultarTiposDescansos() {
      try {
         return persistenciaTiposDescansos.consultarTiposDescansos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposDescansos consultarTipoDescanso(BigInteger secTipoDescanso) {
      try {
         return persistenciaTiposDescansos.consultarTipoDescanso(getEm(), secTipoDescanso);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarVigenciasJornadasTipoDescanso(BigInteger secuenciaTiposDescansos) {
      try {
         log.warn("Secuencia Tipos Jornadas" + secuenciaTiposDescansos);
         return persistenciaTiposDescansos.contarVigenciasJornadasTipoDescanso(getEm(), secuenciaTiposDescansos);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposDescansos contarVigenciasJornadasTipoDescanso ERROR :" + e);
         return null;
      }
   }

}
