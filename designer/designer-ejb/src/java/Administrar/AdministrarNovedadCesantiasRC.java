/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.MotivosCesantias;
import Entidades.NovedadesSistema;
import Entidades.VWActualesTiposTrabajadores;
import InterfaceAdministrar.AdministrarNovedadlCesantiasRCInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@LocalBean
public class AdministrarNovedadCesantiasRC implements AdministrarNovedadlCesantiasRCInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadCesantiasRC.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    @EJB
    PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
    @EJB
    PersistenciaMotivosCesantiasInterface persistenciaMotivosCensantias;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empleados> empleadosCesantias() {
        return persistenciaEmpleados.empleadosCesantias(em);
    }

    @Override
    public List<VWActualesTiposTrabajadores> tiposTrabajadores() {
        return persistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado(em);
    }

    @Override
    public List<MotivosCesantias> consultarMotivosCesantias() {
        List<MotivosCesantias> listMotivosCensantias;
        listMotivosCensantias = persistenciaMotivosCensantias.buscarMotivosCesantias(em);
        return listMotivosCensantias;
    }

    @Override
    public List<Empleados> empleadoscesantiasnoliquidados() {
        return persistenciaEmpleados.consultarCesantiasnoLiquidadas(em);
    }

    @Override
    public List<NovedadesSistema> novedadesnoliquidadas(BigInteger secuenciaEmpleado) {
        return persistenciaEmpleados.novedadescesantiasnoliquidadas(em, secuenciaEmpleado);
    }

    @Override
    public List<NovedadesSistema> todasnovedadescesantias(BigInteger secuenciaEmpleado) {
        return persistenciaEmpleados.novedadescesantiasnoliquidadas(em, secuenciaEmpleado);
    }

}
