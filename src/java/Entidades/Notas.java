/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "notas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notas.findAll", query = "SELECT n FROM Notas n")
    , @NamedQuery(name = "Notas.findById", query = "SELECT n FROM Notas n WHERE n.id = :id")
    , @NamedQuery(name = "Notas.findByNota", query = "SELECT n FROM Notas n WHERE n.nota = :nota")
    , @NamedQuery(name = "Notas.findByDescripcion", query = "SELECT n FROM Notas n WHERE n.descripcion = :descripcion")})
public class Notas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nota")
    private double nota;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "idmatriculaclase", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Matriculaclase idmatriculaclase;
    @JoinColumn(name = "idperiodo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Periodo idperiodo;
    @JoinColumn(name = "idsubcalificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subcalificacion idsubcalificacion;

    public Notas() {
    }

    public Notas(Integer id) {
        this.id = id;
    }

    public Notas(Integer id, double nota, String descripcion) {
        this.id = id;
        this.nota = nota;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Matriculaclase getIdmatriculaclase() {
        return idmatriculaclase;
    }

    public void setIdmatriculaclase(Matriculaclase idmatriculaclase) {
        this.idmatriculaclase = idmatriculaclase;
    }

    public Periodo getIdperiodo() {
        return idperiodo;
    }

    public void setIdperiodo(Periodo idperiodo) {
        this.idperiodo = idperiodo;
    }

    public Subcalificacion getIdsubcalificacion() {
        return idsubcalificacion;
    }

    public void setIdsubcalificacion(Subcalificacion idsubcalificacion) {
        this.idsubcalificacion = idsubcalificacion;
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
        if (!(object instanceof Notas)) {
            return false;
        }
        Notas other = (Notas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Notas[ id=" + id + " ]";
    }
    
}
