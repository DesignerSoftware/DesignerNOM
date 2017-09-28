/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Personas;
import Entidades.PersonasAux;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Personas' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPersonas implements PersistenciaPersonasInterface {

   private static Logger log = Logger.getLogger(PersistenciaPersonas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, Personas personas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(personas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPersonas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Personas personas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(personas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPersonas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Personas personas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(personas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPersonas.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Personas buscarPersona(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Personas persona = em.find(Personas.class, secuencia);
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.buscarPersona() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("PersistenciaPersonas.buscarPersona():  ", e);
         return null;
      }
   }

   @Override
   public List<Personas> consultarPersonas(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM Personas";
         Query query = em.createNativeQuery(sql, Personas.class);
         List<Personas> listaPersonas = query.getResultList();
         if (listaPersonas != null) {
            if (!listaPersonas.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P", PersonasAux.class);
               List<PersonasAux> listaPersonasAux = query2.getResultList();
               log.warn("PersistenciaPersonas.consultarPersonas() Ya consulo Transients");
               if (listaPersonasAux != null) {
                  if (!listaPersonasAux.isEmpty()) {
                     log.warn("PersistenciaPersonas.consultarPersonas() listaPersonasAux.size(): " + listaPersonasAux.size());
                     for (PersonasAux recAux : listaPersonasAux) {
                        for (Personas recPersona : listaPersonas) {
                           if (recAux.getSecuencia().equals(recPersona.getSecuencia())) {
                              recPersona.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return listaPersonas;
      } catch (Exception e) {
         log.error("PersistenciaPersonas.consultarPersonas():  ", e);
         return null;
      }
   }

   @Override
   public void actualizarFotoPersona(EntityManager em, BigInteger identificacion) {
      try {
         em.clear();
         em.getTransaction().begin();
         Query query = em.createNativeQuery("update Personas p set p.pathfoto='S' where p.numerodocumento = ?");
         query.setParameter(1, identificacion);
         query.executeUpdate();
         em.getTransaction().commit();
      } catch (Exception e) {
         log.error("PersistenciaPersonas.actualizarFotoPersona():  ", e);
      }
   }

   @Override
   public Personas buscarFotoPersona(EntityManager em, BigInteger identificacion) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT p from Personas p where p.numerodocumento = :identificacion");
         query.setParameter("identificacion", identificacion);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Personas persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.buscarFotoPersona() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("PersistenciaPersonas.buscarFotoPersona():  ", e);
         Personas personas = null;
         return personas;
      }
   }

   @Override
   public Personas buscarPersonaSecuencia(EntityManager em, BigInteger secuencia) {
      Personas persona;
      try {
         em.clear();
         String sql = "SELECT p.* FROM Personas p WHERE p.secuencia = ?";
         Query query = em.createNativeQuery(sql, Personas.class);
         query.setParameter(1, secuencia);
         //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.buscarPersonaSecuencia() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("PersistenciaPersonas.buscarPersonaSecuencia():  ", e);
         persona = null;
      }
      return persona;
   }

   @Override
   public Personas buscarPersonaPorEmpleado(EntityManager em, BigInteger secEmpleado) {
      Personas persona;
      try {
         em.clear();
         String sql = "SELECT p.* FROM Personas p, Empleados e WHERE e.persona = p.secuencia and e.secuencia = ?";
         Query query = em.createNativeQuery(sql, Personas.class);
         query.setParameter(1, secEmpleado);
         //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.buscarPersonaPorEmpleado() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("PersistenciaPersonas.buscarPersonaPorEmpleado():  ", e);
         persona = null;
      }
      return persona;
   }

   @Override
   public Personas buscarPersonaPorNumeroDocumento(EntityManager em, BigInteger numeroDocumento) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT p FROM Personas p WHERE p.numerodocumento=:numeroDocumento AND p.digitoverificaciondocumento is null");
         query.setParameter("numeroDocumento", numeroDocumento);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Personas persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.buscarPersonaPorNumeroDocumento() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("Error buscarPersonaPorNumeroDocumento PersistenciaPersonas :  ", e);
         return null;
      }
   }

   @Override
   public Personas obtenerUltimaPersonaAlmacenada(EntityManager em, BigInteger documento) {
      try {
         em.clear();
         log.warn("documento : " + documento);
         Query query = em.createQuery("SELECT p FROM Personas p WHERE p.numerodocumento = :documento");
         query.setParameter("documento", documento);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Personas persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaPersonas.obtenerUltimaPersonaAlmacenada() personaAux: " + personaAux);
               if (personaAux != null) {
                  if (personaAux.getSecuencia() != null) {
                     if (personaAux.getSecuencia().equals(persona.getSecuencia())) {
                        persona.llenarTransients(personaAux);
                     }
                  }
               }
            }
         }
         return persona;
      } catch (Exception e) {
         log.error("Error obtenerUltimaPersonaAlmacenada PersistenciaPersonas :  ", e);
         return null;
      }
   }

}
