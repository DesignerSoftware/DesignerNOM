/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosDemandas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosDemandasInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosDemandas.
     *
     * @param listaMotivosDemandas Lista MotivosDemandas que se van a modificar.
     */
    public void modificarMotivosDemandas(List<MotivosDemandas> listaMotivosDemandas);

    /**
     * Método encargado de borrar MotivosDemandas.
     *
     * @param listaMotivosDemandas Lista MotivosDemandas que se van a borrar.
     */
    public void borrarMotivosDemandas(List<MotivosDemandas> listaMotivosDemandas);

    /**
     * Método encargado de crear MotivosDemandas.
     *
     * @param listaMotivosDemandas Lista MotivosDemandas que se van a crear.
     */
    public void crearMotivosDemandas(List<MotivosDemandas> listaMotivosDemandas);

    /**
     * Método encargado de recuperar las MotivosDemandas para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de MotivosDemandas.
     */
    public List<MotivosDemandas> consultarMotivosDemandas();

    /**
     * Método encargado de recuperar una MotivosDemandas dada su secuencia.
     *
     * @param secMotivosDemandas Secuencia del MotivosDemandas
     * @return Retorna una MotivosDemandas.
     */
    public MotivosDemandas consultarMotivoDemanda(BigInteger secMotivosDemandas);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * MotivosDemandas específica y algún Demandas. Adémas de la revisión,
     * establece cuantas relaciones existen.
     *
     * @param secMotivosDemandas Secuencia del MotivosDemandas.
     * @return Retorna el número de Demandas relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger contarDemandasMotivoDemanda(BigInteger secMotivosDemandas);
}
