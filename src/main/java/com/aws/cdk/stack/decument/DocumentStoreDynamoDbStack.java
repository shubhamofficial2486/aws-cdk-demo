package com.aws.cdk.stack.decument;


import org.jetbrains.annotations.Nullable;

import software.amazon.awscdk.core.CfnParameter;
import software.amazon.awscdk.core.CfnParameterProps;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.EnableScalingProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableEncryption;
import software.amazon.awscdk.services.dynamodb.UtilizationScalingProps;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.kms.Key;

public class DocumentStoreDynamoDbStack extends Stack{

	public DocumentStoreDynamoDbStack(@Nullable Construct scope, @Nullable String id) {
		this(scope, id, null);
	}

	public DocumentStoreDynamoDbStack(@Nullable Construct scope, @Nullable String id, @Nullable StackProps props) {
		super(scope, id, props);
		
		CfnParameter envParameter = new CfnParameter(this, "env", 
				CfnParameterProps.builder().defaultValue("local").build());
		
		CfnParameter regionParameter = new CfnParameter(this, "region", 
				CfnParameterProps.builder().defaultValue("me-central-1").build());
		
		CfnParameter readCapacity = new CfnParameter(this, "readCapacity", 
				CfnParameterProps.builder().type("Number").defaultValue("10").build());
		
		CfnParameter writeCapacity = new CfnParameter(this, "writeCapacity", 
				CfnParameterProps.builder().type("Number").defaultValue("40").build());
		
		CfnParameter xksAccountIdParameter = new CfnParameter(this, "xks-account", 
				CfnParameterProps.builder().defaultValue("111111000000").build());
		
		CfnParameter dynamodbKeyIdParameter = new CfnParameter(this, "dynamoKeyId", 
				CfnParameterProps.builder().defaultValue("ar4t5ji-5yh4-4221-8190-ey5y5yheeeae").build());
		
		String env = envParameter.getValueAsString();
		String region = regionParameter.getValueAsString();
		String xksAccount = xksAccountIdParameter.getValueAsString();
		String dynamodbKeyId = dynamodbKeyIdParameter.getValueAsString();
		
		IKey dynamodbKey = Key.fromKeyArn(this, "dynamodbKey", "arn:aws:kms:" + region + ":" + xksAccount + ":key/"+ dynamodbKeyId);
		
		String tableName = env + "document_store";
		
		Table table = Table.Builder.create(this, "document_store_table")
				.tableName(tableName)
				.partitionKey(Attribute.builder().name("document_id")
						.type(AttributeType.STRING)
						.build())
				.billingMode(BillingMode.PROVISIONED)
				.encryption(TableEncryption.CUSTOMER_MANAGED)
				.encryptionKey(dynamodbKey)
				.readCapacity(readCapacity.getValueAsNumber())
				.writeCapacity(writeCapacity.getValueAsNumber())
				.timeToLiveAttribute("ttl")
				.pointInTimeRecovery(true)
				.build();
		
		table.autoScaleReadCapacity(
				EnableScalingProps.builder()
				.minCapacity(1)
				.maxCapacity(readCapacity.getValueAsNumber())
				.build())
		.scaleOnUtilization(UtilizationScalingProps.builder()
				.targetUtilizationPercent(70)
				.build());
		
		table.autoScaleWriteCapacity(
				EnableScalingProps.builder()
				.minCapacity(4)
				.maxCapacity(writeCapacity.getValueAsNumber())
				.build())
		.scaleOnUtilization(UtilizationScalingProps.builder()
				.targetUtilizationPercent(70)
				.build());
	}
	
	

}
