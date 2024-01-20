package com.aws.cdk.stack;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Subnet;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcAttributes;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.kms.Key;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.VersionOptions;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class LambdaStack extends Stack{

	public LambdaStack(@Nullable Construct scope, @Nullable String id, @Nullable StackProps props) {
		super(scope, id, props);
		// TODO Auto-generated constructor stub
		Function lambdaStackFunction = new Function(scope, id, FunctionProps.builder()
				.functionName("DEMO_LAMBDA_FUNCTION")
				.runtime(software.amazon.awscdk.services.lambda.Runtime.PYTHON_3_9)
				.role(Role.fromRoleName(this, "demo-role-id", "demo-iam-lambda"))
				.environmentEncryption(Key.fromKeyArn(this, "xkmsKey", "arnOfKmsKey"))
				.code(Code.fromBucket(Bucket.fromBucketName(this, "bucket-id", "bucketName"), "fileKey"))
				.handler("lambda-handler")
				.memorySize(128)
				.timeout(Duration.seconds(60))
				.environment(Map.of("key","value"))
				.layers(List.of(LayerVersion.fromLayerVersionArn(this, "layer-id", "layer-arn")))
				.allowAllOutbound(getBundlingRequired())
				.currentVersionOptions(VersionOptions.builder()
						.description("v1")
						.removalPolicy(RemovalPolicy.RETAIN).build())
				.vpc(Vpc.fromVpcAttributes(this, id, 
						VpcAttributes.builder()
						.vpcId(id)
						.availabilityZones(getAvailabilityZones())
						.build()))
				.vpcSubnets(SubnetSelection.builder().subnets(List.of(Subnet.fromSubnetId(this, "subnet", "subnetId"))).build())
				.build());
		
		lambdaStackFunction.addEventSource(new SqsEventSource(Queue.fromQueueArn(this, "SQS-id", "sqs-arn")));
	
	}

	public LambdaStack(@Nullable Construct scope, @Nullable String id) {
		super(scope, id);
		
		Function lambdaStackFunction = new Function(scope, id, FunctionProps.builder()
				.functionName("DEMO_LAMBDA_FUNCTION")
				.runtime(software.amazon.awscdk.services.lambda.Runtime.PYTHON_3_9)
				.role(Role.fromRoleName(this, "demo-role-id", "demo-iam-lambda"))
				.environmentEncryption(Key.fromKeyArn(this, "xkmsKey", "arnOfKmsKey"))
				.code(Code.fromBucket(Bucket.fromBucketName(this, "bucket-id", "bucketName"), "fileKey"))
				.handler("lambda-handler")
				.memorySize(128)
				.timeout(Duration.seconds(60))
				.environment(Map.of("key","value"))
				.layers(List.of(LayerVersion.fromLayerVersionArn(this, "layer-id", "layer-arn")))
				.allowAllOutbound(getBundlingRequired())
				.currentVersionOptions(VersionOptions.builder()
						.description("v1")
						.removalPolicy(RemovalPolicy.RETAIN).build())
				.vpc(Vpc.fromVpcAttributes(this, id, 
						VpcAttributes.builder()
						.vpcId(id)
						.availabilityZones(getAvailabilityZones())
						.build()))
				.vpcSubnets(SubnetSelection.builder().subnets(List.of(Subnet.fromSubnetId(this, "subnet", "subnetId"))).build())
				.build());
		
		lambdaStackFunction.addEventSource(new SqsEventSource(Queue.fromQueueArn(this, "SQS-id", "sqs-arn")));
	}
	
	 

}
