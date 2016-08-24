/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Personas;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarVigenciasTallasInterface {

    public void obtenerConexion(String idSesion);

    public void modificarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas);

    public void borrarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas);

    public void crearVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas);

    public List<VigenciasTallas> consultarVigenciasTallasPorEmpleado(BigInteger secPersona);

    public List<TiposTallas> consultarLOVTiposTallas();


    public Empleados consultarEmpleadoSecuenciaPersona(BigInteger secuencia);
}
