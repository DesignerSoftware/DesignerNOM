/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Enfermedades;
import InterfaceAdministrar.AdministrarEnfermedadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnfermedadesInterface;
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
public class AdministrarEnfermedades implements AdministrarEnfermedadesInterface {

   private static Logger log = Logger.getLogger(AdministrarEnfermedades.class);

   @EJB
   PersistenciaEnfermedadesInterface persistenciaEnfermedades;

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

   public void modificarEnfermedades(List<Enfermedades> listDeportesModificadas) {
      try {
         for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEnfermedades.editar(getEm(), listDeportesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarEnfermedades(List<Enfermedades> listDeportesModificadas) {
      try {
         for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEnfermedades.borrar(getEm(), listDeportesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearEnfermedades(List<Enfermedades> listDeportesModificadas) {
      try {
         for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Crear...");
            persistenciaEnfermedades.crear(getEm(), listDeportesModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Enfermedades> consultarEnfermedades() {
      try {
         return persistenciaEnfermedades.buscarEnfermedades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Enfermedades consultarEnfermedad(BigInteger secDeportes) {
      try {
         return persistenciaEnfermedades.buscarEnfermedad(getEm(), secDeportes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger verificarAusentimos(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaEnfermedades.contadorAusentimos(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARENFERMEDADES contadorAusentimos ERROR :" + e);
         return null;
      }
   }

   public BigInteger verificarDetallesLicencias(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaEnfermedades.contadorDetallesLicencias(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARENFERMEDADES contadorDetallesLicencias ERROR :" + e);
         return null;
      }
   }

   public BigInteger verificarEnfermedadesPadecidas(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaEnfermedades.contadorEnfermedadesPadecidas(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARENFERMEDADES contadorEnfermedadesPadecidas ERROR :" + e);
         return null;
      }
   }

   public BigInteger verificarSoAusentismos(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaEnfermedades.contadorSoausentismos(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARENFERMEDADES contadorSoausentismos ERROR :" + e);
         return null;
      }
   }

   public BigInteger verificarSoRevisionesSistemas(BigInteger secuenciaTiposAuxilios) {
      try {
         return persistenciaEnfermedades.contadorSorevisionessSistemas(getEm(), secuenciaTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARENFERMEDADES contadorSorevisionessSistemas ERROR :" + e);
         return null;
      }
   }
}
