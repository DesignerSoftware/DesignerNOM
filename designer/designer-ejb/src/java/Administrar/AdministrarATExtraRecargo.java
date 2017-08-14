/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Contratos;
import Entidades.DetallesExtrasRecargos;
import Entidades.ExtrasRecargos;
import Entidades.TiposDias;
import Entidades.TiposJornadas;
import InterfaceAdministrar.AdministrarATExtraRecargoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaDetallesExtrasRecargosInterface;
import InterfacePersistencia.PersistenciaExtrasRecargosInterface;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
import InterfacePersistencia.PersistenciaTiposJornadasInterface;
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
 * 'ATExtraRecargo'.
 *
 * @author Andres Pineda
 */
@Stateful
public class AdministrarATExtraRecargo implements AdministrarATExtraRecargoInterface {

   private static Logger log = Logger.getLogger(AdministrarATExtraRecargo.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaExtrasRecargos'.
    */
   @EJB
   PersistenciaExtrasRecargosInterface persistenciaExtrasRecargos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDetallesExtrasRecargos'.
    */
   @EJB
   PersistenciaDetallesExtrasRecargosInterface persistenciaDetallesExtrasRecargos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposDias'.
    */
   @EJB
   PersistenciaTiposDiasInterface persistenciaTiposDias;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposJornadas'.
    */
   @EJB
   PersistenciaTiposJornadasInterface persistenciaTiposJornadas;
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
    * 'persistenciaConceptos'.
    */
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
   public List<ExtrasRecargos> consultarExtrasRecargos() {
      try {
         List<ExtrasRecargos> lista = persistenciaExtrasRecargos.buscarExtrasRecargos(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listExtrasRecargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearExtrasRecargos(List<ExtrasRecargos> listaER) {
      try {
         for (int i = 0; i < listaER.size(); i++) {
            persistenciaExtrasRecargos.crear(getEm(), listaER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public void editarExtrasRecargos(List<ExtrasRecargos> listaER) {
      try {
         for (int i = 0; i < listaER.size(); i++) {
            persistenciaExtrasRecargos.editar(getEm(), listaER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarExtrasRecargos(List<ExtrasRecargos> listaER) {
      try {
         for (int i = 0; i < listaER.size(); i++) {
            persistenciaExtrasRecargos.borrar(getEm(), listaER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public List<DetallesExtrasRecargos> consultarDetallesExtrasRecargos(BigInteger secuencia) {
      try {
         List<DetallesExtrasRecargos> lista = persistenciaDetallesExtrasRecargos.buscaDetallesExtrasRecargosPorSecuenciaExtraRecargo(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error listDetallesExtrasRecargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
      try {
         for (int i = 0; i < listaDER.size(); i++) {
            persistenciaDetallesExtrasRecargos.crear(getEm(), listaDER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDetallesExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
      try {
         for (int i = 0; i < listaDER.size(); i++) {
            persistenciaDetallesExtrasRecargos.editar(getEm(), listaDER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDetallesExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetallesExtrasRecargos(List<DetallesExtrasRecargos> listaDER) {
      try {
         for (int i = 0; i < listaDER.size(); i++) {
            persistenciaDetallesExtrasRecargos.borrar(getEm(), listaDER.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDetallesExtrasRecargos Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposDias> consultarLOVListaTiposDias() {
      try {
         List<TiposDias> lista = persistenciaTiposDias.buscarTiposDias(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listTiposDias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposJornadas> consultarLOVTiposJornadas() {
      try {
         List<TiposJornadas> lista = persistenciaTiposJornadas.buscarTiposJornadas(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listTiposJornadas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Contratos> consultarLOVContratos() {
      try {
         List<Contratos> lista = persistenciaContratos.buscarContratos(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> consultarLOVConceptos() {
      try {
         List<Conceptos> lista = persistenciaConceptos.buscarConceptos(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listConceptos Admi : " + e.toString());
         return null;
      }
   }
}
