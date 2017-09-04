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
   private List<Empleados> lovEmpleados;

   public ListasRecurrentes() {
      lovCuentas = new ArrayList<Cuentas>();
      lovCentrosCostos = new ArrayList<CentrosCostos>();
      lovProcesos = new ArrayList<Procesos>();
      lovGruposConceptos = new ArrayList<GruposConceptos>();
      lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      lovFormulas = new ArrayList<Formulas>();
      lovFormulasConceptos = new ArrayList<FormulasConceptos>();
      lovConceptos = new ArrayList<Conceptos>();
      lovEmpleados = new ArrayList<Empleados>();
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostosp) {
//      this.lovCentrosCostos = new ArrayList<CentrosCostos>();
      this.lovCentrosCostos.clear();
      for (CentrosCostos recC : lovCentrosCostosp) {
         this.lovCentrosCostos.add(recC);
      }
      log.info("SET : setLovCentrosCostos");
   }

   public List<Procesos> getLovProcesos() {
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesosp) {
//      this.lovProcesos = new ArrayList<Procesos>();
      this.lovProcesos.clear();
      for (Procesos recC : lovProcesosp) {
         this.lovProcesos.add(recC);
      }
      log.info("SET : setLovProcesos");
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptosp) {
//      this.lovGruposConceptos = new ArrayList<GruposConceptos>();
      this.lovGruposConceptos.clear();
      for (GruposConceptos recC : lovGruposConceptosp) {
         this.lovGruposConceptos.add(recC);
      }
      log.info("SET : setLovGruposConceptos");
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadoresp) {
//      this.lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      this.lovTiposTrabajadores.clear();
      for (TiposTrabajadores recC : lovTiposTrabajadoresp) {
         this.lovTiposTrabajadores.add(recC);
      }
      log.info("SET : setLovTiposTrabajadores");
   }

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulasp) {
//      this.lovFormulas = new ArrayList<Formulas>();
      this.lovFormulas.clear();
      for (Formulas recC : lovFormulasp) {
         this.lovFormulas.add(recC);
      }
      log.info("SET : setLovFormulas");
   }

   public List<FormulasConceptos> getLovFormulasConceptos() {
      return lovFormulasConceptos;
   }

   public void setLovFormulasConceptos(List<FormulasConceptos> lovFormulasConceptosp) {
//      this.lovFormulasConceptos = new ArrayList<FormulasConceptos>();
      this.lovFormulasConceptos.clear();
      for (FormulasConceptos recC : lovFormulasConceptosp) {
         this.lovFormulasConceptos.add(recC);
      }
      log.info("SET : setLovFormulasConceptos");
   }

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptosp) {
//      this.lovConceptos = new ArrayList<Conceptos>();
      this.lovConceptos.clear();
      for (Conceptos recC : lovConceptosp) {
         this.lovConceptos.add(recC);
      }
      log.info("SET : setLovConceptos");
   }

   public List<Cuentas> getLovCuentas() {
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentasp) {
//      this.lovCuentas = new ArrayList<Cuentas>();
      this.lovCuentas.clear();
      for (Cuentas recC : lovCuentasp) {
         this.lovCuentas.add(recC);
      }
      log.info("SET : setLovCuentas");
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleadosp) {
      this.lovEmpleados.clear();
      for (Empleados recC : lovEmpleadosp) {
         this.lovEmpleados.add(recC);
      }
      log.info("SET : setLovEmpleados");
   }

}
