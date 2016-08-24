/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import InterfaceAdministrar.AdministrarHvEntrevistasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarHvEntrevistas implements AdministrarHvEntrevistasInterface {

    @EJB
    PersistenciaHvEntrevistasInterface persistenciaHvEntrevistas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public void modificarHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
        for (int i = 0; i < listHvEntrevistas.size(); i++) {
            System.out.println("Modificando...");
            persistenciaHvEntrevistas.editar(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public void borrarHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
        for (int i = 0; i < listHvEntrevistas.size(); i++) {
            System.out.println("Borrando...");
            persistenciaHvEntrevistas.borrar(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public void crearHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
        for (int i = 0; i < listHvEntrevistas.size(); i++) {
            System.out.println("Creando...");
            persistenciaHvEntrevistas.crear(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public List<HvEntrevistas> consultarHvEntrevistasPorEmpleado(BigInteger secEmpleado) {
        List<HvEntrevistas> listHvEntrevistas;
        try {
            listHvEntrevistas = persistenciaHvEntrevistas.buscarHvEntrevistasPorEmpleado(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en AdministrarHvEntrevistas hvEntrevistasPorEmplado");
            listHvEntrevistas = null;
        }
        return listHvEntrevistas;
    }

    @Override
    public HvEntrevistas consultarHvEntrevista(BigInteger secHvEntrevista) {
        HvEntrevistas hvEntrevistas;
        hvEntrevistas = persistenciaHvEntrevistas.buscarHvEntrevista(em, secHvEntrevista);
        return hvEntrevistas;
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secuencia) {
        Empleados empleado;
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            System.out.println("ERROR AdministrarHvEntrevistas  buscarEmpleado ERROR =====" + e);
            return empleado;
        }
    }

    @Override
    public List<HVHojasDeVida> buscarHVHojasDeVida(BigInteger secuencia) {
        List<HVHojasDeVida> hvHojasDeVida;
        try {
            hvHojasDeVida = persistenciaHvEntrevistas.buscarHvHojaDeVidaPorEmpleado(em, secuencia);
            return hvHojasDeVida;
        } catch (Exception e) {
            hvHojasDeVida = null;
            System.out.println("ERROR AdministrarHvEntrevistas  buscarHVHojasDeVida ERROR =====" + e);
            return hvHojasDeVida;
        }
    }
}
