package py.com.abf.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, py.com.abf.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, py.com.abf.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, py.com.abf.domain.User.class.getName());
            createCache(cm, py.com.abf.domain.Authority.class.getName());
            createCache(cm, py.com.abf.domain.User.class.getName() + ".authorities");
            createCache(cm, py.com.abf.domain.Temas.class.getName());
            createCache(cm, py.com.abf.domain.Temas.class.getName() + ".registroClases");
            createCache(cm, py.com.abf.domain.RegistroClases.class.getName());
            createCache(cm, py.com.abf.domain.TiposDocumentos.class.getName());
            createCache(cm, py.com.abf.domain.TiposDocumentos.class.getName() + ".alumnos");
            createCache(cm, py.com.abf.domain.TiposDocumentos.class.getName() + ".funcionarios");
            createCache(cm, py.com.abf.domain.Alumnos.class.getName());
            createCache(cm, py.com.abf.domain.Alumnos.class.getName() + ".matriculas");
            createCache(cm, py.com.abf.domain.Alumnos.class.getName() + ".prestamos");
            createCache(cm, py.com.abf.domain.Funcionarios.class.getName());
            createCache(cm, py.com.abf.domain.Funcionarios.class.getName() + ".pagos");
            createCache(cm, py.com.abf.domain.Funcionarios.class.getName() + ".registroClases");
            createCache(cm, py.com.abf.domain.Facturas.class.getName());
            createCache(cm, py.com.abf.domain.Facturas.class.getName() + ".facturaDetalles");
            createCache(cm, py.com.abf.domain.Pagos.class.getName());
            createCache(cm, py.com.abf.domain.Productos.class.getName());
            createCache(cm, py.com.abf.domain.Productos.class.getName() + ".pagos");
            createCache(cm, py.com.abf.domain.Productos.class.getName() + ".facturaDetalles");
            createCache(cm, py.com.abf.domain.FacturaDetalle.class.getName());
            createCache(cm, py.com.abf.domain.Materiales.class.getName());
            createCache(cm, py.com.abf.domain.Materiales.class.getName() + ".prestamos");
            createCache(cm, py.com.abf.domain.Prestamos.class.getName());
            createCache(cm, py.com.abf.domain.Matricula.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
