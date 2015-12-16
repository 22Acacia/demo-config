echo "download and install terraform and custom provider"
curl https://releases.hashicorp.com/terraform/0.6.8/terraform_0.6.8_linux_amd64.zip -o $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip
unzip $HOME/$CIRCLE_PROJECT_REPONAME/terraform.zip -d /usr/local/bin/
gsutil cp gs://build-artifacts-public-eu/terraform-provider-googlecli -o /usr/local/bin/terraform-provider-googlecli
chmod +x /usr/local/bin/terraform-provider-googlecli
gsutil cp gs://build-artifacts-public-eu/*.jar -o /usr/local/lib/

echo "ensure kubectl is installed and that dataflow commands for gcloud are installed"
which kubectl
if [ $? -eq 1 ]; then
  /opt/google-cloud-sdk/bin/gcloud components install kubectl -q
fi
/opt/google-cloud-sdk/bin/gcloud alpha -h < /bin/echo  #  alpha/beta can be installed via components
/opt/google-cloud-sdk/bin/gcloud beta -h < /bin/echo   #  sort that out later
chown ubuntu:ubuntu -R ~/.config/

