/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ParametrosConjuntos;
import Entidades.VWDSolucionesNodosN;
import Entidades.VWDSolucionesNodosNDetalle;
import InterfaceAdministrar.AdministrarParametrosConjuntosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaParametrosConjuntosInterface;
import InterfacePersistencia.PersistenciaVWDSolucionesNodosNInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarParametrosConjuntos implements AdministrarParametrosConjuntosInterface {

   private static Logger log = Logger.getLogger(AdministrarParametrosConjuntos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaVWDSolucionesNodosNInterface persistenciaVWDSolucionesNodosN;
    @EJB
    PersistenciaParametrosConjuntosInterface persistenciaParametrosConjuntos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearParametros(ParametrosConjuntos parametrosConjuntos) {
        try {
            persistenciaParametrosConjuntos.crearParametros(em, parametrosConjuntos);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.crearParametros : " + e);
        }
    }

    @Override
    public void editarParametros(ParametrosConjuntos parametrosConjuntos) {
        try {
            persistenciaParametrosConjuntos.editarParametros(em, parametrosConjuntos);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.editarParametros : " + e);
        }
    }

    @Override
    public void borrarParametros(ParametrosConjuntos parametrosConjuntos) {
        try {
            persistenciaParametrosConjuntos.borrarParametros(em, parametrosConjuntos);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.borrarParametros : " + e);
        }
    }

    @Override
    public ParametrosConjuntos consultarParametros() {
        try {
            return persistenciaParametrosConjuntos.consultarParametros(em);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarParametros : " + e);
            return null;
        }
    }

    @Override
    public List<VWDSolucionesNodosN> consultarDSolucionesNodosN(String vistaConsultar, Date fechaVig) {
        try {
            List<VWDSolucionesNodosN> lista = persistenciaVWDSolucionesNodosN.consultarDSolucionesNodosN(em, vistaConsultar, fechaVig);
            if (lista != null) {
                return lista;
            } else {
                log.error("Error AdministrarParametrosConjuntos.consultarDSolucionesNodosN : La consulta retorno Null");
                return new ArrayList<VWDSolucionesNodosN>();
            }
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarDSolucionesNodosN : " + e);
            return new ArrayList<VWDSolucionesNodosN>();
        }
    }

    @Override
    public List<VWDSolucionesNodosN> consultarDSolucionesNodosNLB(String vistaConsultar, Date fechaVig) {
        try {
            List<VWDSolucionesNodosN> lista = persistenciaVWDSolucionesNodosN.consultarDSolucionesNodosNLB(em, vistaConsultar, fechaVig);
            if (lista != null) {
                log.error("Error AdministrarParametrosConjuntos.consultarDSolucionesNodosNLB : La consulta retorno Null");
                return lista;
            } else {
                return new ArrayList<VWDSolucionesNodosN>();
            }
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarDSolucionesNodosNLB : " + e);
            return new ArrayList<VWDSolucionesNodosN>();
        }
    }

    @Override
    public List<VWDSolucionesNodosNDetalle> consultarDetalleN(String vistaConsultar, int numeroConjunto, BigInteger secDescripcion) {
        try {
            return persistenciaVWDSolucionesNodosN.consultarDetalleN(em, vistaConsultar, numeroConjunto, secDescripcion);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarDetalleN : " + e);
            return null;
        }
    }

    @Override
    public List<VWDSolucionesNodosNDetalle> consultarDetalleNLB(String vistaConsultar, int numeroConjunto, BigInteger secDescripcion) {
        try {
            return persistenciaVWDSolucionesNodosN.consultarDetalleNLB(em, vistaConsultar, numeroConjunto, secDescripcion);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarDetalleNLB : " + e);
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Conceptos> consultarConceptos() {
        try {
//            return persistenciaConceptos.buscarConceptos(em);
            List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
            log.warn("AdministrarParametrosConjuntos.consultarConceptos() retorno lista con tamaño: " + lista.size());
            return lista;
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.consultarConceptos : " + e);
            return null;
        }
    }

    @Override
    public void modificarConcepto(Conceptos concepto) {
        try {
            persistenciaConceptos.editar(em, concepto);
        } catch (Exception e) {
            log.error("Error AdministrarParametrosConjuntos.editarConcepto : " + e);
        }
    }
}
