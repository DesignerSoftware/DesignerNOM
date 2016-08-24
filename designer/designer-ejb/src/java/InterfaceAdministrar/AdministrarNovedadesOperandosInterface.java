/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfaceAdministrar;

import Entidades.NovedadesOperandos;
import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNovedadesOperandosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<NovedadesOperandos> buscarNovedadesOperandos(BigInteger secuenciaOperando);

    public void borrarNovedadesOperandos(NovedadesOperandos novedadesOperandos);

    public void crearNovedadesOperandos(NovedadesOperandos novedadesOperandos);
      
    public void modificarNovedadesOperandos(NovedadesOperandos novedadesOperandos);
    
    public List<Operandos> buscarOperandos();
        
}
