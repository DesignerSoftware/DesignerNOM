/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ElementosCausasAccidentes;
import InterfaceAdministrar.AdministrarElementosCausasAccidentesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaElementosCausasAccidentesInterface;
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
 * 'ElementosCausasAccidentes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarElementosCausasAccidentes implements AdministrarElementosCausasAccidentesInterface {

   private static Logger log = Logger.getLogger(AdministrarElementosCausasAccidentes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaElementosCausasAccidentes'.
    */
   @EJB
   PersistenciaElementosCausasAccidentesInterface persistenciaElementosCausasAccidentes;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
      try {
         ElementosCausasAccidentes elementosCausasAccidentesSeleccionada;
         for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            log.warn("Administrar Modificando...");
            elementosCausasAccidentesSeleccionada = listaElementosCausasAccidentes.get(i);
            persistenciaElementosCausasAccidentes.editar(getEm(), elementosCausasAccidentesSeleccionada);
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
      try {
         for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            log.warn("Borrando...");
            persistenciaElementosCausasAccidentes.borrar(getEm(), listaElementosCausasAccidentes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes) {
      try {
         for (int i = 0; i < listaElementosCausasAccidentes.size(); i++) {
            log.warn("Creando...");
            persistenciaElementosCausasAccidentes.crear(getEm(), listaElementosCausasAccidentes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<ElementosCausasAccidentes> consultarElementosCausasAccidentes() {
      try {
         return persistenciaElementosCausasAccidentes.buscarElementosCausasAccidentes(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ElementosCausasAccidentes consultarElementoCausaAccidente(BigInteger secElementosCausasAccidentes) {
      try {
         return persistenciaElementosCausasAccidentes.buscarElementoCausaAccidente(getEm(), secElementosCausasAccidentes);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoAccidentesCausa(BigInteger secTiposAuxilios) {
      try {
         return persistenciaElementosCausasAccidentes.contadorSoAccidentes(getEm(), secTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoAccidentes ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoAccidentesMedicosElementoCausaAccidente(BigInteger secTiposAuxilios) {
      try {
         return persistenciaElementosCausasAccidentes.contadorSoAccidentesMedicos(getEm(), secTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoAccidentesMedicos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoIndicadoresFrElementoCausaAccidente(BigInteger secTiposAuxilios) {
      try {
         return persistenciaElementosCausasAccidentes.contadorSoIndicadoresFr(getEm(), secTiposAuxilios);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARELEMENTOSCAUSASACCIDENTES contadorSoIndicadoresFr ERROR :" + e);
         return null;
      }
   }
}
