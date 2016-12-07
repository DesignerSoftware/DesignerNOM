/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.EvalEvaluadores;
import Entidades.Evalconvocatorias;
import Entidades.Evalplanillas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReporteEvalCompetenciaInterface {

    public void obtenerConexion(String idSesion);

    public ParametrosReportes parametrosDeReporte();

    /**
     *
     * @return
     */
    public List<Inforeportes> listInforeportesUsuario();

    /**
     *
     * @return
     */
    public List<Empresas> listEmpresas();

    public List<Empleados> listEmpleados();

    public List<Estructuras> listEstructuras();

    public List<Evalplanillas> listPlanillas();

    public List<Evalconvocatorias> listConvocatorias(BigInteger secEmpleado);

    public List<Evalconvocatorias> listConvocatorias();

    public List<EvalEvaluadores> listEvaluadores();

    public void modificarParametrosReportes(ParametrosReportes parametroInforme);

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);
}
