package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "GRUPOSPROVISIONES")
public class GruposProvisiones implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "DIASPROVISION")
    private Short diasprovision;
    @Column(name = "DIASPROVISIONADICIONAL")
    private Short diasprovisionadicional;
    @JoinColumn(name = "PERIODICIDADCORTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Periodicidades periodicidadcorte;
    @JoinColumn(name = "OPERANDOBASELIQUIDACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Operandos operandobaseliquidacion;
    @JoinColumn(name = "GRUPOACUMULADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupoacumulado;
    @JoinColumn(name = "GRUPOPAGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private GruposConceptos grupopago;
    @JoinColumn(name = "GRUPOPROVISION", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private GruposConceptos grupoprovision;
    @JoinColumn(name = "GRUPOAJUSTEPROVISION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupoajusteprovision;
    @JoinColumn(name = "GRUPODEFINITIVA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposConceptos grupodefinitiva;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "CUENTACONSOLIDACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Cuentas cuentaconsolidacion;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @OneToOne
    private Conceptos concepto;

    public GruposProvisiones() {
    }

    public GruposProvisiones(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public GruposProvisiones(BigDecimal secuencia, String nombre) {
        this.secuencia = secuencia;
        this.nombre = nombre;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getDiasprovision() {
        return diasprovision;
    }

    public void setDiasprovision(Short diasprovision) {
        this.diasprovision = diasprovision;
    }

    public Short getDiasprovisionadicional() {
        return diasprovisionadicional;
    }

    public void setDiasprovisionadicional(Short diasprovisionadicional) {
        this.diasprovisionadicional = diasprovisionadicional;
    }

    public Periodicidades getPeriodicidadcorte() {
        return periodicidadcorte;
    }

    public void setPeriodicidadcorte(Periodicidades periodicidadcorte) {
        this.periodicidadcorte = periodicidadcorte;
    }

    public Operandos getOperandobaseliquidacion() {
        return operandobaseliquidacion;
    }

    public void setOperandobaseliquidacion(Operandos operandobaseliquidacion) {
        this.operandobaseliquidacion = operandobaseliquidacion;
    }

    public GruposConceptos getGrupoacumulado() {
        return grupoacumulado;
    }

    public void setGrupoacumulado(GruposConceptos grupoacumulado) {
        this.grupoacumulado = grupoacumulado;
    }

    public GruposConceptos getGrupopago() {
        return grupopago;
    }

    public void setGrupopago(GruposConceptos grupopago) {
        this.grupopago = grupopago;
    }

    public GruposConceptos getGrupoprovision() {
        return grupoprovision;
    }

    public void setGrupoprovision(GruposConceptos grupoprovision) {
        this.grupoprovision = grupoprovision;
    }

    public GruposConceptos getGrupoajusteprovision() {
        return grupoajusteprovision;
    }

    public void setGrupoajusteprovision(GruposConceptos grupoajusteprovision) {
        this.grupoajusteprovision = grupoajusteprovision;
    }

    public GruposConceptos getGrupodefinitiva() {
        return grupodefinitiva;
    }

    public void setGrupodefinitiva(GruposConceptos grupodefinitiva) {
        this.grupodefinitiva = grupodefinitiva;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Cuentas getCuentaconsolidacion() {
        return cuentaconsolidacion;
    }

    public void setCuentaconsolidacion(Cuentas cuentaconsolidacion) {
        this.cuentaconsolidacion = cuentaconsolidacion;
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
        if (!(object instanceof GruposProvisiones)) {
            return false;
        }
        GruposProvisiones other = (GruposProvisiones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.GruposProvisiones[ secuencia=" + secuencia + " ]";
    }
    
}
