(ns metrics.reporters.ganglia
  "Ganglia reporter interface"
  (:require [metrics.core  :refer [default-registry]]
            [metrics.reporters :as mrep])
  (:import java.util.concurrent.TimeUnit
           [com.codahale.metrics MetricRegistry MetricFilter]
           [com.codahale.metrics.ganglia GangliaReporter GangliaReporter$Builder]))

(defn ^GangliaReporter reporter
  ([ganglia opts]
     (reporter default-registry opts))
  ([^MetricRegistry reg ganglia opts]
     (let [b (GangliaReporter/forRegistry reg)]
       (when-let [^TimeUnit ru (:rate-unit opts)]
         (.convertRatesTo b ru))
       (when-let [^TimeUnit du (:duration-unit opts)]
         (.convertDurationsTo b du))
       (when-let [^MetricFilter f (:filter opts)]
         (.filter b f))
       (.build b ganglia))))

(defn start
  "Report all metrics to ganglia periodically."
  [^GangliaReporter r ^long minutes]
  (mrep/start r minutes))

(defn stop
  "Stops reporting."
  [^GangliaReporter r]
  (mrep/stop r))
