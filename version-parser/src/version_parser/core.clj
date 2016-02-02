(ns version-parser.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import [java.io PushbackReader]))

(def conf (with-open [r (clojure.java.io/reader "../tinyconfig.clj")] 
  (read (PushbackReader. r))))

(defn -main
  [& args]
  (def pipelines (:pipelines conf))

  (doseq [[k v]pipelines] 
    (spit "../jar-versions" 
      (clojure.string/join " " 
        [(clojure.string/join "/" [(:key v) (:transform-jar v)]) (:transform-jar v)]))))

