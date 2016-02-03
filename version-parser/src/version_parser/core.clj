(ns version-parser.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import [java.io PushbackReader]))

;; read config into map
(def conf (with-open [r (clojure.java.io/reader "../tinyconfig.clj")] 
  (read (PushbackReader. r))))

(defn -main
  [& args]
  
  ; clear out the file before writing to it
  (spit "../jar-versions" "")
  
  (def pipelines (:pipelines conf))
  
  ; for each pipeline, create <key>/jarname jarname pairs and write to file
  (doseq [[k v]pipelines] 
    (spit "../jar-versions" 
      (clojure.string/join " " 
        [(clojure.string/join "/" [(:bucket v) (:key v) (:transform-jar v)]) (:transform-jar v) "\n"]) :append true))


  ; find the non-pipeline jars
  (def otherjars (:system-jar-info (:config conf)))
  
  (doseq [[k v]otherjars]
    (spit "../jar-versions"
      (clojure.string/join " "
        [(clojure.string/join "/" [(:bucket v) (:key v) (:name v)]) (:name v) "\n"]) :append true)))