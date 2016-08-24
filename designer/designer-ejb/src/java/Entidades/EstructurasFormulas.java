package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;

/**
 *
 * @author PROYECTO01
 */
@Entity
@SqlResultSetMapping(
        name = "ConsultaEstructurasFormulas",
        entities = {
            @EntityResult(
                    entityClass = EstructurasFormulas.class,
                    fields = {
                        @FieldResult(name = "posicion", column = "POSICION"),
                        @FieldResult(name = "nivel", column = "NIVEL"),
                        @FieldResult(name = "nombreNodo", column = "NOMBRENODO"),
                        @FieldResult(name = "secuenciaNodo", column = "NODO"),
                        @FieldResult(name = "descripcion", column = "DESCRIPCION"),
                        @FieldResult(name = "formula", column = "FORMULA"),
                        @FieldResult(name = "formulaHijo", column = "FORMULAHIJO"),
                        @FieldResult(name = "historiaFormula", column = "HISTORIAFORMULA"),
                        @FieldResult(name = "historiaFormulaHijo", column = "HISTORIAFORMULAHIJO")
                    }
            )
        }
)
public class EstructurasFormulas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private int nivel;
    private String nombreNodo;
    private int posicion;
    private BigInteger secuenciaNodo;
    private String descripcion;
    private BigInteger formula;
    private BigInteger formulaHijo;
    private BigInteger historiaFormula;
    private BigInteger historiaFormulaHijo;
    private String strFormula;
    private String strHijo;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getNombreNodo() {
        return nombreNodo;
    }

    public void setNombreNodo(String nombreNodo) {
        this.nombreNodo = nombreNodo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public BigInteger getSecuenciaNodo() {
        return secuenciaNodo;
    }

    public void setSecuenciaNodo(BigInteger secuenciaNodo) {
        this.secuenciaNodo = secuenciaNodo;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = " ";
        }
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

    public String getStrFormula() {
        if (formula != null) {
            strFormula = formula.toString();
        } else {
            strFormula = " ";
        }
        return strFormula;
    }

    public void setStrFormula(String strFormula) {
        this.strFormula = strFormula;
    }

    public String getStrHijo() {
        if (formulaHijo != null) {
            strHijo = formulaHijo.toString();
        } else {
            strHijo = " ";
        }
        return strHijo;
    }

    public void setStrHijo(String strHijo) {
        this.strHijo = strHijo;
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
        if (!(object instanceof EstructurasFormulas)) {
            return false;
        }
        EstructurasFormulas other = (EstructurasFormulas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EstructurasFormulas[ id=" + id + " ]";
    }

}
