/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ErroresLiquidacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarErroresLiquidacionesInterface {

    public void obtenerConexion(String idSesion);

    public List<ErroresLiquidacion> consultarErroresLiquidacionEmpleado(BigInteger secEmpleado);

    public void borrarErroresLiquidaciones(List<ErroresLiquidacion> listaErroresLiquidacion);
    public void borrarTodosErroresLiquidacion();
}
