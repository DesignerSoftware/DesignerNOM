package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "RELACIONESINCAPACIDADES")
public class RelacionesIncapacidades implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private short ano;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private short mes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ORIGENINCAPACIDAD")
    private String origenincapacidad;
    @Size(max = 20)
    @Column(name = "FORMARECONOCIMIENTO")
    private String formareconocimiento;
    @Column(name = "DIASCOBRO")
    private Short diascobro;
    @Column(name = "BASICO")
    private BigInteger basico;
    @Column(name = "BASELIQUIDACION")
    private BigInteger baseliquidacion;
    @Column(name = "BASECOBRO")
    private BigInteger basecobro;
    @Column(name = "VALORCOBRO")
    private BigInteger valorcobro;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Terceros terceros;
    @JoinColumn(name = "SOAUSENTISMO", referencedColumnName = "SECUENCIA")
    @OneToOne(optional = false)
    private Soausentismos soausentismos;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresas;

    public RelacionesIncapacidades() {
    }

    public RelacionesIncapacidades(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public RelacionesIncapacidades(BigDecimal secuencia, short ano, short mes, String origenincapacidad) {
        this.secuencia = secuencia;
        this.ano = ano;
        this.mes = mes;
        this.origenincapacidad = origenincapacidad;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public short getAno() {
        return ano;
    }

    public void setAno(short ano) {
        this.ano = ano;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public String getOrigenincapacidad() {
        return origenincapacidad;
    }

    public void setOrigenincapacidad(String origenincapacidad) {
        this.origenincapacidad = origenincapacidad;
    }

    public String getFormareconocimiento() {
        return formareconocimiento;
    }

    public void setFormareconocimiento(String formareconocimiento) {
        this.formareconocimiento = formareconocimiento;
    }

    public Short getDiascobro() {
        return diascobro;
    }

    public void setDiascobro(Short diascobro) {
        this.diascobro = diascobro;
    }

    public BigInteger getBasico() {
        return basico;
    }

    public void setBasico(BigInteger basico) {
        this.basico = basico;
    }

    public BigInteger getBaseliquidacion() {
        return baseliquidacion;
    }

    public void setBaseliquidacion(BigInteger baseliquidacion) {
        this.baseliquidacion = baseliquidacion;
    }

    public BigInteger getBasecobro() {
        return basecobro;
    }

    public void setBasecobro(BigInteger basecobro) {
        this.basecobro = basecobro;
    }

    public BigInteger getValorcobro() {
        return valorcobro;
    }

    public void setValorcobro(BigInteger valorcobro) {
        this.valorcobro = valorcobro;
    }

    public Terceros getTerceros() {
        return terceros;
    }

    public void setTerceros(Terceros terceros) {
        this.terceros = terceros;
    }

    public Soausentismos getSoausentismos() {
        return soausentismos;
    }

    public void setSoausentismos(Soausentismos soausentismos) {
        this.soausentismos = soausentismos;
    }

    public Empresas getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresas empresas) {
        this.empresas = empresas;
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
        if (!(object instanceof RelacionesIncapacidades)) {
            return false;
        }
        RelacionesIncapacidades other = (RelacionesIncapacidades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.RelacionesIncapacidades[ secuencia=" + secuencia + " ]";
    }
    
}
