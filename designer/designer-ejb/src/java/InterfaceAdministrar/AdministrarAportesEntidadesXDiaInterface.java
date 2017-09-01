/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.AportesEntidadesXDia;
import Entidades.Empleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarAportesEntidadesXDiaInterface {

    public void obtenerConexion(String idSesion);

    public List<AportesEntidadesXDia> consultarAportesEntidadesXDia();

    public List<AportesEntidadesXDia> consultarAportesEntidadesPorEmpleadoMesYAnio(BigInteger secEmpleado, short mes, short ano);

    public void crearAportesEntidadesXDia(List<AportesEntidadesXDia> listaAE);

    public void editarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE);

    public void borrarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE);
    
    public BigDecimal consultarTarifas(BigInteger secEmpresa, short mes, short ano, BigInteger secEmpleado, BigInteger secTipoEntidad);
    
     public Empleados buscarEmpleado(BigInteger secuenciaEmpleado);
}
