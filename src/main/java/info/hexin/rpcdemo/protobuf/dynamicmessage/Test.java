package info.hexin.rpcdemo.protobuf.dynamicmessage;

import java.util.HashMap;
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

public class Test {

	private static String MSG = "msg";

	public static void main(String[] args) throws DescriptorValidationException, InvalidProtocolBufferException {

		// SelfDescribingMessage 描述文件
		FileDescriptor fileDescriptor = createSelfDescribingMessage();
		Descriptor descriptor = fileDescriptor.findMessageTypeByName("SelfDescribingMessage");

		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "hexin");
		map.put("password", "password");
		map.put("adfasfd", "1111");


		byte[] buf = assign(descriptor, map);
		server(buf);
	}

	/**
	 * 将要传递的对象生成需要传递的buf数组
	 * 
	 * @param descriptor
	 * @param value
	 * @return
	 * @throws DescriptorValidationException
	 */
	private static byte[] assign(Descriptor descriptor, Object value) throws DescriptorValidationException {
		Map<String, ByteString> map = encapsulation(value);

		DynamicMessage.Builder selfDescribingMessage = DynamicMessage.newBuilder(descriptor);
		selfDescribingMessage.setField(descriptor.findFieldByName("proto_files"), map.get("proto_files"));
		selfDescribingMessage.setField(descriptor.findFieldByName("type_name"), MSG);
		selfDescribingMessage.setField(descriptor.findFieldByName("message_data"), map.get("message_data"));
		return selfDescribingMessage.build().toByteArray();
	}

	/**
	 * 封装需要传递的对象
	 * 
	 * @param value
	 * @return
	 * @throws DescriptorValidationException
	 */
	private static Map<String, ByteString> encapsulation(Object value) throws DescriptorValidationException {

		Map<String, ByteString> resultMap = new HashMap<String, ByteString>();

		FileDescriptorProto.Builder fileDescriptorProtoBuilder = FileDescriptorProto.newBuilder();
		DescriptorProto.Builder messageProtoBuilderA = DescriptorProto.newBuilder();
		// message 描述
		messageProtoBuilderA.setName(MSG);

		if (value instanceof Map) {
			Map<?, ?> valueMap = (Map<?, ?>) value;
			int i = 1;
			for (Map.Entry<?, ?> entry : valueMap.entrySet()) {
				Object key = entry.getKey();
				// Field
				DescriptorProtos.FieldDescriptorProto.Builder nameField = DescriptorProtos.FieldDescriptorProto
						.newBuilder();
				nameField.setName(key.toString()).setNumber(i++).setType(Type.TYPE_STRING);
				messageProtoBuilderA.addField(nameField.build());
			}
			fileDescriptorProtoBuilder.addMessageType(messageProtoBuilderA);
			FileDescriptorProto descriptorProto = fileDescriptorProtoBuilder.build();
			FileDescriptor messagequery = FileDescriptor.buildFrom(fileDescriptorProtoBuilder.build(),
					new FileDescriptor[0]);

			resultMap.put("proto_files", descriptorProto.toByteString());
			Descriptor msgQuery = messagequery.findMessageTypeByName(MSG);
			DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(msgQuery);

			for (Map.Entry<?, ?> entry : valueMap.entrySet()) {
				Object key = entry.getKey();
				messageBuilder.setField(msgQuery.findFieldByName(key.toString()), entry.getValue());
			}
			resultMap.put("message_data", messageBuilder.build().toByteString());
		}
		return resultMap;
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
			DynamicMessage message = DynamicMessage.parseFrom(fileDescriptor.findMessageTypeByName(typeName),
					messageData);
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
