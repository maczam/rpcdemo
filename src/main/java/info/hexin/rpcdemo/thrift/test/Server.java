package info.hexin.rpcdemo.thrift.test;

import info.hexin.rpcdemo.thrift.UserService;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class Server {
	public static void main(String[] args) {
		try {

			TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(9090);

			UserServiceHandler handler = new UserServiceHandler();
			UserService.Processor processor = new UserService.Processor(handler);

			THsHaServer.Args arg = new THsHaServer.Args(tnbSocketTransport);
			arg.protocolFactory(new TCompactProtocol.Factory());
			arg.transportFactory(new TFramedTransport.Factory());
			arg.processorFactory(new TProcessorFactory(processor));
			final TServer server = new THsHaServer(arg);
			System.out.println("start");
			
			Thread t = 	new Thread(){

				@Override
				public void run() {
					try {
//						server.serve();
						Thread.sleep(1000);
//						server.stop();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			server.serve();
			Thread.sleep(5000);
//			t.interrupt();
		
		} catch (Exception x) {
			x.printStackTrace();
		}
		System.out.println("done.");
	}
}
