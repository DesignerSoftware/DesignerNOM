/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ObjetosJsf;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarPermisosObjetosJsfInterface {

    public void obtenerConexion(String idSesion);

    public List<ObjetosJsf> consultarEnable(String nomPantalla);
    
}
