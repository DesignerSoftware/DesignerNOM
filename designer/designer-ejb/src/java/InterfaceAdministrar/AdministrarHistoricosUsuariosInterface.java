/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.HistoricosUsuarios;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarHistoricosUsuariosInterface {

public void obtenerConexion(String idSesion);
public List <HistoricosUsuarios> consultarHistoricosUsuarios(BigInteger secUsuario);
public void crearHistoricosUsuarios(List<HistoricosUsuarios> listaCrear);
public void modificarHistoricosUsuarios(List<HistoricosUsuarios> listaModificar);
public void borrarHistoricosUsuarios(List<HistoricosUsuarios> listaBorrar);
public List<Personas> lovPersonas();
public List<Perfiles> lovPerfiles();
public List<Usuarios> lovUsuarios(BigInteger secUsuario);
}
