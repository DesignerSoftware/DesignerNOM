/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlNavegacion;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.GruposConceptos;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class ListasRecurrentes {

   private static Logger log = Logger.getLogger(ListasRecurrentes.class);

   private List<Cuentas> lovCuentas;
   private List<CentrosCostos> lovCentrosCostos;
   private List<Procesos> lovProcesos;
   private List<GruposConceptos> lovGruposConceptos;
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private List<Formulas> lovFormulas;
   private List<FormulasConceptos> lovFormulasConceptos;
   private List<Conceptos> lovConceptos;
   private List<Empleados> lovEmpleadosActivos;
   private List<Empleados> lovEmpleadosNovedad;
   private List<Empleados> lovEmpleadosActPenc;

   public ListasRecurrentes() {
      lovCuentas = null;
      lovCentrosCostos = null;
      lovProcesos = null;
      lovGruposConceptos = null;
      lovTiposTrabajadores = null;
      lovFormulas = null;
      lovFormulasConceptos = null;
      lovConceptos = null;
      lovEmpleadosActivos = null;
      lovEmpleadosNovedad = null;
      lovEmpleadosActPenc = null;
//      lovCuentas = new ArrayList<Cuentas>();
//      lovCentrosCostos = new ArrayList<CentrosCostos>();
//      lovProcesos = new ArrayList<Procesos>();
//      lovGruposConceptos = new ArrayList<GruposConceptos>();
//      lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
//      lovFormulas = new ArrayList<Formulas>();
//      lovFormulasConceptos = new ArrayList<FormulasConceptos>();
//      lovConceptos = new ArrayList<Conceptos>();
//      lovEmpleadosActivos = new ArrayList<Empleados>();
//      lovEmpleadosNovedad = new ArrayList<Empleados>();
//      lovEmpleadosActPenc = new ArrayList<Empleados>();
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      if (lovCentrosCostos == null) {
      }
      lovCentrosCostos = new ArrayList<CentrosCostos>();
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostosp) {
      getLovCentrosCostos().clear();
//      this.lovCentrosCostos.clear();
      for (CentrosCostos recC : lovCentrosCostosp) {
         this.lovCentrosCostos.add(recC);
      }
      log.info("SET : setLovCentrosCostos");
   }

   public List<Procesos> getLovProcesos() {
      if (lovProcesos == null) {
      }
      lovProcesos = new ArrayList<Procesos>();
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesosp) {
      getLovProcesos().clear();
//      this.lovProcesos.clear();
      for (Procesos recC : lovProcesosp) {
         this.lovProcesos.add(recC);
      }
      log.info("SET : setLovProcesos");
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      if (lovGruposConceptos == null) {
      }
      lovGruposConceptos = new ArrayList<GruposConceptos>();
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptosp) {
      getLovGruposConceptos().clear();
//      this.lovGruposConceptos.clear();
      for (GruposConceptos recC : lovGruposConceptosp) {
         this.lovGruposConceptos.add(recC);
      }
      log.info("SET : setLovGruposConceptos");
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      if (lovTiposTrabajadores == null) {
      }
      lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadoresp) {
      getLovTiposTrabajadores().clear();
//      this.lovTiposTrabajadores.clear();
      for (TiposTrabajadores recC : lovTiposTrabajadoresp) {
         this.lovTiposTrabajadores.add(recC);
      }
      log.info("SET : setLovTiposTrabajadores");
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
      }
      lovFormulas = new ArrayList<Formulas>();
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulasp) {
      getLovFormulas().clear();
//      this.lovFormulas.clear();
      for (Formulas recC : lovFormulasp) {
         this.lovFormulas.add(recC);
      }
      log.info("SET : setLovFormulas");
   }

   public List<FormulasConceptos> getLovFormulasConceptos() {
      if (lovFormulasConceptos == null) {
      }
      lovFormulasConceptos = new ArrayList<FormulasConceptos>();
      return lovFormulasConceptos;
   }

   public void setLovFormulasConceptos(List<FormulasConceptos> lovFormulasConceptosp) {
      getLovFormulasConceptos().clear();
//      this.lovFormulasConceptos.clear();
      for (FormulasConceptos recC : lovFormulasConceptosp) {
         this.lovFormulasConceptos.add(recC);
      }
      log.info("SET : setLovFormulasConceptos");
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = new ArrayList<Conceptos>();
      }
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptosp) {
      getLovConceptos().clear();
//      this.lovConceptos.clear();
      for (Conceptos recC : lovConceptosp) {
         this.lovConceptos.add(recC);
      }
      log.info("SET : setLovConceptos");
   }

   public List<Cuentas> getLovCuentas() {
      if (lovCuentas == null) {
         lovCuentas = new ArrayList<Cuentas>();
      }
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentasp) {
      getLovCuentas().clear();
//      this.lovCuentas.clear();
      for (Cuentas recC : lovCuentasp) {
         this.lovCuentas.add(recC);
      }
      log.info("SET : setLovCuentas");
   }

   public List<Empleados> getLovEmpleadosActivos() {
      if (lovEmpleadosActivos == null) {
         lovEmpleadosActivos = new ArrayList<Empleados>();
      }
      return lovEmpleadosActivos;
   }

   public void setLovEmpleadosActivos(List<Empleados> lovEmpleadosp) {
      getLovEmpleadosActivos().clear();
//      this.lovEmpleadosActivos.clear();
      for (Empleados recC : lovEmpleadosp) {
         this.lovEmpleadosActivos.add(recC);
      }
      log.info("SET : setLovEmpleadosActivos");
   }

   public List<Empleados> getLovEmpleadosNovedad() {
      if (lovEmpleadosNovedad == null) {
         lovEmpleadosNovedad = new ArrayList<Empleados>();
      }
      return lovEmpleadosNovedad;
   }

   public void setLovEmpleadosNovedad(List<Empleados> lovEmpleadosp) {
      getLovEmpleadosNovedad().clear();
//      this.lovEmpleadosNovedad.clear();
      for (Empleados recC : lovEmpleadosp) {
         this.lovEmpleadosNovedad.add(recC);
      }
      log.info("SET : setLovEmpleadosNovedad");
   }

   public List<Empleados> getLovEmpleadosActPenc() {
      if (lovEmpleadosActPenc == null) {
         lovEmpleadosActPenc = new ArrayList<Empleados>();
      }
      return lovEmpleadosActPenc;
   }

   public void setLovEmpleadosActPenc(List<Empleados> lovEmpleadosActPencP) {
      getLovEmpleadosActPenc().clear();
//      this.lovEmpleadosActPenc.clear();
      for (Empleados recC : lovEmpleadosActPencP) {
         this.lovEmpleadosActPenc.add(recC);
      }
      log.info("SET : setLovEmpleadosActPenc");
   }

   public void limpiarListasEmpleados() {
      getLovEmpleadosActPenc().clear();
      getLovEmpleadosActivos().clear();
      getLovEmpleadosNovedad().clear();
   }

}
