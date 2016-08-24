/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarNovedadAutoLiquidacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
import InterfacePersistencia.PersistenciaNovedadesAutoLiquidacionInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
@Local
public class AdministrarNovedadAutoLiquidacion implements AdministrarNovedadAutoLiquidacionInterface {

    @EJB
    PersistenciaNovedadesAutoLiquidacionInterface persistenciaNovedadesAuto;
    @EJB
    PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empresas> empresasNovedadAuto() {
        return persistenciaNovedadesAuto.listaEmpresas(em);
    }

    @Override
    public List<Terceros> tercerosNovedadAuto() {
        return persistenciaNovedadesAuto.listaTerceros(em);
    }

    @Override
    public List<TiposEntidades> tiposEntidadesNovedadAuto() {
        return persistenciaNovedadesAuto.listaTiposEntidades(em);
    }

    @Override
    public List<SucursalesPila> sucursalesNovedadAuto(BigInteger secuenciaEmpresa) {
        return persistenciaNovedadesAuto.listasucursalesPila(em, secuenciaEmpresa);
    }

    @Override
    public List<NovedadesAutoLiquidaciones> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
        return persistenciaNovedadesAuto.listaNovedades(em, anio, mes, secuenciaEmpresa);
    }

    @Override
    public void borrarNovedades(NovedadesAutoLiquidaciones novedad) {
        try {
            persistenciaNovedadesAuto.borrar(em, novedad);
        } catch (Exception e) {
            System.out.println("Error borrarNovedades Admi : " + e.toString());
        }
    }

    @Override
    public void crearNovedades(NovedadesAutoLiquidaciones novedad) {
        try {
            persistenciaNovedadesAuto.crear(em, novedad);
        } catch (Exception e) {
            System.out.println("Error crearNovedades Admi : " + e.toString());
        }
    }

    @Override
    public void editarNovedades(NovedadesAutoLiquidaciones novedad) {
        try {
            persistenciaNovedadesAuto.editar(em, novedad);
        } catch (Exception e) {
            System.out.println("Error editarNovedades Admi : " + e.toString());
        }
    }
}
