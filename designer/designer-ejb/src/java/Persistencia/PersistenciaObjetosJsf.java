/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ObjetosJsf;
import InterfacePersistencia.PersistenciaObjetosJsfInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaObjetosJsf implements PersistenciaObjetosJsfInterface {

    @Override
    public void crear(EntityManager em, ObjetosJsf objetojsf) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(objetojsf);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaObjetosJsf.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ObjetosJsf objetojsf) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(objetojsf);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaObjetosJsf.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ObjetosJsf objetojsf) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(objetojsf));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaObjetosJsf.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ObjetosJsf> consultarEnableObjetoJsf(EntityManager em, String perfil, String nomPantalla) {
        try {
            em.clear();
            String sql = "SELECT JSF.* \n"
                    + "FROM PANTALLASSEGURAS PS, BLOQUESPANTALLAS BP ,OBJETOSBLOQUES OB, PERMISOSPANTALLAS PP, PERFILES PER,OBJETOSJSF JSF \n"
                    + "WHERE BP.PANTALLA=PS.SECUENCIA \n"
                    + "AND OB.BLOQUE = BP.SECUENCIA \n"
                    + "AND PP.OBJETOFRM = OB.SECUENCIA \n"
                    + "AND PP.PERFIL=PER.SECUENCIA \n"
                    + "AND JSF.OBJETOFRM = OB.SECUENCIA \n"
                    + "AND PS.NOMBRE = ? \n"
                    + "AND PER.DESCRIPCION = ? \n"
                    + "AND PP.E ='N'";
            Query query = em.createNativeQuery(sql, ObjetosJsf.class);
            query.setParameter(1, nomPantalla);
            query.setParameter(2, perfil);
            List<ObjetosJsf> enable = query.getResultList();
            if (enable != null) {
                if (!enable.isEmpty()) {
                    for (int i = 0; i < enable.size(); i++) {
                        if (enable.get(i).getSecuencia() != null) {
                            em.clear();
                            String sqlAux = "SELECT PP.E \n"
                                    + "FROM PANTALLASSEGURAS PS, BLOQUESPANTALLAS BP ,OBJETOSBLOQUES OB, PERMISOSPANTALLAS PP, PERFILES PER,OBJETOSJSF JSF \n"
                                    + "WHERE BP.PANTALLA=PS.SECUENCIA \n"
                                    + "AND OB.BLOQUE = BP.SECUENCIA \n"
                                    + "AND PP.OBJETOFRM = OB.SECUENCIA \n"
                                    + "AND PP.PERFIL=PER.SECUENCIA \n"
                                    + "AND JSF.OBJETOFRM = OB.SECUENCIA \n"
                                    + "AND PS.NOMBRE = ? \n"
                                    + "AND PER.DESCRIPCION = ? \n"
                                    + "AND PP.E ='N' \n"
                                    + "AND JSF.SECUENCIA = " + enable.get(i).getSecuencia();
                            Query query2 = em.createNativeQuery(sqlAux);
                            query2.setParameter(1, nomPantalla);
                            query2.setParameter(2, perfil);
                            String aux = (String) query2.getSingleResult();
                            enable.get(i).setEnable(aux);
                        }
                    }
                }
            }
            return enable;
        } catch (Exception e) {
            System.out.println("Error en Persistencia.PersistenciaObjetosJsf.consultarEnableObjetoJsf() : " + e.getMessage());
            return null;
        }
    }
}
