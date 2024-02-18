package com.aws.cdk;

import com.aws.cdk.stack.LambdaStack;
import com.aws.cdk.stack.decument.DocumentStoreDynamoDbStack;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.DefaultStackSynthesizer;
import software.amazon.awscdk.core.StackProps;

public class AwsCdkApplication {

	public static void main(String[] args) {
		App app = new App();
		// C:\Users\sharm\AppData\Roaming\npm\cdk.cmd synth demo-lambda-stack
		// C:\Users\sharm\git\aws-cdk-demo
		DefaultStackSynthesizer synthesizer = DefaultStackSynthesizer.Builder.create()
				.generateBootstrapVersionRule(false).build();

		new LambdaStack(app, "demo-lambda-stack",
				StackProps.builder().stackName("demo-lambda-stack").build());
		
		new DocumentStoreDynamoDbStack(app, "document-store-db-stack", 
				StackProps.builder().stackName("document-stort-db-stack").build());
		
		app.synth();
	}

}
