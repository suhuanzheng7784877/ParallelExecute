@echo off
java -Dpe.conf=/%PE_HOME%/conf -Djava.ext.dirs=/%PE_HOME%/lib org.para.distributed.slave.WorkerServer