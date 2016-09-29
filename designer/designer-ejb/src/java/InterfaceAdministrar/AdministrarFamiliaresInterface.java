/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Familiares;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.TiposFamiliares;
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
    public Empleados empleadoActual(BigInteger secuenciaP);
    public void crearPersona(Personas persona);
    public List<TiposFamiliares> consultarTiposFamiliares();
    public List<TiposDocumentos> consultarTiposDocumentos();
    public List<Ciudades> consultarCiudades();
    public List<Personas> consultarPersonas();
    public Personas consultarPersona(BigInteger secPersona);

}
