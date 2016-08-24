/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosRetiros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosRetirosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosRetiros.
     *
     * @param listaMotivosRetiros Lista MotivosRetiros que se van a modificar.
     */
    public void modificarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros);

    /**
     * Método encargado de borrar MotivosRetiros.
     *
     * @param listaMotivosRetiros Lista MotivosRetiros que se van a borrar.
     */
    public void borrarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros);

    /**
     * Método encargado de crear MotivosRetiros.
     *
     * @param listaMotivosRetiros Lista MotivosRetiros que se van a crear.
     */
    public void crearMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros);

    /**
     * Método encargado de recuperar las MotivosRetiros para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de MotivosRetiros.
     */
    public List<MotivosRetiros> consultarMotivosRetiros();

    /**
     * Método encargado de recuperar un MotivoRetiro dada su secuencia.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro
     * @return Retorna un MotivoRetiro.
     */
    public MotivosRetiros consultarMotivoRetiro(BigInteger secMotivosRetiros);

    /**
     * Método encargado de contar la cantidad de HVExperienciasLaborales
     * relacionadas con un MotivoRetiro específico.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro.
     * @return Retorna un número indicando la cantidad de
     * HVExperienciasLaborales cuya secuencia coincide con el valor del
     * parámetro.
     */
    public BigInteger contarHVExperienciasLaboralesMotivoRetiro(BigInteger secMotivosRetiros);

    /**
     * Método encargado de contar la cantidad de NovedadesSistemas relacionadas
     * con un MotivoRetiro específico.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro.
     * @return Retorna un número indicando la cantidad de NovedadesSistemas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarNovedadesSistemasMotivoRetiro(BigInteger secMotivosRetiros);

    /**
     * Método encargado de contar la cantidad de MotivosRetiros relacionadas con
     * un MotivoRetiro específico.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro.
     * @return Retorna un número indicando la cantidad de MotivosRetiros cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarRetiMotivosRetirosMotivoRetiro(BigInteger secMotivosRetiros);

    /**
     * Método encargado de contar la cantidad de Retirados relacionadas con un
     * MotivoRetiro específico.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro.
     * @return Retorna un número indicando la cantidad de Retirados cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarRetiradosMotivoRetiro(BigInteger secMotivosRetiros);
}
