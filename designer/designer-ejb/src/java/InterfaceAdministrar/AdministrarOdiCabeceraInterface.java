/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface AdministrarOdiCabeceraInterface {

    public void obtenerConexion(String idSesion);

    public void borrar(OdisCabeceras odicabecera);

    public void crear(OdisCabeceras odicabecera);

    public void editar(OdisCabeceras odicabecera);

    public void borrarDetalle(OdisDetalles odidetalle);

    public void crearDetalle(OdisDetalles odidetalle);

    public void editarDetalle(OdisDetalles odidetalle);

    public List<Empleados> lovEmpleados();

    public List<Empresas> lovEmpresas();

    public List<Terceros> lovTerceros(BigInteger anio, BigInteger mes);

    public List<TiposEntidades> lovtiposEntidades(BigInteger anio, BigInteger mes);

    public List<RelacionesIncapacidades> lovRelacionesIncapacidades(BigInteger secuenciaEmpleado);

    public List<SucursalesPila> lovSucursales(BigInteger secuenciaEmpresa);

    public List<OdisCabeceras> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa);

}
