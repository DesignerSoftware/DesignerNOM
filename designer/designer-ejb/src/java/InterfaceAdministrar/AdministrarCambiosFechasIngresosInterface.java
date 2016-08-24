/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author user
 */
public interface AdministrarCambiosFechasIngresosInterface {

    public void obtenerConexion(String idSesion);

    public Empleados buscarEmpleado(BigInteger secuencia);

    public void cambiarFechaIngreso(BigInteger secuenciaEmpleado, Date fechaAntigua, Date fechaNueva);

}
