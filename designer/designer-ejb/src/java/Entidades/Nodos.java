package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "NODOS")
public class Nodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POSICION")
    private short posicion;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 400)
    @Column(name = "FORMULA")
    private String formula;
    @JoinColumn(name = "OPERANDO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Operandos operando;
    @JoinColumn(name = "OPERADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Operadores operador;
    @JoinColumn(name = "HISTORIAFORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Historiasformulas historiaformula;
    @Transient
    private String descripcionNodo;

    public Nodos() {
    }

    public Nodos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Nodos(BigInteger secuencia, short posicion) {
        this.secuencia = secuencia;
        this.posicion = posicion;
    }

    public short getPosicion() {
        return posicion;
    }

    public void setPosicion(short posicion) {
        this.posicion = posicion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDescripcionNodo() {
        return descripcionNodo;
    }

    public void setDescripcionNodo(String descripcionNodo) {
        this.descripcionNodo = descripcionNodo;
    }

    public Operandos getOperando() {
        return operando;
    }

    public void setOperando(Operandos operando) {
        this.operando = operando;
    }

    public Operadores getOperador() {
        return operador;
    }

    public void setOperador(Operadores operador) {
        this.operador = operador;
    }

    public Historiasformulas getHistoriaformula() {
        return historiaformula;
    }

    public void setHistoriaformula(Historiasformulas historiaformula) {
        this.historiaformula = historiaformula;
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
        if (!(object instanceof Nodos)) {
            return false;
        }
        Nodos other = (Nodos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Nodos[ secuencia=" + secuencia + " ]";
    }

}
