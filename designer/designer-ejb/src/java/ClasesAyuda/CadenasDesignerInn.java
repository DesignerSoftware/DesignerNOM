/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

/**
 *
 * @author user
 */
public class CadenasDesignerInn implements Comparable {

   private String id;
   private String bd;
   private String nombrepoolJPA;
   private String usapool;

   public CadenasDesignerInn(String id, String nombrepoolJPA, String usapool, String bd) {
      this.id = id;
      this.bd = bd;
      this.nombrepoolJPA = nombrepoolJPA;
      this.usapool = usapool;
   }

   public String getBd() {
      return bd;
   }

   public void setBd(String bd) {
      this.bd = bd;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNombrepoolJPA() {
      return nombrepoolJPA;
   }

   public void setNombrepoolJPA(String nombrepoolJPA) {
      this.nombrepoolJPA = nombrepoolJPA;
   }

   public String getUsapool() {
      return usapool;
   }

   public void setUsapool(String usapool) {
      this.usapool = usapool;
   }

   @Override
   public int compareTo(Object o) {
      int resultado;
      CadenasDesignerInn cDI = (CadenasDesignerInn) o;
      if (this.getId().equalsIgnoreCase(cDI.getId())) {
         resultado = 0;
      } else if (Integer.parseInt(this.getId()) < Integer.parseInt(cDI.getId())) {
         resultado = -1;
      } else {
         resultado = 1;
      }
      return resultado;
   }
}
