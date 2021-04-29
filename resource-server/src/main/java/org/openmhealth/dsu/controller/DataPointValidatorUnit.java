package org.openmhealth.dsu.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.openmhealth.schema.domain.omh.DataPoint;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import utils.DataFile;
import utils.SchemaFile;

public class DataPointValidatorUnit {
	private DataPoint dataPoint;
	private String toValidate;
	public String getToValidate() {
		return toValidate;
	}

	public void setToValidate(String toValidate) {
		this.toValidate = toValidate;
	}

	public DataPointValidatorUnit(DataPoint dataPoint) {
		this.dataPoint = dataPoint;
	}
	
	public boolean isValidBody(){
		ProcessingReport report = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();	
			final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
			final ObjectMapper objectMapper = new ObjectMapper();
			   
			// files name
			String jsonSchemaFileName = "schema/omh/"+dataPoint.getHeader().getBodySchemaId().getName() + "-" + dataPoint.getHeader().getBodySchemaId().getVersion() + ".json";
			String jsonFileName = "validation/omh/validate-schemas/"+ dataPoint.getHeader().getBodySchemaId().getVersion() + "/shouldPass/data.json";
			System.out.println("schema file:" +jsonSchemaFileName);
				       
			//create file and fill it
			File jsonFile = new File(classLoader.getResource(jsonFileName).getFile());
			if(jsonFile.delete()) {
				jsonFile.createNewFile();
			}
		
			//get json body and write it on file
			String toValidate = objectMapper.writeValueAsString(dataPoint.getBody());
			setToValidate(toValidate);
			FileWriter fw = new FileWriter(jsonFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(toValidate);
			bw.close();
			System.out.print("to validate :" + toValidate);
			
			// validate Json with Json Schema
			File jsonSchemaFile = new File(classLoader.getResource(jsonSchemaFileName).getFile());
			JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(jsonSchemaFile.toURI().toString());
			JsonNode testData = objectMapper.readTree(jsonFile);
			SchemaFile fileSchema =  new SchemaFile(jsonSchemaFile.toURI(), jsonSchema);
			DataFile fileData = new DataFile(jsonFile.toURI(), testData);
			report= fileSchema.getJsonSchema().validate(fileData.getData());
		
		} catch (IOException | ProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//return report validation result
		return report.isSuccess();
		
	}
	

}
