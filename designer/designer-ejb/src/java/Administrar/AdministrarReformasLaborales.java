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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<ReformasLaborales> listaReformasLaborales() {
      try {
         List<ReformasLaborales> lista = persistenciaReformasLaborales.buscarReformasLaborales(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaReformasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.crear(em, listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void editarReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.editar(em, listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void borrarReformaLaboral(List<ReformasLaborales> listaRL) {
      try {
         for (int i = 0; i < listaRL.size(); i++) {
            persistenciaReformasLaborales.borrar(em, listaRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public List<DetallesReformasLaborales> listaDetalleReformasLaborales(BigInteger secuencia) {
      try {
         List<DetallesReformasLaborales> lista = persistenciaDetallesReformasLaborales.buscarDetalleReformasParaReformaSecuencia(em, secuencia);
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
            persistenciaDetallesReformasLaborales.crear(em, listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL) {
      try {
         for (int i = 0; i < listaDRL.size(); i++) {
            persistenciaDetallesReformasLaborales.editar(em, listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL) {
      try {
         for (int i = 0; i < listaDRL.size(); i++) {
            persistenciaDetallesReformasLaborales.borrar(em, listaDRL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDetalleReformaLaboral Admi : " + e.toString());
      }
   }

   public String clonarReformaLaboral(String nuevoNombre, short codigoNuevo, short codOrigen) {
      try {
         return persistenciaDetallesReformasLaborales.clonarReformaLaboral(em, nuevoNombre, codigoNuevo, codOrigen);
      } catch (Exception e) {
         log.warn("ERROR clonarReformaLaboral Administrar e: " + e.toString());
         return "ERROR EN LA TRANSACCION DESDE EL SISTEMA";
      }
   }

}
