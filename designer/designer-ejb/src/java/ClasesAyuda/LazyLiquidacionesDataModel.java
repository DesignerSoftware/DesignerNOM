/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import Entidades.LiquidacionesLogs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author user
 */
public class LazyLiquidacionesDataModel extends LazyDataModel<LiquidacionesLogs> {

    private List<LiquidacionesLogs> lista;

    public LazyLiquidacionesDataModel(List<LiquidacionesLogs> lista) {
        this.lista = lista;
    }

    @Override
    public LiquidacionesLogs getRowData(String rowKey) {
        for (LiquidacionesLogs uir : this.lista) {
            if (uir.getSecuencia().toString().equals(rowKey)) {
                return uir;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(LiquidacionesLogs uir) {
        return uir.getSecuencia();
    }

    @Override
    public List<LiquidacionesLogs> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            List<LiquidacionesLogs> data = new ArrayList<LiquidacionesLogs>();

            //filtro
            for (LiquidacionesLogs uir : lista) {
                boolean match = true;

                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object valorFiltro = filters.get(filterProperty);

                            if (valorFiltro != null && uir.getFechadesde().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && uir.getFechahasta().toString().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && uir.getEmpleado().getPersona().getNombreCompleto().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && uir.getOperando().getNombre().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && uir.getProceso().getDescripcion().contains(valorFiltro.toString())) {
                                match = true;
                            } else if (valorFiltro != null && uir.getValor().contains(valorFiltro.toString())) {
                                match = true;
                            } else {
                                match = false;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("error en match : " + e.getMessage());
                            match = false;
                        }
                    }
                }
                if (match == true) {
                    data.add(uir);
                }
            }

            //ordenamiento
//            if (sortField != null) {
//                Collections.sort(data, new LazySorterUIR(sortField, sortOrder));
//            }
            //conteo de registros
            int dataSize = data.size();
            this.setRowCount(dataSize);

            //paginador
            if (dataSize > pageSize) {
                try {
                    return data.subList(first, first + pageSize);
                } catch (IndexOutOfBoundsException e) {
                    return data.subList(first, first + (dataSize % pageSize));
                }
            } else {
                return data;
            }
        } catch (Exception w) {
            System.out.println("error en load : " + w.getMessage());
            return null;
        }
    }

}
