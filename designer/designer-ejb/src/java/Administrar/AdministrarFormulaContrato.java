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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<Formulascontratos> listFormulasContratosParaFormula(BigInteger secuencia) {
      try {
         List<Formulascontratos> lista = persistenciaFormulasContratos.formulasContratosParaFormulaSecuencia(em, secuencia);
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
            persistenciaFormulasContratos.crear(em, listaFC.get(i));
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
            persistenciaFormulasContratos.borrar(em, listaFC.get(i));
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
            persistenciaFormulasContratos.editar(em, listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasContratos Admi : " + e.toString());
      }
   }

   @Override
   public List<Contratos> listContratos() {
      try {
         List<Contratos> lista = persistenciaContratos.buscarContratos(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error listContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Periodicidades> listPeriodicidades() {
      try {
         List<Periodicidades> lista = persistenciaPeriodicidades.consultarPeriodicidades(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error listPeriodicidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> listTerceros() {
      try {
         List<Terceros> lista = persistenciaTerceros.buscarTerceros(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error listTerceros Admi : " + e.toString());
         return null;
      }
   }

   public Formulas actualFormula(BigInteger secuencia) {
      try {
         Formulas form = persistenciaFormulas.buscarFormula(em, secuencia);
         return form;
      } catch (Exception e) {
         log.warn("Error actualFormula Admi : " + e.toString());
         return null;
      }
   }

}
