/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarVigenciasPlantasInterface;
import Entidades.VigenciasPlantas;
import InterfacePersistencia.PersistenciaVigenciasPlantasInterface;
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
public class AdministrarVigenciasPlantas implements AdministrarVigenciasPlantasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasPlantas.class);

   @EJB
   PersistenciaVigenciasPlantasInterface persistenciaVigenciasPlantas;
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

   public void modificarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
      try {
         for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaVigenciasPlantas.editar(getEm(), listaVigenciasPlantas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
      try {
         for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaVigenciasPlantas.borrar(getEm(), listaVigenciasPlantas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
      try {
         for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaVigenciasPlantas.crear(getEm(), listaVigenciasPlantas.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<VigenciasPlantas> consultarVigenciasPlantas() {
      try {
         return persistenciaVigenciasPlantas.consultarVigenciasPlantas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public VigenciasPlantas consultarVigenciaPlanta(BigInteger secVigenciasPlantas) {
      try {
         return persistenciaVigenciasPlantas.consultarVigenciaPlanta(getEm(), secVigenciasPlantas);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarPlantasVigenciaPlanta(BigInteger secVigenciasPlantas) {
      try {
         return persistenciaVigenciasPlantas.contarPlantasVigenciaPlanta(getEm(), secVigenciasPlantas);
      } catch (Exception e) {
         log.error("ERROR AdministrarVigenciasPlantas verificarBorradoVNE ERROR :" + e);
         return null;
      }
   }
}
