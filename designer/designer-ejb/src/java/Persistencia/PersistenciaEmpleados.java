/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import Entidades.NovedadesSistema;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class PersistenciaEmpleados implements PersistenciaEmpleadoInterface {

   @Override
   public void crear(EntityManager em, Empleados empleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(empleados);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.crear() e: " + e);
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
         System.out.println("PersistenciaEmpleados crearConVCargo() se supone creo el empleado con V cargo");
         System.out.println("crearConVCargo() retorno parametro ''SECUENCIA_EMPLEADO'' : " + empleado_Sec);
         return empleado_Sec.toBigInteger();
      } catch (Exception e) {
         System.err.println(this.getClass().getName() + ".crearConVCargo()");
         System.err.println("error al crear el empleado");
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
      return null;
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
         System.out.println(this.getClass().getName() + ".editar() error " + e.toString());
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
         System.out.println(this.getClass().getName() + ".borrar() error " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Empleados buscarEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(Empleados.class, secuencia);
      } catch (Exception e) {
         System.out.println(this.getClass().getName() + ".buscarEmpleado() error " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleados(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "SELECT * FROM EMPLEADOS E\n"
                 + "WHERE EXISTS (SELECT 'x' FROM VWACTUALESTIPOSTRABAJADORES VTT,   TIPOSTRABAJADORES TT \n"
                 + "   WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "   AND   VTT.EMPLEADO = E.SECUENCIA \n"
                 + "   AND   TT.TIPO = 'ACTIVO')";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados;
         listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println(this.getClass().getName() + " error en buscarEmpleados()");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<Empleados> todosEmpleados(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e ORDER BY e.codigoempleado ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.todosEmpleados() e: " + e);
         return null;
      }

   }

   @Override
   public List<Empleados> consultarEmpleadosLiquidacionesLog(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS (SELECT li.secuencia FROM LiquidacionesLogs li WHERE li.empleado.secuencia = e.secuencia) ORDER BY e.codigoempleado ASC");

         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosLiquidacionesLog() e: " + e);
         return null;
      }

   }

   @Override
   public Empleados buscarEmpleadoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         Empleados empleado = (Empleados) query.getSingleResult();
         return empleado;
      } catch (Exception e) {
         System.err.println("Error PersistenciaEmpleados.buscarEmpleadoSecuencia " + e);
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
         return empleado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadoSecuenciaPersona() e: " + e);
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
         System.out.println("Persistencia.PersistenciaEmpleados.verificarCodigoEmpleado_Empresa() e: " + e);
         return false;
      }
   }

   @Override
   public Empleados buscarEmpleadoCodigo_Empresa(EntityManager em, BigInteger codigoEmpleado, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE AND e.empresa = :secEmpresa");
         query.setParameter("codigoE", codigoEmpleado);
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         return empleado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadoCodigo_Empresa() e: " + e);
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleadoTipo(EntityManager em, BigInteger codigoEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
         query.setParameter("codigoE", codigoEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         return empleado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadoTipo() e: " + e);
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleadoCodigo(EntityManager em, BigInteger codigoEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
         query.setParameter("codigoE", codigoEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Empleados empleado = (Empleados) query.getSingleResult();
         return empleado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadoCodigo() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosPorCodigo(EntityManager em, List<BigInteger> codigosEmpleado) {
      List<Empleados> lista = new ArrayList<Empleados>();
      try {
         for (int i = 0; i < codigosEmpleado.size(); i++) {
            em.clear();
            try {
               Query query = em.createQuery("SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoE");
               query.setParameter("codigoE", codigosEmpleado.get(i));
               query.setHint("javax.persistence.cache.storeMode", "REFRESH");
               Empleados empleado = (Empleados) query.getSingleResult();
               lista.add(empleado);
            } catch (Exception ei) {
               System.out.println("ERROR en el for() en buscarEmpleadosPorCodigo() ei : " + ei);
            }
         }
         return lista;
      } catch (Exception e) {
         System.out.println("ERROR en buscarEmpleadosPorCodigo() e : " + e);
         return lista;
      }
   }

   @Override
   public List<Empleados> empleadosComprobantes(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS (SELECT 1 FROM Parametros p ,ParametrosInstancias pi, UsuariosInstancias ui, Usuarios u WHERE p.empleado = e.secuencia and p.secuencia = pi.parametro and pi.instancia = ui.instancia and ui.usuario = u.secuencia and u.alias = :usuarioBD)");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosComprobantes()");
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosNovedad(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select "
                 + "SECUENCIA, "
                 + "CODIGOEMPLEADO, "
                 + "RUTATRANSPORTE, "
                 + "PARQUEADERO, "
                 + "FECHACREACION, "
                 + "CODIGOALTERNATIVO, "
                 + "PAGASUBSIDIOTRANSPORTELEGAL, "
                 + "PERSONA, "
                 + "EMPRESA, "
                 + "USUARIOBD "
                 + "from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + "where tt.secuencia = vtt.tipotrabajador\n"
                 + "and   vtt.empleado = v.secuencia\n"
                 + "and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosNovedad()");
         return null;
      }
   }

   @Override
   public int contarEmpleadosNovedad(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select count(*) \n"
                 + "from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + "where tt.secuencia = vtt.tipotrabajador\n"
                 + "and   vtt.empleado = v.secuencia\n"
                 + "and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO'))";
         Query query = em.createNativeQuery(sqlQuery);
         Long resultado = new Long(query.getSingleResult().toString());
         System.out.println("contarEmpleadosNovedad resultado : " + resultado);

         int N = new Integer(query.getSingleResult().toString());
         System.out.println("contarEmpleadosNovedad retorno : " + N);
         return N;
      } catch (Exception e) {
         System.out.println("Error contarEmpleadosNovedad() : " + e);
         return -1;
      }
   }

   @Override
   public List<Empleados> empleadosNovedadSoloAlgunos(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select "
                 + "SECUENCIA, CODIGOEMPLEADO, RUTATRANSPORTE, PARQUEADERO, FECHACREACION, "
                 + "CODIGOALTERNATIVO, PAGASUBSIDIOTRANSPORTELEGAL, "
                 + "PERSONA, EMPRESA, USUARIOBD "
                 + "from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + "where tt.secuencia = vtt.tipotrabajador\n"
                 + "and   vtt.empleado = v.secuencia\n"
                 + "and tt.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')) and ROWNUM < 50";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         System.out.println("");
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosNovedadSoloAlgunos() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosVacaciones(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select * "
                 + "from empleados v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + "where tt.secuencia = vtt.tipotrabajador\n"
                 + "and vtt.empleado = v.secuencia\n"
                 + "and tt.tipo IN ('ACTIVO'))";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosVacaciones() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleadosParametros(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Empleados e WHERE EXISTS (SELECT vtt FROM VWActualesTiposTrabajadores vtt WHERE vtt.empleado.secuencia = e.secuencia AND vtt.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO')) ORDER BY e.persona.primerapellido");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.lovEmpleadosParametros() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosAuxilios(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select * from empleados v where EXISTS (SELECT 'x' "
                 + "       from   VWACTUALESTIPOSTRABAJADORES        vtt, tipostrabajadores  tt "
                 + "       where tt.secuencia = vtt.tipotrabajador "
                 + "       and   vtt.empleado = v.secuencia "
                 + "              and tt.tipo='ACTIVO')";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosAuxilios()");
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosNovedadEmbargo(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select * from empleados v where EXISTS (SELECT 'X'\n"
                 + "       from   VWACTUALESTIPOSTRABAJADORES        vtt, tipostrabajadores  tt\n"
                 + "       where tt.secuencia = vtt.tipotrabajador\n"
                 + "       and   vtt.empleado = v.secuencia\n"
                 + "              and tt.tipo='ACTIVO')";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosNovedadEmbargo( e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosBusquedaAvanzada(EntityManager em, String queryBusquedaAvanzada) {
      try {
         em.clear();
         Query query = em.createNativeQuery(queryBusquedaAvanzada, Empleados.class);
         List<Empleados> empleado = query.getResultList();
         return empleado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadosBusquedaAvanzada() e: " + e);
         return null;
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
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadosBusquedaAvanzadaCodigo()");
         return null;
      }
   }

   @Override
   public Empleados buscarEmpleadoPorCodigoyEmpresa(EntityManager em, BigDecimal codigo, BigInteger empresa) {
      try {
         em.clear();

         String sql = "SELECT * FROM empleados WHERE CODIGOEMPLEADO =? AND NVL(EMPRESA,?)=?";
         Query query = em.createNativeQuery(sql, Empleados.class);
         query.setParameter(1, codigo);
         query.setParameter(2, empresa);
         query.setParameter(3, empresa);
         Empleados empl = (Empleados) query.getSingleResult();
         return empl;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadoPorCodigoyEmpresa()");
         return null;
      }
   }

   @Override
   public Empleados obtenerUltimoEmpleadoAlmacenado(EntityManager em, BigInteger secuenciaEmpresa, BigDecimal codigoEmpleado) {
      try {
         System.out.println(this.getClass().getName() + "obtenerUltimoEmpleadoAlmacenado :");
         System.out.println("em" + em + ",  secuenciaEmpresa : " + secuenciaEmpresa + ",  codigoEmpleado : " + codigoEmpleado);
         em.clear();
         String sql = "SELECT * FROM EMPLEADOS WHERE EMPRESA = ? AND CODIGOEMPLEADO = ?";
         Query query = em.createNativeQuery(sql, Empleados.class);
         query.setParameter(1, secuenciaEmpresa);
         query.setParameter(2, codigoEmpleado);
         Empleados empl = (Empleados) query.getSingleResult();
         System.out.println("empleado Retornado : " + empl);
         return empl;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.obtenerUltimoEmpleadoAlmacenado()  e: " + e);
         e.printStackTrace();
         System.out.println(this.getClass().getName() + " error en obtenerUltimoEmpleadoAlmacenado");
         //PropertyConfigurator.configure("log4j.properties");
         //logger.error("Metodo: obtenerUltimoEmpleadoAlmacenado - PersistenciaEmpleados - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosParametroAutoliq(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM empleados E WHERE EXISTS (SELECT 1 FROM VigenciasTiposTrabajadores vtte,tipostrabajadores tt\n"
                 + "  WHERE vtte.empleado = E.SECUENCIA\n"
                 + "  and vtte.tipotrabajador = tt.secuencia\n"
                 + "  and tt.tipo != 'DISPONIBLE'\n"
                 + "  AND  vtte.fechavigencia = (select max(fechavigencia)\n"
                 + "  from vigenciastipostrabajadores vtti\n"
                 + "  WHERE  vtti.empleado = vtte.empleado\n"
                 + "  and   vtti.fechavigencia <= last_day(sysdate)))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
          System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosParametroAutoliq()" );
         return null;
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosParaProyecciones(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM Empleados c WHERE  EXISTS (SELECT 'X' FROM Proyecciones n WHERE n.empleado  = c.secuencia)";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosParaProyecciones()");
         return null;
      }
   }

   @Override
   public void eliminarEmpleadoNominaF(EntityManager em, BigInteger secuenciaEmpleado, BigInteger secuenciaPersona) {
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
         System.out.println(this.getClass().getName() + " eliminarEmpleadoNominaF Ya Elimino");
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println(this.getClass().getName() + " eliminarEmpleadoNominaF Entro en el Catch con error : " + e);
      }
   }

   @Override
   public void reingresarEmpleado(EntityManager em, BigInteger codigoEmpleado, BigInteger centroCosto, Date fechaReingreso, BigInteger empresa, Date fechaFinal) {
      EntityTransaction tx = em.getTransaction();
      try {
         em.clear();
         tx.begin();
         String sqlQuery = "call ELIMINAREMPLEADO.reingresar_empleado(?,?,?,?,?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, codigoEmpleado);
         query.setParameter(2, centroCosto);
         query.setParameter(3, fechaReingreso);
         query.setParameter(4, empresa);
         query.setParameter(5, fechaFinal);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.reingresarEmpleado()");
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosReingreso(EntityManager em) {
      try {
         em.clear();
         String sql = "select e.* ,p.* from empleados e,personas p where e.persona = p.secuencia and exists (select 'x' from vigenciastipostrabajadores vt,tipostrabajadores tt where vt.empleado = e.secuencia and vt.tipotrabajador = tt.secuencia and tt.tipo = 'RETIRADO' AND   VT.FECHAVIGENCIA = (SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASTIPOSTRABAJADORES VTTI WHERE FECHAVIGENCIA<= (SELECT  FECHAHASTACAUSADO  FROM VWACTUALESFECHAS) AND VTTI.EMPLEADO = VT.EMPLEADO ))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosReingreso() e: " + e);
         return null;
      }
   }

   @Override
   public Date verificarFecha(EntityManager em, BigInteger secuenciaEmpleado) {
      Date fechaRetiro;
      try {
         em.clear();
         String sql = "select max(r.fecharetiro)\n"
                 + "      from vigenciastipostrabajadores vtt, retirados r, tipostrabajadores tt\n"
                 + "      where vtt.empleado = ?\n"
                 + "      and vtt.secuencia=r.vigenciatipotrabajador\n"
                 + "      and tt.secuencia = vtt.tipotrabajador";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaEmpleado);
         fechaRetiro = (Date) query.getSingleResult();
         return fechaRetiro;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.verificarFecha() e: " + e);
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
         System.out.println("Persistencia.PersistenciaEmpleados.cambiarFechaIngreso() e: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosCuadrillas(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT E.* \n"
                 + " FROM PERSONAS P, EMPLEADOS E \n"
                 + " WHERE E.PERSONA = P.SECUENCIA \n"
                 + " AND EXISTS (SELECT 1 \n"
                 + "            FROM DETALLESTURNOSROTATIVOS DTR \n"
                 + "            WHERE DTR.EMPLEADO=E.SECUENCIA) \n"
                 + "AND EXISTS (SELECT 1 \n"
                 + "            FROM VWACTUALESTIPOSTRABAJADORES VTT,   TIPOSTRABAJADORES TT \n"
                 + "            WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "            AND   VTT.EMPLEADO = E.SECUENCIA    \n"
                 + "            AND   TT.TIPO IN ('ACTIVO','PENSIONADO','RETIRADO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosCuadrillas() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosATHoraExtra(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT *\n"
                 + " FROM EMPLEADOS V \n"
                 + " WHERE (EXISTS (\n"
                 + "     SELECT 'X' FROM VWACTUALESCARGOS VWC \n"
                 + "       WHERE VWC.empleado=V.SECUENCIA\n"
                 + "       AND   ESTRUCTURA IN \n"
                 + "         (select e1.secuencia from estructuras e1\n"
                 + "          start with e1.estructurapadre = \n"
                 + "            (SELECT ESTRUCTURA FROM EERSAUTORIZACIONES EA, USUARIOS U \n"
                 + "		     WHERE U.secuencia=EA.usuario\n"
                 + "		  	 AND   U.alias=USER\n"
                 + "			 AND EA.eerestado=(SELECT SECUENCIA FROM EERSESTADOS WHERE TIPOEER='TURNO' \n"
                 + "			 AND CODIGO=(SELECT MIN(CODIGO) FROM EERSESTADOS WHERE TIPOEER='TURNO')))\n"
                 + "          connect by prior e1.secuencia = e1.estructurapadre))\n"
                 + " AND EXISTS (SELECT 'X'\n"
                 + "      FROM VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt\n"
                 + "      WHERE tt.secuencia = vtt.tipotrabajador\n"
                 + "      AND   vtt.empleado = V.secuencia\n"
                 + "      AND   tt.tipo='ACTIVO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.buscarEmpleadosATHoraExtra() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosParaAprobarHorasExtras(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT E.*\n"
                 + " FROM PERSONAS P, EMPLEADOS E \n"
                 + " WHERE E.PERSONA = P.SECUENCIA\n"
                 + "  AND EXISTS (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT,   TIPOSTRABAJADORES TT \n"
                 + "   WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "   AND   VTT.EMPLEADO = E.SECUENCIA \n"
                 + "   AND   TT.TIPO IN ('ACTIVO','PENSIONADO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.consultarEmpleadosParaAprobarHorasExtras() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosCesantias(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "select v.SECUENCIA,v.PERSONA,v.CODIGOEMPLEADO \n"
                 + "from empleados  v where EXISTS (SELECT 'X' from  VWACTUALESTIPOSTRABAJADORES vtt, tipostrabajadores  tt \n"
                 + "where tt.secuencia = vtt.tipotrabajador\n"
                 + "and vtt.empleado = v.secuencia\n"
                 + "and tt.tipo IN ('ACTIVO'))";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         List<Empleados> listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEmpleados.empleadosCesantias e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> consultarCesantiasnoLiquidadas(EntityManager em) {
      try {
         em.clear();
         String qr = " select v.SECUENCIA,v.PERSONA,v.CODIGOEMPLEADO from empleados v where \n"
                 + " exists (select * from novedadessistema ns\n"
                 + "          where NS.empleado = v.secuencia \n"
                 + "          and ns.tipo = 'CESANTIA'\n"
                 + "          AND not EXISTS (SELECT 'X' \n"
                 + "                          FROM detallesnovedadessistema dns, novedades n, solucionesformulas sf \n"
                 + "                          WHERE ns.secuencia = dns.novedadsistema  AND dns.novedad = n.secuencia AND N.secuencia = SF.novedad))";
         Query query = em.createNativeQuery(qr, Empleados.class);
         List<Empleados> novedadesnoliquidadas = query.getResultList();
         return novedadesnoliquidadas;

      } catch (Exception e) {
         System.err.println("Error: (persistenciaEmpleados.consultarCesantiasnoLiquidadas)" + e);
         return null;
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
         System.err.println("Error: (persistenciaEmpleados.novedadescesantiasnoliquidadas)" + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosAusentismos(EntityManager em) {
      System.out.println("entró a  empleadosAusentismos");
      try {
         System.out.println("entro al try");
         em.clear();
         String sql = "SELECT * \n"
                 + "FROM  EMPLEADOS E \n"
                 + "WHERE  EXISTS (SELECT 'X' FROM VWACTUALESTIPOSTRABAJADORES VTT,   TIPOSTRABAJADORES TT \n"
                 + "   WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "   AND   VTT.EMPLEADO = E.SECUENCIA \n"
                 + "   AND   TT.TIPO = 'ACTIVO')";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> empleadosAusentismos = query.getResultList();
         System.out.println("empleadosAusentismos : " + empleadosAusentismos.size());
         return empleadosAusentismos;
      } catch (Exception e) {
         System.out.println("error persistenciaEmpledos.empleadoAusentismos() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosDefinitiva(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM EMPLEADOS E \n"
                 + "WHERE EXISTS (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT,   TIPOSTRABAJADORES TT \n"
                 + "   WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "   AND   VTT.EMPLEADO = E.SECUENCIA \n"
                 + "   AND   TT.TIPO IN ( 'ACTIVO'	,'PENSIONADO'))";
         Query query = em.createNativeQuery(sql, Empleados.class);
         List<Empleados> empleadosDefinitiva = query.getResultList();
         System.out.println("empleadosDefinitiva : " + empleadosDefinitiva.size());
         return empleadosDefinitiva;
      } catch (Exception e) {
         System.out.println("error persistenciaEmpledos.empleadosDefinitiva() e: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosReemplazosHV(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sqlQuery = "SELECT E.* \n"
                 + "  FROM PERSONAS P, EMPLEADOS E \n"
                 + "  WHERE E.PERSONA = P.SECUENCIA \n"
                 + "  AND EXISTS (SELECT 1 FROM VWACTUALESTIPOSTRABAJADORES VTT, TIPOSTRABAJADORES TT \n"
                 + "  WHERE VTT.TIPOTRABAJADOR = TT.SECUENCIA \n"
                 + "  AND   VTT.EMPLEADO = E.SECUENCIA \n"
                 + "  AND   TT.TIPO = 'ACTIVO') AND E.SECUENCIA = ? ";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class);
         query.setParameter(1, secuenciaEmpleado);
         List<Empleados> listaEmpleados;
         listaEmpleados = query.getResultList();
         return listaEmpleados;
      } catch (Exception e) {
         System.out.println(this.getClass().getName() + " error en buscarEmpleados()");
         e.printStackTrace();
         return null;
      }
   }

}
