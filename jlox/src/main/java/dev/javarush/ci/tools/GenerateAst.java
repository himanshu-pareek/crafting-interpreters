package dev.javarush.ci.tools;

import java.util.Collection;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
	if (args.length != 1) {
	    System.err.println("Usage: generate_ast <output directory>");
	    System.exit(1);
	}
   	String outputDir = args[0];
	defineAst (outputDir, "Expr", List.of(
					      "Binary   : Expr left, Token operator, Expr right",
					      "Grouping : Expr expression",
					      "Literal  : Object value",
					      "Unary    : Token operator, Expr right"
));
	
    }

    private static void defineAst(
				  String outputDir,
				  String baseName,
				  Collection<String> types
				  ) throws IOException {
	String path = outputDir + "/" + baseName + ".java";
	PrintWriter writer = new PrintWriter(path, "UTF-8");

	writer.println("import java.util.List");
	writer.println();
	writer.println("abstract class " + baseName + " {");

	defineVisitor(writer, baseName, types);

	for (String type: types) {
	    String className = type.split(":")[0].trim();
	    String fields = type.split(":")[1].trim();
	    defineType(writer, baseName, className, fields);
	    writer.println();
	}

	// The base abstract method - visitory pattern
	writer.println();
	writer.println("\tabstract <R> R accept(Visitor<R> visitor);");

	
	writer.println("}");
	writer.close();
    }

    private static void defineType(
				   PrintWriter writer,
				   String baseName,
				   String className,
				   String fieldList
				   ) {
	writer.println("\tstatic class " + className + " extends " + baseName + " {");

	// compute fields from `fieldList`
	String[] fields = fieldList.split(", ");

	// Fields
	for (String field: fields) {
	    writer.println("\t\tfinal " + field + ";");
	}

	// Constructor
	writer.println();
	writer.println("\t\t" + className + "(" + fieldList + ") {");
	for (String field: fields) {
	    String name = field.split(" ")[1].trim();
	    writer.println("\t\t\tthis." + name + " = " + name + ";");
	}
	writer.println("\t\t}");

	// Visitor pattern
	writer.println();
	writer.println("\t\t@Override");
	writer.println("\t\t<R> R accept(Visitor<R> visitor) {");
	writer.println("\t\t\treturn visitor.visit" + className + baseName + "(this);");
	writer.println("\t\t}");

	// End class definition
	writer.println();
	writer.println("\t}");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, Collection<String> types) {

	writer.println("\tinterface Visitor<R> {");

	for (String type: types) {
	    String typeName = type.split(":")[0].trim();
	    writer.println("\t\tR visit" + typeName + baseName + "(" +
			   typeName + " " + baseName.toLowerCase() + ");");
	}

	writer.println("\t}");
    }
}
