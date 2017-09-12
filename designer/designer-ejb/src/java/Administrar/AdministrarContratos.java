/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Contratos;
import Entidades.TiposCotizantes;
import InterfaceAdministrar.AdministrarContratosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaTiposCotizantesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Contratos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarContratos implements AdministrarContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarContratos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaContratos'.
    */
   @EJB
   PersistenciaContratosInterface persistenciaContratos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposCotizantes'.
    */
   @EJB
   PersistenciaTiposCotizantesInterface persistenciaTiposCotizantes;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
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
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerConexion() ERROR: " + e);
      }
   }

   @Override
   public List<Contratos> consultarContratos() {
      try {
         return persistenciaContratos.lovContratos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarContratos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposCotizantes> consultaLOVTiposCotizantes() {
      try {
         return persistenciaTiposCotizantes.lovTiposCotizantes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultaLOVTiposCotizantes() ERROR: " + e);
      }
      return null;
   }

   @Override
   public void modificarConceptos(List<Contratos> listContratosModificados) {
      try {
         for (int i = 0; i < listContratosModificados.size(); i++) {
            if (listContratosModificados.get(i).getTipocotizante().getSecuencia() == null) {
               listContratosModificados.get(i).setTipocotizante(null);
            }
            persistenciaContratos.editar(getEm(), listContratosModificados.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarConceptos() ERROR: " + e);
      }
   }

   @Override
   public void borrarConceptos(List<Contratos> listaContratos) {
      try {
         for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i).getTipocotizante().getSecuencia() == null) {
               listaContratos.get(i).setTipocotizante(null);
               persistenciaContratos.borrar(getEm(), listaContratos.get(i));
            } else {
               persistenciaContratos.borrar(getEm(), listaContratos.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarConceptos() ERROR: " + e);
      }
   }

   @Override
   public void crearConceptos(List<Contratos> listaContratos) {
      try {
         for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i).getTipocotizante().getSecuencia() == null) {
               listaContratos.get(i).setTipocotizante(null);
               persistenciaContratos.crear(getEm(), listaContratos.get(i));
            } else {
               persistenciaContratos.crear(getEm(), listaContratos.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearConceptos() ERROR: " + e);
      }
   }

   @Override
   public void reproducirContrato(Short codigoOrigen, Short codigoDestino) {
      try {
         persistenciaContratos.reproducirContrato(getEm(), codigoOrigen, codigoDestino);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".reproducirContrato() ERROR: " + e);
      }
   }

}
