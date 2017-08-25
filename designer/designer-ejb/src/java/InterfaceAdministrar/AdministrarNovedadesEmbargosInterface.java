/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.DetallesFormasDtos;
import Entidades.EersPrestamos;
import Entidades.EersPrestamosDtos;
import Entidades.Empleados;
import Entidades.FormasDtos;
import Entidades.Juzgados;
import Entidades.MotivosEmbargos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TiposEmbargos;
import Entidades.VWPrestamoDtosRealizados;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadesEmbargosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    

    public List<Empleados> listaEmpleados();

//    public List<Empleados> lovEmpleados();

    public List<TiposEmbargos> lovTiposEmbargos();

    public List<Juzgados> lovJuzgados();

    public List<MotivosEmbargos> lovMotivosEmbargos();

    public List<Terceros> lovTerceros();

    public List<EersPrestamos> eersPrestamosEmpleado(BigInteger secuenciaEmpleado);

    public List<FormasDtos> formasDescuentos(BigInteger tipoEmbargo);

    public List<EersPrestamosDtos> eersPrestamosEmpleadoDtos(BigInteger secuenciaEersPrestamo);

    public List<Periodicidades> lovPeriodicidades();

    public List<DetallesFormasDtos> lovDetallesFormasDescuentos(BigInteger formasDtos);

    public List<FormasDtos> lovFormasDtos(BigInteger tipoEmbargo);

    public List<VWPrestamoDtosRealizados> prestamosRealizados(BigInteger secuencia);
    
    public void modificarDetalleEmbargo(List<EersPrestamosDtos> listaDetallesEmbargosModificar);

    public void borrarDetalleEmbargo(EersPrestamosDtos detallesEmbargos);

    public void crearDetalleEmbargo(EersPrestamosDtos detallesEmbargos);
    
    public void modificarEmbargo(List<EersPrestamos> listaEmbargosModificar);

    public void borrarEmbargo(EersPrestamos embargos);

    public void crearEmbargo(EersPrestamos Embargos);
}
