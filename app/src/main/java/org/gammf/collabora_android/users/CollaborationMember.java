package org.gammf.collabora_android.users;

import org.gammf.collabora_android.utils.AccessRight;
import org.gammf.collabora_android.collaborations.general.Collaboration;

/**
 * Represents a member of a {@link Collaboration} with a certain {@link AccessRight}.
 */
public interface CollaborationMember {

    /**
     * @return the username.
     */
    String getUsername();

    /**
     * @return the access right of the user.
     */
    AccessRight getAccessRight();

}
