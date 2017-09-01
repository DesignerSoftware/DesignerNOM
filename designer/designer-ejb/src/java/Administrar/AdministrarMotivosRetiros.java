/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosRetiros;
import InterfaceAdministrar.AdministrarMotivosRetirosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
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
public class AdministrarMotivosRetiros implements AdministrarMotivosRetirosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosRetiros.class);

   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
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
   public void modificarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
      try {
         for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Modificando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.editar(getEm(), listaMotivosRetiros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
      try {
         for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Borrando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.borrar(getEm(), listaMotivosRetiros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
      try {
         for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Creando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.crear(getEm(), listaMotivosRetiros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<MotivosRetiros> consultarMotivosRetiros() {
      try {
         return persistenciaMotivosRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosRetiros consultarMotivoRetiro(BigInteger secMotivosRetiros) {
      try {
         return persistenciaMotivosRetiros.consultarMotivoRetiro(getEm(), secMotivosRetiros);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarHVExperienciasLaboralesMotivoRetiro(BigInteger secMotivosRetiros) {
      try {
         return persistenciaMotivosRetiros.contarHVExperienciasLaboralesMotivoRetiro(getEm(), secMotivosRetiros);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarNovedadesSistemasMotivoRetiro(BigInteger secMotivosRetiros) {
      try {
         return persistenciaMotivosRetiros.contarNovedadesSistemasMotivoRetiro(getEm(), secMotivosRetiros);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarRetiMotivosRetirosMotivoRetiro(BigInteger secMotivosRetiros) {
      try {
         return persistenciaMotivosRetiros.contarRetiMotivosRetirosMotivoRetiro(getEm(), secMotivosRetiros);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarRetiradosMotivoRetiro(BigInteger secMotivosRetiros) {
      try {
         return persistenciaMotivosRetiros.contarRetiradosMotivoRetiro(getEm(), secMotivosRetiros);
      } catch (Exception e) {
         log.error("ERROR AdministrarMotivosRetiros contarRetiradosMotivoRetiro ERROR : " + e);
         return null;
      }
   }
}
