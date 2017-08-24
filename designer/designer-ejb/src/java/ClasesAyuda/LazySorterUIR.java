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
            int value = 0;
            if (sortField.equals("inforeporte.codigo")) {
                value = o1.getInforeporte().getCodigo().compareTo(o2.getInforeporte().getCodigo());
            } else if (sortField.equals("inforeporte.nombre")) {
                value = o1.getInforeporte().getNombre().compareTo(o2.getInforeporte().getNombre());
            } else if (sortField.equals("inforeporte.modulo.nombre")) {
                value = o1.getInforeporte().getModulo().getNombre().compareTo(o2.getInforeporte().getModulo().getNombre());
            } else if (sortField.equals("inforeporte.tipo")) {
                value = o1.getInforeporte().getTipo().compareTo(o2.getInforeporte().getTipo());
            }
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;

        } catch (Exception e) {
            System.out.println("error en compare : " + e.getMessage());
            System.out.println("error en compare : " + e.getCause());
            throw new RuntimeException();
        }
    }

}
