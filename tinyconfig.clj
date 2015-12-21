{:cluster   {:name "hxhstack" :initial_node_count 3 :master_auth {:username "hx" :password "hstack"}}
 :opts      {:composer-classpath    ["/usr/local/lib/angleddream-bundled.jar"] ;where all the jar files live. no trailing slash. may be overriden by env var in production? also be sure to build thick jars from angled-dream for deps
             :maxNumWorkers "1" :numWorkers "1" :zone "europe-west1-c" :workerMachineType "n1-standard-1"
             :stagingLocation "gs://hx-test/staging-eu"}
 :provider  {:credentials "${file(\"/home/ubuntu/demo-config/account.json\")}"  :project "hx-test"}
 :pipelines {"pipeline1bts"
             {:transform-graph ["/usr/local/lib/pipeline1.jar"]}
             "pipeline2bts"
             {:transform-graph ["/usr/local/lib/pipeline2.jar"]}
             "pipeline3bts"
             {:transform-graph ["/usr/local/lib/pipeline3.jar"]}
             }
 :sources   {"stream1bts" {:type "kub"}
             "stream2bts" {:type "kub"}
             "orion" {:type "kub"}
             }
 :sinks     {"sink1bts" {:type "gcs" :bucket "sink1-bts-test"}
             "sink2bts" {:type "gcs" :bucket "sink2-bts-test"}
             "sink3bts" {:type "gcs" :bucket "sink3-bts-test"}
             "orionsink" {:type "gcs" :bucket "orionbucket"}
             }
 :edges     [{:origin "stream1bts" :targets ["pipeline1bts"]}
             {:origin "pipeline1bts" :targets ["pipeline2bts" "pipeline3bts"]}
             {:origin "pipeline2bts" :targets ["sink1bts"  "sink3bts"]}
             {:origin "orion" :targets ["orionsink"]}
             {:origin "pipeline3bts" :targets ["sink2bts" #_"sink4bts"]}
             ]}


