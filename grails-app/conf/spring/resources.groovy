import com.mchange.v2.c3p0.ComboPooledDataSource
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

// Place your Spring DSL code here
beans = {

    /**
     * Use c3p0 to prevent stale/closed DB connections and "broken pipe" errors.
     *
     * http://sourceforge.net/projects/c3p0/
     * http://www.softwareinsane.com/2010/02/broken-pipe-grails-hibernate-spring.html
     */
    dataSource(ComboPooledDataSource) { bean ->
        bean.destroyMethod = 'close'

        // Re-use the connection settings from DataSource.groovy.
        user = CH.config.dataSource.username
        password = CH.config.dataSource.password
        driverClass = CH.config.dataSource.driverClassName
        jdbcUrl = CH.config.dataSource.url

        // Force connections to renew after 4 hours.
        maxConnectionAge = 4 * 60 * 60

        // Eliminate idle connections after 30 minutes.
        maxIdleTimeExcessConnections = 30 * 60
    }


    ldapUserDetailsMapper(kangaroo.AcUserContextMapper) {}


}
