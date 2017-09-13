/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosEstructuras;
import Entidades.UsuariosFiltros;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosFiltrosInterface;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
import java.math.BigDecimal;
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
public class AdministrarUsuariosFiltros implements AdministrarUsuariosFiltrosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosFiltros.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaUsuariosFiltrosInterface persistenciaUsuariosFiltros;

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
   public List<UsuariosFiltros> consultarUsuariosFiltros(BigInteger secUsuarioEstructura) {
      try {
         return persistenciaUsuariosFiltros.consultarUsuariosFiltros(getEm(), secUsuarioEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarUsuariosFiltros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearUsuarioFiltro(List<UsuariosFiltros> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            if (listCrear.get(i).getUsuarioestructura().getSecuencia() == null) {
               listCrear.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if (listCrear.get(i).getUsuariovista().getSecuencia() == null) {
               listCrear.get(i).setUsuariovista(new UsuariosVistas());
            }
            persistenciaUsuariosFiltros.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearUsuarioFiltro() ERROR: " + e);
      }
   }

   @Override
   public void modificarUsuarioFiltro(List<UsuariosFiltros> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            if (listModificar.get(i).getUsuarioestructura().getSecuencia() == null) {
               listModificar.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if (listModificar.get(i).getUsuariovista().getSecuencia() == null) {
               listModificar.get(i).setUsuariovista(new UsuariosVistas());
            }
            persistenciaUsuariosFiltros.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarUsuarioFiltro() ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuarioFiltro(List<UsuariosFiltros> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            if (listBorrar.get(i).getUsuarioestructura().getSecuencia() == null) {
               listBorrar.get(i).setUsuarioestructura(new UsuariosEstructuras());
            }
            if (listBorrar.get(i).getUsuariovista().getSecuencia() == null) {
               listBorrar.get(i).setUsuariovista(new UsuariosVistas());
            }
            persistenciaUsuariosFiltros.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarUsuarioFiltro() ERROR: " + e);
      }
   }

   @Override
   public BigDecimal contarUsuariosFiltros(BigInteger secUsuarioEstructura) {
      try {
         return persistenciaUsuariosFiltros.contarUsuariosFiltros(getEm(), secUsuarioEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarUsuariosFiltros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearFiltroUsuario(BigInteger secuenciaUsuarioVista) {
      try {
         persistenciaUsuariosFiltros.crearFiltroUsuario(getEm(), secuenciaUsuarioVista);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearFiltroUsuario() ERROR: " + e);
      }
   }

}
