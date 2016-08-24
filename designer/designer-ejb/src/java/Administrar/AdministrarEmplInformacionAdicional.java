/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.GruposInfAdicionales;
import Entidades.InformacionesAdicionales;
import InterfaceAdministrar.AdministrarEmplInformacionAdicionalInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaGruposInfAdicionalesInterface;
import InterfacePersistencia.PersistenciaInformacionesAdicionalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEmplInformacionAdicional implements AdministrarEmplInformacionAdicionalInterface {

    @EJB
    PersistenciaInformacionesAdicionalesInterface persistenciaInformacionesAdicionales;
    @EJB
    PersistenciaGruposInfAdicionalesInterface persistenciaGruposInfAdicionales;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;

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
    public List<InformacionesAdicionales> listInformacionesAdicionalesEmpleado(BigInteger secuencia) {
        try {
            List<InformacionesAdicionales> listEmpleado = persistenciaInformacionesAdicionales.informacionAdicionalEmpleadoSecuencia(em,secuencia);
            return listEmpleado;
        } catch (Exception e) {
            System.out.println("Error listInformacionesAdicionalesEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarInformacionAdicional(List<InformacionesAdicionales> listInfoA) {
        try {
            for (int i = 0; i < listInfoA.size(); i++) {
                if (listInfoA.get(i).getGrupo().getSecuencia() == null) {
                    listInfoA.get(i).setGrupo(null);
                }
                persistenciaInformacionesAdicionales.borrar(em,listInfoA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarInformacionAdicional Admi : " + e.toString());
        }
    }

    @Override
    public void crearInformacionAdicional(List<InformacionesAdicionales> listInfoA) {
        try {
            for (int i = 0; i < listInfoA.size(); i++) {
                if (listInfoA.get(i).getGrupo().getSecuencia() == null) {
                    listInfoA.get(i).setGrupo(null);
                }
                persistenciaInformacionesAdicionales.crear(em,listInfoA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearInformacionAdicional Admi : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionAdicional(List<InformacionesAdicionales> listInfoA) {
        try {
            for (int i = 0; i < listInfoA.size(); i++) {
                if (listInfoA.get(i).getGrupo().getSecuencia() == null) {
                    listInfoA.get(i).setGrupo(null);
                }
                persistenciaInformacionesAdicionales.editar(em,listInfoA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error modificarInformacionAdicional Admi : " + e.toString());
        }
    }

    @Override
    public List<GruposInfAdicionales> listGruposInfAdicionales() {
        try {
            List<GruposInfAdicionales> listGrupos = persistenciaGruposInfAdicionales.buscarGruposInfAdicionales(em);
            return listGrupos;
        } catch (Exception e) {
            System.out.println("Error listGruposInfAdicionales Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleadoSecuencia(em,secuencia);
            return empl;
        } catch (Exception e) {
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

}
