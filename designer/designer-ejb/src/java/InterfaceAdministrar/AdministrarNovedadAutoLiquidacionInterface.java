/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadAutoLiquidacionInterface {

    public void obtenerConexion(String idSesion);

    public List<Empresas> empresasNovedadAuto();

    public List<Terceros> tercerosNovedadAuto();

    public List<TiposEntidades> tiposEntidadesNovedadAuto();

    public List<SucursalesPila> sucursalesNovedadAuto(BigInteger secuenciaEmpresa);

    public List<NovedadesAutoLiquidaciones> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa);

    public void borrarNovedades(NovedadesAutoLiquidaciones novedad);

    public void crearNovedades(NovedadesAutoLiquidaciones novedad);

    public void editarNovedades(NovedadesAutoLiquidaciones novedad);

}
