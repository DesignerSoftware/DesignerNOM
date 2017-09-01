package Administrar;

import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarFormulaContratoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
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
 *
 * @author PROYECTO01
 */
@Stateless
public class AdministrarFormulaContrato implements AdministrarFormulaContratoInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulaContrato.class);

   @EJB
   PersistenciaFormulasContratosInterface persistenciaFormulasContratos;
   @EJB
   PersistenciaContratosInterface persistenciaContratos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
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
   public List<Formulascontratos> listFormulasContratosParaFormula(BigInteger secuencia) {
      try {
         List<Formulascontratos> lista = persistenciaFormulasContratos.formulasContratosParaFormulaSecuencia(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error listFormulasContratosParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasContratos(List<Formulascontratos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            if (listaFC.get(i).getTercero().getSecuencia() == null) {
               listaFC.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.crear(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasContratos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasContratos(List<Formulascontratos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            if (listaFC.get(i).getTercero().getSecuencia() == null) {
               listaFC.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.borrar(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasContratos Admi : " + e.toString());
      }
   }

   @Override
   public void editarFormulasContratos(List<Formulascontratos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            if (listaFC.get(i).getTercero().getSecuencia() == null) {
               listaFC.get(i).setTercero(null);
            }
            persistenciaFormulasContratos.editar(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasContratos Admi : " + e.toString());
      }
   }

   @Override
   public List<Contratos> listContratos() {
      try {
         return persistenciaContratos.buscarContratos(getEm());
      } catch (Exception e) {
         log.warn("Error listContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Periodicidades> listPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.warn("Error listPeriodicidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> listTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.warn("Error listTerceros Admi : " + e.toString());
         return null;
      }
   }

   public Formulas actualFormula(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error actualFormula Admi : " + e.toString());
         return null;
      }
   }

}
