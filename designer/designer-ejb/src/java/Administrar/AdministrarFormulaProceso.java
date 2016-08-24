/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarFormulaProcesoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasProcesosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
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
public class AdministrarFormulaProceso implements AdministrarFormulaProcesoInterface {

    @EJB
    PersistenciaFormulasProcesosInterface persistenciaFormulasProcesos;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
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
    public List<FormulasProcesos> listFormulasProcesosParaFormula(BigInteger secuencia) {
        try {
            List<FormulasProcesos> lista = persistenciaFormulasProcesos.formulasProcesosParaFormulaSecuencia(em,secuencia);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listFormulasProcesosParaFormula Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearFormulasProcesos(List<FormulasProcesos> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasProcesos.crear(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearFormulasProcesos Admi : " + e.toString());
        }
    }

    @Override
    public void editarFormulasProcesos(List<FormulasProcesos> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasProcesos.editar(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarFormulasProcesos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarFormulasProcesos(List<FormulasProcesos> listFN) {
        try {
            for (int i = 0; i < listFN.size(); i++) {
                persistenciaFormulasProcesos.borrar(em,listFN.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarFormulasProcesos Admi : " + e.toString());
        }
    }

    @Override
    public List<Procesos> listProcesos(BigInteger secuencia) {
        try {
            List<Procesos> lista = persistenciaProcesos.buscarProcesos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listProcesos Admi : " + e.toString());
            return null;
        }
    }

    public Formulas formulaActual(BigInteger secuencia) {
        try {
            Formulas form = persistenciaFormulas.buscarFormula(em,secuencia);
            return form;
        } catch (Exception e) {
            System.out.println("Error formulaActual Admi : " + e.toString());
            return null;
        }
    }
}
