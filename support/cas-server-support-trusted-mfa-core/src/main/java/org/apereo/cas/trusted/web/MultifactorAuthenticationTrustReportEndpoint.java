package org.apereo.cas.trusted.web;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.trusted.authentication.api.MultifactorAuthenticationTrustRecord;
import org.apereo.cas.trusted.authentication.api.MultifactorAuthenticationTrustStorage;
import org.apereo.cas.web.BaseCasActuatorEndpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.http.HttpStatus;

import java.util.Set;

/**
 * This is {@link MultifactorAuthenticationTrustReportEndpoint}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Endpoint(id = "multifactorTrustedDevices", enableByDefault = false)
public class MultifactorAuthenticationTrustReportEndpoint extends BaseCasActuatorEndpoint {
    private final ObjectProvider<MultifactorAuthenticationTrustStorage> mfaTrustEngine;

    public MultifactorAuthenticationTrustReportEndpoint(
        final CasConfigurationProperties casProperties,
        final ObjectProvider<MultifactorAuthenticationTrustStorage> mfaTrustEngine) {
        super(casProperties);
        this.mfaTrustEngine = mfaTrustEngine;
    }

    /**
     * Devices registered and trusted.
     *
     * @return the set
     */
    @ReadOperation
    @Operation(summary = "Get collection of trusted devices")
    public Set<? extends MultifactorAuthenticationTrustRecord> devices() {
        expireRecords();
        return this.mfaTrustEngine.getObject().getAll();
    }

    /**
     * Devices for user.
     *
     * @param username the username
     * @return the set
     */
    @ReadOperation
    @Operation(summary = "Get collection of trusted devices for the user", parameters = @Parameter(name = "username", required = true))
    public Set<? extends MultifactorAuthenticationTrustRecord> devicesForUser(
        @Selector
        final String username) {
        expireRecords();
        return this.mfaTrustEngine.getObject().get(username);
    }

    /**
     * Revoke record and return status.
     *
     * @param key the key
     * @return the integer
     */
    @Operation(summary = "Remove trusted device using its key", parameters = @Parameter(name = "key", required = true))
    @DeleteOperation
    public Integer revoke(
        @Selector
        final String key) {
        this.mfaTrustEngine.getObject().remove(key);
        return HttpStatus.OK.value();
    }

    private void expireRecords() {
        this.mfaTrustEngine.getObject().remove();
    }
}
