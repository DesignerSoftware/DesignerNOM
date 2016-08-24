/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.IbcsAutoliquidaciones;
import Entidades.Procesos;
import Entidades.TiposEntidades;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarIbcAutoliquidInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<TiposEntidades> listTiposEntidadesIBCS();

    public List<IbcsAutoliquidaciones> listIBCSAutoliquidaciones(BigInteger secuenciaTE, BigInteger secuenciaEmpl);

    public void crearIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> crearIbcsAutoliquidaciones);

    public void modificarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> modificarIbcsAutoliquidaciones);

    public void borrarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> borrarIbcsAutoliquidaciones);

    public List<Procesos> listProcesos();
    
    public Empleados empleadoActual(BigInteger secuencia);
}
