%Commands file, defining the commands in sequence to execute to configure the system

%Step 1 Install Leiningen
%Assumes Java is installed on the machine
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
cp lein ~/bin/
chmod a+x ~/bin/lein
~/bin/lein

%Download the project from GitHub
%Since this is untested, additional authorizaton to access GitHub may be required
%Or needs a workaround of uploading from the client system
cd ~
wget https://github.com/ThoughtWorksInc/infra-problem/archive/master.zip
tar -xvf master.zip

%unzip the downloaded archive
cd infra-problem-master
chmod a+x Makefile

%install the missing libraries from make file
make libs
make clean all

%Start the required apps
%Make sure to start in background process, or the apps may block the current shell session
python front-end/public serve.py &

%Set environmental variable before starting the app
export $APP_PORT=8181

%Run the services
java -jar quotes.jar &

java -jar newsfeed.jar &

java -jar front-end.jar &