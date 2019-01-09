(defproject lambda/datalog "0.1.0-SNAPSHOT"
  :description "Intro to datalog"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [datascript "0.17.1"]]
  :main ^:skip-aot datalog.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
