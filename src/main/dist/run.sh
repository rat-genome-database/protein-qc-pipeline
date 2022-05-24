# runs the pipeline

. /etc/profile
APPDIR=/home/rgddata/pipelines/protein-qc-pipeline
SERVER=`hostname -s | tr '[a-z]' '[A-Z]'`
EMAIL_LIST=mtutaj@mcw.edu,jthota@mcw.edu
if [ "$SERVER" = "REED" ]; then
  EMAIL_LIST=mtutaj@mcw.edu,jthota@mcw.edu
fi

cd $APPDIR
java -Dlog4j.configurationFile=file://$APPDIR/properties/log4j2.xml \
  -Dspring.config=../properties/default_db2.xml \
  -jar lib/ProteinQcPipeline.jar $1 $2 $3 $4 $5 | tee run.log

mailx -s "[$SERVER] Protein Qc Pipeline OK" $EMAIL_LIST < run.log

echo "==="