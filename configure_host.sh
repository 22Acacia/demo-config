echo "download and install terraform and custom provider"
curl https://releases.hashicorp.com/terraform/0.6.8/terraform_0.6.8_linux_amd64.zip -o $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip
sudo unzip $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip -d /usr/local/bin/

echo "ensure gsutil is installed"
sudo /opt/google-cloud-sdk/bin/gcloud components install gsutil

echo "create the account file"
echo $GOOGLE_CREDENTIALS > $HOME/$CIRCLE_PROJECT_REPONAME/account.json
export GOOGLE_APPLICATION_CREDENTIALS=$HOME/$CIRCLE_PROJECT_REPONAME/account.json

echo "auth the local sudo gcloud"
sudo /opt/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file $HOME/$CIRCLE_PROJECT_REPONAME/account.json

echo "download tip google provider - we need code that's only in tip right now"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://build-artifacts-public-eu/terraform-provider-google /usr/local/bin/terraform-provider-google
chmod +x /usr/local/bin/terraform-provider-google

echo "download googlecli provider"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://build-artifacts-public-eu/terraform-provider-googlecli /usr/local/bin/terraform-provider-googlecli
chmod +x /usr/local/bin/terraform-provider-googlecli

echo "download sossity and deployable jars"
sudo /opt/google-cloud-sdk/bin/gsutil cp gs://build-artifacts-public-eu/*.jar /usr/local/lib/

echo "ensure kubectl is installed and that dataflow commands for gcloud are installed"
which kubectl
if [ $? -eq 1 ]; then
  sudo /opt/google-cloud-sdk/bin/gcloud components install kubectl -q
fi
sudo /opt/google-cloud-sdk/bin/gcloud alpha -h < /bin/echo  #  alpha/beta can be installed via components
sudo /opt/google-cloud-sdk/bin/gcloud beta -h < /bin/echo   #  sort that out later
chown ubuntu:ubuntu -R ~/.config/

