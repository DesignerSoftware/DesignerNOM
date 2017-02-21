/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Empresas;
import Entidades.NovedadesCorreccionesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfacePersistencia.PersistenciaNovedadesCorreccionAutoLInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaNovedadesCorreccionAutoL  implements PersistenciaNovedadesCorreccionAutoLInterface{

        @Override
    public void crear(EntityManager em, NovedadesCorreccionesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedadesCorreccionAutoL.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, NovedadesCorreccionesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedadesCorreccionAutoL.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, NovedadesCorreccionesAutoLiquidaciones novedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(novedades));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNovedadesCorreccionAutoL.borrar: " + e.getMessage());
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
            System.out.println("Error: (listasucursalesPila)" + e.getMessage());
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
            System.out.println("Error: (listaTerceros)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> listaTiposEntidades(EntityManager em) {
        try {
            em.clear();
            String qr = "SELECT SECUENCIA, NOMBRE FROM TIPOSENTIDADES ORDER BY NOMBRE";
//            Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, TiposEntidades.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposentidades = query.getResultList();
            return tiposentidades;
        } catch (Exception e) {
            System.out.println("Error: (listaTiposEntidades)" + e.getMessage());
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
            System.out.println("Error: (listaEmpresas)" + e.getMessage());
            return null;
        }

    }

    @Override
    public List<NovedadesCorreccionesAutoLiquidaciones> listaNovedades(EntityManager em, BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
        try {
            em.clear();
            String qr = "SELECT * FROM NOVEDADESCORRECCIONESAUTOL WHERE ANO=? AND MES=? AND EMPRESA=? ";
//          Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, NovedadesCorreccionesAutoLiquidaciones.class);
            query.setParameter(1, anio);
            query.setParameter(2, mes);
            query.setParameter(3, secuenciaEmpresa);
//          query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<NovedadesCorreccionesAutoLiquidaciones> listanovedades = query.getResultList();
            return listanovedades;
        } catch (Exception e) {
            System.out.println("Error: (listaNovedades)" + e.getMessage());
            return null;
        }
    }
}
