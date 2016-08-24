/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Indices;
import Entidades.TiposIndices;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarIndicesInterface {

    public void obtenerConexion(String idSesion);

    public void modificarIndices(List<Indices> listaIndices);

    public void borrarIndices(List<Indices> listaIndices);

    public void crearIndices(List<Indices> listaIndices);

    public List<Indices> mostrarIndices();

    public List<TiposIndices> consultarLOVTiposIndices();

    public BigInteger contarParametrosIndicesIndice(BigInteger secuenciaIndices);

    public BigInteger contarResultadosIndicesDetallesIndice(BigInteger secuenciaIndices);

    public BigInteger contarResultadosIndicesIndice(BigInteger secuenciaIndices);

    public BigInteger contarResultadosIndicesSoportesIndice(BigInteger secuenciaIndices);

    public BigInteger contarUsuariosIndicesIndice(BigInteger secuenciaIndices);

    public BigInteger contarCodigosRepetidosIndices(Short codigo);
}
