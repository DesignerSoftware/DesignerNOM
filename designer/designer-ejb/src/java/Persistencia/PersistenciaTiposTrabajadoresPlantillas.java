/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.TiposTrabajadores;
import InterfacePersistencia.PersistenciaTiposTrabajadoresPlantillasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTiposTrabajadoresPlantillas implements PersistenciaTiposTrabajadoresPlantillasInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposTrabajadoresPlantillas.class);

    @Override
    public List<PlantillasValidaTC> consultarPlanillaTC(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM PLANTILLASVALIDATC WHERE TIPOTRABAJADOR = ?";
            Query query = em.createNativeQuery(sqlQuery, PlantillasValidaTC.class);
            query.setParameter(1, secTipoT);
            List<PlantillasValidaTC> listaPlantillaTC = query.getResultList();
            return listaPlantillaTC;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarPlanillaTC() : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlantillasValidaTS> consultarPlanillaTS(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM PLANTILLASVALIDATS WHERE TIPOTRABAJADOR = ?";
            Query query = em.createNativeQuery(sqlQuery, PlantillasValidaTS.class);
            query.setParameter(1, secTipoT);
            List<PlantillasValidaTS> listaPlantillaTS = query.getResultList();
            return listaPlantillaTS;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarPlanillaTS() : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlantillasValidaRL> consultarPlanillaRL(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM PLANTILLASVALIDARL WHERE TIPOTRABAJADOR = ?";
            Query query = em.createNativeQuery(sqlQuery, PlantillasValidaRL.class);
            query.setParameter(1, secTipoT);
            List<PlantillasValidaRL> listaPlantillaRL = query.getResultList();
            return listaPlantillaRL;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarPlanillaRL() : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlantillasValidaLL> consultarPlanillaLL(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM PLANTILLASVALIDALL WHERE TIPOTRABAJADOR = ?";
            Query query = em.createNativeQuery(sqlQuery, PlantillasValidaLL.class);
            query.setParameter(1, secTipoT);
            List<PlantillasValidaLL> listaPlantillaLL = query.getResultList();
            return listaPlantillaLL;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarPlanillaLL() : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlantillasValidaNL> consultarPlanillaNL(EntityManager em, BigInteger secTipoT) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM PLANTILLASVALIDANL WHERE TIPOTRABAJADOR = ?";
            Query query = em.createNativeQuery(sqlQuery, PlantillasValidaNL.class);
            query.setParameter(1, secTipoT);
            List<PlantillasValidaNL> listaPlantillaNL = query.getResultList();
            return listaPlantillaNL;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarPlanillaNL() : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposTrabajadores> consultarTiposTrabajadores(EntityManager em) {
        try {
            em.clear();
            String sqlQuery = "SELECT * FROM TIPOSTRABAJADORES ORDER BY CODIGO ";
            Query query = em.createNativeQuery(sqlQuery, TiposTrabajadores.class);
            List<TiposTrabajadores> listatt = query.getResultList();
            return listatt;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTrabajadoresPlantillas.consultarTiposTrabajadores() : " + e.toString());
            return null;
        }
    }

    @Override
    public boolean consultarRegistrosSecundarios(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "SELECT count(*) FROM TIPOSTRABAJADORES TI WHERE SECUENCIA = ?  AND (EXISTS\n"
                    + "(SELECT 'X' FROM PLANTILLASVALIDATC WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                    + "OR EXISTS\n"
                    + "(SELECT 'X' FROM PLANTILLASVALIDATS WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                    + "OR EXISTS\n"
                    + "(SELECT 'X' FROM PLANTILLASVALIDARL WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                    + "OR EXISTS\n"
                    + "(SELECT 'X' FROM PLANTILLASVALIDANL WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                    + "OR EXISTS\n"
                    + "(SELECT 'X' FROM PLANTILLASVALIDALL WHERE TIPOTRABAJADOR = TI.SECUENCIA))";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            BigDecimal r = (BigDecimal) query.getSingleResult();
            if (r.intValue() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("PersistenciaTiposTrabajadoresPlantillas.consultarRegistrosSecundarios()" + e.getMessage());
            return false;
        }
    }

}
