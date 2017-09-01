/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Festivos;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarFestivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFestivosInterface;
import InterfacePersistencia.PersistenciaPaisesInterface;
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
public class AdministrarFestivos implements AdministrarFestivosInterface {

   private static Logger log = Logger.getLogger(AdministrarFestivos.class);

   @EJB
   PersistenciaPaisesInterface persistenciaPaises;
   @EJB
   PersistenciaFestivosInterface persistenciaFestivos;
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

   public void modificarFestivos(List<Festivos> listaFestivos) {
      try {
         for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFestivos.editar(getEm(), listaFestivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarFestivos(List<Festivos> listaFestivos) {
      try {
         for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFestivos.borrar(getEm(), listaFestivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearFestivos(List<Festivos> listaFestivos) {
      try {
         for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaFestivos.crear(getEm(), listaFestivos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Festivos> consultarFestivosPais(BigInteger secPais) {
      try {
         return persistenciaFestivos.consultarFestivosPais(getEm(), secPais);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Paises> consultarLOVPaises() {
      try {
         return persistenciaPaises.consultarPaises(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
