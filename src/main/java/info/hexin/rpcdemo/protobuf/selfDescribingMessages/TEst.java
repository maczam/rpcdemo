package info.hexin.rpcdemo.protobuf.selfDescribingMessages;

import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.DescriptorProtos.DescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public class TEst {
	public static void main(String[] args) throws DescriptorValidationException, InvalidProtocolBufferException {

		// SelfDescribingMessage 描述文件
		FileDescriptor fileDescriptor = createSelfDescribingMessage();
		Descriptor descriptor = fileDescriptor.findMessageTypeByName("SelfDescribingMessage");
		DynamicMessage.Builder selfDescribingMessage = DynamicMessage.newBuilder(descriptor);

		// 构造proto_files 值
		FileDescriptorProto messageFileDescriptor = createMessageDescriptor();
		selfDescribingMessage.setField(descriptor.findFieldByName("proto_files"), messageFileDescriptor.toByteString());

		// 构造type_name 值
		selfDescribingMessage.setField(descriptor.findFieldByName("type_name"), "msg");

		// 构造message_data 值
		FileDescriptor messagequery = FileDescriptor.buildFrom(messageFileDescriptor, new FileDescriptor[0]);
		Descriptor msgQuery = messagequery.findMessageTypeByName("msg");
		DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(msgQuery);
		messageBuilder.setField(msgQuery.findFieldByName("name"), "hexinfdsafdsafdsafdsa......");

		DynamicMessage dynamicMessage = messageBuilder.build();
		selfDescribingMessage.setField(descriptor.findFieldByName("message_data"), dynamicMessage.toByteString());
		byte[] buf = selfDescribingMessage.build().toByteArray();

		System.out.println(buf.length);
		server(buf);
	}

	/**
	 * 模拟接收端
	 * 
	 * @param buf
	 * @throws DescriptorValidationException
	 * @throws InvalidProtocolBufferException
	 */
	private static void server(byte[] buf) throws DescriptorValidationException, InvalidProtocolBufferException {
		FileDescriptor serverfileDescriptor = createSelfDescribingMessage();
		DynamicMessage serverMessage = DynamicMessage.parseFrom(
				serverfileDescriptor.findMessageTypeByName("SelfDescribingMessage"), buf);
		Map<FieldDescriptor, Object> map = serverMessage.getAllFields();

		FileDescriptorProto protoFiles = null;
		ByteString messageData = null;
		String typeName = null;
		for (Map.Entry<FieldDescriptor, Object> b : map.entrySet()) {
			if ("proto_files".equals(b.getKey().getName())) {
				protoFiles = FileDescriptorProto.parseFrom((ByteString) b.getValue());
			} else if ("type_name".equals(b.getKey().getName())) {
				typeName = b.getValue().toString();
			} else if ("message_data".equals(b.getKey().getName())) {
				messageData = (ByteString) b.getValue();
			}
		}

		if (protoFiles != null && messageData != null && typeName != null) {
			FileDescriptor fileDescriptor = FileDescriptor.buildFrom(protoFiles, new FileDescriptor[0]);
			DynamicMessage message = DynamicMessage.parseFrom(fileDescriptor.findMessageTypeByName(typeName), messageData);
			Map<FieldDescriptor, Object> messageMap = message.getAllFields();
			for (Map.Entry<FieldDescriptor, Object> b : messageMap.entrySet()) {
				System.out.println(b.getKey().getFullName());
				System.out.println(b.getValue());
			}
		} else {
			System.out.println("descriptorProto != null && value != null && typeName != null");
		}
	}

	/**
	 * 生成自描述文件
	 * 
	 * @return
	 * @throws DescriptorValidationException
	 */
	private static FileDescriptor createSelfDescribingMessage() throws DescriptorValidationException {
		// 构建描述文件
		FileDescriptorProto.Builder fileDescriptorProtoBuilder = FileDescriptorProto.newBuilder();
		// message 描述
		DescriptorProto.Builder messageProtoBuilderA = DescriptorProto.newBuilder();
		messageProtoBuilderA.setName("SelfDescribingMessage");

		// Field
		// Set of .proto files which define the type.
		DescriptorProtos.FieldDescriptorProto.Builder proto_files = DescriptorProtos.FieldDescriptorProto.newBuilder();
		proto_files.setName("proto_files").setNumber(1).setType(Type.TYPE_BYTES);
		messageProtoBuilderA.addField(proto_files.build());

		// Name of the message type. Must be defined by one of the files in
		DescriptorProtos.FieldDescriptorProto.Builder type_name = DescriptorProtos.FieldDescriptorProto.newBuilder();
		type_name.setName("type_name").setNumber(2).setType(Type.TYPE_STRING);
		messageProtoBuilderA.addField(type_name.build());

		// The message data.
		DescriptorProtos.FieldDescriptorProto.Builder message_data = DescriptorProtos.FieldDescriptorProto.newBuilder();
		message_data.setName("message_data").setNumber(3).setType(Type.TYPE_BYTES);
		messageProtoBuilderA.addField(message_data.build());

		fileDescriptorProtoBuilder.addMessageType(messageProtoBuilderA);
		FileDescriptor fileDescriptor = FileDescriptor.buildFrom(fileDescriptorProtoBuilder.build(),
				new FileDescriptor[0]);
		return fileDescriptor;
	}

	/**
	 * 创建内部描述文件
	 * 
	 * @return
	 */
	private static FileDescriptorProto createMessageDescriptor() {
		FileDescriptorProto.Builder fileDescriptorProtoBuilder = FileDescriptorProto.newBuilder();
		// message 描述
		DescriptorProto.Builder messageProtoBuilderA = DescriptorProto.newBuilder();
		messageProtoBuilderA.setName("msg");
		
		// Field
		DescriptorProtos.FieldDescriptorProto.Builder nameField = DescriptorProtos.FieldDescriptorProto.newBuilder();
		nameField.setName("name").setNumber(1).setType(Type.TYPE_STRING);
		messageProtoBuilderA.addField(nameField.build());

		fileDescriptorProtoBuilder.addMessageType(messageProtoBuilderA);
		return fileDescriptorProtoBuilder.build();
	}
}
