# runs the pipeline

. /etc/profile
APPDIR=/home/rgddata/pipelines/ProteinQcPipeline
SERVER=`hostname -s | tr '[a-z]' '[A-Z]'`
EMAIL_LIST=mtutaj@mcw.edu,jthota@mcw.edu
if [ "$SERVER" = "KYLE" ]; then
  EMAIL_LIST=mtutaj@mcw.edu,jthota@mcw.edu
fi

cd $APPDIR
java -Dlog4j.configuration=file://$APPDIR/properties/log4j.properties \
  -Dspring.config=../properties/default_db.xml \
  -jar ProteinQcPipeline.jar $1 $2 $3 $4 $5 | tee run.log

mailx -s "[$SERVER] Protein Qc Pipeline OK" $EMAIL_LIST < run.log

echo "==="