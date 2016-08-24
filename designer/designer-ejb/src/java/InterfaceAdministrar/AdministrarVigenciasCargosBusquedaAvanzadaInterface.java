/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosCambiosCargos;
import Entidades.Papeles;
import Entidades.Personas;
import java.util.List;

/**
 *
 * @author PROYECTO01
 */
public interface AdministrarVigenciasCargosBusquedaAvanzadaInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de recuperar los Estructuras necesarios para la lista de valores.
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> lovEstructura();
    /**
     * Método encargado de recuperar los MotivosCambiosCargos necesarios para la lista de valores.
     * @return Retorna una lista de MotivosCambiosCargos.
     */
    public List<MotivosCambiosCargos> lovMotivosCambiosCargos();
    /**
     * Método encargado de recuperar los CentrosCostos necesarios para la lista de valores.
     * @return Retorna una lista de CentrosCostos.
     */
    public List<CentrosCostos> lovCentrosCostos();
    /**
     * Método encargado de recuperar los Papeles necesarios para la lista de valores.
     * @return Retorna una lista de Papeles.
     */
    public List<Papeles> lovPapeles();
    /**
     * Método encargado de recuperar los Cargos necesarios para la lista de valores.
     * @return Retorna una lista de Cargos.
     */
    public List<Cargos> lovCargos();
    /**
     * Método encargado de recuperar los Empleados necesarios para la lista de valores.
     * @return Retorna una lista de Empleados.
     */
    public List<Empleados> lovEmpleados();
    
}
