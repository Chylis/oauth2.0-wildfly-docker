#!/bin/bash

# Usage: setup_wildfly.sh [WildFly mode] [configuration file]
#
# The default mode is 'standalone' and default configuration is based on the
# mode. It can be 'standalone.xml' or 'domain.xml'.

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=${JBOSS_HOME}/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

function wait_for_server() {
  until `${JBOSS_CLI} -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

echo "=> Starting WildFly server"
${JBOSS_HOME}/bin/${JBOSS_MODE}.sh -b 0.0.0.0 -c ${JBOSS_CONFIG} &

echo "=> Waiting for the server to boot"
wait_for_server

# The below variables are created by Docker when linking containers.
# The link name/alias is assumed to be "db", the mysql port is assumed to be 3306
# The variable naming is rather weird, <name>_PORT_<port>_<protocol> - it exposes the port number in the variable name itself.
echo "=> MYSQL database: " ${DB_ENV_MYSQL_DATABASE}
echo "=> MYSQL user: " ${DB_ENV_MYSQL_USER}
echo "=> MYSQL (docker host): " ${DB_PORT_3306_TCP_ADDR}
echo "=> MYSQL (docker port): " ${DB_PORT_3306_TCP_PORT}
CONNECTION_URL=jdbc:mysql://${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/${DB_ENV_MYSQL_DATABASE}
echo "Connection URL: " ${CONNECTION_URL}

#The below command creates the JDBC resource for WildFly using jboss-cli.
${JBOSS_CLI} --connect << EOF
batch
# Add MySQL module
module add --name=com.mysql --resources=/opt/jboss/wildfly/customization/mysql-connector-java-5.1.39-bin.jar --dependencies=javax.api,javax.transaction.api

# Add MySQL driver
/subsystem=datasources/jdbc-driver=mysql-xa:add( \
driver-name=mysql-xa, \
driver-module-name=com.mysql, \
driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource \
)

# Add the xa-datasource
/subsystem=datasources/xa-data-source="XAMySqlDS":add(jndi-name="java:/XAMySqlDS",driver-name="mysql-xa")
/subsystem=datasources/xa-data-source="XAMySqlDS"/xa-datasource-properties=URL:add(value="$CONNECTION_URL")
/subsystem=datasources/xa-data-source="XAMySqlDS"/xa-datasource-properties=User:add(value="${DB_ENV_MYSQL_USER}")
/subsystem=datasources/xa-data-source="XAMySqlDS"/xa-datasource-properties=Password:add(value="${DB_ENV_MYSQL_PASSWORD}")

# Execute the batch
run-batch
EOF

echo "=> Deploying the WAR"
cp /opt/jboss/wildfly/customization/*.war ${JBOSS_HOME}/${JBOSS_MODE}/deployments/

echo "=> Shutting down WildFly"
if [ "$JBOSS_MODE" = "standalone" ]; then
  ${JBOSS_CLI} -c ":shutdown"
else
  ${JBOSS_CLI} -c "/host=*:shutdown"
fi

echo "=> Restarting WildFly"
${JBOSS_HOME}/bin/${JBOSS_MODE}.sh -b 0.0.0.0 -c ${JBOSS_CONFIG}