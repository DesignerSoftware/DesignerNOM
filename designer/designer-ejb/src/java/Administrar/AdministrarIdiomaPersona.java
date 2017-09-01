/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarIdiomaPersonaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIdiomasInterface;
import InterfacePersistencia.PersistenciaIdiomasPersonasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
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
public class AdministrarIdiomaPersona implements AdministrarIdiomaPersonaInterface {

   private static Logger log = Logger.getLogger(AdministrarIdiomaPersona.class);

   @EJB
   PersistenciaIdiomasPersonasInterface persistenciaIdiomasPersonas;
   @EJB
   PersistenciaIdiomasInterface persistenciaIdiomas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;

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
   public void crearIdiomasPersonas(List<IdiomasPersonas> listaID) {
      try {
         for (int i = 0; i < listaID.size(); i++) {
            persistenciaIdiomasPersonas.crear(getEm(), listaID.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarIdiomasPersonas(List<IdiomasPersonas> listaID) {
      try {
         for (int i = 0; i < listaID.size(); i++) {
            if (listaID.get(i).getIdioma().getSecuencia() == null) {
               listaID.get(i).setIdioma(null);
            }
            persistenciaIdiomasPersonas.borrar(getEm(), listaID.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
      }
   }

   @Override
   public void editarIdiomasPersonas(List<IdiomasPersonas> listaID) {
      try {
         for (int i = 0; i < listaID.size(); i++) {
            if (listaID.get(i).getIdioma().getSecuencia() == null) {
               listaID.get(i).setIdioma(null);
            }
            persistenciaIdiomasPersonas.editar(getEm(), listaID.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearIdiomarPersonas Admi : " + e.toString());
      }
   }

   @Override
   public List<IdiomasPersonas> listIdiomasPersonas(BigInteger secuencia) {
      try {
         return persistenciaIdiomasPersonas.idiomasPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listIdiomasPersonas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Idiomas> listIdiomas() {
      try {
         return persistenciaIdiomas.buscarIdiomas(getEm());
      } catch (Exception e) {
         log.warn("Error lisIdiomas Admi : " + e.toString());
         return null;
      }
   }

   public Empleados empleadoActual(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Personas obtenerPersonaPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleado.buscarPersonaPorEmpleado(em, secEmpleado);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + " ERROR en obtenerPersonaPorEmpleado() : " + e.getMessage());
         return null;
      }
   }
}
