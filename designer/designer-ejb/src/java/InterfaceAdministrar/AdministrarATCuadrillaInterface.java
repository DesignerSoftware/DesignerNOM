/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cuadrillas;
import Entidades.DetallesTurnosRotativos;
import Entidades.Empleados;
import Entidades.Turnosrotativos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarATCuadrillaInterface {

    public void obtenerConexion(String idSesion);

    public List<Cuadrillas> obtenerCuadrillas();

    public void crearCuadrillas(List<Cuadrillas> listaC);

    public void editarCuadrillas(List<Cuadrillas> listaC);

    public void borrarCuadrillas(List<Cuadrillas> listaC);

    public List<Turnosrotativos> obtenerTurnosRotativos(BigInteger secuencia);

    public void crearTurnosRotativos(List<Turnosrotativos> listaTR);

    public void editarTurnosRotativos(List<Turnosrotativos> listaTR);

    public void borrarTurnosRotativos(List<Turnosrotativos> listaTR);

    public List<DetallesTurnosRotativos> obtenerDetallesTurnosRotativos(BigInteger secuencia);

    public void crearDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR);

    public void editarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR);

    public void borrarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR);

    public List<Empleados> lovEmpleados();

    public void borrarProgramacionCompleta();

    public List<Empleados> consultarEmpleadosProcesoBuscarEmpleadosCuadrillas();

    public List<Cuadrillas> obtenerInfoCuadrillasPorEmpleado(BigInteger secuencia);

}
