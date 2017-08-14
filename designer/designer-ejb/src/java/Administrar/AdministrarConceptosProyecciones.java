/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosProyecciones;
import InterfaceAdministrar.AdministrarConceptosProyeccionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosProyeccionesInterface;
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
public class AdministrarConceptosProyecciones implements AdministrarConceptosProyeccionesInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosProyecciones.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaConceptosProyeccionesInterface persistenciaConceptosProyecciones;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void crearConceptosProyecciones(List<ConceptosProyecciones> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.crear(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarConceptosProyecciones(List<ConceptosProyecciones> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.borrar(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void modificarConceptosProyecciones(List<ConceptosProyecciones> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosProyecciones.editar(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<ConceptosProyecciones> consultarConceptosProyecciones() {
      try {
         List<ConceptosProyecciones> lista = persistenciaConceptosProyecciones.buscarConceptosProyecciones(getEm());
         return lista;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Conceptos> consultarLOVConceptos() {
      try {
         List<Conceptos> lista = persistenciaConceptos.buscarConceptos(getEm());
         return lista;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
