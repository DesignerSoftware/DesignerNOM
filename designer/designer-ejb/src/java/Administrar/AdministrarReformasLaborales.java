/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.DetallesReformasLaborales;
import Entidades.ReformasLaborales;
import InterfaceAdministrar.AdministrarReformasLaboralesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesReformasLaboralesInterface;
import InterfacePersistencia.PersistenciaReformasLaboralesInterface;
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
 * 'SolucionFormula'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarReformasLaborales implements AdministrarReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(AdministrarReformasLaborales.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaReformasLaborales'.
    */
   @EJB
   PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDetallesReformasLaborales'.
    */
   @EJB
   PersistenciaDetallesReformasLaboralesInterface persistenciaDetallesReformasLaborales;
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
   public List<ReformasLaborales> listaReformasLaborales() {
      try {
         return persistenciaReformasLaborales.buscarReformasLaborales(getEm());
      } catch (Exception e) {
         log.warn("Error listaReformasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.crear(getEm(), listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void editarReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.editar(getEm(), listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void borrarReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.borrar(getEm(), listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public List<DetallesReformasLaborales> listaDetalleReformasLaborales(BigInteger secuencia) {
      try {
         List<DetallesReformasLaborales> lista = persistenciaDetallesReformasLaborales.buscarDetalleReformasParaReformaSecuencia(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaDetalleReformasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL) {
      try {
         for (int i = 0; i < listaDRL.size(); i++) {
            persistenciaDetallesReformasLaborales.crear(getEm(), listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL) {
      try {
         for (int i = 0; i < listaDRL.size(); i++) {
            persistenciaDetallesReformasLaborales.editar(getEm(), listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL) {
      try {
         for (int i = 0; i < listaDRL.size(); i++) {
            persistenciaDetallesReformasLaborales.borrar(getEm(), listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   public String clonarReformaLaboral(String nuevoNombre, short codigoNuevo, short codOrigen) {
      try {
         return persistenciaDetallesReformasLaborales.clonarReformaLaboral(getEm(), nuevoNombre, codigoNuevo, codOrigen);
      } catch (Exception e) {
         log.warn("ERROR clonarReformaLaboral Administrar e: " + e.toString());
         return "ERROR EN LA TRANSACCION DESDE EL SISTEMA";
      }
   }

}
