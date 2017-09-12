/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Grupostiposentidades;
import InterfaceAdministrar.AdministrarGruposTiposEntidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
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
public class AdministrarGruposTiposEntidades implements AdministrarGruposTiposEntidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposTiposEntidades.class);

   @EJB
   PersistenciaGruposTiposEntidadesInterface persistenciaGruposTiposEntidades;

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
   public void modificarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
      try {
         for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaGruposTiposEntidades.editar(getEm(), listaGruposTiposEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarGruposTiposEntidades() ERROR: " + e);
      }
   }

   @Override
   public void borrarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
      try {
         for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaGruposTiposEntidades.borrar(getEm(), listaGruposTiposEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarGruposTiposEntidades() ERROR: " + e);
      }
   }

   @Override
   public void crearGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
      try {
         for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaGruposTiposEntidades.crear(getEm(), listaGruposTiposEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearGruposTiposEntidades() ERROR: " + e);
      }
   }

   @Override
   public List<Grupostiposentidades> consultarGruposTiposEntidades() {
      try {
         return persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarGruposTiposEntidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Grupostiposentidades consultarGrupoTipoEntidad(BigInteger secGruposTiposEntidades) {
      try {
         return persistenciaGruposTiposEntidades.consultarGrupoTipoEntidad(getEm(), secGruposTiposEntidades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarGrupoTipoEntidad() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarTSgruposTiposEntidadesTipoEntidad(BigInteger secGruposTiposEntidades) {
      try {
         return persistenciaGruposTiposEntidades.contarTSgruposTiposEntidadesTipoEntidad(getEm(), secGruposTiposEntidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposTiposEntidades contarTSgruposTiposEntidadesTipoEntidad ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarTiposEntidadesGrupoTipoEntidad(BigInteger secGruposTiposEntidades) {
      try {
         return persistenciaGruposTiposEntidades.contarTiposEntidadesGrupoTipoEntidad(getEm(), secGruposTiposEntidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposTiposEntidades contarTiposEntidadesGrupoTipoEntidad ERROR : " + e);
         return null;
      }
   }
}
