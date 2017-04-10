/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarOperandosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<Operandos> buscarOperandos();

    public void borrarOperando(Operandos operandos);
    public void crearOperando(Operandos operandos);
    public void modificarOperando(List<Operandos> listaOperandosModificar);
    public String buscarValores(BigInteger secuenciaOperando);
    public Operandos consultarOperandoActual(BigInteger secOperando);
    public String clonarOperando(short codigoO, String nombreDes, String descripcionDes);
    
}
