/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Proyecciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarProyeccionesInterface {

    public void obtenerConexion(String idSesion);

    public void borrarProyecciones(List<Proyecciones> lista);

    public List<Proyecciones> consultarProyeccionesEmpleado(BigInteger secEmpleado);

    public List<Empleados> consultarLOVEmpleados();
}
