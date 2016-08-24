package Administrar;

import Entidades.Conceptos;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author PROYECTO01
 */
@Stateless
public class AdministrarFormulaConcepto implements AdministrarFormulaConceptoInterface {

    @EJB
    PersistenciaFormulasConceptosInterface persistenciaFormulasConceptos;
    @EJB
    PersistenciaFormulasInterface persistenciaFormulas;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<FormulasConceptos> formulasConceptosParaFormula(BigInteger secuencia) {
        try {
            List<FormulasConceptos> lista = persistenciaFormulasConceptos.formulasConceptosParaFormulaSecuencia(em, secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error formulasConceptosParaFormula Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearFormulasConceptos(List<FormulasConceptos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaFormulasConceptos.crear(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void editarFormulasConceptos(List<FormulasConceptos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaFormulasConceptos.editar(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarFormulasConceptos(List<FormulasConceptos> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaFormulasConceptos.borrar(em, lista.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public List<FormulasConceptos> listFormulasConceptos() {
        try {
            List<FormulasConceptos> lista = persistenciaFormulasConceptos.buscarFormulasConceptos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listFormulasConceptos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Conceptos> listConceptos() {
        try {
            List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listConceptos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Formulas formulaActual(BigInteger secuencia) {
        try {
            Formulas form = persistenciaFormulas.buscarFormula(em, secuencia);
            return form;
        } catch (Exception e) {
            System.out.println("Error formulaActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<FormulasConceptos> cargarFormulasConcepto(BigInteger secConcepto) {
        try {
            List<FormulasConceptos> lista = persistenciaFormulasConceptos.formulasConcepto(em, secConcepto);
            if (lista.isEmpty() || lista == null) {
                System.out.println("Error cargarFormulasConcepto: formulasConcepto trae lista vacia o nula Admi");
                return null;
            } else {
                return lista;
            }
        } catch (Exception e) {
            System.out.println("Error cargarFormulasConcepto Admi : " + e.toString());
            return null;
        }
    }
    /*
     * @Override public boolean
     * verificarExistenciaConceptoFormulasConcepto(EntityManager em, BigInteger
     * secConcepto) { try { boolean lista =
     * persistenciaFormulasConceptos.verificarExistenciaConceptoFormulasConcepto(em,
     * secConcepto); return lista; } catch (Exception e) {
     * System.out.println("Error verificarExistenciaConceptoFormulasConcepto
     * Admi : " + e.toString()); return false; } }
     *
     * @Override public boolean cargarFormulasConcepto(EntityManager em,
     * BigInteger secuencia, BigInteger secFormula) { try { boolean lista =
     * persistenciaFormulasConceptos.verificarFormulaCargue_Concepto(em,
     * secuencia, secFormula); return lista; } catch (Exception e) {
     * System.out.println("Error cargarFormulaConcepto Admi : " + e.toString());
     * return false; }
    }
     */
}
