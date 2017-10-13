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
import Entidades.PruebaEmpleados;
import Entidades.Terceros;
import Entidades.Usuarios;
import Entidades.VWActualesTiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Viktor
 */
public interface AdministrarNovedadesEmpleadosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public int solucionesFormulas(BigInteger secuenciaNovedad);

    public int cuantosEmpleadosNovedad();
    public List<PruebaEmpleados> empleadosNovedadSoloAlgunos();
    public List<PruebaEmpleados> empleadosNovedades();

//    public List<Empleados> lovEmpleados();

    public Empleados elEmpleado(BigInteger secuenciaEmpleado);

    public PruebaEmpleados novedadEmpleado(BigInteger secuenciaEmpleado);

    public List<VWActualesTiposTrabajadores> tiposTrabajadores();
    
    public Date vigenciaTipoContratoSecEmpleado(BigInteger secuencia);

    public List<Novedades> novedadesEmpleado(BigInteger secuenciaEmpleado);

    public List<Conceptos> lovConceptos();

    public List<Formulas> lovFormulas();

    public List<Periodicidades> lovPeriodicidades();

    public List<Terceros> lovTerceros();

    public String borrarNovedades(Novedades novedades);

    public String crearNovedades(Novedades novedades);

    public String modificarNovedades(Novedades novedades);

    public String alias();

    public Usuarios usuarioBD(String alias);

    public List<Novedades> todasNovedades(BigInteger secuenciaEmpleado);
   
}
