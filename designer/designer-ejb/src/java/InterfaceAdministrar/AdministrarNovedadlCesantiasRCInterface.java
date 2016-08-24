/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.MotivosCesantias;
import Entidades.NovedadesSistema;
import Entidades.VWActualesTiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadlCesantiasRCInterface {
    public void obtenerConexion(String idSesion);
    public List<Empleados> empleadosCesantias();
    public List<VWActualesTiposTrabajadores> tiposTrabajadores();
    public List<MotivosCesantias> consultarMotivosCesantias();
    public List<Empleados>empleadoscesantiasnoliquidados();
    public List<NovedadesSistema> novedadesnoliquidadas(BigInteger secuenciaEmpleado);
    public List<NovedadesSistema> todasnovedadescesantias(BigInteger secuenciaEmpleado);
    
}
