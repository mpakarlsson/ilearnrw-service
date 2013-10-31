package com.ilearnrw.usermanager.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Permission {
        
        @Id
        @GeneratedValue
        private Integer id;
        
        private String name;
        
        @OneToMany(cascade=CascadeType.ALL)
        @JoinTable(name="role_permissions",
                joinColumns = {@JoinColumn(name="permissions_id", referencedColumnName="id")},
                inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}
        )
        private Set<Permission> rolePermissions;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Set<Permission> getRolePermissions() {
                return rolePermissions;
        }

        public void setRolePermissions(Set<Permission> rolePermissions) {
                this.rolePermissions = rolePermissions;
        }
        
}