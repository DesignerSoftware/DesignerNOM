/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import java.sql.SQLException;

/**
 *
 * @author user
 */
public class ExtraeCausaExcepcion {

    /**
     * Metodo encargado de retornar el ultimo error que se capturo en los try -
     * catch.
     *
     * @param e Exception
     * @return Retorna el ultimo error capturado.
     */
    public static Throwable getLastThrowable(Exception e) {
        Throwable t;
        for (t = e.getCause(); t.getCause() != null; t = t.getCause());
        return t;
    }

    public static int obtenerCodigoSQLException(Exception e) throws Exception {
        int codigo = 0;
        Throwable t = getLastThrowable(e);
        if (t instanceof SQLException) {
            SQLException sqle = (SQLException) t;
            codigo = sqle.getErrorCode();
        } else {
            throw e;
        }
        return codigo;
    }
    public static String obtenerMensajeSQLException(Exception e) {
        String mensaje;
        Throwable t = getLastThrowable(e);
        mensaje = t.getMessage();
//        if (t instanceof SQLException) {
//            SQLException sqle = (SQLException) t;
//            mensaje = sqle.getMessage();
//        } else {
//            throw e;
//        }
        return mensaje;
    }
}
