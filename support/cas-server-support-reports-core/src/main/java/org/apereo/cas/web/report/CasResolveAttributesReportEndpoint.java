package org.apereo.cas.web.report;

import org.apereo.cas.authentication.credential.BasicIdentifiableCredential;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.BaseCasActuatorEndpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.HashMap;
import java.util.Map;

/**
 * This is {@link CasResolveAttributesReportEndpoint}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Endpoint(id = "resolveAttributes", enableByDefault = false)
public class CasResolveAttributesReportEndpoint extends BaseCasActuatorEndpoint {
    private final ObjectProvider<PrincipalResolver> defaultPrincipalResolver;

    public CasResolveAttributesReportEndpoint(
        final CasConfigurationProperties casProperties,
        final ObjectProvider<PrincipalResolver> defaultPrincipalResolver) {
        super(casProperties);
        this.defaultPrincipalResolver = defaultPrincipalResolver;
    }


    /**
     * Resolve principal attributes map.
     *
     * @param uid the uid
     * @return the map
     */
    @ReadOperation
    @Operation(summary = "Resolve principal attributes for user", parameters = @Parameter(name = "uid", required = true))
    public Map<String, Object> resolvePrincipalAttributes(
        @Selector
        final String uid) {
        val p = defaultPrincipalResolver.getObject().resolve(new BasicIdentifiableCredential(uid));
        val map = new HashMap<String, Object>();
        map.put("uid", p.getId());
        map.put("attributes", p.getAttributes());
        return map;
    }
}
