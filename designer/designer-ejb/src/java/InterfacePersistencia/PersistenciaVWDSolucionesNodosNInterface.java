/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosConjuntos;
import Entidades.VWDSolucionesNodosN;
import Entidades.VWDSolucionesNodosNDetalle;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVWDSolucionesNodosNInterface {

    public List<VWDSolucionesNodosN> consultarDSolucionesNodosN(EntityManager em, String vistaConsultar, Date fechaParametro);

    public List<VWDSolucionesNodosN> consultarDSolucionesNodosNLB(EntityManager em, String vistaConsultar, Date fechaParametro);

    public List<VWDSolucionesNodosNDetalle> consultarDetalleN(EntityManager em, String vistaConsultar, int numeroConjunto, BigInteger secDescripcion);

    public List<VWDSolucionesNodosNDetalle> consultarDetalleNLB(EntityManager em, String vistaConsultar, int numeroConjunto, BigInteger secDescripcion);

}
