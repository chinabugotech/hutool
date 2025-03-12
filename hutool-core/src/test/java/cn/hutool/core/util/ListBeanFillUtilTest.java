package cn.hutool.core.util;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 集合中对象属性填充 测试类
 *
 * @author fancg
 **/
public class ListBeanFillUtilTest {


	private List<User> userList;
	private List<Address> addressList;

	@BeforeEach
	public void setInit() {
		User user = new User();
		user.setId(1);
		user.setName("fancg");

		userList = new ArrayList<>();
		userList.add(user);

		Address address = new Address();
		address.setUserId(1);
		address.setCountry("中国");
		address.setCity("重庆");

		addressList = new ArrayList<>();
		addressList.add(address);

	}

	@Test
	public void fill() {
		ListBeanFillUtil.fill(userList, User::getId, addressList, Address::getUserId, (s, t) -> {
			s.setCountry(t.getCountry());
			s.setCity(t.getCity());
		});
		assertEquals("中国", userList.get(0).getCountry());
		assertEquals("重庆", userList.get(0).getCity());
	}

	@Test
	public void fillMap() {
		Map<Integer, Address> addressMap = addressList.stream().collect(Collectors.toMap(Address::getUserId, t -> t));
		ListBeanFillUtil.fillByMap(userList, User::getId, addressMap, (s, t) -> {
			s.setCountry(t.getCountry());
			s.setCity(t.getCity());
		});
		assertEquals("中国", userList.get(0).getCountry());
		assertEquals("重庆", userList.get(0).getCity());
	}

	@Test
	public void fillByFunc() {
		ListBeanFillUtil.fill(userList, User::getId, this::getAddressList, Address::getUserId, (s, t) -> {
			s.setCountry(t.getCountry());
			s.setCity(t.getCity());
		});
		assertEquals("中国", userList.get(0).getCountry());
		assertEquals("重庆", userList.get(0).getCity());
	}

	@Test
	public void fillByMapFunc() {
		ListBeanFillUtil.fillByMap(userList, User::getId, this::getAddressMap, (s, t) -> {
			s.setCountry(t.getCountry());
			s.setCity(t.getCity());
		});
		assertEquals("中国", userList.get(0).getCountry());
		assertEquals("重庆", userList.get(0).getCity());
	}

	@Getter
	@Setter
	class User {
		private Integer id;
		private String name;
		private String country;
		private String city;
	}

	@Getter
	@Setter
	class Address {
		private Integer userId;
		private String country;
		private String city;
	}

	private List<Address> getAddressList(List<Integer> userIdList) {
		return userIdList.stream().map(u -> {
			Address address = new Address();
			address.setUserId(u);
			address.setCountry("中国");
			address.setCity("重庆");
			return address;
		}).collect(Collectors.toList());
	}

	private Map<Integer, Address> getAddressMap(List<Integer> userIdList) {
		return getAddressList(userIdList).stream().collect(Collectors.toMap(Address::getUserId, t -> t));
	}
}
