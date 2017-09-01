/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.EscalafonesSalariales;
import Entidades.GruposSalariales;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarEscalafonesSalarialesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEscalafonesSalarialesInterface;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EscalafonSalarial'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarEscalafonesSalariales implements AdministrarEscalafonesSalarialesInterface {

   private static Logger log = Logger.getLogger(AdministrarEscalafonesSalariales.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEscalafonesSalariales'.
    */
   @EJB
   PersistenciaEscalafonesSalarialesInterface persistenciaEscalafonesSalariales;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaGruposSalariales'.
    */
   @EJB
   PersistenciaGruposSalarialesInterface persistenciaGruposSalariales;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaGruposSalariales'.
    */
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
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

   @Override
   public List<EscalafonesSalariales> listaEscalafonesSalariales() {
      try {
         return persistenciaEscalafonesSalariales.buscarEscalafones(getEm());
      } catch (Exception e) {
         log.warn("Error listaEscalafonesSalariales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
      try {
         for (int i = 0; i < listaES.size(); i++) {
            persistenciaEscalafonesSalariales.crear(getEm(), listaES.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearEscalafonesSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void editarEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
      try {
         for (int i = 0; i < listaES.size(); i++) {
            persistenciaEscalafonesSalariales.editar(getEm(), listaES.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarEscalafonesSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void borrarEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
      try {
         for (int i = 0; i < listaES.size(); i++) {
            persistenciaEscalafonesSalariales.borrar(getEm(), listaES.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarEscalafonesSalariales Admi : " + e.toString());
      }
   }

   @Override
   public List<GruposSalariales> listaGruposSalarialesParaEscalafonSalarial(BigInteger secEscalafon) {
      try {
         return persistenciaGruposSalariales.buscarGruposSalarialesParaEscalafonSalarial(getEm(), secEscalafon);
      } catch (Exception e) {
         log.warn("Error listaGruposSalarialesParaEscalafonSalarial Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearGruposSalariales(List<GruposSalariales> listaGS) {
      try {
         for (int i = 0; i < listaGS.size(); i++) {
            persistenciaGruposSalariales.crear(getEm(), listaGS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void editarGruposSalariales(List<GruposSalariales> listaGS) {
      try {
         for (int i = 0; i < listaGS.size(); i++) {
            persistenciaGruposSalariales.editar(getEm(), listaGS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarGruposSalariales Admi : " + e.toString());
      }
   }

   @Override
   public void borrarGruposSalariales(List<GruposSalariales> listaGS) {
      try {
         for (int i = 0; i < listaGS.size(); i++) {
            persistenciaGruposSalariales.borrar(getEm(), listaGS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error persistenciaEscalafonesSalariales Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

}
