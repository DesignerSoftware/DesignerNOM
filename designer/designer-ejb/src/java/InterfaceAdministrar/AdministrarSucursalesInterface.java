/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Sucursales;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSucursalesInterface {
/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void modificarSucursales(List<Sucursales> listaSucursales);

    public void borrarSucursales(List<Sucursales> listaSucursales);

    public void crearSucursales(List<Sucursales> listaSucursales);

    public List<Sucursales> consultarSucursales();

    public List<Bancos> consultarLOVBancos();

    public List<Ciudades> consultarLOVCiudades();

    public BigInteger contarVigenciasFormasPagosSucursal(BigInteger secuenciaSucursal);
}
