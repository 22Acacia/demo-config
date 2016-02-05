set -x

echo "generate full set of configfiles"
config_list=""
for config in $(ls *.clj); do
  config_list="${config_list},${config}"
done
config_list=${config_list:1:${#config_list}-1}

echo "create terraform file"
java -jar /usr/local/lib/sossity-0.1.0-SNAPSHOT-standalone.jar -c $config_list -o tinyconfig-terraform.tf.json
ret_var=$?

if [ $ret_var -ne 0 ]; then
  echo "failed to generate terraform config.  Abort mission!"
  exit $ret_var
fi

echo "regester for remote state management"
terraform remote config -backend=atlas -backend-config="name=coffeepac/demo-config" -backend-config="access_token=$ATLAS_TOKEN"
ret_var=$?

if [ $ret_var -ne 0 ]; then
  echo "Failed to register remote state.  Abort mission!"
  exit $ret_var
fi


#  da plan!
cat <(echo "") | terraform plan
ret_var=$?

if [ $ret_var -eq 0 ]; then
  #  da execution
  cat <(echo "") | terraform apply
  ret_var=$?
fi

echo "current state of the system"
terraform show

echo "always save the state no matter what happened above"
terraform remote push
tf_state_push_ret_var=$?

echo "and save the terraform file as well"
sudo /opt/google-cloud-sdk/bin/gsutil cp tinyconfig-terraform.tf.json gs://${GSTORAGE_DEST_BUCKET}/tinyconfig-terraform.tf.json
tf_conf_push_ret_var=$?

exit $(($ret_var || $tf_state_push_ret_var || $tf_conf_push_ret_var ))


