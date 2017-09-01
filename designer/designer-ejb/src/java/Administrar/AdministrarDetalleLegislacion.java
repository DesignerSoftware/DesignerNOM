/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarDetalleLegislacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaFormulasContratosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'DetalleLegislacion'.
 *
 * @author Andres Pineda.
 */
@Stateless
public class AdministrarDetalleLegislacion implements AdministrarDetalleLegislacionInterface {

   private static Logger log = Logger.getLogger(AdministrarDetalleLegislacion.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTerceros'.
    */
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaPeriodicidades'.
    */
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaConceptos'.
    */
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasContratos'.
    */
   @EJB
   PersistenciaFormulasContratosInterface persistenciaFormulasContratos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulas'.
    */
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaContratos'.
    */
   @EJB
   PersistenciaContratosInterface persistenciaContratos;
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
   public List<Terceros> consultarLOVTerceros() {
      try {
         List<Terceros> lista = persistenciaTerceros.buscarTerceros(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listTerceros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Periodicidades> consultarLOVPeriodicidades() {
      try {
         List<Periodicidades> lista = persistenciaPeriodicidades.consultarPeriodicidades(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listPeriodicidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Formulas> consultarLOVFormulas() {
      try {
         List<Formulas> lista = persistenciaFormulas.buscarFormulas(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listFormulas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Formulascontratos> consultarListaFormulasContratosContrato(BigInteger secContrato) {
      try {
         List<Formulascontratos> lista = persistenciaFormulasContratos.formulasContratosParaContratoSecuencia(getEm(), secContrato);
         int tam = lista.size();
         if (tam >= 1) {
            for (int i = 0; i < tam; i++) {
               String aux = persistenciaConceptos.conceptoParaFormulaContrato(getEm(), lista.get(i).getFormula().getSecuencia(), lista.get(i).getFechafinal());
               lista.get(i).setStrConcepto(aux);
            }
         }
         return lista;
      } catch (Exception e) {
         log.warn("Error listFormulasContratosParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasContratos(List<Formulascontratos> listaFormulasContratos) {
      try {
         for (int i = 0; i < listaFormulasContratos.size(); i++) {
            if (listaFormulasContratos.get(i).getTercero().getSecuencia() == null) {
               listaFormulasContratos.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.crear(getEm(), listaFormulasContratos.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulaContrato Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasContratos(List<Formulascontratos> listaFormulasContratos) {
      try {
         for (int i = 0; i < listaFormulasContratos.size(); i++) {
            if (listaFormulasContratos.get(i).getTercero().getSecuencia() == null) {
               listaFormulasContratos.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.borrar(getEm(), listaFormulasContratos.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarrFormulaContrato Admi : " + e.toString());
      }
   }

   @Override
   public void modificarFormulasContratos(List<Formulascontratos> listaFormulasContratos) {
      try {
         for (int i = 0; i < listaFormulasContratos.size(); i++) {
            if (listaFormulasContratos.get(i).getTercero().getSecuencia() == null) {
               listaFormulasContratos.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.editar(getEm(), listaFormulasContratos.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulaContrato Admi : " + e.toString());
      }
   }

   @Override
   public Contratos consultarContrato(BigInteger secContrato) {
      try {
         return persistenciaContratos.buscarContratoSecuencia(getEm(), secContrato);
      } catch (Exception e) {
         log.warn("Error contratoActual Admi : " + e.toString());
         return null;
      }
   }
}
