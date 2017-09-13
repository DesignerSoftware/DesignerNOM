/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposUnidades;
import Entidades.Unidades;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUnidadesInterface;
import InterfacePersistencia.PersistenciaTiposUnidadesInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
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
public class AdministrarUnidades implements AdministrarUnidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarUnidades.class);

   @EJB
   PersistenciaUnidadesInterface persistenciaUnidades;
   @EJB
   PersistenciaTiposUnidadesInterface persistenciaTiposUnidades;
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

   public List<Unidades> consultarUnidades() {
      try {
         return persistenciaUnidades.consultarUnidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarUnidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposUnidades> consultarTiposUnidades() {
      try {
         return persistenciaTiposUnidades.consultarTiposUnidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposUnidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarUnidades(List<Unidades> listaUnidades) {
      try {
         Unidades c;
         for (int i = 0; i < listaUnidades.size(); i++) {
            log.warn("Modificando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {
               listaUnidades.get(i).setCodigo(null);
               c = listaUnidades.get(i);
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
               listaUnidades.get(i).setTipounidad(null);
               c = listaUnidades.get(i);
            } else {
               c = listaUnidades.get(i);
            }
            persistenciaUnidades.editar(getEm(), c);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarUnidades() ERROR: " + e);
      }
   }

   @Override
   public void borrarUnidades(List<Unidades> listaUnidades) {
      try {
         for (int i = 0; i < listaUnidades.size(); i++) {
            log.warn("Borrando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {
               listaUnidades.get(i).setCodigo(null);
               persistenciaUnidades.borrar(getEm(), listaUnidades.get(i));
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
               listaUnidades.get(i).setTipounidad(null);
            } else {
               persistenciaUnidades.borrar(getEm(), listaUnidades.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarUnidades() ERROR: " + e);
      }
   }

   @Override
   public void crearUnidades(List<Unidades> listaUnidades) {
      try {
         for (int i = 0; i < listaUnidades.size(); i++) {
            log.warn("Creando...");
            if (listaUnidades.get(i).getCodigo().equals(null)) {

               listaUnidades.get(i).setCodigo(null);
               persistenciaUnidades.crear(getEm(), listaUnidades.get(i));
            } else if (listaUnidades.get(i).getTipounidad().getSecuencia() == null) {
               listaUnidades.get(i).setTipounidad(null);
            } else {
               persistenciaUnidades.crear(getEm(), listaUnidades.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearUnidades() ERROR: " + e);
      }
   }

}
