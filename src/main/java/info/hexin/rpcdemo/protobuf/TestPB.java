package info.hexin.rpcdemo.protobuf;

import info.hexin.rpcdemo.protobuf.PersonProbuf.Person;
import info.hexin.rpcdemo.protobuf.PersonProbuf.Person.PhoneNumber;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestPB {
	public static void main(String[] args) {
		PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
		builder.setEmail("kkk@email.com");
		builder.setId(1);
		builder.setName("TestName");
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("131111111")
				.setType(PersonProbuf.Person.PhoneType.MOBILE));
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("011111")
				.setType(PersonProbuf.Person.PhoneType.HOME));
		Person person = builder.build();
		byte[] buf = person.toByteArray();
		try {
			Person person2 = PersonProbuf.Person.parseFrom(buf);
			System.out.println(person2.getName() + ", " + person2.getEmail());
			List<PhoneNumber> lstPhones = person2.getPhoneList();
			for (PhoneNumber phoneNumber : lstPhones) {
				System.out.println(phoneNumber.getNumber());
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		System.out.println(buf);
	}
}
