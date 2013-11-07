package com.ilearnrw.usermanager.model;

import javax.validation.constraints.Size;

public class User {
	
        private Integer id;
        
        @Size(min = 4, max = 20)
        public String username;
        
        @Size(min = 4, max = 20)
        public String password;
        
        public boolean enabled;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }
 
        public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
}