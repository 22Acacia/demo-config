# demo-config
demonstration configuration for end-to-end build/deploy system.

This repo houses three files:
- tinyconfig.clj
- tinyconfig-terraform.tj.json
- circle.yml

tinyconfig.clj describes an infrastructure for executing dataprocessing
flows.  this file drives sossity, the 22acacia application that drives
terraform which handles interacting with various cloud providers to 
make things happen.

only tinyconfig.clj should be edited by human hands.  tinyconfig-
terrform.tj.json is machine created.

circle.yml tells circle ci what to do and acts as the final orchestrator
for deploying the infrastructure described in tinyconfig.clj and implied
in the storage bucket build-artifacts-public-eu.
