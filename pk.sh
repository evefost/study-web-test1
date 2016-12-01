cd /workspace/idea_web/study-web-test2
mvn clean
mvn -P dev_debug package
mvn -P dev_release package
mvn -P test_debug package
mvn -P test_release package
mvn -P prd_debug package
mvn -P prd_release package


