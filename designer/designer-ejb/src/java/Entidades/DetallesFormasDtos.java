package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "DETALLESFORMASDTOS")
public class DetallesFormasDtos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "METODO")
    private String metodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "BASEBASICO")
    private String basebasico;
    @Size(max = 1)
    @Column(name = "EXCLUYESML")
    private String excluyesml;
    @Size(max = 1)
    @Column(name = "CUOTAFIJAAUMENTABASICOENERO")
    private String cuotafijaaumentabasicoenero;
    @Size(max = 1)
    @Column(name = "CUOTAFIJAAUMENTABASICOANUAL")
    private String cuotafijaaumentabasicoanual;
    @Size(max = 1)
    @Column(name = "CUOTAFIJAAUMENTASMLVENERO")
    private String cuotafijaaumentasmlvenero;
    @Size(max = 1)
    @Column(name = "CUOTAFIJAAUMENTASMLVANUAL")
    private String cuotafijaaumentasmlvanual;
    @JoinColumn(name = "TIPODTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposDtos tipodto;
    @JoinColumn(name = "BASEGRUPO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos basegrupo;
    @JoinColumn(name = "GRUPODISPARADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupodisparador;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Formulas formula;
    @JoinColumn(name = "FORMADTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private FormasDtos formadto;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Conceptos concepto;

    public DetallesFormasDtos() {
    }

    public DetallesFormasDtos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public DetallesFormasDtos(BigInteger secuencia, String descripcion, String metodo, String basebasico) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.metodo = metodo;
        this.basebasico = basebasico;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getBasebasico() {
        return basebasico;
    }

    public void setBasebasico(String basebasico) {
        this.basebasico = basebasico;
    }

    public String getExcluyesml() {
        return excluyesml;
    }

    public void setExcluyesml(String excluyesml) {
        this.excluyesml = excluyesml;
    }

    public String getCuotafijaaumentabasicoenero() {
        return cuotafijaaumentabasicoenero;
    }

    public void setCuotafijaaumentabasicoenero(String cuotafijaaumentabasicoenero) {
        this.cuotafijaaumentabasicoenero = cuotafijaaumentabasicoenero;
    }

    public String getCuotafijaaumentabasicoanual() {
        return cuotafijaaumentabasicoanual;
    }

    public void setCuotafijaaumentabasicoanual(String cuotafijaaumentabasicoanual) {
        this.cuotafijaaumentabasicoanual = cuotafijaaumentabasicoanual;
    }

    public String getCuotafijaaumentasmlvenero() {
        return cuotafijaaumentasmlvenero;
    }

    public void setCuotafijaaumentasmlvenero(String cuotafijaaumentasmlvenero) {
        this.cuotafijaaumentasmlvenero = cuotafijaaumentasmlvenero;
    }

    public String getCuotafijaaumentasmlvanual() {
        return cuotafijaaumentasmlvanual;
    }

    public void setCuotafijaaumentasmlvanual(String cuotafijaaumentasmlvanual) {
        this.cuotafijaaumentasmlvanual = cuotafijaaumentasmlvanual;
    }

    public TiposDtos getTipodto() {
        return tipodto;
    }

    public void setTipodto(TiposDtos tipodto) {
        this.tipodto = tipodto;
    }

    public GruposConceptos getBasegrupo() {
        return basegrupo;
    }

    public void setBasegrupo(GruposConceptos basegrupo) {
        this.basegrupo = basegrupo;
    }

    public GruposConceptos getGrupodisparador() {
        return grupodisparador;
    }

    public void setGrupodisparador(GruposConceptos grupodisparador) {
        this.grupodisparador = grupodisparador;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public FormasDtos getFormadto() {
        return formadto;
    }

    public void setFormadto(FormasDtos formadto) {
        this.formadto = formadto;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
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
        if (!(object instanceof DetallesFormasDtos)) {
            return false;
        }
        DetallesFormasDtos other = (DetallesFormasDtos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesFormasDtos[ secuencia=" + secuencia + " ]";
    }
    
}
