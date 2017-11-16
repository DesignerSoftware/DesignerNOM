/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Errores;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarErroresInterfaz {

    public void obtenerConexion(String idSesion);

    public String modificarErrores(Errores errores);

    public String borrarErrores(Errores errores);

    public String crearErrores(Errores errores);

    public Errores consultarErrores(BigInteger secErrores);

    public List<Errores> consultarErrores();

}
