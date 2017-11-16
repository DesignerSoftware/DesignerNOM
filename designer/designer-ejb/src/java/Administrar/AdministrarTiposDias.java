/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposDiasInterface;
import Entidades.TiposDias;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
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
public class AdministrarTiposDias implements AdministrarTiposDiasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposDias.class);

   @EJB
   PersistenciaTiposDiasInterface persistenciaTiposDias;
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
   public void modificarTiposDias(List<TiposDias> listaTiposDias) {
      try {
         for (int i = 0; i < listaTiposDias.size(); i++) {
            persistenciaTiposDias.editar(getEm(), listaTiposDias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposDias() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposDias(List<TiposDias> listaTiposDias) {
      try {
         for (int i = 0; i < listaTiposDias.size(); i++) {
            persistenciaTiposDias.borrar(getEm(), listaTiposDias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposDias() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposDias(List<TiposDias> listaTiposDias) {
      for (int i = 0; i < listaTiposDias.size(); i++) {
         persistenciaTiposDias.crear(getEm(), listaTiposDias.get(i));
      }
      try {
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposDias() ERROR: " + e);
      }
   }

   @Override
   public List<TiposDias> mostrarTiposDias() {
      try {
         return persistenciaTiposDias.buscarTiposDias(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".mostrarTiposDias() ERROR: " + e);
         return null;
      }
   }

   @Override

   public TiposDias mostrarTipoDia(BigInteger secTipoDia) {
      try {
         return persistenciaTiposDias.buscarTipoDia(getEm(), secTipoDia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".mostrarTipoDia() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarDiasLaborales(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaTiposDias.contadorDiasLaborales(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSDIAS VERIFICARDIASLABORALES ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarExtrasRecargos(BigInteger secuenciaTiposDias) {
      try {
         return persistenciaTiposDias.contadorExtrasRecargos(getEm(), secuenciaTiposDias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARTIPOSDIAS VERIFICAREXTRASRECARGOS ERROR :" + e);
         return null;
      }
   }
}
