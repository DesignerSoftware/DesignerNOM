/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosRetroactivos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosRetroactivosInterface;
import InterfaceAdministrar.AdministrarConceptosRetroactivosInterface;
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
public class AdministrarConceptosRetroactivos implements AdministrarConceptosRetroactivosInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosRetroactivos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaConceptosRetroactivosInterface persistenciaConceptosRetroactivos;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void crearConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.crear(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.borrar(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void modificarConceptosRetroactivos(List<ConceptosRetroactivos> lista) {
      try {
         for (int j = 0; j < lista.size(); j++) {
            persistenciaConceptosRetroactivos.editar(getEm(), lista.get(j));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<ConceptosRetroactivos> consultarConceptosRetroactivos() {
      try {
         List<ConceptosRetroactivos> lista = persistenciaConceptosRetroactivos.buscarConceptosRetroactivos(getEm());
         return lista;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Conceptos> consultarLOVConceptos() {
      try {
         List<Conceptos> lista = persistenciaConceptos.buscarConceptos(getEm());
         return lista;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Conceptos> consultarLOVConceptosRetro() {
      try {
         List<Conceptos> lista = persistenciaConceptos.buscarConceptos(getEm());
         return lista;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
