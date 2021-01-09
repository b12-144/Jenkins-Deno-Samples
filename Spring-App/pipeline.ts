//docker run -it --entrypoint= -v "C:/Arquivos/Jenkins/Deno:/scripts" tezine/tinyos deno run -A /scripts/pipeline.ts

enum Mode {
    unkown,
    local,
    localJenkins,
    devJenkins,
    qualJenkins,
    prodJenkins
}

export interface AWSCredentials {
    AssumedRoleUser: AssumedRoleUser;
    Credentials: Credentials;
}

export interface AssumedRoleUser {
    AssumedRoleId: string;
    Arn: string;
}

export interface Credentials {
    SecretAccessKey: string;
    SessionToken: string;
    Expiration: string;
    AccessKeyId: string;
}

function getMode(m: string): Mode {
    let mode = Mode.unkown;
    switch (m/*mode*/) {
        case 'local':
            mode = Mode.local
            break;
        case 'localJenkins':
            mode = Mode.localJenkins
            break;
        case 'devJenkins':
            mode = Mode.devJenkins
            break;
        case 'qualJenkins':
            mode = Mode.qualJenkins
            break;
        case 'prodJenkins':
            mode = Mode.prodJenkins
            break;
        default:
            console.log(`invalid mode:${m}`);
            break;
    }
    return mode;
}

export class Publisher {
    localFolder='/home/workspace';//there's no need to change this. Just remember to execute from Jenkins with -v "$(pwd)":/home/deno
    mode = Mode.unkown;
    dockerImage='tezine/spring-deno'

    constructor(mode: Mode) {
        this.mode = mode;
        console.log(`loading publisher in ${Mode[mode]} mode...`)
    }

    public async dockerLogin(login:string, password:string){
        try{
            Deno.chdir(this.localFolder);
            console.log('login into docker...')
            const p = Deno.run({cmd: ['docker', 'login', '--username', `${login}`, '--password',`${password}`],});
            const output = await p.status();
            if(output.success){
                console.log('OK');
                return true;
            }
        }catch (e) {
            console.error('Error:',e);
        }
        console.error('Unable to login into Docker registry');
        return false;
    }

    public async dockerBuild(version:string) {
        try {
            Deno.chdir(this.localFolder);
            console.log('running docker build...')
            let output=await Deno.run({cmd: ['docker', 'build','-t',`${this.dockerImage}:${version}`,'-t',`${this.dockerImage}:latest`,'.' ],}).status();
            if (output.success) {
                console.log('OK');
                return true;
            }
        } catch (e) {
            console.error('Error:', e);
        }
        console.error('Unable to exec docker build');
        return false;
    }

    public async dockerPush(){
        try{
            Deno.chdir(this.localFolder);
            console.log(`pushing docker image ${this.dockerImage}...`)
            let output=await Deno.run({cmd: ['docker', 'push', `${this.dockerImage}`],}).status();
            if(output.success){
                console.log('OK');
                return true;
            }
        }catch (e) {
            console.error('Error:',e);
        }
        console.error('Unable to push docker image');
        return false;
    }


    public async gradleBuild(){
        try {
            Deno.chdir(this.localFolder);
            console.log('running gradle build...');
            await Deno.run({cmd: ['chmod', '777', 'gradlew'],}).status();//hack. I don't know why gradlew didn't have permission to execute
            let output=await Deno.run({cmd: ['./gradlew', 'build'],}).status();
            if(output.success){
                console.log('OK');
                return true;
            }
        } catch (e) {
            console.error('Error:',e);
        }
        console.error('Unable to run gradlew build');
        return false;
    }

