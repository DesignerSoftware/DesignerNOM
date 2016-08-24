/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.OtrosCertificados;
import Entidades.TiposCertificados;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplMvrsInterface {

    public List<OtrosCertificados> listOtrosCertificadosEmpleado(BigInteger secuencia);

    public List<Mvrs> listMvrsEmpleado(BigInteger secuenciaEmpl);

    public void crearOtrosCertificados(List<OtrosCertificados> crearOtrosCertificados);

    public void modificarOtrosCertificados(List<OtrosCertificados> modificarOtrosCertificados);

    public void borrarOtrosCertificados(List<OtrosCertificados> borrarOtrosCertificados);

    public void crearMvrs(List<Mvrs> crearMvrs);

    public void modificarMvrs(List<Mvrs> modificarMvrs);

    public void borrarMvrs(List<Mvrs> borrarMvrs);

    public List<Motivosmvrs> listMotivos();

    public List<TiposCertificados> listTiposCertificados();

    public Empleados empleadoActual(BigInteger secuencia);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
