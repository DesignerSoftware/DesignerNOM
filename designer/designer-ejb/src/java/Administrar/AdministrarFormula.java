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
import javax.persistence.EntityManagerFactory;
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
   public List<Formulas> formulas() {
      try {
         return persistenciaFormulas.lovFormulas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificar(List<Formulas> listFormulasModificadas) {
      try {
         for (int i = 0; i < listFormulasModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listFormulasModificadas.get(i).isPeriodicidadFormula() == true) {
               listFormulasModificadas.get(i).setPeriodicidadindependiente("S");
            } else if (listFormulasModificadas.get(i).isPeriodicidadFormula() == false) {
               listFormulasModificadas.get(i).setPeriodicidadindependiente("N");
            }
            persistenciaFormulas.editar(getEm(), listFormulasModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrar(Formulas formula) {
      try {
         persistenciaFormulas.borrar(getEm(), formula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crear(Formulas formula) {
      try {
         persistenciaFormulas.crear(getEm(), formula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void clonarFormula(String nombreCortoOrigen, String nombreCortoClon, String nombreLargoClon, String observacionClon) {
      try {
         persistenciaFormulas.clonarFormulas(getEm(), nombreCortoOrigen, nombreCortoClon, nombreLargoClon, observacionClon);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void operandoFormula(BigInteger secFormula) {
      try {
         persistenciaFormulas.operandoFormulas(getEm(), secFormula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Formulas buscarFormulaSecuencia(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarFormulaSecuencia Admi : " + e.toString());
         return null;
      }
   }
}
