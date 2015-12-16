echo "go to repo working directory"
cd $HOME/$CIRCLE_PROJECT_REPONAME

echo "create terraform file"
java -jar /usr/local/lib/sossity-standalone.jar -c "$HOME/$CIRCLE_PROJECT_REPONAME/tinyconfig.clj" -o "$HOME/$CIRCLE_PROJECT_REPONAME/tinyconfig-terraform.tf.json"

#  it has a bug, null is the value of the subscribers.  this breaks things so remove it
sed -i /null/d tinyconfig-terraform.tf.json

echo "regester for remote state management"
terraform remote config -backend=atlas -backend-config="name=coffeepac/demo-config" -backend-config="access_token=$ATLAS_TOKEN"

echo "create the account file"
echo $GOOGLE_CREDENTIALS > account.json
export GOOGLE_APPLICATION_CREDENTIALS=$HOME/$CIRCLE_PROJECT_REPONAME/account.json

#  da plan!
cat <(echo "") | terraform plan

#  da execution
cat <(echo "") | terraform apply

echo "always save the state no matter what happened above"
terraform remote push

echo "and save the terraform file as well"
gsutil tinyconfig-terraform.tf.json gs://build-artifacts-public-eu/tinyconfig-terraform.tf.json
