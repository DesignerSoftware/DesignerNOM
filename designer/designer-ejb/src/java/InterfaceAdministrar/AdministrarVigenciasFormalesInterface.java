/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.AdiestramientosF;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.TiposEducaciones;
import Entidades.VigenciasFormales;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarVigenciasFormalesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona);
    public Personas encontrarPersona(BigInteger secPersona);
    public List<TiposEducaciones> lovTiposEducaciones();
    public List<Profesiones> lovProfesiones();
    public List<Instituciones> lovInstituciones();
    public List<AdiestramientosF> lovAdiestramientosF();
    public void modificarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesModificar);
    public void borrarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesBorrar);
    public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear);
    public Empleados empleadoActual(BigInteger secuenciaE);
    
}
