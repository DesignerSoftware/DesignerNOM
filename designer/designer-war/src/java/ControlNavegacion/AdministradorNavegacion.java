/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlNavegacion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class AdministradorNavegacion {
   
   private List<String> listaPaginasAnteriores;
   
   public AdministradorNavegacion() {
//      ArrayList<String> listaPaginasAnteriores = new ArrayList<String>();
      listaPaginasAnteriores = new ArrayList<String>();
      listaPaginasAnteriores.add("iniciored");
   }

   public void adicionarPagina(String pag) {
      listaPaginasAnteriores.add(pag);
      System.out.println("AdministradorNavegacion.adicionarPagina() listaPaginasAnteriores : " + listaPaginasAnteriores);
   }

   public void quitarPagina() {
      listaPaginasAnteriores.remove((listaPaginasAnteriores.size() - 1));
      System.out.println("AdministradorNavegacion.quitarPagina() listaPaginasAnteriores : " + listaPaginasAnteriores);
   }

   public String retornarPaginaAnterior() {
      String s = listaPaginasAnteriores.get((listaPaginasAnteriores.size() - 1));
      listaPaginasAnteriores.remove((listaPaginasAnteriores.size() - 1));
      System.out.println("AdministradorNavegacion.retornarPaginaAnterior() listaPaginasAnteriores : " + listaPaginasAnteriores);
      return s;
   }

   public List<String> getListaPaginasAnteriores() {
      return listaPaginasAnteriores;
   }

   public void setListaPaginasAnteriores(List<String> listaPaginasAnteriores) {
      this.listaPaginasAnteriores = listaPaginasAnteriores;
   }
}
