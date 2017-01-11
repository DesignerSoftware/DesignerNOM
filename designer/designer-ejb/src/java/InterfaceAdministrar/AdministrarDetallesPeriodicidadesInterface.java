/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.DetallesPeriodicidades;
import Entidades.Periodicidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarDetallesPeriodicidadesInterface {
     public void obtenerConexion(String idSesion);

    public void modificarDetallePeriodicidad(List<DetallesPeriodicidades> listaModificar);

    public void borrarDetallePeriodicidad(List<DetallesPeriodicidades> listaBorrar);

    public void crearDetallePeriodicidad(List<DetallesPeriodicidades> listaCrear);

    public List<DetallesPeriodicidades> consultarDetallesPeriodicidades(BigInteger secuenciaPeriodicidad);  
    
    public Periodicidades consultarPeriodicidadPorSecuencia(BigInteger secuenciaPeriodicidad);   
}
