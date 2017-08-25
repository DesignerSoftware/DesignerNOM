/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlNavegacion;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
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

   public ListasRecurrentes() {
      lovCuentas = new ArrayList<Cuentas>();
      lovCentrosCostos = new ArrayList<CentrosCostos>();
      lovProcesos = new ArrayList<Procesos>();
      lovGruposConceptos = new ArrayList<GruposConceptos>();
      lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      lovFormulas = new ArrayList<Formulas>();
      lovFormulasConceptos = new ArrayList<FormulasConceptos>();
      lovConceptos = new ArrayList<Conceptos>();
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostosp) {
//      this.lovCentrosCostos = new ArrayList<CentrosCostos>(lovCentrosCostosp);
      this.lovCentrosCostos = new ArrayList<CentrosCostos>();
      for (CentrosCostos recC : lovCentrosCostosp) {
         this.lovCentrosCostos.add(recC);
      }
//      this.lovCentrosCostos.addAll(lovCentrosCostos);
      log.info("SET : setLovCentrosCostos");
   }

   public List<Procesos> getLovProcesos() {
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesosp) {
//      this.lovProcesos = new ArrayList<Procesos>(lovProcesosp);
      this.lovProcesos = new ArrayList<Procesos>();
      for (Procesos recC : lovProcesosp) {
         this.lovProcesos.add(recC);
      }
//      this.lovProcesos.addAll(lovProcesos);
      log.info("SET : setLovProcesos");
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptosp) {
//      this.lovGruposConceptos = new ArrayList<GruposConceptos>(lovGruposConceptosp);
      this.lovGruposConceptos = new ArrayList<GruposConceptos>();
      for (GruposConceptos recC : lovGruposConceptosp) {
         this.lovGruposConceptos.add(recC);
      }
//      this.lovGruposConceptos.addAll(lovGruposConceptos);
      log.info("SET : setLovGruposConceptos");
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadoresp) {
//      this.lovTiposTrabajadores = new ArrayList<TiposTrabajadores>(lovTiposTrabajadoresp);
      this.lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      for (TiposTrabajadores recC : lovTiposTrabajadoresp) {
         this.lovTiposTrabajadores.add(recC);
      }
//      this.lovTiposTrabajadores.addAll(lovTiposTrabajadores);
      log.info("SET : setLovTiposTrabajadores");
   }

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulasp) {
//      this.lovFormulas = new ArrayList<Formulas>(lovFormulasp);
      this.lovFormulas = new ArrayList<Formulas>();
      for (Formulas recC : lovFormulasp) {
         this.lovFormulas.add(recC);
      }
//      this.lovFormulas.addAll(lovFormulas);
      log.info("SET : setLovFormulas");
   }

   public List<FormulasConceptos> getLovFormulasConceptos() {
      return lovFormulasConceptos;
   }

   public void setLovFormulasConceptos(List<FormulasConceptos> lovFormulasConceptosp) {
//      this.lovFormulasConceptos = new ArrayList<FormulasConceptos>(lovFormulasConceptosp);
      this.lovFormulasConceptos = new ArrayList<FormulasConceptos>();
      for (FormulasConceptos recC : lovFormulasConceptosp) {
         this.lovFormulasConceptos.add(recC);
      }
//      this.lovFormulasConceptos.addAll(lovFormulasConceptos);
      log.info("SET : setLovFormulasConceptos");
   }

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptosp) {
//      this.lovConceptos = new ArrayList<Conceptos>(lovConceptosp);
      this.lovConceptos = new ArrayList<Conceptos>();
      for (Conceptos recC : lovConceptosp) {
         this.lovConceptos.add(recC);
      }
//      this.lovConceptos.addAll(lovConceptos);
      log.info("SET : setLovConceptos");
   }

   public List<Cuentas> getLovCuentas() {
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentasp) {
//      this.lovCuentas = new ArrayList<Cuentas>(lovCuentasp);
      this.lovCuentas = new ArrayList<Cuentas>();
      for (Cuentas recC : lovCuentasp) {
         this.lovCuentas.add(recC);
      }
//      this.lovCuentas.addAll(lovCuentas);
      log.info("SET : setLovCuentas");
   }

}
