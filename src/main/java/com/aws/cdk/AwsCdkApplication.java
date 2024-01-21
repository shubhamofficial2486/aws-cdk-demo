package com.aws.cdk;

import com.aws.cdk.stack.LambdaStack;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.DefaultStackSynthesizer;
import software.amazon.awscdk.core.StackProps;

public class AwsCdkApplication {

	public static void main(String[] args) {
		App app = new App();
		// C:\Users\sharm\AppData\Roaming\npm\cdk.cmd synth demo-lambda-stack
		DefaultStackSynthesizer synthesizer = DefaultStackSynthesizer.Builder.create()
				.generateBootstrapVersionRule(false).build();

		new LambdaStack(app, "demo-lambda-stack",
				StackProps.builder().stackName("demo-lambda-stack").synthesizer(synthesizer).build());

		app.synth();
	}

}
