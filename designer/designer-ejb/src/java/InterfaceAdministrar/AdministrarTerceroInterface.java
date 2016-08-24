package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTerceroInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<Terceros> obtenerListTerceros(BigInteger secuencia);
    public void modificarTercero(Terceros t);
    public void borrarTercero(Terceros t);
    public void crearTercero(Terceros t);
    //
    public List<TercerosSucursales> obtenerListTercerosSucursales(BigInteger secuencia);
    public void modificarTerceroSucursales(TercerosSucursales t);
    public void borrarTerceroSucursales(TercerosSucursales t);
    public void crearTerceroSucursales(TercerosSucursales t);
    //
    public List<Empresas> listEmpresas();
    public List<Ciudades> listCiudades();
    
}
