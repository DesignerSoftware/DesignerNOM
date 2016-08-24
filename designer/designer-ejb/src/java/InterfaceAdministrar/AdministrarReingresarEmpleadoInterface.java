/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarReingresarEmpleadoInterface {

    public void obtenerConexion(String idSesion);

    public void reintegrarEmpleado(BigInteger codigoEmpleado, BigInteger centroCosto, Date fechaReingreso, BigInteger empresa, Date fechaFinal);

    public List<Empleados> listaEmpleados();

    public List<Estructuras> listaEstructuras();
    
    public Date obtenerFechaRetiro(BigInteger secuenciaEmpleado);

}
