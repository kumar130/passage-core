package com.nm.common.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kre5335 on 4/17/2017.
 */
@ConfigurationProperties(prefix = "keycloak.auth.token.claims")
public class KeycloakAuthTokenProperties {

    private String groupName = "groups_aad";

    private String roleName = "roles_aad";
    
    private String fieldId  = "fid";
    
    private String workForGroup = "wf_group";

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getWorkForGroup() {
        return workForGroup;
    }

    public void setWorkForGroup(String workForGroup) {
        this.workForGroup = workForGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
