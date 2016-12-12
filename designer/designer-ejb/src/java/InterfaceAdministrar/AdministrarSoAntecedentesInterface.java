/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoAntecedentes;
import Entidades.SoTiposAntecedentes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarSoAntecedentesInterface {
    
    public void obtenerConexion(String idSesion);
    public void modificarAntecedente(List<SoAntecedentes> listaModificar);
    public void crearAntecedente(List<SoAntecedentes> listaCrear);
    public void borrarAntecedente(List<SoAntecedentes> listaBorrar);
    public List<SoAntecedentes> consultarAntecedentesPorTipo(BigInteger secTipoAntecedente);
    public List<SoAntecedentes> consultarAntecedentes();
    public List<SoTiposAntecedentes> consultarTiposAntecedentes();

}
