/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import InterfaceAdministrar.AdministrarJornadasLaboralesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaJornadasInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaJornadasSemanalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarJornadasLaborales implements AdministrarJornadasLaboralesInterface {

   private static Logger log = Logger.getLogger(AdministrarJornadasLaborales.class);

   @EJB
   PersistenciaJornadasLaboralesInterface persistenciaJornadasLaborales;
   @EJB
   PersistenciaJornadasSemanalesInterface persistenciaJornadasSemanales;
   @EJB
   PersistenciaJornadasInterface persistenciaJornadas;
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

   // Metodos
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   /**
    *
    * @return
    */
   @Override
   public List<JornadasLaborales> consultarJornadasLaborales() {
      try {
         return persistenciaJornadasLaborales.buscarJornadasLaborales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Jornadas> consultarJornadas() {
      try {
         return persistenciaJornadas.consultarJornadas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales) {
      try {
         for (int i = 0; i < listaJornadasLaborales.size(); i++) {
//         if (listaJornadasLaborales.get(i).getCodigo().equals(null)) {
//            listaJornadasLaborales.get(i).setCodigo(null);
//            persistenciaJornadasLaborales.editar(getEm(), listaJornadasLaborales.get(i));
//         } else if (listaJornadasLaborales.get(i).getJornada().getSecuencia() == null) {
//            listaJornadasLaborales.get(i).setJornada(null);
//         } else {
            persistenciaJornadasLaborales.editar(getEm(), listaJornadasLaborales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales) {
      try {
         for (int i = 0; i < listaJornadasLaborales.size(); i++) {
            log.warn("Borrando..JornadasLaborales.");
//         if (listaJornadasLaborales.get(i).getCodigo().equals(null)) {
//            listaJornadasLaborales.get(i).setCodigo(null);
//            persistenciaJornadasLaborales.borrar(getEm(), listaJornadasLaborales.get(i));
//         } else if (listaJornadasLaborales.get(i).getJornada().getSecuencia() == null) {
//            listaJornadasLaborales.get(i).setJornada(null);
//         } else {
            persistenciaJornadasLaborales.borrar(getEm(), listaJornadasLaborales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearJornadasLaborales(List<JornadasLaborales> listaJornadasLaborales) {
      try {
         for (int i = 0; i < listaJornadasLaborales.size(); i++) {
            log.warn("Creando. JornadasLaborales..");
            persistenciaJornadasLaborales.crear(getEm(), listaJornadasLaborales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Modificando JornadasSemanales...");
            persistenciaJornadasSemanales.editar(getEm(), listaJornadasSemanales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Borrando JornadasSemanales...");
            persistenciaJornadasSemanales.borrar(getEm(), listaJornadasSemanales.get(i));

         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Creando JornadasSemanales...");
            log.warn("secuencia: " + listaJornadasSemanales.get(i).getSecuencia());
            persistenciaJornadasSemanales.crear(getEm(), listaJornadasSemanales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<JornadasSemanales> consultarJornadasSemanales(BigInteger secuencia) {
      try {
         return persistenciaJornadasSemanales.buscarJornadasSemanalesPorJornadaLaboral(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
