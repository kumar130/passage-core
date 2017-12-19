package com.nm.logging;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kre5335 on 5/22/2017.
 */
@ConfigurationProperties(prefix = "uMove.logging.sleuth")
public class SleuthLoggingProperties {

    private String mdcName = "fid";
    private String userJwsClaimName = "fid";

    public String getMdcName() {
        return mdcName;
    }

    public void setMdcName(String mdcName) {
        this.mdcName = mdcName;
    }

    public String getUserJwsClaimName() {
        return userJwsClaimName;
    }

    public void setUserJwsClaimName(String userJwsClaimName) {
        this.userJwsClaimName = userJwsClaimName;
    }
}
