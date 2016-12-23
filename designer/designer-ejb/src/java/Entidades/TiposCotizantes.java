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
@Table(name = "TIPOSCOTIZANTES")
public class TiposCotizantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 1)
    @Column(name = "COTIZAPENSION")
    private String cotizapension;
    @Size(max = 1)
    @Column(name = "COTIZASALUD")
    private String cotizasalud;
    @Size(max = 1)
    @Column(name = "COTIZARIESGO")
    private String cotizariesgo;
    @Size(max = 1)
    @Column(name = "COTIZAPARAFISCAL")
    private String cotizaparafiscal;
    @Size(max = 1)
    @Column(name = "COTIZAESAP")
    private String cotizaesap;
    @Size(max = 1)
    @Column(name = "COTIZAMEN")
    private String cotizamen;
    @Column(name = "CODIGOALTERNATIVO")
    private BigInteger codigoalternativo;
    @Column(name = "SUBTIPOCOTIZANTE")
    private BigInteger subtipocotizante;
    @Size(max = 1)
    @Column(name = "EXTRANJERO")
    private String extranjero;
    @Transient
    private boolean cotizapensionBool;
    @Transient
    private boolean cotizasaludBool;
    @Transient
    private boolean cotizariesgoBool;
    @Transient
    private boolean cotizaparafiscalBool;
    @Transient
    private boolean cotizaesapBool;
    @Transient
    private boolean cotizamenBool;
    @Transient
    private boolean codigoalternativoBool;
    @Transient
    private boolean extranjeroBool;
    @Transient
    private String estadoSubTipoCotizante;

    public TiposCotizantes() {
    }

    public TiposCotizantes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposCotizantes(BigInteger secuencia, BigInteger codigo, String descripcion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigo() {
        if (codigo == null) {
            codigo = BigInteger.valueOf(0);
        }
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        if (descripcion != null) {
            descripcion = descripcion.toUpperCase();
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCotizapension() {
        if (cotizapension == null) {
            cotizapension = "N";
        }
        return cotizapension;
    }

    public void setCotizapension(String cotizapension) {
        this.cotizapension = cotizapension;
    }

    public String getCotizasalud() {
        if (cotizasalud == null) {
            cotizasalud = "N";
        }
        return cotizasalud;
    }

    public void setCotizasalud(String cotizasalud) {
        this.cotizasalud = cotizasalud;
    }

    public String getCotizariesgo() {
        if (cotizariesgo == null) {
            cotizariesgo = "N";
        }
        return cotizariesgo;
    }

    public void setCotizariesgo(String cotizariesgo) {
        this.cotizariesgo = cotizariesgo;
    }

    public String getCotizaparafiscal() {
        if (cotizaparafiscal == null) {
            cotizaparafiscal = "N";
        }
        return cotizaparafiscal;
    }

    public void setCotizaparafiscal(String cotizaparafiscal) {
        this.cotizaparafiscal = cotizaparafiscal;
    }

    public String getCotizaesap() {
        if (cotizaesap == null) {
            cotizaesap = "N";
        }
        return cotizaesap;
    }

    public void setCotizaesap(String cotizaesap) {
        this.cotizaesap = cotizaesap;
    }

    public String getCotizamen() {
        if (cotizamen == null) {
            cotizamen = "N";
        }
        return cotizamen;
    }

    public void setCotizamen(String cotizamen) {
        this.cotizamen = cotizamen;
    }

    public BigInteger getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(BigInteger codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public BigInteger getSubtipocotizante() {
        return subtipocotizante;
    }

    public void setSubtipocotizante(BigInteger subtipocotizante) {
        this.subtipocotizante = subtipocotizante;
    }

    public boolean isCodigoalternativoBool() {
        return codigoalternativoBool;
    }

    public void setCodigoalternativoBool(boolean codigoalternativoBool) {
        this.codigoalternativoBool = codigoalternativoBool;
    }

    public String getExtranjero() {
        return extranjero;
    }

    public void setExtranjero(String extranjero) {
        this.extranjero = extranjero;
    }

    public boolean isCotizapensionBool() {
        getCotizapension();
        if (cotizapension == null || cotizapension.equalsIgnoreCase("N")) {
            cotizapensionBool = false;
        } else {
            cotizapensionBool = true;
        }
        return cotizapensionBool;
    }

    public void setCotizapensionBool(boolean cotizapensionBool) {
        if (cotizapensionBool == true) {
            cotizapension = "S";
        } else {
            cotizapension = "N";
        }
        this.cotizapensionBool = cotizapensionBool;
    }

    public boolean isCotizasaludBool() {
        getCotizasalud();
        if (cotizasalud != null) {
            if (cotizasalud.equals("S")) {
                cotizasaludBool = true;
            } else {
                cotizasaludBool = false;
            }
        } else {
            cotizasaludBool = false;
        }
        return cotizasaludBool;
    }

    public void setCotizasaludBool(boolean cotizasaludBool) {
        if (cotizasaludBool == true) {
            cotizasalud = "S";
        } else {
            cotizasalud = "N";
        }
        this.cotizasaludBool = cotizasaludBool;
    }

    public boolean isCotizariesgoBool() {
        getCotizariesgo();
        if (cotizariesgo != null) {
            if (cotizariesgo.equals("S")) {
                cotizariesgoBool = true;
            } else {
                cotizariesgoBool = false;
            }
        } else {
            cotizariesgoBool = false;
        }

        return cotizariesgoBool;
    }

    public void setCotizariesgoBool(boolean cotizariesgoBool) {
        if (cotizariesgoBool == true) {
            cotizariesgo = "S";
        } else {
            cotizariesgo = "N";
        }
        this.cotizariesgoBool = cotizariesgoBool;
    }

    public boolean isCotizaparafiscalBool() {
        getCotizaparafiscal();
        if (cotizaparafiscal != null) {
            if (cotizaparafiscal.equals("S")) {
                cotizaparafiscalBool = true;
            } else {
                cotizaparafiscalBool = false;
            }
        } else {
            cotizaparafiscalBool = false;
        }
        return cotizaparafiscalBool;
    }

    public void setCotizaparafiscalBool(boolean cotizaparafiscalBool) {
        if (cotizaparafiscalBool == true) {
            cotizaparafiscal = "S";
        } else {
            cotizaparafiscal = "N";
        }
        this.cotizaparafiscalBool = cotizaparafiscalBool;
    }

    public boolean isCotizaesapBool() {
        getCotizaesap();
        if (cotizaesap != null) {
            if (cotizaesap.equals("S")) {
                cotizaesapBool = true;
            } else {
                cotizaesapBool = false;
            }
        } else {
            cotizaesapBool = false;
        }
        return cotizaesapBool;
    }

    public void setCotizaesapBool(boolean cotizaesapBool) {
        if (cotizaesapBool == true) {
            cotizaesap = "S";
        } else {
            cotizaesap = "N";
        }
        this.cotizaesapBool = cotizaesapBool;
    }

    public boolean isCotizamenBool() {
        getCotizamen();
        if (cotizamen != null) {
            if (cotizamen.equals("S")) {
                cotizamenBool = true;
            } else {
                cotizamenBool = false;
            }
        } else {
            cotizamenBool = false;
        }
        return cotizamenBool;
    }

    public void setCotizamenBool(boolean cotizamenBool) {
        if (cotizamenBool == true) {
            cotizamen = "S";
        } else {
            cotizamen = "N";
        }
        this.cotizamenBool = cotizamenBool;
    }

    public boolean isExtranjeroBool() {
        getExtranjero();
        if (extranjero != null) {
            if (extranjero.equals("S")) {
                extranjeroBool = true;
            } else {
                extranjeroBool = false;
            }
        } else {
            extranjeroBool = false;
        }
        return extranjeroBool;
    }

    public String getEstadoSubTipoCotizante() {

        getSubtipocotizante();
        if (subtipocotizante == null) {
            estadoSubTipoCotizante = "";
        } else {
            if (subtipocotizante.intValue() == 0) {
                estadoSubTipoCotizante = "0. Valor desconocido";
            }
            if (subtipocotizante.intValue() == 1) {
                estadoSubTipoCotizante = "1. Dependiente pensionado por vejez activo";
            }
            if (subtipocotizante.intValue() == 2) {
                estadoSubTipoCotizante = "2. Independiente pensionado por vejez activo";
            }
            if (subtipocotizante.intValue() == 3) {
                estadoSubTipoCotizante = "3. Cotizante no obligado a cotización a pensiones por edad";
            }
            if (subtipocotizante.intValue() == 4) {
                estadoSubTipoCotizante = "4. Cotizante con requisitos cumplidos para pensión";
            }
            if (subtipocotizante.intValue() == 5) {
                estadoSubTipoCotizante = "5. Cotizante a quien se le ha reconocido indemnización sustitutiva o devolución de saldos";
            }
            if (subtipocotizante.intValue() == 6) {
                estadoSubTipoCotizante = "6. Cotizante perteneciente a un régimen exceptuado de pensiones o a entidades autorizadas para recibir aportes exclusivamente de un grupo de sus propios trabajadores";
            }
        }
        return estadoSubTipoCotizante;
    }

    public void setEstadoSubTipoCotizante(String estadoSubTipoCotizante) {

        this.estadoSubTipoCotizante = estadoSubTipoCotizante;
//        if (estadoSubTipoCotizante.equals("null")) {
//            setSubtipocotizante(null);
//        } else if (estadoSubTipoCotizante.equals("1")) {
//            setSubtipocotizante(new Short("1"));
//        } else if (estadoSubTipoCotizante.equals("2")) {
//            setSubtipocotizante(new Short("2"));
//        } else if (estadoSubTipoCotizante.equals("3")) {
//            setSubtipocotizante(new Short("3"));
//        } else if (estadoSubTipoCotizante.equals("4")) {
//            setSubtipocotizante(new Short("4"));
//        } else if (estadoSubTipoCotizante.equals("5")) {
//            setSubtipocotizante(new Short("5"));
//        } else if (estadoSubTipoCotizante.equals("6")) {
//            setSubtipocotizante(new Short("6"));
//        }
    }

    public void setExtranjeroBool(boolean extranjeroBool) {
        if (extranjeroBool == true) {
            extranjero = "S";
        } else {
            extranjero = "N";
        }
        this.extranjeroBool = extranjeroBool;
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
        if (!(object instanceof TiposCotizantes)) {
            return false;
        }
        TiposCotizantes other = (TiposCotizantes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposcotizantes[ secuencia=" + secuencia + " ]";
    }
}
