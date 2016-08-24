/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ClavesSap;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarClavesSapInterface {

    public void obtenerConexion(String idSesion);

    public void modificarClavesSap(List<ClavesSap> listaClavesSap);

    public void borrarClavesSap(List<ClavesSap> listaClavesSap);

    public void crearClavesSap(List<ClavesSap> listaClavesSap);

    public List<ClavesSap> consultarClavesSap();

    public List<ClavesSap> consultarLOVClavesSap();

    public BigInteger contarClavesContablesCreditoClaveSap(BigInteger secuencia);

    public BigInteger contarClavesContablesDebitoClaveSap(BigInteger secuencia);
}
