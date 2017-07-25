package Administrar;

import Entidades.Formulas;
import InterfaceAdministrar.AdministrarFormulaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarFormula implements AdministrarFormulaInterface {

   private static Logger log = Logger.getLogger(AdministrarFormula.class);

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
   public List<Formulas> formulas() {
      return persistenciaFormulas.lovFormulas(em);
   }

   @Override
   public void modificar(List<Formulas> listFormulasModificadas) {
      for (int i = 0; i < listFormulasModificadas.size(); i++) {
         log.warn("Modificando...");
         if (listFormulasModificadas.get(i).isPeriodicidadFormula() == true) {
            listFormulasModificadas.get(i).setPeriodicidadindependiente("S");
         } else if (listFormulasModificadas.get(i).isPeriodicidadFormula() == false) {
            listFormulasModificadas.get(i).setPeriodicidadindependiente("N");
         }
         persistenciaFormulas.editar(em, listFormulasModificadas.get(i));
      }
   }

   @Override
   public void borrar(Formulas formula) {
      persistenciaFormulas.borrar(em, formula);
   }

   @Override
   public void crear(Formulas formula) {
      persistenciaFormulas.crear(em, formula);
   }

   @Override
   public void clonarFormula(String nombreCortoOrigen, String nombreCortoClon, String nombreLargoClon, String observacionClon) {
      persistenciaFormulas.clonarFormulas(em, nombreCortoOrigen, nombreCortoClon, nombreLargoClon, observacionClon);
   }

   @Override
   public void operandoFormula(BigInteger secFormula) {
      persistenciaFormulas.operandoFormulas(em, secFormula);
   }

   @Override
   public Formulas buscarFormulaSecuencia(BigInteger secuencia) {
      try {
         Formulas etc = persistenciaFormulas.buscarFormula(em, secuencia);
         return etc;
      } catch (Exception e) {
         log.warn("Error buscarFormulaSecuencia Admi : " + e.toString());
         return null;
      }
   }
}
