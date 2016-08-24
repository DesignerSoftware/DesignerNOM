/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Recordatorios;
import java.util.List;

public interface AdministrarRecordatoriosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<Recordatorios> recordatorios();

    public List<Recordatorios> mensajesRecordatorios();

    public void borrar(Recordatorios proverbios);

    public void crear(Recordatorios proverbios);

    public void modificar(List<Recordatorios> listaProverbiosModificar);

    public void borrarMU(Recordatorios mensajeUsuario);

    public void crearMU(Recordatorios mensajeUsuario);

    public void modificarMU(List<Recordatorios> listaMensajesUsuariosModificar);

}
