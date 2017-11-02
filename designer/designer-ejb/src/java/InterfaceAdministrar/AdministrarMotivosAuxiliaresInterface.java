/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosAuxiliares;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarMotivosAuxiliaresInterface {

    public void obtenerConexion(String idSesion);

    public String modificarMotivosAuxiliares(MotivosAuxiliares motivo);

    public String borrarMotivosAuxiliares(MotivosAuxiliares motivo);

    public String crearMotivosAuxiliares(MotivosAuxiliares motivo);

    public List<MotivosAuxiliares> buscarMotivosAuxiliares();
}
