package info.hexin.rpcdemo.protobuf.selfDescribingMessages;

import info.hexin.rpcdemo.protobuf.PersonProbuf;
import info.hexin.rpcdemo.protobuf.PersonProbuf.Person;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
//CRC-32，而是选用 adler32
public class UDPClient {
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket();
		PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
		builder.setEmail("kkk@emai1111l.com");
		builder.setId(1);
		builder.setName("TestName1111111111111111111111111111111111111111111111111111");
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("1311fdsfdsafdsa11111")
				.setType(PersonProbuf.Person.PhoneType.MOBILE));
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("011ffffffffffffdsafdsas111")
				.setType(PersonProbuf.Person.PhoneType.HOME));
		Person person = builder.build();
		System.out.println(person.toString());
		byte[] buf = person.toByteArray();

		ByteBuffer buffer = ByteBuffer.allocate(4 + buf.length);
		buffer.putInt(buf.length);
		buffer.put(buf);
		System.out.println(" UdpSend >> " + buf.length);
		ds.send(new DatagramPacket(buffer.array(), buf.length + 4, InetAddress.getByName("localhost"), 3000));
		ds.close();
	}
}
