package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@SqlResultSetMapping(
        name = "DetallesFormulasComprobantes",
        entities = {
    @EntityResult(
            entityClass = DetallesFormulas.class,
            fields = {
        @FieldResult(name = "nivel", column = "NIVEL"),
        @FieldResult(name = "id", column = "ID"),
        @FieldResult(name = "posicion", column = "POSICION"),
        @FieldResult(name = "nombreNodo", column = "NOMBRENODO"),
        @FieldResult(name = "nodo", column = "NODO"),
        @FieldResult(name = "operando", column = "OPERANDO"),
        @FieldResult(name = "descripcion", column = "DESCRIPCION"),
        @FieldResult(name = "formula", column = "FORMULA"),
        @FieldResult(name = "formulaHijo", column = "FORMULAHIJO"),
        @FieldResult(name = "historiaFormula", column = "HISTORIAFORMULA"),
        @FieldResult(name = "historiaFormulaHijo", column = "HISTORIAFORMULAHIJO"),
        @FieldResult(name = "valor", column = "VALOR")
    })
})
public class DetallesFormulas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private Integer posicion;
    private Integer nivel;
    private String nombreNodo;
    private BigInteger nodo;
    private BigInteger operando;
    private String descripcion;
    private BigInteger formula;
    private BigInteger formulaHijo;
    private BigInteger historiaFormula;
    private BigInteger historiaFormulaHijo;
    private String valor;
    
    

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getNombreNodo() {
        return nombreNodo;
    }

    public void setNombreNodo(String nombreNodo) {
        this.nombreNodo = nombreNodo;
    }

    public BigInteger getNodo() {
        return nodo;
    }

    public void setNodo(BigInteger nodo) {
        this.nodo = nodo;
    }

    public BigInteger getOperando() {
        return operando;
    }

    public void setOperando(BigInteger operando) {
        this.operando = operando;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getFormula() {
        return formula;
    }

    public void setFormula(BigInteger formula) {
        this.formula = formula;
    }

    public BigInteger getFormulaHijo() {
        return formulaHijo;
    }

    public void setFormulaHijo(BigInteger formulaHijo) {
        this.formulaHijo = formulaHijo;
    }

    public BigInteger getHistoriaFormula() {
        return historiaFormula;
    }

    public void setHistoriaFormula(BigInteger historiaFormula) {
        this.historiaFormula = historiaFormula;
    }

    public BigInteger getHistoriaFormulaHijo() {
        return historiaFormulaHijo;
    }

    public void setHistoriaFormulaHijo(BigInteger historiaFormulaHijo) {
        this.historiaFormulaHijo = historiaFormulaHijo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
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
        if (!(object instanceof DetallesFormulas)) {
            return false;
        }
        DetallesFormulas other = (DetallesFormulas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesFormulas[ id=" + id + " ]";
    }
}
