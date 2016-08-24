package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "COLUMNASESCENARIOS")
public class ColumnasEscenarios implements Serializable {
   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Size(max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 100)
    @Column(name = "NOMBRECOLUMNA")
    private String nombrecolumna;
    @Size(max = 106)
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "TAMANO")
    private BigInteger tamano;
    @Column(name = "COL_PRECISION")
    private BigInteger colPrecision;
    @Size(max = 500)
    @Column(name = "LOVCOLUMNA1")
    private String lovcolumna1;
    @Size(max = 500)
    @Column(name = "LOVCOLUMNA2")
    private String lovcolumna2;
    @Size(max = 2000)
    @Column(name = "LOVFROM")
    private String lovfrom;
    @Size(max = 10)
    @Column(name = "CLASECONDICION")
    private String clasecondicion;
    @Size(max = 20)
    @Column(name = "OPERADORESPECIAL")
    private String operadorespecial;
    @Size(max = 500)
    @Column(name = "SUBQUERYESPECIAL")
    private String subqueryespecial;
    @JoinColumn(name = "ESCENARIO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Escenarios escenario;

    public ColumnasEscenarios() {
    }

    public ColumnasEscenarios(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombrecolumna() {
        return nombrecolumna;
    }

    public void setNombrecolumna(String nombrecolumna) {
        this.nombrecolumna = nombrecolumna;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigInteger getTamano() {
        return tamano;
    }

    public void setTamano(BigInteger tamano) {
        this.tamano = tamano;
    }

    public BigInteger getColPrecision() {
        return colPrecision;
    }

    public void setColPrecision(BigInteger colPrecision) {
        this.colPrecision = colPrecision;
    }

    public String getLovcolumna1() {
        return lovcolumna1;
    }

    public void setLovcolumna1(String lovcolumna1) {
        this.lovcolumna1 = lovcolumna1;
    }

    public String getLovcolumna2() {
        return lovcolumna2;
    }

    public void setLovcolumna2(String lovcolumna2) {
        this.lovcolumna2 = lovcolumna2;
    }

    public String getLovfrom() {
        return lovfrom;
    }

    public void setLovfrom(String lovfrom) {
        this.lovfrom = lovfrom;
    }

    public String getClasecondicion() {
        return clasecondicion;
    }

    public void setClasecondicion(String clasecondicion) {
        this.clasecondicion = clasecondicion;
    }

    public String getOperadorespecial() {
        return operadorespecial;
    }

    public void setOperadorespecial(String operadorespecial) {
        this.operadorespecial = operadorespecial;
    }

    public String getSubqueryespecial() {
        return subqueryespecial;
    }

    public void setSubqueryespecial(String subqueryespecial) {
        this.subqueryespecial = subqueryespecial;
    }

    public Escenarios getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenarios escenario) {
        this.escenario = escenario;
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
        if (!(object instanceof ColumnasEscenarios)) {
            return false;
        }
        ColumnasEscenarios other = (ColumnasEscenarios) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ColumnasEscenarios[ secuencia=" + secuencia + " ]";
    }
    
}
