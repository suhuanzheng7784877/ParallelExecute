namespace java org.para.distributed.thrift

service  DistributedParallelExecuteService {
	bool startDistributedParallelExecute(1:string jarHttpURI,2:string mainClassName,3:i32 blockNum,4:map<string,string> parameterMap)
}