package info.hexin.rpcdemo.protobuf.selfDescribingMessages;

import info.hexin.rpcdemo.protobuf.PersonProbuf;
import info.hexin.rpcdemo.protobuf.PersonProbuf.Person;
import info.hexin.rpcdemo.protobuf.PersonProbuf.Person.PhoneNumber;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

public class UDPServer {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(3000);
		try {
			while (true) {
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf, 1024);
				ds.receive(dp);
				byte[] bytes = dp.getData();
				ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
				buffer.put(bytes);
				buffer.position(0);
				try {
					int l = buffer.getInt();
					System.out.println(" UdpRecv >> " + l);
					byte[] b = new byte[l];
					buffer.get(b);
					Person person2 = PersonProbuf.Person.parseFrom(b);
					System.out.println(person2.getName() + ", " + person2.getEmail());
					List<PhoneNumber> lstPhones = person2.getPhoneList();
					for (PhoneNumber phoneNumber : lstPhones) {
						System.out.println(phoneNumber.getNumber());
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			ds.close();
		}
	}
}
