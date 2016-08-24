/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Organigramas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import InterfacePersistencia.PersistenciaOrganigramasInterface;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> Clase encargada de realizar operaciones sobre la tabla
 * 'Organigramas' de la base de datos.
 *
 * @author Hugo David Sin Gutiérrez.
 */
@Stateless
public class PersistenciaOrganigramas implements PersistenciaOrganigramasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Organigramas organigramas) {
        System.out.println("em : " + em);
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(organigramas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaOrganigramas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Organigramas organigramas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(organigramas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaOrganigramas.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Organigramas organigramas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(organigramas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaOrganigramas.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Organigramas buscarOrganigrama(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Organigramas.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Organigramas> buscarOrganigramas(EntityManager em) {
        try {
            System.out.println("Si entro al EJB PersistenciaOrganigramas");
            em.clear();
            Query query = em.createQuery("SELECT o FROM Organigramas o");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Organigramas> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarOrganigramas PersistenciaOrganigramas : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Organigramas> buscarOrganigramasVigentes(EntityManager em, BigInteger secEmpresa, Date fechaVigencia) {
        try {
            em.clear();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = formatoFecha.format(fechaVigencia);
            Query query = em.createQuery("SELECT o FROM Organigramas o WHERE o.empresa.secuencia = :secEmpresa AND o.fecha >= TO_DATE(:fechaVigencia,'dd/MM/yyyy')");
            query.setParameter("secEmpresa", secEmpresa);
            query.setParameter("fechaVigencia", fecha);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Organigramas> listOrganigramas = query.getResultList();
            return listOrganigramas;
        } catch (Exception e) {
            System.out.println("PersistenciaOrganigramas: Fallo en la busqueda de los organigramas por fecha y por empresa ");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Organigramas> buscarOrganigramasEmpresa(EntityManager em, BigInteger secEmpresa) {
        System.out.println("buscarOrganigramasEmpresa : secEmpresa : " + secEmpresa);
        System.out.println("buscarOrganigramasEmpresa : em : " + em);
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM Organigramas o WHERE o.empresa.secuencia = :secEmpresa");
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Organigramas> listOrganigramas = query.getResultList();
            System.out.println("buscarOrganigramasEmpresa : listOrganigramas : " + em);
            /*
             * if (listOrganigramas != null) { for (Organigramas organigrama :
             * listOrganigramas) { System.out.println("organigrama : " +
             * organigrama); } }
             */
            System.out.println("Otra empresa");
            return listOrganigramas;
        } catch (Exception e) {
            System.out.println("PersistenciaOrganigramas: Fallo en la busqueda de los organigramas por empresa ");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Organigramas organigramaBaseArbol(EntityManager em, short codigoOrg) {
        try {
            em.clear();
            Organigramas organigrama = null;
            Query query = em.createQuery("SELECT COUNT(o) FROM Organigramas o WHERE o.codigo = :codigoOrg");
            query.setParameter("codigoOrg", codigoOrg);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query query2 = em.createQuery("SELECT o FROM Organigramas o WHERE o.codigo = :codigoOrg");
                query2.setParameter("codigoOrg", codigoOrg);
                query2.setHint("javax.persistence.cache.storeMode", "REFRESH");
                organigrama = (Organigramas) query2.getSingleResult();
            }
            return organigrama;
        } catch (Exception e) {
            System.out.println("Exepcion en organigramaBaseArbol " + e);
            return null;
        }
    }
}
