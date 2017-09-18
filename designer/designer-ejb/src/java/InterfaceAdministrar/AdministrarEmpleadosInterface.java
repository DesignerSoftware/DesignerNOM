/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmpleadosInterface {

    public void obtenerConexion(String idSesion);

    public List<Empleados> listaEmpleados();

    public void editarEmpleado(List<Empleados> listaE);

    public void cambiarCodEmpl(BigDecimal codactual, BigDecimal codnuevo);

    public List<Empleados> listaEmpleadosEmpresa();
}
