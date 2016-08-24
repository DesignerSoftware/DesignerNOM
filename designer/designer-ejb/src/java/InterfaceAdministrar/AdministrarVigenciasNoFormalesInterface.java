/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.AdiestramientosNF;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.VigenciasNoFormales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciasNoFormalesInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasNoFormales> vigenciasNoFormalesPersona(BigInteger secPersona);
    public Personas encontrarPersona(BigInteger secPersona);
    public List<Cursos> lovCursos();
    public List<Instituciones> lovInstituciones();
    public List<AdiestramientosNF> lovAdiestramientosNF();
    public void modificarVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesModificar);
    public void borrarVigenciaNoFormal(VigenciasNoFormales vigenciasNoFormales);
    public void crearVigenciaNoFormal(VigenciasNoFormales vigenciasNoFormales);
    public Empleados empleadoActual(BigInteger secuenciaE);
}
