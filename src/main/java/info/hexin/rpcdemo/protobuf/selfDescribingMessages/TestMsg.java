package info.hexin.rpcdemo.protobuf.selfDescribingMessages;

import java.io.IOException;
import java.util.Map;

import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;

public class TestMsg {
	public static void main(String[] args) throws IOException, DescriptorValidationException {

		FileDescriptorProto.Builder fileDescriptorProtoBuilder = FileDescriptorProto.newBuilder();
		DescriptorProto.Builder messageProtoBuilderA = DescriptorProto.newBuilder();
		messageProtoBuilderA.setName("A");
		DescriptorProto.Builder messageProtoBuilderB = DescriptorProto.newBuilder();
		messageProtoBuilderB.setName("B");

		messageProtoBuilderA.addFieldBuilder().setName("f").setNumber(2).setType(Type.TYPE_STRING);

		fileDescriptorProtoBuilder.addMessageType(messageProtoBuilderA);
		fileDescriptorProtoBuilder.addMessageType(messageProtoBuilderB);

		FileDescriptorProto fileDescriptorProto = fileDescriptorProtoBuilder.build();
		FileDescriptor fileDescriptor = FileDescriptor.buildFrom(fileDescriptorProto, new FileDescriptor[0]);

		DynamicMessage.Builder messageA = DynamicMessage.newBuilder(fileDescriptor.findMessageTypeByName("A"));
		DynamicMessage.Builder messageB = DynamicMessage.newBuilder(fileDescriptor.findMessageTypeByName("B"));

		messageA.setField(fileDescriptor.findMessageTypeByName("A").findFieldByName("f"), "hexin");
		
		
		byte[] buf = messageA.build().toByteArray();
		DynamicMessage message = DynamicMessage.parseFrom(fileDescriptor.findMessageTypeByName("A"), buf);
		Map<FieldDescriptor, Object> map = message.getAllFields();
		for (Map.Entry<FieldDescriptor, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey().getFullName());
			System.out.println(entry.getValue());
		}
		
	}
}
