/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.EvalResultadosConv;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEvalResultadosConvInterface {

    public void obtenerConexion(String idSesion);
    public void crear(List<EvalResultadosConv> listCrear);
    public void borrar(List<EvalResultadosConv> listBorrar);
    public void editar(List<EvalResultadosConv> listModificar);
    public List<EvalResultadosConv> buscarEvalResultadosConvocatorias(BigInteger secEmpleado);
     public Empleados empleadoActual(BigInteger secuenciaP);
}
