/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosRedondeos;
import Entidades.TiposRedondeos;
import InterfaceAdministrar.AdministrarConceptosRedondeosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosRedondeosInterface;
import InterfacePersistencia.PersistenciaTiposRedondeosInterface;
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
public class AdministrarConceptosRedondeos implements AdministrarConceptosRedondeosInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosRedondeos.class);

   @EJB
   PersistenciaConceptosRedondeosInterface persistenciaConceptosRedondeos;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   @EJB
   PersistenciaTiposRedondeosInterface persistenciaTiposRedondeos;

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

//Conceptos Redondeos
   @Override
   public void borrarConceptosRedondeos(ConceptosRedondeos conceptosRedondeos) {
      try {
         persistenciaConceptosRedondeos.borrar(getEm(), conceptosRedondeos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearConceptosRedondeos(ConceptosRedondeos conceptosRedondeos) {
      try {
         persistenciaConceptosRedondeos.crear(getEm(), conceptosRedondeos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarConceptosRedondeos(List<ConceptosRedondeos> listaConceptosRedondeosModificar) {
      try {
         for (int i = 0; i < listaConceptosRedondeosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaConceptosRedondeos.editar(getEm(), listaConceptosRedondeosModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<ConceptosRedondeos> consultarConceptosRedondeos() {
      try {
         List<ConceptosRedondeos> actual = persistenciaConceptosRedondeos.buscarConceptosRedondeos(getEm());
         return actual;
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

   //LOV Conceptos
   @Override
   public List<Conceptos> lovConceptos() {
      try {
         List<Conceptos> actual = persistenciaConceptos.buscarConceptos(getEm());
         return actual;
      } catch (Exception e) {
         log.warn("Error lovConceptos: " + e.toString());
         return null;
      }
   }

   //LOV Tipos Redondeos
   @Override
   public List<TiposRedondeos> lovTiposRedondeos() {
      try {
         List<TiposRedondeos> actual = persistenciaTiposRedondeos.buscarTiposRedondeos(getEm());
         return actual;
      } catch (Exception e) {
         log.warn("Error lovTiposRedondeos: " + e.toString());
         return null;
      }
   }

}
