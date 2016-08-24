/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Enfoques;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaEnfoquesInterface {

    /**
     * Método encargado de insertar un Enfoque en la base de datos.
     *
     * @param Enfoques Enfoque que se quiere crear.
     */
    public void crear(EntityManager em,Enfoques Enfoques);

    /**
     * Método encargado de modificar un Enfoque de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param Enfoques Enfoques con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Enfoques Enfoques);

    /**
     * Método encargado de eliminar de la base de datos un Enfoque que entra por
     * parámetro.
     *
     * @param Enfoques Enfoques que se quiere eliminar.
     */
    public void borrar(EntityManager em,Enfoques Enfoques);

    /**
     * Método encargado de buscar el TipoDia con la secEnfoques dada por
     * parámetro.
     *
     * @param secEnfoques secEnfoques del Enfoque que se quiere encontrar.
     * @return Retorna el Enfoque identificado con la secEnfoques dada por
     * parámetro.
     */
    public Enfoques buscarEnfoque(EntityManager em,BigInteger secEnfoques);

    /**
     * Método encargado de buscar todas los Enfoques existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Enfoques.
     */
    public List<Enfoques> buscarEnfoques(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un Enfoque
     * específica y algún Proyecto. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfoques secEnfoques de un Enfoque.
     * @return Retorna el número de TiposDetalles relacionados con la Enfoque
     * cuya secEnfoques coincide con el parámetro.
     */
    public BigInteger contadorTiposDetalles(EntityManager em,BigInteger secEnfoques);
}
