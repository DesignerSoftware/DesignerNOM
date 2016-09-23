/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Familiares;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarFamiliaresInterface {
 public void obtenerConexion(String idSesion);
    public void modificarFamiliares(List<Familiares> listaModificar);
    public void borrarFamiliares(List<Familiares> listaBorrar);
    public void crearFamilares(List<Familiares> listaCrear);
    public List<Familiares> consultarFamiliares(BigInteger secuenciaEmp);

}
