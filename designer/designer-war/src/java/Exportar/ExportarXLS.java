/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Exportar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.component.export.ExcelExporter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Constants;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class ExportarXLS extends ExcelExporter {

    @Override
    public void export(FacesContext context, DataTable table, String filename, boolean pageOnly, boolean selectionOnly, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();

        if (preProcessor != null) {
            preProcessor.invoke(context.getELContext(), new Object[]{wb});
        }

        addColumnFacets(table, sheet, ColumnType.HEADER);

        if (pageOnly) {
            exportPageOnly(context, table, sheet);
        } else if (selectionOnly) {
            exportSelectionOnly(context, table, sheet);
        } else {
            exportAll(context, table, sheet);
        }

        table.setRowIndex(-1);

        if (postProcessor != null) {
            postProcessor.invoke(context.getELContext(), new Object[]{wb});
        }

        writeExcelToResponse(context.getExternalContext(), wb, filename);
    }

    protected void exportPageOnly(FacesContext context, DataTable table, Sheet sheet) {
        int first = table.getFirst();
        int rowsToExport = first + table.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, sheet, rowIndex);
        }
    }

    protected void exportSelectionOnly(FacesContext context, DataTable table, Sheet sheet) {
        Object selection = table.getSelection();
        String var = table.getVar();

        if (selection != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));

                    exportCells(table, sheet);
                }
            } else {
                requestMap.put(var, selection);

                exportCells(table, sheet);
            }
        }
    }

    protected void exportAll(FacesContext context, DataTable table, Sheet sheet) {
        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rows = table.getRows();
        boolean lazy = table.isLazy();

        if (lazy) {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rowIndex % rows == 0) {
                    table.setFirst(rowIndex);
                    table.loadLazyData();
                }

                exportRow(table, sheet, rowIndex);
            }

            //restore
            table.setFirst(first);
            table.loadLazyData();
        } else {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, sheet, rowIndex);
            }

            //restore
            table.setFirst(first);
        }
    }

    protected void exportRow(DataTable table, Sheet sheet, int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, sheet);
    }

    protected void exportCells(DataTable table, Sheet sheet) {
        int sheetRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(sheetRowIndex);

        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(row, col.getChildren());
            }
        }
    }

    @Override
    protected void addColumnFacets(DataTable table, Sheet sheet, ColumnType columnType) {
        int sheetRowIndex = columnType.equals(ColumnType.HEADER) ? 0 : (sheet.getLastRowNum() + 1);
        Row rowHeader = sheet.createRow(sheetRowIndex);

        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(rowHeader, col.getFacet(columnType.facet()));
            }
        }
    }

    @Override
    protected void addColumnValue(Row row, UIComponent component) {
        int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        Cell cell = row.createCell(cellIndex);
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        cell.setCellValue(new HSSFRichTextString(value));
    }

    @Override
    protected void addColumnValue(Row row, List<UIComponent> components) {
        int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        Cell cell = row.createCell(cellIndex);
        StringBuilder builder = new StringBuilder();
        FacesContext context = FacesContext.getCurrentInstance();

        for (UIComponent component : components) {
            if (component.isRendered()) {
                String value = exportValue(context, component);

                if (value != null) {
                    builder.append(value);
                }
            }
        }

        cell.setCellValue(new HSSFRichTextString(builder.toString()));
    }

    @Override
    protected void writeExcelToResponse(ExternalContext externalContext, Workbook generatedExcel, String filename) throws IOException {
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());

        OutputStream out = externalContext.getResponseOutputStream();
        generatedExcel.write(out);
        externalContext.responseFlushBuffer();
    }
}
