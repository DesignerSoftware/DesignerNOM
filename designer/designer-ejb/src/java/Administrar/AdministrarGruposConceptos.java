/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.GruposConceptos;
import Entidades.VigenciasGruposConceptos;
import InterfaceAdministrar.AdministrarGruposConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposConceptosInterface;
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
public class AdministrarGruposConceptos implements AdministrarGruposConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposConceptos.class);

   @EJB
   PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
   @EJB
   PersistenciaVigenciasGruposConceptosInterface persistenciaVigenciasGruposConceptos;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
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

   @Override
   public List<GruposConceptos> buscarGruposConceptos() {
      try {
         return persistenciaGruposConceptos.buscarGruposConceptos(getEm());
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
         return null;
      }
   }

   @Override
   public void modificarGruposConceptos(List<GruposConceptos> listaGruposConceptosModificar) {
      try {
         for (int i = 0; i < listaGruposConceptosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaGruposConceptosModificar.get(i).getFundamental() == null) {
               listaGruposConceptosModificar.get(i).setFundamental(null);
            }
            persistenciaGruposConceptos.editar(getEm(), listaGruposConceptosModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarGruposConceptos(GruposConceptos gruposConceptos) {
      try {
         persistenciaGruposConceptos.borrar(getEm(), gruposConceptos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearGruposConceptos(GruposConceptos gruposConceptos) {
      try {
         persistenciaGruposConceptos.crear(getEm(), gruposConceptos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

//Vigencias Grupos Conceptos
   @Override
   public void modificarVigenciaGruposConceptos(List<VigenciasGruposConceptos> listaVigenciasGruposConceptosModificar) {
      try {
         for (int i = 0; i < listaVigenciasGruposConceptosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasGruposConceptos.editar(getEm(), listaVigenciasGruposConceptosModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos) {
      try {
         persistenciaVigenciasGruposConceptos.borrar(getEm(), vigenciasGruposConceptos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaGruposConceptos(VigenciasGruposConceptos vigenciasGruposConceptos) {
      try {
         persistenciaVigenciasGruposConceptos.crear(getEm(), vigenciasGruposConceptos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasGruposConceptos> buscarVigenciasGruposConceptos(BigInteger secuencia) {
      try {
         return persistenciaVigenciasGruposConceptos.listVigenciasGruposConceptosPorGrupoConcepto(getEm(), secuencia);
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
         return null;
      }
   }

   //LOV DE ABAJO
   public List<Conceptos> lovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasFormales.vigenciasFormalesPersona " + e);
         return null;
      }
   }

}
