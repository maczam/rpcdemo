package info.hexin.rpcdemo.thrift.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class TestRpc {
	public static void main(String[] args) {
		
		//InetSocketAddress address = new InetSocketAddress(9000);
		try {
			ServerSocket ss=new ServerSocket(9999);
//			ss.close();
			ServerSocket s1=new ServerSocket(9999);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
