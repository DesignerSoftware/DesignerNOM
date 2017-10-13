/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.Usuarios;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadesTercerosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public int solucionesFormulas(BigInteger secuenciaNovedad);

    public List<Empleados> lovEmpleados();

    public List<Terceros> Terceros();

    public List<Formulas> lovFormulas();

    public List<Periodicidades> lovPeriodicidades();

    public List<Terceros> lovTerceros();
    
    public List<Conceptos> lovConceptos();

    public String borrarNovedades(Novedades novedades);

    public String crearNovedades(Novedades novedades);

    public String modificarNovedades(Novedades novedades);

    public String alias();

    public Usuarios usuarioBD(String alias);
    
    public List<Novedades> novedadesTercero(BigInteger secuenciaTercero);
    
    public List<Novedades> todasNovedadesTercero(BigInteger secuenciaTercero);
    
}
