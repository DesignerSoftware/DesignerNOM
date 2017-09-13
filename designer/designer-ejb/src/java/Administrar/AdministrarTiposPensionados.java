/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposPensionados;
import InterfaceAdministrar.AdministrarTiposPensionadosInterface;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarTiposPensionados implements AdministrarTiposPensionadosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposPensionados.class);

   @EJB
   PersistenciaTiposPensionadosInterface persistenciaTiposPensionados;
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
   public void modificarTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
      try {
         for (int i = 0; i < listaTiposPensionados.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposPensionados.editar(getEm(), listaTiposPensionados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposPensionados() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
      try {
         for (int i = 0; i < listaTiposPensionados.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposPensionados.borrar(getEm(), listaTiposPensionados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposPensionados() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
      try {
         for (int i = 0; i < listaTiposPensionados.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposPensionados.crear(getEm(), listaTiposPensionados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposPensionados() ERROR: " + e);
      }
   }

   public List<TiposPensionados> consultarTiposPensionados() {
      try {
         return persistenciaTiposPensionados.consultarTiposPensionados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposPensionados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposPensionados consultarTipoPensionado(BigInteger secTiposPensionados) {
      try {
         return persistenciaTiposPensionados.consultarTipoPensionado(getEm(), secTiposPensionados);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoPensionado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarRetiradosTipoPensionado(BigInteger secTiposPensionados) {
      try {
         return persistenciaTiposPensionados.contarPensionadosTipoPension(getEm(), secTiposPensionados);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposPensionados contarEscalafones ERROR : " + e);
         return null;
      }
   }
}
