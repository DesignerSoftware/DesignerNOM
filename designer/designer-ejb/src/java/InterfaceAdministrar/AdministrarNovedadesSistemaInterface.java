/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.MotivosDefinitivas;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadesSistemaInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<NovedadesSistema> novedadesEmpleado(BigInteger secuenciaEmpleado);

    public void borrarNovedades(NovedadesSistema novedades);

    public void crearNovedades(NovedadesSistema novedades);

    public void modificarNovedades(NovedadesSistema novedades);

    public List<Empleados> buscarEmpleados();
    
//    public List<Empleados> lovEmpleados();
    
    public List<MotivosDefinitivas> lovMotivos();
     
    public List<MotivosRetiros> lovRetiros();
    
    public List<NovedadesSistema> vacacionesEmpleado(BigInteger secuenciaEmpleado);
    
    public List<NovedadesSistema> cesantiasEmpleado(BigInteger secuenciaEmpleado);
    
    public List<Vacaciones> periodosEmpleado(BigInteger secuenciaEmpleado);
    
    public BigDecimal valorCesantias(BigInteger secuenciaEmpleado);
    
    public BigDecimal valorIntCesantias(BigInteger secuenciaEmpleado);
    

    
}
