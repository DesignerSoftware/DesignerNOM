/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Motivosmvrs;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosMvrsInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosEmbargos.
     *
     * @param listaMotivosMvrs Lista MotivosEmbargos que se van a modificar.
     */
    public void modificarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs);

    /**
     * Método encargado de borrar MotivosEmbargos.
     *
     * @param listaMotivosMvrs Lista MotivosEmbargos que se van a borrar.
     */
    public void borrarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs);

    /**
     * Método encargado de crear MotivosEmbargos.
     *
     * @param listaMotivosMvrs Lista MotivosEmbargos que se van a crear.
     */
    public void crearMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs);

    /**
     * Método encargado de recuperar las MotivosEmbargos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de MotivosEmbargos.
     */
    public List<Motivosmvrs> consultarMotivosMvrs();

    /**
     * Método encargado de recuperar una MotivosEmbargos dada su secuencia.
     *
     * @param secMotivosMvrs Secuencia del MotivosEmbargos
     * @return Retorna una MotivosEmbargos.
     */
    public Motivosmvrs consultarMotivosMvrs(BigInteger secMotivosMvrs);
}
