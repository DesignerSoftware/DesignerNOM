/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Idiomas;
import InterfaceAdministrar.AdministrarIdiomasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaIdiomasInterface;
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
public class AdministrarIdiomas implements AdministrarIdiomasInterface {

   private static Logger log = Logger.getLogger(AdministrarIdiomas.class);

   @EJB
   PersistenciaIdiomasInterface persistenciaIdiomas;

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

   public void modificarIdiomas(List<Idiomas> listaIdiomas) {
      try {
         for (int i = 0; i < listaIdiomas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaIdiomas.editar(getEm(), listaIdiomas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarIdiomas() ERROR: " + e);
      }
   }

   public void borrarIdiomas(List<Idiomas> listaIdiomas) {
      try {
         for (int i = 0; i < listaIdiomas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaIdiomas.borrar(getEm(), listaIdiomas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarIdiomas() ERROR: " + e);
      }
   }

   public void crearIdiomas(List<Idiomas> listaIdiomas) {
      try {
         for (int i = 0; i < listaIdiomas.size(); i++) {
            log.warn("Administrar crear...");
            persistenciaIdiomas.crear(getEm(), listaIdiomas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearIdiomas() ERROR: " + e);
      }
   }

   public List<Idiomas> mostrarIdiomas() {
      try {
         return persistenciaIdiomas.buscarIdiomas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".mostrarIdiomas() ERROR: " + e);
         return null;
      }
   }

   public Idiomas consultarIdioma(BigInteger secIdiomas) {
      try {
         return persistenciaIdiomas.buscarIdioma(getEm(), secIdiomas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarIdioma() ERROR: " + e);
         return null;
      }
   }

   public BigInteger verificarBorradoIdiomasPersonas(BigInteger secuenciaIdiomas) {
      try {
         log.error("Secuencia Idiomas Personas " + secuenciaIdiomas);
         return persistenciaIdiomas.contadorIdiomasPersonas(getEm(), secuenciaIdiomas);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarIdiomas verificarBorradoIdiomasPersonas ERROR :" + e);
         return null;
      }
   }
}
