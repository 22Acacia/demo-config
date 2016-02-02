(defproject version-parser "0.1.0-SNAPSHOT"
  :description "parses tinyconfig.clj and writes out version information for build process"
  :url "http://22acacia.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :main ^:skip-aot version-parser.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
