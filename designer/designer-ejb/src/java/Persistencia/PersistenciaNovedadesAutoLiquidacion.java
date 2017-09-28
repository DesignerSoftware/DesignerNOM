/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfacePersistencia.PersistenciaNovedadesAutoLiquidacionInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaNovedadesAutoLiquidacion implements PersistenciaNovedadesAutoLiquidacionInterface {

   private static Logger log = Logger.getLogger(PersistenciaNovedadesAutoLiquidacion.class);

    @Override
    public void crear(EntityManager em, NovedadesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesAutoLiquidacion.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, NovedadesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesAutoLiquidacion.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, NovedadesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(novedades));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesAutoLiquidacion.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SucursalesPila> listasucursalesPila(EntityManager em, BigInteger secuenciaEmpresa) {
        try {
            em.clear();
            String qr = "SELECT DESCRIPCION, codigo, secuencia FROM SUCURSALES_PILA WHERE EMPRESA=?";
            Query query = em.createNativeQuery(qr, SucursalesPila.class);
            query.setParameter(1, secuenciaEmpresa);
            List<SucursalesPila> listasucursalespila = query.getResultList();
            return listasucursalespila;
        } catch (Exception e) {
            log.error("Error: (listasucursalesPila) ", e);
            return null;
        }
    }

    @Override
    public List<Terceros> listaTerceros(EntityManager em) {
        try {
            em.clear();
            String qr = " SELECT SECUENCIA, NIT, NOMBRE FROM TERCEROS ORDER BY NOMBRE";
//            Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, Terceros.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Terceros> listaTerceros = query.getResultList();
            return listaTerceros;
        } catch (Exception e) {
            log.error("Error: (listaTerceros) ", e);
            return null;
        }
    }

    @Override
    public List<TiposEntidades> listaTiposEntidades(EntityManager em) {
        try {
            em.clear();
            String qr = "SELECT SECUENCIA, NOMBRE FROM TIPOSENTIDADES  te WHERE EXISTS (SELECT  'x' FROM grupostiposentidades gte \n"
                    + "             WHERE gte.secuencia=te.grupo\n"
                    + "             AND gte.requeridopila='S')\n"
                    + "ORDER BY NOMBRE ";
//            Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, TiposEntidades.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposentidades = query.getResultList();
            return tiposentidades;
        } catch (Exception e) {
            log.error("Error: (listaTiposEntidades) ", e);
            return null;
        }
    }

    @Override
    public List<Empresas> listaEmpresas(EntityManager em) {
        try {
            em.clear();
            String qr = "SELECT secuencia,nombre, codigo FROM empresas";
//            Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, Empresas.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Empresas> listaEmpresas = query.getResultList();
            return listaEmpresas;
        } catch (Exception e) {
            log.error("Error: (listaEmpresas) ", e);
            return null;
        }

    }

    @Override
    public List<NovedadesAutoLiquidaciones> listaNovedades(EntityManager em, BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
        try {
            em.clear();
            String qr = "SELECT * FROM NOVEDADESAUTOLIQUIDACIONES WHERE ANO=? AND MES=? AND EMPRESA=? ";
//          Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, NovedadesAutoLiquidaciones.class);
            query.setParameter(1, anio);
            query.setParameter(2, mes);
            query.setParameter(3, secuenciaEmpresa);
//          query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<NovedadesAutoLiquidaciones> listanovedades = query.getResultList();
            return listanovedades;
        } catch (Exception e) {
            log.error("Error: (listaNovedades) ", e);
            return null;
        }
    }

}
