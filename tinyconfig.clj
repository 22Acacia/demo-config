{:config    {:remote-composer-classpath "/usr/local/lib/angleddream-bundled-0.1-ALPHA.jar"
             :remote-libs-path          "/usr/local/lib"
             :sink-resource-version "1"
             :source-resource-version "1"
             :error-buckets             false
             :system-jar-info {:angleddream {:name "angleddream-bundled-0.1-ALPHA.jar"
                                             :pail "build-artifacts-public-eu"
                                             :key  "angleddream"}
                               :sossity     {:name "sossity-0.1.0-SNAPSHOT-standalone.jar"
                                             :pail "build-artifacts-public-eu"
                                             :key  "sossity"}}}
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
 :pipelines {"testpipeline"
             {:transform-jar "identitypipeline-0.1-ALPHA.jar"
              :pail "build-artifacts-public-eu"
              :key "identitypipeline"}
             "orionidentitypipe"
             {:transform-jar  "identitypipeline-0.1-ALPHA.jar"
              :pail "build-artifacts-public-eu"
              :key "identitypipeline"}
             "orionbqfilter"
             {:transform-jar "orion-transform-0.1-ALPHA.jar"
              :pail "build-artifacts-public-eu"
              :key "orion-transform"}}
 :sources   {"testendpoint" {:type "kub"}
             "orion"      {:type "kub"}}
 :sinks     {"testsink"                 {:type "gcs" :bucket "testsink-bucket"}
             "orionsink"                {:type "gcs" :bucket "orionsinkbucket"}
             "orionbq"                  {:type "bq" :bigQueryDataset "hx_orion_staging" :bigQueryTable "hx_orion" :bigQuerySchema "/home/ubuntu/demo-config/orion.json"}}
 :edges     [{:origin "testendpoint" :targets ["testpipeline"]}
             {:origin "testpipeline" :targets ["testsink"]}
             {:origin "orion" :targets ["orionbqfilter" "orionidentitypipe"]}
             {:origin "orionidentitypipe" :targets ["orionsink"]}
             {:origin "orionbqfilter" :targets ["orionbq"]}]}
