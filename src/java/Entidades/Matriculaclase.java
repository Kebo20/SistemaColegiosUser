/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "matriculaclase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matriculaclase.findAll", query = "SELECT m FROM Matriculaclase m")
    , @NamedQuery(name = "Matriculaclase.findById", query = "SELECT m FROM Matriculaclase m WHERE m.id = :id")})
public class Matriculaclase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idmatriculaclase")
    private List<Agendaalumno> agendaalumnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idmatriculaclase")
    private List<Notas> notasList;
    @JoinColumn(name = "idclase", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clase idclase;
    @JoinColumn(name = "idmatricula", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Matricula idmatricula;

    public Matriculaclase() {
    }

    public Matriculaclase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<Agendaalumno> getAgendaalumnoList() {
        return agendaalumnoList;
    }

    public void setAgendaalumnoList(List<Agendaalumno> agendaalumnoList) {
        this.agendaalumnoList = agendaalumnoList;
    }

    @XmlTransient
    public List<Notas> getNotasList() {
        return notasList;
    }

    public void setNotasList(List<Notas> notasList) {
        this.notasList = notasList;
    }

    public Clase getIdclase() {
        return idclase;
    }

    public void setIdclase(Clase idclase) {
        this.idclase = idclase;
    }

    public Matricula getIdmatricula() {
        return idmatricula;
    }

    public void setIdmatricula(Matricula idmatricula) {
        this.idmatricula = idmatricula;
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
        if (!(object instanceof Matriculaclase)) {
            return false;
        }
        Matriculaclase other = (Matriculaclase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Matriculaclase[ id=" + id + " ]";
    }
    
}
