/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.LiquidacionesLogs;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazySorterLiqLogs implements Comparator<LiquidacionesLogs> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterLiqLogs(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(LiquidacionesLogs o1, LiquidacionesLogs o2) {
        try {
            int value = 0;
            if (sortField.equals("fechadesde")) {
                value = o1.getFechadesde().toString().compareTo(o2.getFechadesde().toString());
            } else if (sortField.equals("fechahasta")) {
                value = o1.getFechahasta().toString().compareTo(o2.getFechahasta().toString());
            } else if (sortField.equals("empleado.persona.nombreCompleto")) {
                value = o1.getEmpleado().getPersona().getNombreCompleto().compareTo(o2.getEmpleado().getPersona().getNombreCompleto());
            } else if (sortField.equals("operando.nombre")) {
                value = o1.getOperando().getNombre().compareTo(o2.getOperando().getNombre());
            } else if (sortField.equals("proceso.descripcion")) {
                value = o1.getProceso().getDescripcion().compareTo(o2.getProceso().getDescripcion());
            } else if (sortField.equals("valor")) {
                value = o1.getValor().compareTo(o2.getValor());
            }
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;

        } catch (Exception e) {
            System.out.println("error en compare : " + e.getMessage());
            System.out.println("error en compare : " + e.getCause());
            throw new RuntimeException();
        }
    }

}
