package org.apereo.cas.support.saml.util.credential;

import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.opensaml.security.credential.BasicCredential;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This is {@link BasicResourceCredentialFactoryBeanTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@Tag("SAML")
public class BasicResourceCredentialFactoryBeanTests {
    @Test
    public void verifyKeys() throws Exception {
        val factory = new BasicResourceCredentialFactoryBean();
        assertSame(BasicCredential.class, factory.getObjectType());
        assertTrue(factory.isSingleton());
        factory.setUsageType("UNSPECIFIED");
        factory.setPrivateKeyInfo(new ClassPathResource("keys/private.pem"));
        factory.setPublicKeyInfo(new ClassPathResource("keys/public.pem"));
        assertNotNull(factory.getObject());
    }

    @Test
    public void verifyMissingPrivKeys() {
        val factory = new BasicResourceCredentialFactoryBean();
        factory.setPrivateKeyInfo(new ClassPathResource("keys/badprivate.pem"));
        assertThrows(BeanCreationException.class, factory::getObject);
    }

    @Test
    public void verifyMissingSecretKeys() {
        val factory = new BasicResourceCredentialFactoryBean();
        factory.setSecretKeyInfo(new ClassPathResource("keys/badsec.pem"));
        assertThrows(BeanCreationException.class, factory::getObject);
    }

    @Test
    public void verifyNoKeys() {
        val factory = new BasicResourceCredentialFactoryBean();
        assertThrows(BeanCreationException.class, factory::getObject);
    }

    @Test
    public void verifyNoKeyInfo() {
        val factory = new BasicResourceCredentialFactoryBean();
        factory.setPrivateKeyInfo(null);
        assertThrows(FatalBeanException.class, factory::getObject);
        factory.setPublicKeyInfo(mock(Resource.class));
        assertThrows(FatalBeanException.class, factory::getPublicKey);
    }

    @Test
    public void verifyMismatchedKeys() {
        val factory = new BasicResourceCredentialFactoryBean();
        factory.setPrivateKeyInfo(new ClassPathResource("keys/private.pem"));
        factory.setPublicKeyInfo(new ClassPathResource("keys/badpublic.key"));
        assertThrows(BeanCreationException.class, factory::getObject);
    }

    @Test
    public void verifyPublicKeys() throws Exception {
        val factory = new BasicResourceCredentialFactoryBean();
        assertSame(BasicCredential.class, factory.getObjectType());
        assertTrue(factory.isSingleton());
        factory.setUsageType("UNSPECIFIED");
        factory.setPublicKeyInfo(new ClassPathResource("keys/public.pem"));
        assertNotNull(factory.getObject());
    }

    @Test
    public void verifySecretKeys() throws Exception {
        val factory = new BasicResourceCredentialFactoryBean();
        factory.setUsageType("UNSPECIFIED");
        factory.setSecretKeyInfo(new ClassPathResource("keys/secret.key"));
        factory.setSecretKeyAlgorithm("RSA");

        factory.setSecretKeyEncoding(BasicResourceCredentialFactoryBean.SecretKeyEncoding.BINARY);
        assertNotNull(factory.getObject());

        factory.setSecretKeyEncoding(BasicResourceCredentialFactoryBean.SecretKeyEncoding.HEX);
        assertThrows(BeanCreationException.class, factory::getObject);

        factory.setSecretKeyEncoding(BasicResourceCredentialFactoryBean.SecretKeyEncoding.BASE64);
        assertNotNull(factory.getObject());
    }
}