    public async awsSetup():Promise<boolean>{
        try {
            // if(this.mode==Mode.standalone) process=await Deno.run({cmd: ['aws', 'sts', 'assume-role', '--role-arn', 'arn:aws:iam::YourRole', '--role-session-name', 'TempSession'],stdout:"piped"});
            switch(this.mode){
                case Mode.local:
                    console.log('checking aws credentials...');
                    let output=await Deno.run({cmd: ['aws','sts', 'get-caller-identity']}).status();
                    if(output.success){
                        console.log('OK');
                        return true;
                    }else console.log('unable to check credentials. Did you get aws credentials?')
                    break;
                default: {//executed from jenkins
                    console.log('running aws setup...');
                    let process = await Deno.run({cmd: ['aws', 'sts', 'assume-role', '--role-arn', 'arn:aws:iam::yourRole', '--role-session-name', 'TempSession'], stdout: "piped"});
                    await process.status();
                    let out = new TextDecoder().decode(await process.output());
                    console.log('aws output:', out);
                    let aws: AWSCredentials = JSON.parse(out);
                    console.log('access key:', aws.Credentials.AccessKeyId);
                    await Deno.run({cmd: ['aws', 'configure', 'set', 'aws_access_key_id', aws.Credentials.AccessKeyId]}).status();
                    await Deno.run({cmd: ['aws', 'configure', 'set', 'aws_secret_access_key', aws.Credentials.SecretAccessKey]}).status();
                    let output = await Deno.run({cmd: ['aws', 'configure', 'set', 'aws_session_token', aws.Credentials.SessionToken]}).status();
                    if (output.success) {
                        console.log('OK');
                        return true;
                    }
                    break;
                }
            }
        } catch (e) {
            console.error('Error:',e);
        }
        console.error('Unable to run aws setup');
        return false;
    }

    public async kubernetesApply(){
        try{
            Deno.chdir(this.localFolder);
            console.log('applying kubernetes...');
            await this.awsSetup()
            if(this.mode!=Mode.local) {
                await Deno.run({cmd: ['aws', 'sts', 'get-caller-identity']}).status();
                await Deno.run({cmd: ['aws', 'eks', '--region', 'sa-east-1', 'update-kubeconfig', '--name', 'myOrg'],}).status()
            }
            let output=await Deno.run({cmd: ['kubectl', 'apply', '-f','kubernetes.yaml'],}).status()
            if(output.success){
                console.log('OK');
                return true;
            }
        }catch (e) {
            console.error('Error:',e);
        }
        console.error('Unable to apply kubernetes');
        return false;
    }

    public async allStages(){
        if(!await this.awsSetup())return false;
        if(!await this.gradleBuild())return false;
        if(!await this.dockerBuild('1.0.1'))return false;
        if(!await this.kubernetesApply())return false;
        return true;
    }
}

/*
 * First mandatory argument: mode [local, devJenkins, qualJenkins, prodJenkins]
 */
await main();
async function main() {
    let args = Deno.args;
    if (args.length < 2) {
        console.log('you must call this script using 2 arguments: mode, stage')
        Deno.exit(-1)
    }
    let mode = getMode(args[0])
    if (mode === Mode.unkown) return Deno.exit(-1)
    let publisher = new Publisher(mode);
    switch (args[1]/*stage*/) {
        case 'allStages':
            if (!await publisher.allStages()) Deno.exit(-1)
            break;
        case 'dockerLogin':
            if(args.length!==4){console.error('you must pass docker login and password');Deno.exit(-1)}
            if (!await publisher.dockerLogin(args[2],args[3])) Deno.exit(-1);
            break;
        case 'dockerBuild':
            if(args.length!==3){console.error('you must pass the version number');Deno.exit(-1)}
            if (!await publisher.dockerBuild(args[2])) Deno.exit(-1);
            break;
        case 'dockerPush':
            if (!await publisher.dockerPush()) Deno.exit(-1);
            break;
        case 'kubernetesApply':
            if (!await publisher.kubernetesApply()) Deno.exit(-1);
            break;
        case 'awsSetup':
            if (!await publisher.awsSetup()) Deno.exit(-1);
            break;
        case 'gradleBuild':
            if (!await publisher.gradleBuild()) Deno.exit(-1);
            break;
        default:
            console.log('invalid stage')
            Deno.exit(-1)
    }
    console.log('SUCCESS');
}
