package kangaroo

import org.apache.commons.lang.builder.HashCodeBuilder

class AcUserAcRole implements Serializable {

    AcUser acUser
    AcRole acRole

    boolean equals(other) {
        if (!(other instanceof AcUserAcRole)) {
            return false
        }

        other.acUser?.id == acUser?.id &&
                other.acRole?.id == acRole?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (acUser) builder.append(acUser.id)
        if (acRole) builder.append(acRole.id)
        builder.toHashCode()
    }

    static AcUserAcRole get(long acUserId, long acRoleId) {
        find 'from AcUserAcRole where acUser.id=:acUserId and acRole.id=:acRoleId',
                [acUserId: acUserId, acRoleId: acRoleId]
    }

    static AcUserAcRole create(AcUser acUser, AcRole acRole, boolean flush = false) {
        new AcUserAcRole(acUser: acUser, acRole: acRole).save(flush: flush, insert: true)
    }

    static boolean remove(AcUser acUser, AcRole acRole, boolean flush = false) {
        AcUserAcRole instance = AcUserAcRole.findByAcUserAndAcRole(acUser, acRole)
        if (!instance) {
            return false
        }

        instance.delete(flush: flush)
        true
    }

    static void removeAll(AcUser acUser) {
        executeUpdate 'DELETE FROM AcUserAcRole WHERE acUser=:acUser', [acUser: acUser]
    }

    static void removeAll(AcRole acRole) {
        executeUpdate 'DELETE FROM AcUserAcRole WHERE acRole=:acRole', [acRole: acRole]
    }

    static mapping = {
        id composite: ['acRole', 'acUser']
        version false
    }
}
