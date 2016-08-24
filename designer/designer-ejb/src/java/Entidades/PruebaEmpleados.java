package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;

/**
 *
 * @author Viktor
 */
@Entity
@SqlResultSetMapping(name = "PruebaEmpleadosAsignacionBasica",
        entities = {
            @EntityResult(entityClass = PruebaEmpleados.class,
                    fields = {
                        @FieldResult(name = "codigo", column = "CODIGO"),
                        @FieldResult(name = "nombre", column = "NOMBRE"),
                        @FieldResult(name = "valor", column = "VALOR"),
                        @FieldResult(name = "tipo", column = "TIPO"),})
        })

public class PruebaEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private BigDecimal codigo;
    private String nombre;
    private BigInteger valor;
    private String tipo;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigDecimal getCodigo() {
        return codigo;
    }

    public void setCodigo(BigDecimal codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PruebaEmpleados)) {
            return false;
        }
        PruebaEmpleados other = (PruebaEmpleados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PruebaEmpleados[ id=" + id + " ]";
    }
}
