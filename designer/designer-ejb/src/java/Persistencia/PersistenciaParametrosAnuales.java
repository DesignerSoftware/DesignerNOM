/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaParametrosAnualesInterface;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
@Local
public class PersistenciaParametrosAnuales implements PersistenciaParametrosAnualesInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametrosAnuales.class);

    @Override
    public BigDecimal consultarSMLV(EntityManager em) {
        try {
            em.clear();
            String sql = "   SELECT VALORREAL\n"
                    + "   FROM TIPOSCONSTANTES  TC \n"
                    + "   WHERE TC.OPERANDO=(SELECT SECUENCIA FROM OPERANDOS WHERE CODIGO=2409)\n"
                    + "   AND TC.FECHAINICIAL = (SELECT MAX(TCI.FECHAINICIAL)\n"
                    + "                          FROM TIPOSCONSTANTES TCI\n"
                    + "                          WHERE TCI.OPERANDO=TC.OPERANDO\n"
                    + "                          AND TCI.FECHAINICIAL <= (SELECT last_day(p.fechahastacausado) \n"
                    + "                                                   FROM PARAMETROSESTRUCTURAS p,usuarios u \n"
                    + "                                                   WHERE p.usuario=u.secuencia \n"
                    + "                                                   AND u.alias=USER) \n"
                    + "                       )";
            Query query = em.createNativeQuery(sql);
            BigDecimal smlv = (BigDecimal) query.getSingleResult();
            return smlv;
        } catch (Exception e) {
            log.error("error PersistenciaParametrosAnuales.consultarSMLV : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal consultarAuxTransporte(EntityManager em) {
        try {
            em.clear();
            String sql = " SELECT VALORREAL -- TO_CHAR(VALORREAL,'$999,999,999')\n"
                    + "   FROM TIPOSCONSTANTES  TC \n"
                    + "  WHERE TC.OPERANDO=(SELECT SECUENCIA FROM OPERANDOS WHERE CODIGO=2410)\n"
                    + "  AND TC.FECHAINICIAL = (SELECT MAX(TCI.FECHAINICIAL)\n"
                    + "                       FROM TIPOSCONSTANTES TCI\n"
                    + "                       WHERE TCI.OPERANDO=TC.OPERANDO\n"
                    + "                       AND TCI.FECHAINICIAL <= (SELECT last_day(p.fechahastacausado) \n"
                    + "                                                FROM PARAMETROSESTRUCTURAS p,usuarios u \n"
                    + "                                                WHERE p.usuario=u.secuencia \n"
                    + "                                                AND u.alias=USER) \n"
                    + "                       )";
            Query query = em.createNativeQuery(sql);
            BigDecimal auxtrans = (BigDecimal) query.getSingleResult();
            return auxtrans;
        } catch (Exception e) {
            log.error("error PersistenciaParametrosAnuales.consultarSMLV : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal consultarValorUVT(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT UVT\n"
                    + "  FROM VIGENCIASRETENCIONES VR \n"
                    + "  WHERE VR.FECHAVIGENCIA = (SELECT MAX(VRI.FECHAVIGENCIA)\n"
                    + "                       FROM VIGENCIASRETENCIONES VRI\n"
                    + "                       WHERE VRI.FECHAVIGENCIA <= (SELECT last_day(p.fechahastacausado) \n"
                    + "                                                FROM PARAMETROSESTRUCTURAS p,usuarios u \n"
                    + "                                                WHERE p.usuario=u.secuencia \n"
                    + "                                                AND u.alias=USER) \n"
                    + "                       )";
            Query query = em.createNativeQuery(sql);
            BigDecimal valoruvt = (BigDecimal) query.getSingleResult();
            return valoruvt;
        } catch (Exception e) {
            log.error("error PersistenciaParametrosAnuales.consultarSMLV : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal consultarvalorMinIBC(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT (VALORREAL*4)\n"
                    + "     FROM TIPOSCONSTANTES  TC \n"
                    + "   WHERE TC.OPERANDO=(SELECT SECUENCIA FROM OPERANDOS WHERE CODIGO=2409)\n"
                    + "   AND TC.FECHAINICIAL = (SELECT MAX(TCI.FECHAINICIAL)\n"
                    + "                          FROM TIPOSCONSTANTES TCI\n"
                    + "                          WHERE TCI.OPERANDO=TC.OPERANDO\n"
                    + "                          AND TCI.FECHAINICIAL <= (SELECT last_day(p.fechahastacausado) \n"
                    + "                                                   FROM PARAMETROSESTRUCTURAS p,usuarios u \n"
                    + "                                                   WHERE p.usuario=u.secuencia \n"
                    + "                                                   AND u.alias=USER) \n"
                    + "                       ) ";
            Query query = em.createNativeQuery(sql);
            BigDecimal valorminibc = (BigDecimal) query.getSingleResult();
            return valorminibc;
        } catch (Exception e) {
            log.error("error PersistenciaParametrosAnuales.consultarSMLV : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal consultarTopeMaxSegSocial(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT (VALORREAL*25)\n"
                    + "   FROM TIPOSCONSTANTES  TC \n"
                    + "   WHERE TC.OPERANDO=(SELECT SECUENCIA FROM OPERANDOS WHERE CODIGO=2409)\n"
                    + "   AND TC.FECHAINICIAL = (SELECT MAX(TCI.FECHAINICIAL)\n"
                    + "                          FROM TIPOSCONSTANTES TCI\n"
                    + "                          WHERE TCI.OPERANDO=TC.OPERANDO\n"
                    + "                          AND TCI.FECHAINICIAL <= (SELECT last_day(p.fechahastacausado) \n"
                    + "                                                   FROM PARAMETROSESTRUCTURAS p,usuarios u \n"
                    + "                                                   WHERE p.usuario=u.secuencia \n"
                    + "                                                   AND u.alias=USER) \n"
                    + "                       )";
            Query query = em.createNativeQuery(sql);
            BigDecimal topesegsocial = (BigDecimal) query.getSingleResult();
            return topesegsocial;
        } catch (Exception e) {
            log.error("error PersistenciaParametrosAnuales.consultarSMLV : " + e.getMessage());
            return null;
        }
    }

}
