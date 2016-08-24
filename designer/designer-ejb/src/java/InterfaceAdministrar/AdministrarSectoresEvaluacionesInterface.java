/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SectoresEvaluaciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSectoresEvaluacionesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar SectoresEvaluaciones.
     *
     * @param listaSectoresEvaluaciones Lista SectoresEvaluaciones que se van a
     * modificar.
     */
    public void modificarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones);

    /**
     * Método encargado de borrar SectoresEvaluaciones.
     *
     * @param listaSectoresEvaluaciones Lista SectoresEvaluaciones que se van a borrar.
     *
     */
    public void borrarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones);

    /**
     * Método encargado de crear SectoresEvaluaciones.
     *
     * @param listaSectoresEvaluaciones Lista SectoresEvaluaciones que se van a crear.
     *
     */
    public void crearSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones);

    /**
     * Metodo encargado de traer todas las SectoresEvaluaciones de la base de datos.
     *
     * @return Lista de SectoresEvaluaciones.
     */
    public List<SectoresEvaluaciones> consultarSectoresEvaluaciones();

    /**
     * Método encargado de recuperar un SectoresEvaluaciones dada su secuencia.
     *
     * @param secSectoresEvaluaciones Secuencia del SectorEvaluacion.
     * @return Retorna el SectorEvaluacion cuya secuencia coincida con el valor
     * del parámetro.
     */
    public SectoresEvaluaciones consultarSectorEvaluacion(BigInteger secSectoresEvaluaciones);
}
