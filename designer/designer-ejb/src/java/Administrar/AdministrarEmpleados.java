/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarEmpleadosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import Persistencia.PersistenciaEmpleados;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEmpleados implements AdministrarEmpleadosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEmpleadoInterface persitenciaEmpleados;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empleados> listaEmpleados() {
        try {
            List<Empleados> lista = persitenciaEmpleados.todosEmpleados(em);
            return lista;
        } catch (Exception e) {
            System.out.println("error en AdministrarEmpleados.listaEmpleados" + e);
            return null;
        }
    }

    @Override
    public void editarEmpleado(List<Empleados> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                persitenciaEmpleados.editar(em, listaE.get(i));
            }
        } catch (Exception e) {
            System.out.println("erroe en AdministrarEmpleados.editarEmpleado : " + e);
        }
    }

}
