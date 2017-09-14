package org.gammf.collabora_android.notes;

import org.gammf.collabora_android.modules.Module;

/**
 * Represents a {@link Note} belonging to a {@link Module}.
 */
public interface ModuleNote extends Note {

    /**
     * @return the single {@link Note}.
     */
    Note getNote();

    /**
     * @return the identifier of the {@link Module} that contains the {@link Note}.
     */
    String getModuleId();

}
