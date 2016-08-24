/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Eventos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEventosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar Eventos.
     *
     * @param listaEventos Lista Eventos que se van a modificar.
     */
    public void modificarEventos(List<Eventos> listaEventos);

    /**
     * Método encargado de borrar Eventos.
     *
     * @param listaEventos Lista Eventos que se van a borrar.
     */
    public void borrarEventos(List<Eventos> listaEventos);

    /**
     * Método encargado de crear Eventos.
     *
     * @param listaEventos Lista Eventos que se van a crear.
     */
    public void crearEventos(List<Eventos> listaEventos);

    /**
     * Método encargado de recuperar un Evento dada su secuencia.
     *
     * @param secDeportes Secuencia del Evento.
     * @return Retorna un Evento cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Eventos consultarEvento(BigInteger secDeportes);

    /**
     * Metodo encargado de traer todas los Eventos de la base de datos.
     *
     * @return Lista de Eventos.
     */
    public List<Eventos> consultarEventos();

    /**
     * Método encargado de contar la cantidad de VigenciasEventos relacionadas
     * con un Evento específico.
     *
     * @param secEventos Secuencia del Evento.
     * @return Retorna un número indicando la cantidad de VigenciasEventos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarVigenciasEventos(BigInteger secEventos);
}
