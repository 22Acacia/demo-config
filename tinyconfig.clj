{:config    {:remote-composer-classpath "/usr/local/lib/angleddream-bundled-0.1-ALPHA.jar"
             :remote-libs-path          "/usr/local/lib"
             :sink-resource-version "1"
             :source-resource-version "1"
             :appengine-gstoragekey "source-1.0-ALPHA"
             :default-sink-docker-image "gcr.io/hx-test/store-sink"
             :error-buckets             false
             :system-jar-info {:angleddream {:name "angleddream-bundled-0.1-ALPHA.jar"
                                             :pail "build-artifacts-public-eu"
                                             :key  "angleddream"}
                               :sossity     {:name "sossity-0.1.0-SNAPSHOT-standalone.jar"
                                             :pail "build-artifacts-public-eu"
                                             :key  "sossity"}}}
 :cluster   {:name        "hxhstack" :initial_node_count 2
             :master_auth {:username "hx" :password "hstack"}
             :node_config {:oauth_scopes ["https://www.googleapis.com/auth/compute"
                                          "https://www.googleapis.com/auth/devstorage.read_only"
                                          "https://www.googleapis.com/auth/logging.write"
                                          "https://www.googleapis.com/auth/monitoring"
                                          "https://www.googleapis.com/auth/cloud-platform"]
                           :machine_type "n1-standard-1"}}
 :opts      {:maxNumWorkers   "1" :numWorkers "1" :zone "europe-west1-c"
             :workerMachineType "n1-standard-1"   :stagingLocation "gs://hx-test/staging-eu"}
 :provider  {:credentials "${file(\"/home/ubuntu/demo-config/account.json\")}"
             :project "hx-test"}
 :pipelines {"testpipeline"
             {:transform-jar "identitypipeline-bundled-0.1-ALPHA.jar"
              :pail "build-artifacts-public-eu"
              :key "sossity-identity-pipeline-java"}
}
 :sources   {"testendpoint" {:type "gae"}
             }
 :sinks     {"testsink"     {:type "gcs" :bucket "testsink-bucket"}
             }
 :edges     [{:origin "testendpoint" :targets ["testpipeline"]}
             {:origin "testpipeline" :targets ["testsink"]}
             ]}
