/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Operadores;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarOperadoresInterface {

    public void obtenerConexion(String idSesion);

    public List<Operadores> listOperadores();

}
