/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposSalariales;
import Entidades.VigenciasGruposSalariales;
import InterfaceAdministrar.AdministrarGrupoSalarialInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposSalarialesInterface;
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
public class AdministrarGrupoSalarial implements AdministrarGrupoSalarialInterface {

   private static Logger log = Logger.getLogger(AdministrarGrupoSalarial.class);

   @EJB
   PersistenciaGruposSalarialesInterface persistenciaGruposSalariales;
   @EJB
   PersistenciaVigenciasGruposSalarialesInterface persistenciaVigenciasGruposSalariales;
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
   public List<GruposSalariales> listGruposSalariales() {
      try {
         return persistenciaGruposSalariales.buscarGruposSalariales(getEm());
      } catch (Exception e) {
         log.warn("Error listGruposSalariales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearGruposSalariales(List<GruposSalariales> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaGruposSalariales.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void editarGruposSalariales(List<GruposSalariales> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaGruposSalariales.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void borrarGruposSalariales(List<GruposSalariales> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaGruposSalariales.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public List<VigenciasGruposSalariales> lisVigenciasGruposSalarialesSecuencia(BigInteger secuencia) {
      try {
         List<VigenciasGruposSalariales> VgruposSalariales = persistenciaVigenciasGruposSalariales.buscarVigenciaGrupoSalarialSecuenciaGrupoSal(getEm(), secuencia);
         return VgruposSalariales;
      } catch (Exception e) {
         log.warn("Error lisVigenciasGruposSalarialesSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaVigenciasGruposSalariales.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaVigenciasGruposSalariales.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaVigenciasGruposSalariales.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasGruposSalariales Admi : " + e.toString());
      }
   }
}
