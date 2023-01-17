package com.google.cloud.firestore.it;

import com.google.cloud.firestore.Firestore;

public class TestHelper {
  /** Returns whether the tests are running against the Firestore emulator. */
  static boolean isRunningAgainstFirestoreEmulator(Firestore firestore) {
    return firestore.getOptions().getHost().startsWith("localhost:");
  }
}
