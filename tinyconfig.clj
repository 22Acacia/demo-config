{:config    {:remote-composer-classpath "/usr/local/lib/angleddream-bundled.jar"
             :local-angleddream-path    "/home/bradford/proj/angled-dream/target/angleddream-bundled-0.1-ALPHA.jar"
             :remote-libs-path          "/usr/local/lib"
             :error-buckets             true}

 :cluster   {:name        "hxhstack" :initial_node_count 4 :master_auth {:username "hx" :password "hstack"}
             :node_config {:oauth_scopes ["https://www.googleapis.com/auth/compute"
                                          "https://www.googleapis.com/auth/devstorage.read_only"
                                          "https://www.googleapis.com/auth/logging.write"
                                          "https://www.googleapis.com/auth/monitoring"
                                          "https://www.googleapis.com/auth/cloud-platform"]
                           :machine_type "n1-standard-1"}}
 :opts      {:maxNumWorkers   "1" :numWorkers "1" :zone "europe-west1-c" :workerMachineType "n1-standard-1"
             :stagingLocation "gs://hx-test/staging-eu"}
 :provider  {:credentials "${file(\"/home/ubuntu/demo-config/account.json\")}" :project "hx-test"}
 :pipelines {"pipeline1bts"
             {:transform-jar "/usr/local/lib/pipeline1.jar"}
             "orionidentityb"
             {:transform-jar "/usr/local/lib/pipeline1.jar"}
             "orionpipe"
             {:transform-jar "/usr/local/lib/orion-transform.jar"}
             "orionresponsys"
             {:transform-jar "/usr/local/lib/orion-responsys.jar"}
             "orionresponsysmailer"
             {:transform-jar "/usr/local/lib/orion-responsys-mailer.jar"}}
 :sources   {"stream1bts" {:type "kub"}
             "orion"      {:type "kub"}}
 :sinks     {"sink1bts"                 {:type "gcs" :bucket "sink1-bts-test-two"}
             "orionsink"                {:type "gcs" :bucket "orionbucket-two"}
             "orionresponsyssink"       {:type "gcs" :bucket "orionresponsys"}
             "orionresponsysmailersink" {:type "gcs" :bucket "orionresponsysmailersink"}
             "orionbq"                  {:type "bq" :bigQueryDataset "hx_orion" :bigQueryTable "hx_test"}}
 :edges     [{:origin "stream1bts" :targets ["pipeline1bts"]}
             {:origin "pipeline1bts" :targets ["sink1bts"]}
             {:origin "orion" :targets ["orionpipe" "orionidentityb"]}
             {:origin "orionidentityb" :targets ["orionsink" "orionresponsys" "orionresponsysmailer"]}
             {:origin "orionresponsys" :targets ["orionresponsyssink"]}
             {:origin "orionresponsysmailer" :targets ["orionresponsysmailersink"]}
             {:origin "orionpipe" :targets ["orionbq"]}]}
