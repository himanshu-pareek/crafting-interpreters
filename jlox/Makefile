build: src/main/java/dev/javarush/ci/lox/*.java
	javac -d build src/main/java/dev/javarush/ci/lox/*.java


export 	filePath := $(shell realpath "src/main/java/dev/javarush/ci/lox")
expr:
	javac -d build src/main/java/dev/javarush/ci/tools/GenerateAst.java
	cd build && java dev.javarush.ci.tools.GenerateAst $(filePath)

