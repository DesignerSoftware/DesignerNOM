/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosTurnos;
import Entidades.TurnosEmpleados;
import Entidades.VWEstadosExtras;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarATHoraExtraInterface {

    public void obtenerConexion(String idSesion);

    public List<Empleados> buscarEmpleados();

    public List<TurnosEmpleados> buscarTurnosEmpleadosPorEmpleado(BigInteger secuencia);

    public void crearTurnosEmpleados(List<TurnosEmpleados> listaTR);

    public void editarTurnosEmpleados(List<TurnosEmpleados> listaTR);

    public void borrarTurnosEmpleados(List<TurnosEmpleados> listaTR);

    public List<VWEstadosExtras> buscarDetallesHorasExtrasPorTurnoEmpleado(BigInteger secuencia);

    public List<MotivosTurnos> lovMotivosTurnos();

    public List<Estructuras> lovEstructuras();

}
