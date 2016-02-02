set -ex

cwd=`pwd`

echo "download and install terraform and custom provider"
curl https://releases.hashicorp.com/terraform/0.6.9/terraform_0.6.9_linux_amd64.zip -o $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip
sudo unzip $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip -d /usr/local/bin/

echo "ensure gsutil is installed"
sudo /opt/google-cloud-sdk/bin/gcloud components install gsutil

echo "create the account file"
echo $GOOGLE_CREDENTIALS > $HOME/$CIRCLE_PROJECT_REPONAME/account.json
export GOOGLE_APPLICATION_CREDENTIALS=$HOME/$CIRCLE_PROJECT_REPONAME/account.json

echo "auth the local sudo gcloud"
sudo /opt/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file $HOME/$CIRCLE_PROJECT_REPONAME/account.json

echo "download googlebigquery provider"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://${GSTORAGE_DEST_BUCKET}/terraform-provider-googlebigquery /usr/local/bin/terraform-provider-googlebigquery
sudo chmod +x /usr/local/bin/terraform-provider-googlebigquery

echo "download googleappengine provider"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://${GSTORAGE_DEST_BUCKET}/terraform-provider-googleappengine /usr/local/bin/terraform-provider-googleappengine
sudo chmod +x /usr/local/bin/terraform-provider-googleappengine

echo "download googlecli provider"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://${GSTORAGE_DEST_BUCKET}/terraform-provider-googlecli /usr/local/bin/terraform-provider-googlecli
sudo chmod +x /usr/local/bin/terraform-provider-googlecli

echo "create specified jars list"
cd version-parser
lein run

echo "download specified jars"
while read jar_spec; do
  jar_spec_arr=($jar_spec)
  sudo /opt/google-cloud-sdk/bin/gsutil cp gs://${GSTORAGE_DEST_BUCKET}/${jar_spec_arr[0]} /usr/local/lib/${jar_spec_arr[1]}
done < ${cwd}/jar-versions
exit 0
echo "ensure kubectl is installed and that dataflow commands for gcloud are installed"
which kubectl
if [ $? -eq 1 ]; then
  sudo /opt/google-cloud-sdk/bin/gcloud components install kubectl -q
fi
sudo /opt/google-cloud-sdk/bin/gcloud alpha -h < /bin/echo  #  alpha/beta can be installed via components
sudo /opt/google-cloud-sdk/bin/gcloud beta -h < /bin/echo   #  sort that out later
sudo chown ubuntu:ubuntu -R ~/.config/

echo "auth the local user gcloud for terraform/cdf needs"
/opt/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file $HOME/$CIRCLE_PROJECT_REPONAME/account.json

