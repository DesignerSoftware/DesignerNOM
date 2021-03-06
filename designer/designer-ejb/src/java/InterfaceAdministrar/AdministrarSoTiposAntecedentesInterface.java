/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoTiposAntecedentes;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarSoTiposAntecedentesInterface {
    public void obtenerConexion(String idSesion);
    public void modificarTipoAntecedente(List<SoTiposAntecedentes> listaModificar);
    public void crearTipoAntecedente(List<SoTiposAntecedentes> listaCrear);
    public void borrarTipoAntecedente(List<SoTiposAntecedentes> listaBorrar);
    public List<SoTiposAntecedentes> consultarTiposAntecedentes();    
}
