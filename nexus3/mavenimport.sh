#!/bin/bash
# copy and run this script to the root of the repository directory containing files
# this script attempts to exclude uploading itself explicitly so the script name is       important
# Get command line params

# put mavenimport.sh in folder '/home/$USER/.m2/repository', and then
# chmod +x mavenimport.sh
# ./mavenimport.sh -u admin -p admin123 -r http://172.20.10.6:8081/repository/my_repo/
```


while getopts ":r:u:p:" opt; do
  case $opt in
    r) REPO_URL="$OPTARG"
    ;;
    u) USERNAME="$OPTARG"
    ;;
    p) PASSWORD="$OPTARG"
    ;;
esac
done

find . -type f -not -path './mavenimport\.sh*' -not -path '*/\.*' -not -path '*/\^archetype\-catalog\.xml*' -not -path '*/\^maven\-metadata\-local*\.xml' -not -path '*/\^maven\-metadata\-deployment*\.xml' | sed "s|^\./||" | xargs -I '{}' curl -u "$USERNAME:$PASSWORD" -X PUT -v -T {} ${REPO_URL}/{} ;

