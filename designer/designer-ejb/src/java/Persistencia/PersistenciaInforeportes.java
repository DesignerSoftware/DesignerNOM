/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Inforeportes;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Inforeportes' de la
 * base de datos. (las busquedas se realizan sobre la tabla
 * 'UsuariosInforeportes')
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaInforeportes implements PersistenciaInforeportesInterface {

   private static Logger log = Logger.getLogger(PersistenciaInforeportes.class);

    private EntityManager em;

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Inforeportes inforeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(inforeportes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInforeportes.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Inforeportes inforeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(inforeportes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInforeportes.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Inforeportes inforeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(inforeportes));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaInforeportes.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportes(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT i FROM Inforeportes i");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error PersistenciaInforeportes buscarInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioNomina(EntityManager em) {
        try {
            em.clear();
            //Query query = em.createQuery("SELECT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'NOM' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'NOM' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.contador DESC, ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioLaboral(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'LBL' AND ui.inforeporte.codigo <> 17 AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioBanco(EntityManager em) {
        try {
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'BAN' "
                    + "AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a)and ui.inforeporte.nombre IS NOT null ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioContabilidad(EntityManager em) {
        try {
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'CON' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioPersonal(EntityManager em) {
        try {
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'PER' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioBienestar(EntityManager em) {
        try {
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'BIN' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioSeguridadSocial(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'SES' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioCapacitacion(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'CAP' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuario PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesUsuarioEvaluacionCompetencias(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT ui.inforeporte FROM UsuariosInforeportes ui WHERE ui.inforeporte.modulo.nombrecorto = 'ECO' AND ui.usuario.alias = (SELECT a.alias FROM ActualUsuario a) ORDER BY ui.inforeporte.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error en buscarInforeportesUsuarioEvaluacionCompetencias PersistenciaInforeportes :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarInforeportesAdminReportes(EntityManager em) {
       try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM INFOREPORTES ORDER BY CODIGO,MODULO",Inforeportes.class);
            List<Inforeportes> inforeportes = (List<Inforeportes>) query.getResultList();
            return inforeportes;
        } catch (Exception e) {
            log.error("Error PersistenciaInforeportes buscarInforeportesAdminReportes :  ", e);
            return null;
        }
    }

}
