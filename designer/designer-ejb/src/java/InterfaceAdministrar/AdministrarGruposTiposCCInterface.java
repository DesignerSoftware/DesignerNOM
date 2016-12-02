/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.GruposTiposCC;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarGruposTiposCCInterface {
 
public void obtenerConexion(String idSesion);
public void crearGrupo(List<GruposTiposCC> listaCrear);    
public void editarGrupo(List<GruposTiposCC> listaEditar);    
public void borrarGrupo(List<GruposTiposCC> listaBorrar);    
public List<GruposTiposCC> consultarGrupos();
    
}
