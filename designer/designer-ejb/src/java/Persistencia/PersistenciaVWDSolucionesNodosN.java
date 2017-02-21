/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.ParametrosConjuntos;
import Entidades.VWDSolucionesNodosN;
import Entidades.VWDSolucionesNodosNDetalle;
import InterfacePersistencia.PersistenciaVWDSolucionesNodosNInterface;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVWDSolucionesNodosN implements PersistenciaVWDSolucionesNodosNInterface {

    @Override
    public List<VWDSolucionesNodosN> consultarDSolucionesNodosN(EntityManager em, String vistaConsultar, Date fechaParametro) {
        try {
            List<VWDSolucionesNodosN> listaSN = new ArrayList<VWDSolucionesNodosN>();
            if (vistaConsultar.equals("VWDSOLUCIONESNODOS0")) {
                DateFormat formatoF = new SimpleDateFormat("ddMMyyyy");
                String fecha = formatoF.format(fechaParametro);
                em.clear();
                String sqlQuery = "SELECT VW.*, E.CODIGOEMPLEADO CODIGOEMPLEADO, C.NOMBRE CARGO, ES.NOMBRE ESTRUCTURA\n"
                        + "FROM VWDSOLUCIONESNODOS0 VW, EMPLEADOS E, VIGENCIASCARGOS VC, CARGOS C, ESTRUCTURAS ES WHERE \n"
                        + "VW.Y = E.SECUENCIA AND VC.EMPLEADO = E.SECUENCIA AND VC.CARGO = C.SECUENCIA \n"
                        + "AND VC.ESTRUCTURA = ES.SECUENCIA AND VC.FECHAVIGENCIA = "
                        + "(SELECT MAX(VCI.FECHAVIGENCIA) FROM VIGENCIASCARGOS VCI WHERE VCI.EMPLEADO=VC.EMPLEADO\n"
                        + "AND VCI.FECHAVIGENCIA <= TO_DATE('" + fecha + "', 'ddmmyyyy'))";
                System.out.println(this.getClass().getName() + " sqlQuery : " + sqlQuery);
                Query query = em.createNativeQuery(sqlQuery, VWDSolucionesNodosN.class);
                listaSN = query.getResultList();
            } else {
                em.clear();
                String sqlQuery = "SELECT * FROM " + vistaConsultar;
                System.out.println(this.getClass().getName() + " sqlQuery : " + sqlQuery);
                Query query = em.createNativeQuery(sqlQuery, VWDSolucionesNodosN.class);
                listaSN = query.getResultList();
            }
            return listaSN;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + " consultarDSolucionesNodosN() catch() ERROR : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VWDSolucionesNodosN> consultarDSolucionesNodosNLB(EntityManager em, String vistaConsultar, Date fechaParametro) {
        try {
            List<VWDSolucionesNodosN> listaSNLB = new ArrayList<VWDSolucionesNodosN>();
            if (vistaConsultar.equals("VWDSOLUCIONESNODOS0")) {
                DateFormat formatoF = new SimpleDateFormat("ddMMyyyy");
                String fecha = formatoF.format(fechaParametro);
                em.clear();
                String sqlQuery = "SELECT VW.*, E.CODIGOEMPLEADO CODIGOEMPLEADO, C.NOMBRE CARGO, ES.NOMBRE ESTRUCTURA\n"
                        + "FROM VWDSOLUCIONESNODOS0LB VW, EMPLEADOS E, VIGENCIASCARGOS VC, CARGOS C, ESTRUCTURAS ES WHERE \n"
                        + "VW.Y = E.SECUENCIA AND VC.EMPLEADO = E.SECUENCIA AND VC.CARGO = C.SECUENCIA \n"
                        + "AND VC.ESTRUCTURA = ES.SECUENCIA AND VC.FECHAVIGENCIA = "
                        + "(SELECT MAX(VCI.FECHAVIGENCIA) FROM VIGENCIASCARGOS VCI WHERE VCI.EMPLEADO=VC.EMPLEADO\n"
                        + "AND VCI.FECHAVIGENCIA <= TO_DATE('" + fecha + "', 'ddmmyyyy'))";
                System.out.println(this.getClass().getName() + " sqlQuery : " + sqlQuery);
                Query query = em.createNativeQuery(sqlQuery, VWDSolucionesNodosN.class);
                listaSNLB = query.getResultList();
            } else {
                em.clear();
                String sqlQuery = "SELECT * FROM " + vistaConsultar + "LB";
                System.out.println(this.getClass().getName() + " sqlQuery : " + sqlQuery);
                Query query = em.createNativeQuery(sqlQuery, VWDSolucionesNodosN.class);
                listaSNLB = query.getResultList();
            }
            return listaSNLB;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + " consultarDSolucionesNodosNLB() catch() ERROR : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VWDSolucionesNodosNDetalle> consultarDetalleN(EntityManager em, String vistaConsultar, int numeroConjunto, BigInteger secDescripcion
    ) {
        try {
            String vista = vistaConsultar + "DETALLE";
            String conjunto = "CONJUNTO" + numeroConjunto;
            String q = "SELECT VW.*, E.CODIGOEMPLEADO, P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO||' '||P.NOMBRE NOMBREEMPLEADO, C.DESCRIPCION NOMBRECONCEPTO FROM " + vista + " VW, EMPLEADOS E, "
                    + "PERSONAS P, CONCEPTOS C WHERE VW.Y = " + secDescripcion + " AND VW." + conjunto + "<>0 AND VW.EMPLEADO = E.SECUENCIA AND P.SECUENCIA = E.PERSONA AND C.SECUENCIA = VW.X";

            em.clear();
            Query query = em.createNativeQuery(q, VWDSolucionesNodosNDetalle.class);
            System.out.println("consultarDetalleN query q : " + q);
            List<VWDSolucionesNodosNDetalle> listaSNDetalle = query.getResultList();
            return listaSNDetalle;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + " consultarDetalleN() catch() ERROR : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VWDSolucionesNodosNDetalle> consultarDetalleNLB(EntityManager em, String vistaConsultar, int numeroConjunto, BigInteger secDescripcion
    ) {
        try {
            String vista = vistaConsultar + "LBDETALLE";
            String conjunto = "CONJUNTO" + numeroConjunto;
            String q = "SELECT VW.*, E.CODIGOEMPLEADO, P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO||' '||P.NOMBRE NOMBREEMPLEADO, C.DESCRIPCION NOMBRECONCEPTO FROM " + vista + " VW, EMPLEADOS E, "
                    + "PERSONAS P, CONCEPTOS C WHERE VW.Y = " + secDescripcion + " AND VW." + conjunto + "<>0 AND VW.EMPLEADO = E.SECUENCIA AND P.SECUENCIA = E.PERSONA AND C.SECUENCIA = VW.X";
            em.clear();
            Query query = em.createNativeQuery(q, VWDSolucionesNodosNDetalle.class);
            System.out.println("consultarDetalleNLB q : " + q);
            System.out.println(this.getClass().getName() + " query : " + query.toString());
            List<VWDSolucionesNodosNDetalle> listaSNLBDetalle = query.getResultList();
            return listaSNLBDetalle;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + " consultarDetalleNLB() catch() ERROR : " + e.getMessage());
            return null;
        }
    }
}
