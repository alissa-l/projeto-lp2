# Se quiser buildar e rodar utilize (gradle e sdkman necessarios) :
sdk install gradle

gradle publish

java -jar app/maven/com/example/app/*/*.jar

# Pelo contrario, um unico comando basta para rodar o arquivo jar já presente:

java -jar app/maven/com/example/app/*/*.jar
