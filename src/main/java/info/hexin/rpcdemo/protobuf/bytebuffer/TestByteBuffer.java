package info.hexin.rpcdemo.protobuf.bytebuffer;

import java.nio.ByteBuffer;

import org.junit.Test;

public class TestByteBuffer {
	
	@Test
	public void test2() {
		ByteBuffer buf = ByteBuffer.allocate(20);
		buf.putInt(4);
		byte[] bb = "aaaaaa".getBytes();
		buf.wrap(bb);
		
		buf.position(0);
		System.out.println(buf.getInt());
		byte[] b = new byte[6];
		buf.get(b);
		System.out.println(new String(b));
	}

	//@Test
	public void test1() {
		ByteBuffer buf = ByteBuffer.allocate(20);
		System.out.println(buf.position() + "   " + buf.limit());
		buf.putInt(100);
		System.out.println(buf.position() + "   " + buf.limit());

		buf.mark();
		buf.reset();
		buf.get();
		System.out.println(buf.position() + "   " + buf.limit());
	}
}
