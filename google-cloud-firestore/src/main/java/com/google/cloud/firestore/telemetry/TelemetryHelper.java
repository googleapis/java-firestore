package com.google.cloud.firestore.telemetry;

import static com.google.cloud.firestore.telemetry.TelemetryConstants.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.View;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class TelemetryHelper {

  private TelemetryHelper() {} // Private constructor to prevent instantiation

  private static String CLIENT_UID;

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();
    gaxMetrics.forEach(metric -> defineView(views, metric, GAX_METER_NAME));
    firestoreMetrics.forEach(metric -> defineView(views, metric, FIRESTORE_METER_NAME));
    return views.build();
  }

  public static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap, String id, String meter) {
    InstrumentSelector selector =
        InstrumentSelector.builder().setMeterName(meter).setName(METRIC_PREFIX + "/" + id).build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(
                COMMON_ATTRIBUTES.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .build();
    View view = View.builder().setAttributeFilter(attributesFilter).build();

    viewMap.put(selector, view);
  }

  public static String getClientUid() {
    if (CLIENT_UID == null) {
      CLIENT_UID = generateClientUid();
    }
    return CLIENT_UID;
  }

  private static String generateClientUid() {
    String identifier = UUID.randomUUID().toString();
    String pid = getProcessId();
    String hostname = getHostName();
    return String.format("%s@%s@%s", identifier, pid, hostname);
  }

  private static String getHostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "localhost";
    }
  }

  private static String getProcessId() {
    try {
      // Check if Java 9+ and ProcessHandle class is available
      Class<?> processHandleClass = Class.forName("java.lang.ProcessHandle");
      Method currentMethod = processHandleClass.getMethod("current");
      Object processHandleInstance = currentMethod.invoke(null);
      Method pidMethod = processHandleClass.getMethod("pid");
      long pid = (long) pidMethod.invoke(processHandleInstance);
      return Long.toString(pid);
    } catch (Exception e) {
      // Fallback to Java 8 method
      final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
      if (jvmName != null && jvmName.contains("@")) {
        return jvmName.split("@")[0];
      } else {
        return "unknown";
      }
    }
  }
}
