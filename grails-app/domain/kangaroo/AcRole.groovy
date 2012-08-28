package kangaroo

import org.springframework.security.core.GrantedAuthority;

class AcRole implements Serializable, GrantedAuthority {

    String authority

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
    }


}
