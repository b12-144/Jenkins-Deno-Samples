rem Remember to get aws creds before executing this script file.
rem First we start tinyOS container
docker run --name tinyos ^
    --rm -td ^
    -v %USERPROFILE%:/root:rw ^
    -v "/var/run/docker.sock:/var/run/docker.sock:rw" ^
    -v %cd%:/home/workspace ^
    tezine/tinyos

rem Now we execute all stages.
docker exec -it tinyos ^
    /bin/deno run --unstable --quiet -A ^
    /home/workspace/pipeline.ts ^
    local allStages

