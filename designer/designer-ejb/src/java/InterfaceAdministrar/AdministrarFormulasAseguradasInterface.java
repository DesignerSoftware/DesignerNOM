/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Formulas;
import Entidades.FormulasAseguradas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarFormulasAseguradasInterface {

    public void modificarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas);

    public void borrarFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas);

    public void crearFormulasAseguradas(List<FormulasAseguradas> listaFormulasAseguradas);

    public List<FormulasAseguradas> consultarFormulasAseguradas();

    public List<Formulas> consultarLOVFormulas();

    public List<Procesos> consultarLOVProcesos();

    public List<Periodicidades> consultarLOVPPeriodicidades();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
