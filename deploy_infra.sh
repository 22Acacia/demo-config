echo "go to repo working directory"
cd $HOME/$CIRCLE_PROJECT_REPONAME

echo "create terraform file"
java -jar /usr/local/lib/sossity-standalone.jar -c "$HOME/$CIRCLE_PROJECT_REPONAME/tinyconfig.clj" -o "$HOME/$CIRCLE_PROJECT_REPONAME/tinyconfig-terraform.tf.json"
ret_var=$?

if [ $ret_var -ne 0 ]; then
  echo "failed to generate terraform config.  Abort mission!"
  exit $ret_var
fi

#  it has a bug, null is the value of the subscribers.  this breaks things so remove it
#  this bug should be going away soon so we're not super worried about it
sed -i /null/d tinyconfig-terraform.tf.json


echo "regester for remote state management"
terraform remote config -backend=atlas -backend-config="name=coffeepac/demo-config" -backend-config="access_token=$ATLAS_TOKEN"
ret_var=$?

if [ $ret_var -ne 0 ]; then
  echo "Failed to register remote state.  Abort mission!"
  exit $ret_var
fi

echo "create the account file"
echo $GOOGLE_CREDENTIALS > account.json  #  assume this works
export GOOGLE_APPLICATION_CREDENTIALS=$HOME/$CIRCLE_PROJECT_REPONAME/account.json  #  assume this works

#  da plan!
cat <(echo "") | terraform plan
ret_var=$?

if [ $ret_var -eq 0 ]; then
  #  da execution
  cat <(echo "") | terraform apply
  ret_var=$?
fi

echo "always save the state no matter what happened above"
terraform remote push
tf_state_push_ret_var=$?

echo "and save the terraform file as well"
sudo /opt/google-cloud-sdk/bin/gsutil cp tinyconfig-terraform.tf.json gs://build-artifacts-public-eu/tinyconfig-terraform.tf.json
tf_conf_push_ret_var=$?

exit $(($ret_var || $$ $tf_state_push_ret_var || $tf_conf_push_ret_var ))


