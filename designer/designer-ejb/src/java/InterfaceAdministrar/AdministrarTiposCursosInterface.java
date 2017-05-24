/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposCursos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
public interface AdministrarTiposCursosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void modificarTiposCursos(List<TiposCursos> listaTiposCursos);

    public void borrarTiposCursos(List<TiposCursos> listaTiposCursos);

    public void crearTiposCursos(List<TiposCursos> listaTiposCursos);

    public List<TiposCursos> consultarTiposCursos();

    public TiposCursos consultarTipoIndicador(BigInteger secMotivoDemanda);

    public BigInteger contarCursosTipoCurso(BigInteger secuenciaVigenciasIndicadores);
}
