/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.UsuariosInforeportes;
import java.lang.reflect.Field;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazySorterUIR implements Comparator<UsuariosInforeportes> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterUIR(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(UsuariosInforeportes o1, UsuariosInforeportes o2) {
        try {
//            Object value1 = UsuariosInforeportes.class.getField(sortField).get(o1);
//            Object value2 = UsuariosInforeportes.class.getField(sortField).get(o2);
//            int value = ((Comparable)value1).compareTo(value2);
//            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;

//      Field f = UsuariosInforeportes.class.getDeclaredField(this.sortField);  
//      f.setAccessible(true);  
//      Object value1 = f.get(o1);  
//      Object value2 = f.get(o2);  
//  
//      int cmp = ((Comparable)value1).compareTo(value2);  
//        
//      return SortOrder.ASCENDING.equals(sortOrder) ? cmp : -1 * cmp;  
            int value = 0;
            Field[] campos = UsuariosInforeportes.class.getFields();
            for (Field campo : campos) {
                System.out.println("campo: " + campo.toString());
            }
            value = o1.getSecuencia().compareTo(o2.getSecuencia());
            System.out.println("value : " + value);
            return value;

        } catch (Exception e) {
            System.out.println("error en compare : " + e.getMessage());
            System.out.println("error en compare : " + e.getCause());
            throw new RuntimeException();
        }
    }

}
