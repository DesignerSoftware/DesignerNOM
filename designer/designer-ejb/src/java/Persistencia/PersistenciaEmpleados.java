/**
 * DocumentaciÃ³n a cargo de Hugo David Sin GutiÃ©rrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.EmpleadosAux;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import Entidades.NovedadesSistema;
import Entidades.Personas;
import Entidades.PersonasAux;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class PersistenciaEmpleados implements PersistenciaEmpleadoInterface {

   private List<Empleados> listaEmpleados = new ArrayList<Empleados>();
   private static Logger log = Logger.getLogger(PersistenciaEmpleados.class);

   @Override
   public void crear(EntityManager em, Empleados empleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(empleados);
         tx.commit();
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.crear() e: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public BigInteger crearConVCargo(EntityManager em, BigDecimal codigoEmpleado, BigInteger secPersona, BigInteger secEmpresa,
           BigInteger secCargo, BigInteger secEstructura, Date fechaIngreso, BigInteger secMotivoCargo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("EMPLEADOCURRENT_PKG.CREAR_EMPLEADO_CON_VCARGO");
         query.registerStoredProcedureParameter(1, BigDecimal.class, ParameterMode.INOUT);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(5, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(6, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(7, BigInteger.class, ParameterMode.IN);

         query.setParameter(1, codigoEmpleado);
         query.setParameter(2, secPersona);
         query.setParameter(3, secEmpresa);
         query.setParameter(4, secCargo);
         query.setParameter(5, secEstructura);
         query.setParameter(6, fechaIngreso);
         query.setParameter(7, secMotivoCargo);

         query.execute();
         query.hasMoreResults();
         BigDecimal empleado_Sec = (BigDecimal) query.getOutputParameterValue(1);
         log.warn("PersistenciaEmpleados crearConVCargo() se supone creo el empleado con V cargo");
         log.warn("crearConVCargo() retorno parametro ''SECUENCIA_EMPLEADO'' : " + empleado_Sec);
         tx.commit();
         return empleado_Sec.toBigInteger();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".crearConVCargo()");
         log.error("error al crear el empleado");
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
         return null;
      }
   }

   @Override
   public void editar(EntityManager em, Empleados empleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(empleados);
         tx.commit();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".editar() error " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Empleados empleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(empleados));
         tx.commit();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".borrar() error " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Empleados buscarEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Empleados empleado = em.find(Empleados.class, secuencia);
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".buscarEmpleado() error " + e.toString());
         return null;
      }
   }

   @Override
   public Personas buscarPersonaPorEmpleado(EntityManager em, BigInteger secEmpleado) {
      Personas persona;
      try {
         em.clear();
         String sql = "SELECT p.* FROM Personas p, Empleados e WHERE e.persona = p.secuencia and e.secuencia = ?";
         Query query = em.createNativeQuery(sql, Personas.class);
         query.setParameter(1, secEmpleado);
         persona = (Personas) query.getSingleResult();
         if (persona != null) {
            if (persona.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT P.SECUENCIA, (select nombrelargo from TIPOSDOCUMENTOS where secuencia = p.TIPODOCUMENTO) NOMBRETIPODOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADDOCUMENTO) NOMBRECIUDADDOCUMENTO,\n"
                       + " (select nombre from ciudades where secuencia = p.CIUDADNACIMIENTO) NOMBRECIUDADNACIMIENTO\n"
                       + " FROM PERSONAS P WHERE P.SECUENCIA = " + persona.getSecuencia(), PersonasAux.class);
               PersonasAux personaAux = (PersonasAux) query2.getSingleResult();
               log.warn("PersistenciaEmpleados.buscarPersonaPorEmpleado() personaAux: " + personaAux);
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
         log.error("PersistenciaEmpleados.buscarPersonaPorEmpleado()" + e.getMessage());
         persona = null;
      }
      return persona;
   }

   @Override
   public List<Empleados> buscarEmpleadosActivos(EntityManager em) {
      log.warn("PersistenciaEmpleados.buscarEmpleadosActivos()");
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS"
                 + " (SELECT vtt FROM VWActualesTiposTrabajadores vtt WHERE vtt.empleado = e.secuencia"
                 + " AND vtt.tipoTrabajador.tipo = 'ACTIVO')");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
//         String sqlQuery = "SELECT * FROM EMPLEADOS E\n"
//                 + " WHERE EXISTS (SELECT 'x' FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
//                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
//                 + " AND VTT.EMPLEADO = E.SECUENCIA \n"
//                 + " AND TT.TIPO = 'ACTIVO')";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " AND EXISTS (SELECT 'x' FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT\n"
                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA\n"
                 + " AND VTT.EMPLEADO = E.SECUENCIA\n"
                 + " AND TT.TIPO = 'ACTIVO')");
//         if (listaEmpleados != null) {
//            log.warn("buscarEmpleadosActivos() listaEmpleados.size(): " + listaEmpleados.size());
//         }
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error(this.getClass().getName() + " error en buscarEmpleados() ERROR: " + e);
         e.printStackTrace();
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> todosEmpleados(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e ORDER BY e.codigoempleado ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " ORDER BY E.codigoempleado ASC");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.todosEmpleados() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosLiquidacionesLog(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS (SELECT li.secuencia FROM LiquidacionesLogs li WHERE li.empleado = e.secuencia) ORDER BY e.codigoempleado ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " AND EXISTS (SELECT li.secuencia FROM LiquidacionesLogs li WHERE li.empleado = e.secuencia) ORDER BY e.codigoempleado ASC");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosLiquidacionesLog() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public Empleados buscarEmpleadoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         Empleados empleado = (Empleados) query.getSingleResult();
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error("Error PersistenciaEmpleados.buscarEmpleadoSecuencia " + e);
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleadoSecuenciaPersona(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.persona.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadoSecuenciaPersona() e: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarCodigoEmpleado_Empresa(EntityManager em, BigInteger codigoEmpleado, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(e) FROM Empleados e WHERE e.codigoempleado = :codigo AND e.empresa = :secEmpresa");
         query.setParameter("codigo", codigoEmpleado);
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado > 0;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.verificarCodigoEmpleado_Empresa() e: " + e);
         return false;
      }
   }

//   @Override
//   public Empleados buscarEmpleadoPorCodigoyEmpresa(EntityManager em, BigDecimal codigo, BigInteger empresa) {
//      try {
//         em.clear();
//         String sql = "SELECT * FROM empleados WHERE CODIGOEMPLEADO =? AND NVL(EMPRESA,?)=?";
//         Query query = em.createNativeQuery(sql, Empleados.class);
//         query.setParameter(1, codigo);
//         query.setParameter(2, empresa);
//         query.setParameter(3, empresa);
//         Empleados empleado = (Empleados) query.getSingleResult();
//            empleado = consultarTransients(em, empleado);
//         return empleado;
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadoPorCodigoyEmpresa()");
//         return null;
//      }
//   }
   @Override
   public Empleados buscarEmpleadoCodigo_Empresa(EntityManager em, BigInteger codigoEmpleado, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE AND e.empresa = :secEmpresa");
         query.setParameter("codigoE", codigoEmpleado);
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadoCodigo_Empresa() e: " + e);
         return null;
      }
   }

//   @Override
//   public Empleados buscarEmpleadoTipo(EntityManager em, BigInteger codigoEmpleado) {
//      try {
//         em.clear();
//         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
//         query.setParameter("codigoE", codigoEmpleado);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
//         Empleados empleado = (Empleados) query.getSingleResult();
//            empleado = consultarTransients(em, empleado);
//         return empleado;
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadoTipo() e: " + e);
//         return null;
//      }
//   }
   @Override
   public Empleados buscarEmpleadoCodigo(EntityManager em, BigInteger codigoEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
         query.setParameter("codigoE", codigoEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadoCodigo() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosPorCodigo(EntityManager em, List<BigInteger> codigosEmpleado) {
      try {
         for (int i = 0; i < codigosEmpleado.size(); i++) {
            em.clear();
            try {
               Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
               query.setParameter("codigoE", codigosEmpleado.get(i));
               query.setHint("javax.persistence.cache.storeMode", "REFRESH");
               Empleados empleado = (Empleados) query.getSingleResult();
               empleado = consultarTransients(em, empleado);
               listaEmpleados.add(empleado);
            } catch (Exception ei) {
               log.error("ERROR en el for() en buscarEmpleadosPorCodigo() ei : " + ei);
            }
         }
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("ERROR en buscarEmpleadosPorCodigo() e : " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> empleadosComprobantes(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS (SELECT 1 FROM Parametros p ,ParametrosInstancias pi, UsuariosInstancias ui, Usuarios u WHERE p.empleado = e.secuencia and p.secuencia = pi.parametro and pi.instancia = ui.instancia and ui.usuario = u.secuencia and u.alias = :usuarioBD)");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " AND EXISTS (SELECT 1 FROM Parametros p ,ParametrosInstancias pi, UsuariosInstancias ui, Usuarios u\n"
                 + " WHERE p.empleado = e.secuencia and p.secuencia = pi.parametro"
                 + " and pi.instancia = ui.instancia and ui.usuario = u.secuencia and u.alias = '" + usuarioBD + "')");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.empleadosComprobantes()");
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> empleadosNovedad(EntityManager em) {
      try {
         em.clear();
//         String sqlQuery = "select SECUENCIA, CODIGOEMPLEADO, RUTATRANSPORTE, PARQUEADERO, FECHACREACION, CODIGOALTERNATIVO,\n"
//                 + " PAGASUBSIDIOTRANSPORTELEGAL, PERSONA, EMPRESA, USUARIOBD from empleados v where \n"
//                 + " EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
//                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = v.secuencia and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS"
                 + " (SELECT vtt FROM VWActualesTiposTrabajadores vtt WHERE vtt.empleado = e.secuencia"
                 + " AND vtt.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))", Empleados.class);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaEmpleados = query.getResultList();
         log.warn("PersistenciaEmpleados.empleadosNovedad() 1");
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " AND EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = E.secuencia and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.empleadosNovedad()");
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public int contarEmpleadosNovedad(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select count(*) \n"
                 + " from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " where tt.secuencia = vtt.tipotrabajador\n"
                 + " and vtt.empleado = v.secuencia\n"
                 + " and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))";
         Query query = em.createNativeQuery(sqlQuery);
         Long resultado = new Long(query.getSingleResult().toString());
         log.warn("contarEmpleadosNovedad resultado : " + resultado);

         int N = new Integer(query.getSingleResult().toString());
         log.warn("contarEmpleadosNovedad retorno : " + N);
         return N;
      } catch (Exception e) {
         log.error("Error contarEmpleadosNovedad() : " + e);
         return -1;
      }
   }

   @Override
   public List<Empleados> empleadosNovedadSoloAlgunos(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select SECUENCIA, CODIGOEMPLEADO, RUTATRANSPORTE, PARQUEADERO, FECHACREACION, CODIGOALTERNATIVO,\n"
                 + " PAGASUBSIDIOTRANSPORTELEGAL, PERSONA, EMPRESA, USUARIOBD from empleados v where \n"
                 + " EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = v.secuencia and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')) and ROWNUM < 50";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA\n"
                 + " AND EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = E.secuencia and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')) and ROWNUM < 50");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.empleadosNovedadSoloAlgunos() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

//   @Override
//   public List<Empleados> empleadosVacaciones(EntityManager em) {
//      try {
//         em.clear();
//         String sqlQuery = "select * from empleados v where EXISTS"
//                 + " (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
//                 + " where tt.secuencia = vtt.tipotrabajador\n"
//                 + " and vtt.empleado = v.secuencia\n"
//                 + " and tt.tipo IN ('ACTIVO'))";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
//         listaEmpleados = query.getResultList();
//         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
//                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
//                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
//                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS"
//                 + " (SELECT 'X' from VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores tt\n"
//                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = E.secuencia and tt.tipo IN ('ACTIVO'))");
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.empleadosVacaciones() e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }
   @Override
   public List<Empleados> buscarEmpleadosActivosPensionados(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS"
                 + " (SELECT vtt FROM VWActualesTiposTrabajadores vtt WHERE vtt.empleado = e.secuencia"
                 + " AND vtt.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO'))");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
                 + " (SELECT 1 FROM VWActualesTiposTrabajadores vtt, TIPOSTRABAJADORES TT WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA AND vtt.empleado = e.secuencia\n"
                 + " AND TT.tipo IN ('ACTIVO','PENSIONADO'))");
         List<Empleados> listaEmpleados2 = new ArrayList<Empleados>();
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               Empleados auxEmple;
               while (listaEmpleados.size() > 0) {
                  auxEmple = listaEmpleados.get(0);
                  if (listaEmpleados.size() > 1) {
                     for (int i = 1; i < listaEmpleados.size(); i++) {
                        if (auxEmple.getPrimerApellidoPersona().compareTo(listaEmpleados.get(i).getPrimerApellidoPersona()) == 1) {
                           auxEmple = listaEmpleados.get(i);
                        }
                     }
                  }
                  listaEmpleados2.add(auxEmple);
                  listaEmpleados.remove(auxEmple);
               }
            }
         }
         return listaEmpleados2;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.lovEmpleadosParametros() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

//   @Override
//   public List<Empleados> empleadosAuxilios(EntityManager em) {
//      try {
//         em.clear();
//         String sqlQuery = "select * from empleados v where EXISTS (SELECT 'x'"
//                 + " from VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores tt"
//                 + " where tt.secuencia = vtt.tipotrabajador"
//                 + " and vtt.empleado = v.secuencia "
//                 + " and tt.tipo='ACTIVO')";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
//         listaEmpleados = query.getResultList();
//         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
//                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
//                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
//                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
//                 + " (SELECT 'x' from VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores tt\n"
//                 + " where tt.secuencia = vtt.tipotrabajador and vtt.empleado = E.secuencia and tt.tipo='ACTIVO')");
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.empleadosAuxilios()");
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }
//   @Override
//   public List<Empleados> empleadosNovedadEmbargo(EntityManager em) {
//      try {
//         em.clear();
//         String sqlQuery = "select * from empleados v where EXISTS (SELECT 'X'\n"
//                 + " from VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores tt\n"
//                 + " where tt.secuencia = vtt.tipotrabajador\n"
//                 + " and vtt.empleado = v.secuencia\n"
//                 + " and tt.tipo='ACTIVO')";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
//         listaEmpleados = query.getResultList();
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.empleadosNovedadEmbargo( e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }
   @Override
   public List<Empleados> buscarEmpleadosBusquedaAvanzada(EntityManager em, String queryBusquedaAvanzada) {
      try {
         em.clear();
         Query query = em.createNativeQuery(queryBusquedaAvanzada, Empleados.class);
         listaEmpleados = query.getResultList();
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               for (Empleados recEmpleado : listaEmpleados) {
                  recEmpleado = consultarTransients(em, recEmpleado);
               }
            }
         }
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadosBusquedaAvanzada() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<BigInteger> buscarEmpleadosBusquedaAvanzadaCodigo(EntityManager em, String queryBusquedaAvanzada) {
      try {
         em.clear();
         Query query = em.createNativeQuery(queryBusquedaAvanzada);
         List<BigInteger> empleado = query.getResultList();
         return empleado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadosBusquedaAvanzadaCodigo()");
         return null;
      }
   }

   @Override
   public Empleados obtenerUltimoEmpleadoAlmacenado(EntityManager em, BigInteger secuenciaEmpresa, BigDecimal codigoEmpleado) {
      try {
         log.warn(this.getClass().getName() + "obtenerUltimoEmpleadoAlmacenado :");
         log.warn("em" + em + ",  secuenciaEmpresa : " + secuenciaEmpresa + ",  codigoEmpleado : " + codigoEmpleado);
         em.clear();
         String sql = "SELECT * FROM EMPLEADOS WHERE EMPRESA = ? AND CODIGOEMPLEADO = ?";
         Query query = em.createNativeQuery(sql, Empleados.class);
         query.setParameter(1, secuenciaEmpresa);
         query.setParameter(2, codigoEmpleado);
         Empleados empleado = (Empleados) query.getSingleResult();
         log.warn("empleado Retornado : " + empleado);
         empleado = consultarTransients(em, empleado);
         return empleado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.obtenerUltimoEmpleadoAlmacenado()  e: " + e);
         e.printStackTrace();
         log.error(this.getClass().getName() + " error en obtenerUltimoEmpleadoAlmacenado");
         return null;
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosParametroAutoliq(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM empleados E WHERE EXISTS (SELECT 1 FROM VigenciasTiposTrabajadores vtte, tipostrabajadores tt\n"
                 + "  WHERE vtte.empleado = E.SECUENCIA\n"
                 + "  and vtte.tipotrabajador = tt.secuencia\n"
                 + "  and tt.tipo != 'DISPONIBLE'\n"
                 + "  AND vtte.fechavigencia = (select max(fechavigencia)\n"
                 + "  from vigenciastipostrabajadores vtti\n"
                 + "  WHERE vtti.empleado = vtte.empleado\n"
                 + "  and vtti.fechavigencia <= last_day(sysdate)))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS (SELECT 1 FROM VigenciasTiposTrabajadores vtte,tipostrabajadores tt\n"
                 + " WHERE vtte.empleado = E.SECUENCIA\n"
                 + " and vtte.tipotrabajador = tt.secuencia\n"
                 + " and tt.tipo != 'DISPONIBLE'\n"
                 + " AND vtte.fechavigencia = (select max(fechavigencia)\n"
                 + " from vigenciastipostrabajadores vtti\n"
                 + " WHERE vtti.empleado = vtte.empleado\n"
                 + " and vtti.fechavigencia <= last_day(sysdate)))");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosParametroAutoliq()");
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosParaProyecciones(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM Empleados c WHERE  EXISTS (SELECT 'X' FROM Proyecciones n WHERE n.empleado  = c.secuencia)";
         Query query = em.createNativeQuery(sql, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS (SELECT 'X' FROM Proyecciones n WHERE n.empleado = E.secuencia)");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosParaProyecciones()");
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public boolean eliminarEmpleadoNominaF(EntityManager em, BigInteger secuenciaEmpleado, BigInteger secuenciaPersona) {
      EntityTransaction tx = em.getTransaction();
      try {
         em.clear();
         tx.begin();
         String sqlQuery = "call ELIMINAREMPLEADO.eli_emple_no_liqui(?,?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuenciaEmpleado);
         query.setParameter(2, secuenciaPersona);
         query.executeUpdate();
         tx.commit();
         log.warn(this.getClass().getName() + " eliminarEmpleadoNominaF Ya Elimino");
         return true;
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error(this.getClass().getName() + " eliminarEmpleadoNominaF No elimino, con error : " + e);
         return false;
      }
   }

   @Override
   public void reingresarEmpleado(EntityManager em, BigDecimal codigoEmpleado, String centroCosto, Date fechaReingreso, short empresa, Date fechaFinal) {
      EntityTransaction tx = em.getTransaction();
      try {
         em.clear();
         tx.begin();
         log.warn("PersistenciaEmpleados.reingresarEmpleado() 1 Parametros: ");
         log.warn("codigoEmpleado: " + codigoEmpleado + ", centroCosto: " + centroCosto + ", fechaReingreso: " + fechaReingreso + ", empresa: " + empresa + ", fechaFinal: " + fechaFinal + "");
         StoredProcedureQuery procedimiento = em.createStoredProcedureQuery("ELIMINAREMPLEADO.reingresar_empleado");
         log.warn("PersistenciaEmpleados.reingresarEmpleado() 2 query: " + procedimiento);
         procedimiento.registerStoredProcedureParameter(1, BigDecimal.class, ParameterMode.IN);
         procedimiento.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
         procedimiento.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
         procedimiento.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
         procedimiento.registerStoredProcedureParameter(5, Date.class, ParameterMode.IN);
         procedimiento.setParameter(1, codigoEmpleado);
         procedimiento.setParameter(2, centroCosto);
         procedimiento.setParameter(3, fechaReingreso);
         procedimiento.setParameter(4, empresa);
         procedimiento.setParameter(5, fechaFinal);
         log.warn("PersistenciaEmpleados.reingresarEmpleado() 3");
         procedimiento.execute();
         log.warn("PersistenciaEmpleados.reingresarEmpleado() 4");
         tx.commit();
         log.warn("PersistenciaEmpleados.reingresarEmpleado() Ya ejecuto el commit");
//         em.clear();
//         tx.begin();
//         log.warn("PersistenciaEmpleados.reingresarEmpleado() 1 Parametros: ");
//         log.warn("codigoEmpleado: " + codigoEmpleado + ", centroCosto: " + centroCosto + ", fechaReingreso: " + fechaReingreso + ", empresa: " + empresa + ", fechaFinal: " + fechaFinal + "");
//         Query query = em.createNativeQuery("call ELIMINAREMPLEADO.reingresar_empleado(?,?,?)");
//         log.warn("PersistenciaEmpleados.reingresarEmpleado() 2 query: " + query);
//         query.setParameter(1, codigoEmpleado);
//         query.setParameter(2, centroCosto);
//         query.setParameter(3, fechaReingreso);
////         query.setParameter(4, empresa);
////         query.setParameter(5, fechaFinal);
//         log.warn("PersistenciaEmpleados.reingresarEmpleado() 3");
//         query.executeUpdate();
//         log.warn("PersistenciaEmpleados.reingresarEmpleado() 4");
//         tx.commit();
//         log.warn("PersistenciaEmpleados.reingresarEmpleado() Ya ejecuto el commit");
      } catch (Exception e) {
         log.error("PersistenciaEmpleados.reingresarEmpleado() ERROR : " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosReingreso(EntityManager em) {
      try {
         em.clear();
         String sql = "select e.* ,p.* from empleados e,personas p where e.persona = p.secuencia and exists"
                 + " (select 'x' from vigenciastipostrabajadores vt,tipostrabajadores tt"
                 + " where vt.empleado = e.secuencia and vt.tipotrabajador = tt.secuencia and tt.tipo = 'RETIRADO'"
                 + " AND VT.FECHAVIGENCIA = (SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASTIPOSTRABAJADORES VTTI"
                 + " WHERE FECHAVIGENCIA<= (SELECT  FECHAHASTACAUSADO  FROM VWACTUALESFECHAS) AND VTTI.EMPLEADO = VT.EMPLEADO))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
                 + " (select 'x' from vigenciastipostrabajadores vt,tipostrabajadores tt\n"
                 + " where vt.empleado = e.secuencia and vt.tipotrabajador = tt.secuencia and tt.tipo = 'RETIRADO'\n"
                 + " AND VT.FECHAVIGENCIA = (SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASTIPOSTRABAJADORES VTTI\n"
                 + " WHERE FECHAVIGENCIA<= (SELECT FECHAHASTACAUSADO  FROM VWACTUALESFECHAS) AND VTTI.EMPLEADO = VT.EMPLEADO))");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosReingreso() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public Date verificarFecha(EntityManager em, BigInteger secuenciaEmpleado) {
      Date fechaRetiro;
      try {
         em.clear();
         String sql = "select max(r.fecharetiro)\n"
                 + " from vigenciastipostrabajadores vtt, retirados r, tipostrabajadores tt\n"
                 + " where vtt.empleado = ?\n"
                 + " and vtt.secuencia = r.vigenciatipotrabajador\n"
                 + " and tt.secuencia = vtt.tipotrabajador";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaEmpleado);
         fechaRetiro = (Date) query.getSingleResult();
         return fechaRetiro;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.verificarFecha() e: " + e);
         return null;
      }
   }

   @Override
   public void cambiarFechaIngreso(EntityManager em, BigInteger secuenciaEmpleado, Date fechaAntigua, Date fechaNueva) {
      EntityTransaction tx = em.getTransaction();
      try {
         em.clear();
         tx.begin();
         String sqlQuery = "call ELIMINAREMPLEADO.cambiarfechaingreso(?,?,?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuenciaEmpleado);
         query.setParameter(2, fechaAntigua);
         query.setParameter(3, fechaNueva);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.cambiarFechaIngreso() e: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosCuadrillas(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT E.* FROM PERSONAS P, EMPLEADOS E \n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND EXISTS"
                 + " (SELECT 1 FROM DETALLESTURNOSROTATIVOS DTR \n"
                 + " WHERE DTR.EMPLEADO=E.SECUENCIA) AND EXISTS"
                 + " (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + " AND VTT.EMPLEADO = E.SECUENCIA\n"
                 + " AND TT.TIPO IN ('ACTIVO','PENSIONADO','RETIRADO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
                 + " (SELECT 1 FROM DETALLESTURNOSROTATIVOS DTR\n"
                 + " WHERE DTR.EMPLEADO=E.SECUENCIA) AND EXISTS\n"
                 + " (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT\n"
                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA\n"
                 + " AND VTT.EMPLEADO = E.SECUENCIA\n"
                 + " AND TT.TIPO IN ('ACTIVO','PENSIONADO','RETIRADO'))");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosCuadrillas() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosATHoraExtra(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT *\n"
                 + " FROM EMPLEADOS V \n"
                 + " WHERE (EXISTS (\n"
                 + " SELECT 'X' FROM VWACTUALESCARGOS VWC \n"
                 + " WHERE VWC.empleado=V.SECUENCIA\n"
                 + " AND ESTRUCTURA IN \n"
                 + " (select e1.secuencia from estructuras e1\n"
                 + " start with e1.estructurapadre = \n"
                 + " (SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                 + " WHERE U.secuencia=EA.usuario\n"
                 + " AND U.alias=USER\n"
                 + " AND EA.eerestado=(SELECT SECUENCIA FROM EERSESTADOS WHERE TIPOEER='TURNO' \n"
                 + " AND CODIGO=(SELECT MIN(CODIGO) FROM EERSESTADOS WHERE TIPOEER='TURNO')))\n"
                 + " connect by prior e1.secuencia = e1.estructurapadre))\n"
                 + " AND EXISTS (SELECT 'X'\n"
                 + " FROM VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " WHERE tt.secuencia = vtt.tipotrabajador\n"
                 + " AND vtt.empleado = V.secuencia\n"
                 + " AND tt.tipo='ACTIVO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS (\n"
                 + " SELECT 'X' FROM VWACTUALESCARGOS VWC\n"
                 + " WHERE VWC.empleado=E.SECUENCIA\n"
                 + " AND ESTRUCTURA IN\n"
                 + " (select e1.secuencia from estructuras e1\n"
                 + " start with e1.estructurapadre =\n"
                 + " (SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U\n"
                 + " WHERE U.secuencia=EA.usuario\n"
                 + " AND U.alias=USER\n"
                 + " AND EA.eerestado=(SELECT SECUENCIA FROM EERSESTADOS WHERE TIPOEER='TURNO'\n"
                 + " AND CODIGO=(SELECT MIN(CODIGO) FROM EERSESTADOS WHERE TIPOEER='TURNO')))\n"
                 + " connect by prior e1.secuencia = e1.estructurapadre))\n"
                 + " AND EXISTS (SELECT 'X'\n"
                 + " FROM VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + " WHERE tt.secuencia = vtt.tipotrabajador\n"
                 + " AND vtt.empleado = E.secuencia\n"
                 + " AND tt.tipo='ACTIVO')");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaEmpleados.buscarEmpleadosATHoraExtra() e: " + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }
//
//   @Override
//   public List<Empleados> consultarEmpleadosParaAprobarHorasExtras(EntityManager em) {
//      try {
//         em.clear();
//         String sql = "SELECT E.* FROM PERSONAS P, EMPLEADOS E \n"
//                 + " WHERE E.PERSONA = P.SECUENCIA AND EXISTS"
//                 + " (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
//                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
//                 + " AND VTT.EMPLEADO = E.SECUENCIA \n"
//                 + " AND TT.TIPO IN ('ACTIVO','PENSIONADO'))";
//         Query query = em.createNativeQuery(sql, Empleados.class);
//         listaEmpleados = query.getResultList();
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.consultarEmpleadosParaAprobarHorasExtras() e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }

//   @Override
//   public List<Empleados> empleadosCesantias(EntityManager em) {
//      try {
//         em.clear();
//         String sqlQuery = "select v.SECUENCIA,v.PERSONA,v.CODIGOEMPLEADO \n"
//                 + "from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt \n"
//                 + "where tt.secuencia = vtt.tipotrabajador\n"
//                 + "and vtt.empleado = v.secuencia\n"
//                 + "and tt.tipo IN ('ACTIVO'))";
//         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
//         listaEmpleados = query.getResultList();
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("Persistencia.PersistenciaEmpleados.empleadosCesantias e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }
   @Override
   public List<Empleados> consultarCesantiasnoLiquidadas(EntityManager em) {
      try {
         em.clear();
         String qr = "select v.* from empleados v where \n"
                 + " exists (select * from novedadessistema ns\n"
                 + "          where NS.empleado = v.secuencia \n"
                 + "          and ns.tipo = 'CESANTIA'\n"
                 + "          AND not EXISTS (SELECT 'X' \n"
                 + "                          FROM detallesnovedadessistema dns, novedades n, solucionesformulas sf \n"
                 + "                          WHERE ns.secuencia = dns.novedadsistema  AND dns.novedad = n.secuencia AND N.secuencia = SF.novedad))";
         Query query = em.createNativeQuery(qr, Empleados.class);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
                 + " (select * from novedadessistema ns\n"
                 + " where NS.empleado = E.secuencia\n"
                 + " and ns.tipo = 'CESANTIA'\n"
                 + " AND not EXISTS (SELECT 'X'\n"
                 + " FROM detallesnovedadessistema dns, novedades n, solucionesformulas sf \n"
                 + " WHERE ns.secuencia = dns.novedadsistema  AND dns.novedad = n.secuencia AND N.secuencia = SF.novedad))");
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error("Error: (persistenciaEmpleados.consultarCesantiasnoLiquidadas)" + e);
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public List<NovedadesSistema> novedadescesantiasnoliquidadas(EntityManager em, BigInteger secuenciaEmpleado) {

      try {
         em.clear();
         String qr = "select * from novedadessistema ns \n"
                 + "                where NS.empleado = ? \n"
                 + "                and ns.tipo = 'CESANTIA' \n"
                 + "                AND not EXISTS (SELECT 'X' \n"
                 + "                                FROM detallesnovedadessistema dns, novedades n, solucionesformulas sf\n"
                 + "                                WHERE ns.secuencia = dns.novedadsistema \n"
                 + "                                AND dns.novedad = n.secuencia\n"
                 + "                                AND N.secuencia = SF.novedad)";
         Query query = em.createNativeQuery(qr, NovedadesSistema.class);
         query.setParameter(1, secuenciaEmpleado);
         List<NovedadesSistema> novedadesnoliquidadas = query.getResultList();
         return novedadesnoliquidadas;
      } catch (Exception e) {
         log.error("Error: (persistenciaEmpleados.novedadescesantiasnoliquidadas)" + e);
         return null;
      }
   }
//
//   @Override
//   public List<Empleados> empleadosAusentismos(EntityManager em) {
//      log.warn("entrÃ³ a  empleadosAusentismos");
//      try {
//         log.warn("entro al try");
//         em.clear();
//         String sql = "SELECT * FROM  EMPLEADOS E  WHERE  EXISTS"
//                 + " (SELECT 'X' FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
//                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
//                 + " AND VTT.EMPLEADO = E.SECUENCIA \n"
//                 + " AND TT.TIPO = 'ACTIVO')";
//         Query query = em.createNativeQuery(sql, Empleados.class);
//         listaEmpleados = query.getResultList();
//         log.warn("empleadosAusentismos : " + listaEmpleados.size());
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("error persistenciaEmpledos.empleadoAusentismos() e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }

//   @Override
//   public List<Empleados> empleadosDefinitiva(EntityManager em) {
//      try {
//         em.clear();
//         String sql = "SELECT * FROM EMPLEADOS E \n"
//                 + " WHERE EXISTS (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
//                 + " WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
//                 + " AND VTT.EMPLEADO = E.SECUENCIA \n"
//                 + " AND TT.TIPO IN ('ACTIVO', 'PENSIONADO'))";
//         Query query = em.createNativeQuery(sql, Empleados.class);
//         listaEmpleados = query.getResultList();
//         log.warn("empleadosDefinitiva : " + listaEmpleados.size());
//         return (new ArrayList<Empleados>(listaEmpleados));
//      } catch (Exception e) {
//         log.error("error persistenciaEmpledos.empleadosDefinitiva() e: " + e);
//         return null;
//      } finally {
//         listaEmpleados.clear();
//      }
//   }
   @Override
   public List<Empleados> empleadosReemplazosHV(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sqlQuery = "SELECT E.* FROM PERSONAS P, EMPLEADOS E \n"
                 + "  WHERE E.PERSONA = P.SECUENCIA AND EXISTS"
                 + " (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
                 + "  WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "  AND VTT.EMPLEADO = E.SECUENCIA \n"
                 + "  AND TT.TIPO = 'ACTIVO') AND E.SECUENCIA = ? ";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         query.setParameter(1, secuenciaEmpleado);
         listaEmpleados = query.getResultList();
         consultarTransientsLista(em, "SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                 + "P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                 + "FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                 + "WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS\n"
                 + "(SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
                 + "WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "AND VTT.EMPLEADO = E.SECUENCIA\n"
                 + "AND TT.TIPO = 'ACTIVO') AND E.SECUENCIA = " + secuenciaEmpleado);
         return (new ArrayList<Empleados>(listaEmpleados));
      } catch (Exception e) {
         log.error(this.getClass().getName() + " error en buscarEmpleados()");
         e.printStackTrace();
         return null;
      } finally {
         listaEmpleados.clear();
      }
   }

   @Override
   public void cambiarCodEmpleado(EntityManager em, BigDecimal codactual, BigDecimal codnuevo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call empleados_pkg.CambiarCodigoEmpleado(?,?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, codactual);
         query.setParameter(2, codnuevo);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEmpleados.cambiarCodEmpleado. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   private Empleados consultarTransients(EntityManager em, Empleados empleado) {
      try {
         if (empleado != null) {
            if (empleado.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,"
                       + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                       + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                       + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND E.SECUENCIA = " + empleado.getSecuencia(), EmpleadosAux.class);
               EmpleadosAux empleadoAux = (EmpleadosAux) query2.getSingleResult();
               log.warn("PersistenciaEmpleados.consultarTransients() empleadoAux: " + empleadoAux);
               if (empleadoAux != null) {
                  if (empleadoAux.getSecuencia() != null) {
                     if (empleadoAux.getSecuencia().equals(empleado.getSecuencia())) {
                        empleado.llenarTransients(empleadoAux);
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTransients() ERROR: " + e);
         e.getStackTrace();
      }
      return empleado;
   }

   private void consultarTransientsLista(EntityManager em, String query) {
      try {
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery(query, EmpleadosAux.class);
               List<EmpleadosAux> listaEmpleadosAux = query2.getResultList();
               log.warn("PersistenciaEmpleados.consultarTransientsLista() Ya consulo Transients");
               if (listaEmpleadosAux != null) {
                  if (!listaEmpleadosAux.isEmpty()) {
                     log.warn("PersistenciaEmpleados.consultarTransientsLista() listaEmpleadosAux.size(): " + listaEmpleadosAux.size());
                     for (EmpleadosAux recAux : listaEmpleadosAux) {
                        for (Empleados recEmpleado : listaEmpleados) {
                           if (recAux.getSecuencia().equals(recEmpleado.getSecuencia())) {
                              recEmpleado.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTransientsLista() ERROR: " + e);
         e.getStackTrace();
      }
   }
}
