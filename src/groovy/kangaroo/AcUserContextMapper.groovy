package kangaroo

import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.core.userdetails.User

/**
 * An instance of this class is used by the system when the LDAP authentication is successful.   The purpose
 * is to derive a current user.   The spring security core will install the user as the springSecurity.principal.
 * Since we are only extracting data from the institutions Active Directory (LDAP) server, we will create an 
 * instance of an AcUser.   While not persisted, the user object will provide access to the list of granted
 * authorities.  
 *
 * @author mahiggs
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see org.springframework.security.core.GrantedAuthority
 * @see ace.AceUser
 */
class AcUserContextMapper implements UserDetailsContextMapper {

    static final String LDAP_ACID_ATTRIBUTE_NAME = "extensionAttribute3"
    static final String LDAP_ACGROUP_ATTRIBUTE_NAME = "extensionAttribute6"

    /**
     * @return the appropriate user object for the authenticated user.  We assume the LDAP server will provide
     * the Austin College ID number of the user.   We first check to see if our system has special users using
     * this acId.  If so, we set the authorities accordingly (ROLE_VPAA, ROLE_DEAN, ROLE_ADMIN).   Otherwise, we check to see
     * if the user is a known instructor.   If so, the user will be assigned ROLE_FACULTY.
     * <p>
     * NOTE: When this method is called, there is no current hibernate session.   So we need to eagerly prefetch any
     * data we may need.
     */
    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<GrantedAuthority> authorities) {


        println "username: " + username;

        // println "attrs" + ctx.attributes
        // println "auth: " + authorities;

        def fullnameStr = ctx.getStringAttribute("cn")

        String acIdStr = ctx.getStringAttribute(LDAP_ACID_ATTRIBUTE_NAME)
        String acIdStrNoZeros = acIdStr.replaceFirst("^0+", "")        // remove leading zeros from acId number

        def acGrpStr = ctx.getStringAttribute(LDAP_ACGROUP_ATTRIBUTE_NAME)

        def roles = []


        def normalUser = Professor.findById(username)

        if (normalUser) {
            roles = [AcRole.findByAuthority("ROLE_FACULTY")]
        } else {
            roles = [AcRole.findByAuthority("ROLE_GUEST")]
        }

        // inject user with the proper roles based on what we get from the ldap server.
        def usr = new User(username, "xyzzy", true, true, true, true, roles)

        println ">> ${usr}"

        return usr;
    }


    @Override
    public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
        throw new IllegalStateException("Only retrieving data from LDAP is currently supported")
    }


}