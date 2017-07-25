/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarOdiCabeceraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaOdiCabeceraInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarOdiCabecera implements AdministrarOdiCabeceraInterface {

   private static Logger log = Logger.getLogger(AdministrarOdiCabecera.class);

    @EJB
    PersistenciaOdiCabeceraInterface persistenciaOdicabecera;
//    @EJB
//    PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void borrar(OdisCabeceras odicabecera) {
        try {
            persistenciaOdicabecera.borrar(em, odicabecera);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.borrar : " + e.toString());
        }
    }

    @Override
    public void crear(OdisCabeceras odicabecera) {
        try {
            persistenciaOdicabecera.crear(em, odicabecera);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.crear : " + e.toString());
        }
    }

    @Override
    public void editar(OdisCabeceras odicabecera) {
        try {
            persistenciaOdicabecera.editar(em, odicabecera);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.editar : " + e.toString());
        }
    }

    @Override
    public List<Empleados> lovEmpleados() {
        return persistenciaOdicabecera.lovEmpleados(em);
    }

    @Override
    public List<Empresas> lovEmpresas() {
        return persistenciaOdicabecera.lovEmpresas(em);
    }

    @Override
    public List<Terceros> lovTerceros(BigInteger anio, BigInteger mes) {
        return persistenciaOdicabecera.lovTerceros(em,anio,mes);
    }

    @Override
    public List<TiposEntidades> lovtiposEntidades(BigInteger anio, BigInteger mes) {
        return persistenciaOdicabecera.lovTiposEntidades(em,anio,mes);
    }

    @Override
    public List<RelacionesIncapacidades> lovRelacionesIncapacidades(BigInteger secuenciaEmpleado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SucursalesPila> lovSucursales(BigInteger secuenciaEmpresa) {
            return persistenciaOdicabecera.lovSucursalesPila(em, secuenciaEmpresa);
    }

    @Override
    public List<OdisCabeceras> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
        return persistenciaOdicabecera.listOdisCabeceras(em, anio, mes, secuenciaEmpresa);
    }

    @Override
    public void borrarDetalle(OdisDetalles odidetalle) {
        try {
            persistenciaOdicabecera.borrarDetalle(em,odidetalle);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.borrarDetalle : " + e.toString());
        }
    }

    @Override
    public void crearDetalle(OdisDetalles odidetalle) {
        try {
            persistenciaOdicabecera.crearDetalle(em, odidetalle);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.crearDetalle : " + e.toString());
        }
    }

    @Override
    public void editarDetalle(OdisDetalles odidetalle) {
        try {
            persistenciaOdicabecera.editarDetalle(em, odidetalle);
        } catch (Exception e) {
            log.warn("Error AdministrarOdiCabecera.editarDetalle : " + e.toString());
        }
    }

}
