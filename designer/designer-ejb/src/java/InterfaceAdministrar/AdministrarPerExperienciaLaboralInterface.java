/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.MotivosRetiros;
import Entidades.SectoresEconomicos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarPerExperienciaLaboralInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public Empleados empleadoActual(BigInteger secuencia);

    public List<SectoresEconomicos> listSectoresEconomicos();

    public List<MotivosRetiros> listMotivosRetiros();

    public void crearExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public void editarExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL);

    public List<HvExperienciasLaborales> listExperienciasLaboralesSecuenciaEmpleado(BigInteger secuencia);
    
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia);
}
