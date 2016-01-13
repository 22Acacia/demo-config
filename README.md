# demo-config
demonstration configuration for end-to-end build/deploy system.

This repo houses four files:
- tinyconfig.clj
- tinyconfig-terraform.tj.json
- circle.yml
- jar-specs.txt

tinyconfig.clj describes an infrastructure for executing dataprocessing
flows.  this file drives sossity, the 22acacia application that drives
terraform which handles interacting with various cloud providers to 
make things happen.

only tinyconfig.clj should be edited by human hands.  tinyconfig-
terrform.tj.json is machine created.

circle.yml tells circle ci what to do and acts as the final orchestrator
for deploying the infrastructure described in tinyconfig.clj and implied
in the storage bucket build-artifacts-public-eu.

jar-specs.txt tells this project which versions of the input jars to 
deploy.  this file has three columns.  the first is the app name which
doubles as the root of the key to use to download all artifacts from
google storage.  the next column is a regular expression, as used by
egrep, and it is applied to the VERSIONS.txt file which is present in
the root key in google storage (there is already code in this repo to
download this fie but make sure you do if you are operating outside of
this project).  this regular expression should return only one record 
from the VERSIONS.txt file.  that record is the final  segment to the 
google storage key for which jar to download.  the final column is the 
name on disk to save the chosen jar file too.

#  accessing remote state locally
see coffeepac for a valid ATLAS_TOKEN, or change who owns the atlas 
account that this state is stored in.  Then run the following in an
empty directory:

terraform remote config -backend=atlas -backend-config="name=coffeepac/demo-config" -backend-config="access_token=$ATLAS_TOKEN"

NOTE:  if you do this in a directory where you already have terraform
state files and then you push your local state you will overwrite the 
previous state.  So please, use an empty directory
