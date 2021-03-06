/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Jornadas;
import InterfaceAdministrar.AdministrarJornadasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaJornadasInterface;
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
public class AdministrarJornadas implements AdministrarJornadasInterface {

   private static Logger log = Logger.getLogger(AdministrarJornadas.class);

   @EJB
   PersistenciaJornadasInterface persistenciaJornadas;
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

   public void modificarJornadas(List<Jornadas> listaJornadas) {
      try {
         for (int i = 0; i < listaJornadas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaJornadas.editar(getEm(), listaJornadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarJornadas() ERROR: " + e);
      }
   }

   public void borrarJornadas(List<Jornadas> listaJornadas) {
      try {
         for (int i = 0; i < listaJornadas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaJornadas.borrar(getEm(), listaJornadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarJornadas() ERROR: " + e);
      }
   }

   public void crearJornadas(List<Jornadas> listaJornadas) {
      try {
         for (int i = 0; i < listaJornadas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaJornadas.crear(getEm(), listaJornadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearJornadas() ERROR: " + e);
      }
   }

   @Override
   public List<Jornadas> consultarJornadas() {
      try {
         return persistenciaJornadas.consultarJornadas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarJornadas() ERROR: " + e);
         return null;
      }
   }

   public Jornadas consultarJornada(BigInteger secJornadas) {
      try {
         return persistenciaJornadas.consultarJornada(getEm(), secJornadas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarJornada() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarJornadasLaboralesJornada(BigInteger secJornadas) {
      try {
         return persistenciaJornadas.contarJornadasLaboralesJornada(getEm(), secJornadas);
      } catch (Exception e) {
         log.error("ERROR AdministrarJornadas contarJornadasLaboralesJornada ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarTarifasEscalafonesJornada(BigInteger secJornadas) {
      try {
         return persistenciaJornadas.contarTarifasEscalafonesJornada(getEm(), secJornadas);
      } catch (Exception e) {
         log.error("ERROR AdministrarJornadas contarTarifasEscalafonesJornada ERROR : " + e);
         return null;
      }
   }
}
