/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalDimensiones;
import InterfaceAdministrar.AdministrarEvalDimensionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalDimensionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEvalDimensiones implements AdministrarEvalDimensionesInterface {

    @EJB
    PersistenciaEvalDimensionesInterface persistenciaTiposCentrosCostos;
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
    public void modificarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
        for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposCentrosCostos.editar(em,listaEvalDimensiones.get(i));
        }
    }

    @Override
    public void borrarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
        for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposCentrosCostos.borrar(em,listaEvalDimensiones.get(i));
        }
    }

    @Override
    public void crearEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones) {
        for (int i = 0; i < listaEvalDimensiones.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposCentrosCostos.crear(em,listaEvalDimensiones.get(i));
        }
    }

    @Override
    public EvalDimensiones consultarEvalDimension(BigInteger secTipoCentrosCostos) {
        EvalDimensiones evalDimensiones;
        evalDimensiones = persistenciaTiposCentrosCostos.buscarEvalDimension(em,secTipoCentrosCostos);
        return evalDimensiones;
    }

    @Override
    public List<EvalDimensiones> consultarEvalDimensiones() {
        List<EvalDimensiones> listEvalDimensiones;
        listEvalDimensiones = persistenciaTiposCentrosCostos.buscarEvalDimensiones(em);
        return listEvalDimensiones;
    }

    @Override
    public BigInteger verificarEvalPlanillas(BigInteger secuenciaTiposAuxilios) {
        BigInteger verificarEvalPlanillas = null;
        try {
            verificarEvalPlanillas = persistenciaTiposCentrosCostos.contradorEvalPlanillas(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRAREVALPLANILLAS verificarEvalPlanillas ERROR :" + e);
        } finally {
            return verificarEvalPlanillas;
        }
    }
}
