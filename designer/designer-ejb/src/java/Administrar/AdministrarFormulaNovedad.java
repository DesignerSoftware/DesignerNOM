package Administrar;

import Entidades.Formulas;
import Entidades.FormulasNovedades;
import InterfaceAdministrar.AdministrarFormulaNovedadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasNovedadesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class AdministrarFormulaNovedad implements AdministrarFormulaNovedadInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulaNovedad.class);

   @EJB
   PersistenciaFormulasNovedadesInterface persistenciaFormulasNovedades;
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
   public List<FormulasNovedades> listFormulasNovedadesParaFormula(BigInteger secuencia) {
      log.warn("Administrar.AdministrarFormulaNovedad.listFormulasNovedadesParaFormula() secuencia : " + secuencia);
      try {
         return persistenciaFormulasNovedades.formulasNovedadesParaFormulaSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listFormulasNovedadesParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasNovedades(List<FormulasNovedades> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasNovedades.crear(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void editarFormulasNovedades(List<FormulasNovedades> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasNovedades.editar(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasNovedades(List<FormulasNovedades> listFN) {
      try {
         for (int i = 0; i < listFN.size(); i++) {
            persistenciaFormulasNovedades.borrar(getEm(), listFN.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasNovedades Admi : " + e.toString());
      }
   }

   @Override
   public List<Formulas> listFormulas(BigInteger secuencia) {
      try {
         Formulas form = persistenciaFormulas.buscarFormula(getEm(), secuencia);
         List<Formulas> lista = new ArrayList<Formulas>();
         lista.add(form);
         return lista;
      } catch (Exception e) {
         log.warn("Error listFormulas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Formulas formulaActual(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

}
