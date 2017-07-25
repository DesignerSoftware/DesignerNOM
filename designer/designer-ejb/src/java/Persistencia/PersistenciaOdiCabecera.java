/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfacePersistencia.PersistenciaOdiCabeceraInterface;
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
public class PersistenciaOdiCabecera implements PersistenciaOdiCabeceraInterface {

   private static Logger log = Logger.getLogger(PersistenciaOdiCabecera.class);

    @Override
    public void crear(EntityManager em, OdisCabeceras odicabecera) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(odicabecera);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, OdisCabeceras odicabecera) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(odicabecera);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, OdisCabeceras odicabecera) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(odicabecera));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Terceros> lovTerceros(EntityManager em, BigInteger anio, BigInteger mes) {
        try {
            em.clear();
            String qr = "SELECT ter.secuencia, ter.nit, ter.nombre\n"
                    + "             FROM TERCEROS ter\n"
                    + "             where exists (select 'x' from aportesentidades ae \n"
                    + "             where ae.tercero=ter.secuencia\n"
                    + "             and ae.ano=? \n"
                    + "             and ae.mes=? ) ";
            Query query = em.createNativeQuery(qr, Terceros.class);
            query.setParameter(1, anio);
            query.setParameter(2, mes);
            List<Terceros> listaTerceros = query.getResultList();
            return listaTerceros;
        } catch (Exception e) {
            log.error("Error: (lovTerceros)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> lovTiposEntidades(EntityManager em, BigInteger anio, BigInteger mes) {
        try {
            em.clear();
            String qr = "select * from tiposentidades te \n"
                    + "where exists (select 'x' from aportesentidades ae \n"
                    + "              where ae.tipoentidad=te.secuencia \n"
                    + "              and ae.ano=? \n"
                    + "              and ae.mes=? ) ";

            Query query = em.createNativeQuery(qr, TiposEntidades.class);
            query.setParameter(1, anio);
            query.setParameter(2, mes);
            List<TiposEntidades> tiposentidades = query.getResultList();
            return tiposentidades;
        } catch (Exception e) {
            log.error("Error: (lovTiposEntidades)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Empresas> lovEmpresas(EntityManager em) {
        try {
            em.clear();
            String qr = "SELECT secuencia,nombre, codigo FROM empresas";
            Query query = em.createNativeQuery(qr, Empresas.class);
            List<Empresas> listaEmpresas = query.getResultList();
            return listaEmpresas;
        } catch (Exception e) {
            log.error("Error: (lovEmpresas)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Empleados> lovEmpleados(EntityManager em) {
        try {
            em.clear();
            String qr = "SELECT EMP.*\n"
                    + "FROM   PERSONAS PER,\n"
                    + "       EMPLEADOS EMP\n"
                    + "WHERE  EMP.PERSONA=PER.SECUENCIA\n"
                    + "AND EXISTS (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
                    + "   WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                    + "   AND   VTT.EMPLEADO = EMP.SECUENCIA \n"
                    + "   AND   TT.TIPO = 'ACTIVO')"; //odiscabeceras.empresa

            Query query = em.createNativeQuery(qr, Empleados.class);
            List<Empleados> lovempleados = query.getResultList();
            return lovempleados;
        } catch (Exception e) {
            log.error("Error: (lovSucursalesPila)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<SucursalesPila> lovSucursalesPila(EntityManager em, BigInteger secuenciaEmpresa) {
        try {
            em.clear();
            String qr = "SELECT DESCRIPCION, codigo, secuencia FROM SUCURSALES_PILA WHERE EMPRESA=?"; //odiscabeceras.empresa
            Query query = em.createNativeQuery(qr, SucursalesPila.class);
            query.setParameter(1, secuenciaEmpresa);
            List<SucursalesPila> listasucursalespila = query.getResultList();
            return listasucursalespila;
        } catch (Exception e) {
            log.error("Error: (lovSucursalesPila)" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<RelacionesIncapacidades> lovRelacionesIncapacidades(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            String qr = "SELECT r.secuencia,r.mes||'-'||r.ano||' '||r.origenincapacidad detalle, r.valorcobro valorcobro, so.NUMEROCERTIFICADO\n"
                    + "FROM relacionesincapacidades R, terceros t, soausentismos so\n"
                    + "where R.TERCERO = T.SECUENCIA\n"
                    + "and so.secuencia=r.soausentismo\n"
                    + "and so.empleado=?";
            Query query = em.createNativeQuery(qr, RelacionesIncapacidades.class);
            query.setParameter(1, secuenciaEmpleado);
            List<RelacionesIncapacidades> lovrelacionesincapacidades = query.getResultList();
            return lovrelacionesincapacidades;

        } catch (Exception e) {
            log.error("Error: (lovRelacionesIncapacidades) " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<OdisCabeceras> listOdisCabeceras(EntityManager em, BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
        try {
            em.clear();
            String qr = "SELECT * FROM ODISCABECERAS WHERE ANO=? AND MES=? AND EMPRESA=? ";
//          Query query = em.createNativeQuery(qr);
            Query query = em.createNativeQuery(qr, OdisCabeceras.class);
            query.setParameter(1, anio);
            query.setParameter(2, mes);
            query.setParameter(3, secuenciaEmpresa);
//          query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<OdisCabeceras> listaodiscabeceras = query.getResultList();
            return listaodiscabeceras;
        } catch (Exception e) {
            log.error("Error: (listaNovedades)" + e.getMessage());
            return null;
        }
    }

    @Override
    public void crearDetalle(EntityManager em, OdisDetalles odidetalle) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(odidetalle);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.crearDetalle: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editarDetalle(EntityManager em, OdisDetalles odidetalle) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(odidetalle);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.editarDetalle: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrarDetalle(EntityManager em, OdisDetalles odidetalle) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(odidetalle));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOdiCabecera.borrarDetalle: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
