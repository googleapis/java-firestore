package com.google.cloud.firestore;

/** Controls the append behaviour of fields. */
public interface AppendOptions {
    /** Returns whether a path should be split up or not. */
    boolean splitPath();

    /** Default behaviour of appending paths. */
    final class DefaultAppendOptions implements AppendOptions {
        @Override
        public boolean splitPath() {
            return true;
        }
    }
}
