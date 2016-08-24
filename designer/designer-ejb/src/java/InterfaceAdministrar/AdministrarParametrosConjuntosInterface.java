/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.ParametrosConjuntos;
import Entidades.VWDSolucionesNodosN;
import Entidades.VWDSolucionesNodosNDetalle;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarParametrosConjuntosInterface {
    
    public void obtenerConexion(String idSesion);

    public void crearParametros(ParametrosConjuntos parametrosConjuntos);

    public void editarParametros(ParametrosConjuntos parametrosConjuntos);

    public void borrarParametros(ParametrosConjuntos parametrosConjuntos);

    public ParametrosConjuntos consultarParametros();

    public List<VWDSolucionesNodosN> consultarDSolucionesNodosN(String vistaConsultar, Date fechaVig);

    public List<VWDSolucionesNodosN> consultarDSolucionesNodosNLB(String vistaConsultar, Date fechaVig);

    public List<VWDSolucionesNodosNDetalle> consultarDetalleN(String vistaConsultar, int numeroConjunto, BigInteger secDescripcion);

    public List<VWDSolucionesNodosNDetalle> consultarDetalleNLB(String vistaConsultar, int numeroConjunto, BigInteger secDescripcion);
    
    public List<Conceptos> consultarConceptos();
    
    public void modificarConcepto(Conceptos concepto);

}
