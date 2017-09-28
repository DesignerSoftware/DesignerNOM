/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.Contratos;
import Entidades.NormasLaborales;
import Entidades.ReformasLaborales;
import Entidades.TiposContratos;
import Entidades.TiposSueldos;
import InterfacePersistencia.PersistenciaPlantillasTTInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PersistenciaPlantillasTT implements PersistenciaPlantillasTTInterface {

   private static Logger log = Logger.getLogger(PersistenciaPlantillasTT.class);

    @Override
    public List<ReformasLaborales> consultarReformasLaboralesValidas(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM REFORMASLABORALES RL WHERE NVL(TIPOSTRABAJADORES_PKG.PLANTILLAVALIDARl(?, RL.SECUENCIA),'S')='S'";
            Query query = em.createNativeQuery(sqlQuery, ReformasLaborales.class);
            query.setParameter(1, secTipoT);
            List<ReformasLaborales> listaReformasLaborales = query.getResultList();
            return listaReformasLaborales;
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasTT.consultarReformasLaboralesValidas() :  ", e);
            return null;
        }
    }

    @Override
    public List<TiposSueldos> consultarTiposSueldosValidos(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM TIPOSSUELDOS TS WHERE NVL(TIPOSTRABAJADORES_PKG.PLANTILLAVALIDATS(?, TS.SECUENCIA),'S')='S'";
            Query query = em.createNativeQuery(sqlQuery, TiposSueldos.class);
            query.setParameter(1, secTipoT);
            List<TiposSueldos> listaTiposSueldos = query.getResultList();
            return listaTiposSueldos;
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasTT.consultarTiposSueldosValidos() :  ", e);
            return null;
        }
    }

    @Override
    public List<TiposContratos> consultarTiposContratosValidos(EntityManager em, BigInteger secTipoT) {
       try {
            em.clear();
            String sqlQuery = "SELECT * FROM TIPOSCONTRATOS TC WHERE NVL(TIPOSTRABAJADORES_PKG.PLANTILLAVALIDATC(?, TC.SECUENCIA),'S')='S'";
            Query query = em.createNativeQuery(sqlQuery, TiposContratos.class);
            query.setParameter(1, secTipoT);
            List<TiposContratos> listaTiposContratos = query.getResultList();
            return listaTiposContratos;
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasTT.consultarTiposContratosValidos() :  ", e);
            return null;
        }
    }

    @Override
    public List<NormasLaborales> consultarNormasLaboralesValidas(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM NORMASLABORALES NL WHERE NVL(TIPOSTRABAJADORES_PKG.PLANTILLAVALIDANL(?, NL.SECUENCIA),'S')='S'";
            Query query = em.createNativeQuery(sqlQuery, NormasLaborales.class);
            query.setParameter(1, secTipoT);
            List<NormasLaborales> listaNormasLaborales = query.getResultList();
            return listaNormasLaborales;
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasTT.consultarNormasLaboralesValidas() :  ", e);
            return null;
        }
    }

    @Override
    public List<Contratos> consultarContratosValidos(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM CONTRATOS CO WHERE NVL(TIPOSTRABAJADORES_PKG.PLANTILLAVALIDALL(?, CO.SECUENCIA),'S')='S'";
            Query query = em.createNativeQuery(sqlQuery, Contratos.class);
            query.setParameter(1, secTipoT);
            List<Contratos> listaContratos = query.getResultList();
            return listaContratos;
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasTT.consultarContratosValidos() :  ", e);
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
