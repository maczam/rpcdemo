package info.hexin.rpcdemo.thrift.test;

import info.hexin.rpcdemo.thrift.User;
import info.hexin.rpcdemo.thrift.UserNotFound;
import info.hexin.rpcdemo.thrift.UserService;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Client {
	public static void main(String[] args) throws IOException {
		try {
			TIOStreamTransport streamTransport = new TSocket("localhost", 9090, 3000);
			TTransport transport = new TFramedTransport(streamTransport);
	        TProtocol protocol = new TCompactProtocol(transport);  
	        UserService.Client client = new UserService.Client(protocol);  
	        transport.open();
			System.out.println("test2");
			try {
				User user2 = client.getUser("login10");
				System.out.println("name=" + user2.getName());
			} catch (UserNotFound e) {
				System.out.println(e.getMessage());
			}
			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		}
	}
}
