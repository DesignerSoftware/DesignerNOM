/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Bancos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

@Local
public interface AdministrarBancosInterface {

    public void obtenerConexion(String idSesion);
    
    public void modificarBanco(List<Bancos> listaModificar);

    public void borrarBanco(List<Bancos> listaBorrar);

    public void crearBanco(List<Bancos> listaCrear);

    public List<Bancos> consultarBancos();
    
    public Bancos consultarBancosPorSecuencia(BigInteger secuencia);
}
