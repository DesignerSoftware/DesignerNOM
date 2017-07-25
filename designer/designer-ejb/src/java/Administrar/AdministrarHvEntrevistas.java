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
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarHvEntrevistas implements AdministrarHvEntrevistasInterface {

   private static Logger log = Logger.getLogger(AdministrarHvEntrevistas.class);

    @EJB
    PersistenciaHvEntrevistasInterface persistenciaHvEntrevistas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaHVHojasDeVidaInterface persistenciaHVHojasDeVida;
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
            log.warn("Modificando...");
            persistenciaHvEntrevistas.editar(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public void borrarHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
        for (int i = 0; i < listHvEntrevistas.size(); i++) {
            log.warn("Borrando...");
            persistenciaHvEntrevistas.borrar(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public void crearHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
        for (int i = 0; i < listHvEntrevistas.size(); i++) {
            if (listHvEntrevistas.get(i).getHojadevida() == null) {
                listHvEntrevistas.get(i).setHojadevida(new HVHojasDeVida());
            }
            persistenciaHvEntrevistas.crear(em, listHvEntrevistas.get(i));
        }
    }

    @Override
    public List<HvEntrevistas> consultarHvEntrevistasPorEmpleado(BigInteger secPersona) {
        List<HvEntrevistas> listHvEntrevistas;
        try {
            listHvEntrevistas = persistenciaHvEntrevistas.buscarHvEntrevistasPorEmpleado(em, secPersona);
        } catch (Exception e) {
            log.warn("Error en AdministrarHvEntrevistas hvEntrevistasPorEmplado");
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
            log.warn("ERROR AdministrarHvEntrevistas  buscarEmpleado ERROR =====" + e);
            return empleado;
        }
    }

    @Override
    public List<HVHojasDeVida> buscarHVHojasDeVida(BigInteger secuencia) {
        List<HVHojasDeVida> hvHojasDeVida;
        try {
            hvHojasDeVida = persistenciaHvEntrevistas.buscarHvHojaDeVidaPorPersona(em, secuencia);
            return hvHojasDeVida;
        } catch (Exception e) {
            log.warn("ERROR AdministrarHvEntrevistas  buscarHVHojasDeVida ERROR =====" + e);
            return null;
        }
    }

    @Override
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
        try {
            HVHojasDeVida hojaVida = persistenciaHVHojasDeVida.hvHojaDeVidaPersona(em, secuencia);
            return hojaVida;
        } catch (Exception e) {
            log.warn("Error obtenerHojaVidaPersona Admi : " + e.toString());
            return null;
        }
    }
}
