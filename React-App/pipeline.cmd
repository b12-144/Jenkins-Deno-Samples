::This script runs your pipeline.ts inside a docker container (using tinyos as docker image)
::We use -td to keep the container running (-t) in background (-d)
::After running tinyos container, you can get access into its shell by executing:
::docker exec -it tinyos /bin/sh
:: We need to use --unstable flag to use Deno function exists(). More info here: https://deno.land/std@0.74.0/fs/README.md#exists
docker run --name tinyos -v "/var/run/docker.sock:/var/run/docker.sock:rw" --rm -td tezine/tinyos
docker cp pipeline.ts tinyos:/home/deno/pipeline.ts

docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone clone feature/pipeline weky4cv
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone npmInstall
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone npmBuild
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone dockerLogin [YourDockerLogin]
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone dockerBuild 1.0.0
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone dockerPush
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone kubernetesApply

docker kill tinyos

::Alternativaly you can execute all stages at once:
docker exec tinyos deno run --unstable -A /home/deno/pipeline.ts standalone allStages feature/pipeline weky4cv [YourPassword] [YourDockerLogin] [YourDockerPassword] 1.0.0

