/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Personas;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasTallasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposTallasInterface;
import InterfacePersistencia.PersistenciaVigenciasTallasInterface;
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
public class AdministrarVigenciasTallas implements AdministrarVigenciasTallasInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaVigenciasTallasInterface persistenciaVigenciasTallas;
    @EJB
    PersistenciaTiposTallasInterface persistenciaTiposTallas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    private EntityManager em;

    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
        for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaVigenciasTallas.editar(em, listaVigenciasTallas.get(i));
        }
    }

    public void borrarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
        for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaVigenciasTallas.borrar(em, listaVigenciasTallas.get(i));
        }
    }

    public void crearVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
        for (int i = 0; i < listaVigenciasTallas.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaVigenciasTallas.crear(em, listaVigenciasTallas.get(i));
        }
    }

    public List<VigenciasTallas> consultarVigenciasTallasPorEmpleado(BigInteger secPersona) {
        List<VigenciasTallas> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaVigenciasTallas.consultarVigenciasTallasPorPersona(em, secPersona);
        return listMotivosCambiosCargos;
    }

    @Override
    public List<TiposTallas> consultarLOVTiposTallas() {
        List<TiposTallas> listTiposTallas = persistenciaTiposTallas.buscarTiposTallas(em);
        return listTiposTallas;
    }



    public Empleados consultarEmpleadoSecuenciaPersona(BigInteger secuencia) {
        Empleados persona;
        try {
            persona = persistenciaEmpleado.buscarEmpleadoSecuenciaPersona(em, secuencia);
            return persona;
        } catch (Exception e) {
            persona = null;
            System.out.println("ERROR AdministrarHvReferencias  consultarEmpleadoSecuenciaPersona ERROR =====" + e);
            return persona;
        }
    }

}
