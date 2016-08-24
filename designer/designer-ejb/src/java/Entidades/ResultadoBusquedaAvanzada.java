package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;

/**
 *
 * @author Administrador
 */
@Entity
@SqlResultSetMapping(
        name = "ConsultaBusquedaAvanzada",
        entities = {
            @EntityResult(
                    entityClass = ResultadoBusquedaAvanzada.class,
                    fields = {
                        @FieldResult(name = "secuencia", column = "SECUENCIA"),
                        @FieldResult(name = "codigoEmpleado", column = "CODIGOEMPLEADO"),
                        @FieldResult(name = "primerApellido", column = "PRIMERAPELLIDO"),
                        @FieldResult(name = "segundoApellido", column = "SEGUNDOAPELLIDO"),
                        @FieldResult(name = "nombre", column = "NOMBREEMPLEADO"),
                        @FieldResult(name = "columna0", column = "COLUMNA0"),
                        @FieldResult(name = "columna1", column = "COLUMNA1"),
                        @FieldResult(name = "columna2", column = "COLUMNA2"),
                        @FieldResult(name = "columna3", column = "COLUMNA3"),
                        @FieldResult(name = "columna4", column = "COLUMNA4"),
                        @FieldResult(name = "columna5", column = "COLUMNA5"),
                        @FieldResult(name = "columna6", column = "COLUMNA6"),
                        @FieldResult(name = "columna7", column = "COLUMNA7"),
                        @FieldResult(name = "columna8", column = "COLUMNA8"),
                        @FieldResult(name = "columna9", column = "COLUMNA9")
                    }
            )
        }
)
public class ResultadoBusquedaAvanzada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger secuencia;
    private String codigoEmpleado;
    private String primerApellido;
    private String segundoApellido;
    private String nombre;
    private String columna0;
    private String columna1;
    private String columna2;
    private String columna3;
    private String columna4;
    private String columna5;
    private String columna6;
    private String columna7;
    private String columna8;
    private String columna9;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigo) {
        this.codigoEmpleado = codigo;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primeroApellido) {
        this.primerApellido = primeroApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColumna0() {
        return columna0;
    }

    public void setColumna0(String columna0) {
        this.columna0 = columna0;
    }

    public String getColumna1() {
        return columna1;
    }

    public void setColumna1(String columna1) {
        this.columna1 = columna1;
    }

    public String getColumna2() {
        return columna2;
    }

    public void setColumna2(String columna2) {
        this.columna2 = columna2;
    }

    public String getColumna3() {
        return columna3;
    }

    public void setColumna3(String columna3) {
        this.columna3 = columna3;
    }

    public String getColumna4() {
        return columna4;
    }

    public void setColumna4(String columna4) {
        this.columna4 = columna4;
    }

    public String getColumna5() {
        return columna5;
    }

    public void setColumna5(String columna5) {
        this.columna5 = columna5;
    }

    public String getColumna6() {
        return columna6;
    }

    public void setColumna6(String columna6) {
        this.columna6 = columna6;
    }

    public String getColumna7() {
        return columna7;
    }

    public void setColumna7(String columna7) {
        this.columna7 = columna7;
    }

    public String getColumna8() {
        return columna8;
    }

    public void setColumna8(String columna8) {
        this.columna8 = columna8;
    }

    public String getColumna9() {
        return columna9;
    }

    public void setColumna9(String columna9) {
        this.columna9 = columna9;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultadoBusquedaAvanzada)) {
            return false;
        }
        ResultadoBusquedaAvanzada other = (ResultadoBusquedaAvanzada) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ResultadoBusquedaAvanzada[ secuencia=" + secuencia + " ]";
    }

}
