/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVWContabilidadResumida1Interface {

    public void obtenerConexion(String idSesion);

    public List<VWContabilidadResumida1> obtenerContabilidadResumida(Date FechaIni, Date FechaFin, BigInteger secProceso);

    public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD);

    public List<Procesos> lovProcesos();

    public ActualUsuario obtenerActualUsuario();

    public List<Empresas> lovEmpresas();

    public Date obtenerFechaMaxContabilizaciones();

    public Date obtenerFechaMaxInterconSapBO();

    public ParametrosEstructuras parametrosLiquidacion();

    public List<VWContabilidadDetallada> obtenerContabilidadDetallada(Date FechaIni, Date FechaFin, BigInteger secProceso);
    
    public void modificarParametroContable(ParametrosContables parametro);
    
    public Integer abrirPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso);
    
    public void actualizarPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso);
    
}
