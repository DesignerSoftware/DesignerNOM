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

/**
 *
 * @author PROYECTO01
 */
@Stateless
public class AdministrarFormulaNovedad implements AdministrarFormulaNovedadInterface {

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

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<FormulasNovedades> listFormulasNovedadesParaFormula(BigInteger secuencia) {
        try {
            List<FormulasNovedades> lista = persistenciaFormulasNovedades.formulasNovedadesParaFormulaSecuencia(em,secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listFormulasNovedadesParaFormula Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearFormulasNovedades(List<FormulasNovedades> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasNovedades.crear(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearFormulasNovedades Admi : " + e.toString());
        }
    }

    @Override
    public void editarFormulasNovedades(List<FormulasNovedades> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasNovedades.editar(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearFormulasNovedades Admi : " + e.toString());
        }
    }

    @Override
    public void borrarFormulasNovedades(List<FormulasNovedades> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasNovedades.borrar(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarFormulasNovedades Admi : " + e.toString());
        }
    }

    @Override
    public List<Formulas> listFormulas(BigInteger secuencia) {
        try {
            Formulas form = persistenciaFormulas.buscarFormula(em,secuencia);
            List<Formulas> lista = new ArrayList<Formulas>();
            lista.add(form);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listFormulas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Formulas formulaActual(BigInteger secuencia) {
        try {
            Formulas form = persistenciaFormulas.buscarFormula(em,secuencia);
            return form;
        } catch (Exception e) {
            return null;
        }
    }

}
