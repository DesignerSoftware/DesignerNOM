/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarSoAntecedentesMedicosInterface {
  public void obtenerConexion(String idSesion);   
  public void modificarAntecedentesM(List<SoAntecedentesMedicos> listaModificar);
  public void crearAntecedentesM(List<SoAntecedentesMedicos> listaCrear);
  public void borrarAntecedentesM(List<SoAntecedentesMedicos> listaBorrar);
  public List<SoTiposAntecedentes> consultarTiposAntecedentes();
  public List<SoAntecedentes> consultarAntecedentes(BigInteger secTipoAntecedente);
  public List<SoAntecedentesMedicos> consultarAntecedentesMedicos(BigInteger secPersona);
}
