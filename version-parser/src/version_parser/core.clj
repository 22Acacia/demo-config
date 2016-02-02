(ns version-parser.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import [java.io PushbackReader]))

;; read config into map
(def conf (with-open [r (clojure.java.io/reader "../tinyconfig.clj")] 
  (read (PushbackReader. r))))

(defn -main
  [& args]
  (def pipelines (:pipelines conf))

  ; clear out the file before writing to it
  (spit "../jar-versions" "")

  ; for each pipeline, create <key>/jarname jarname pairs and write to file
  (doseq [[k v]pipelines] 
    (spit "../jar-versions" 
      (clojure.string/join " " 
        [(clojure.string/join "/" [(:key v) (:transform-jar v)]) (:transform-jar v) "\n"]) :append true)))

